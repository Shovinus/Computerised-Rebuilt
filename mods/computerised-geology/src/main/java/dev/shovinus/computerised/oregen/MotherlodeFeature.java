package dev.shovinus.computerised.oregen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;

/** Chunk-local rasteriser for seed-stable, multi-chunk branching motherlodes. */
public final class MotherlodeFeature extends Feature<MotherlodeConfiguration> {
    private static final int MAX_FORK_DEPTH = 3;
    private static final int MAX_COMPONENTS = 512;

    public MotherlodeFeature(Codec<MotherlodeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<MotherlodeConfiguration> context) {
        WorldGenLevel level = context.level();
        MotherlodeConfiguration config = context.config();
        ChunkPos target = new ChunkPos(context.origin());
        int search = config.shape().searchRadiusChunks();
        boolean placed = false;

        for (int sourceX = target.x - search; sourceX <= target.x + search; sourceX++) {
            for (int sourceZ = target.z - search; sourceZ <= target.z + search; sourceZ++) {
                long sourceSeed = mixChunk(level.getSeed(), sourceX, sourceZ, config.salt());
                RandomSource sourceRandom = RandomSource.create(sourceSeed);
                int count = occurrenceCount(config.frequency(), sourceRandom);
                for (int occurrence = 0; occurrence < count; occurrence++) {
                    long depositSeed = sourceRandom.nextLong();
                    RandomSource depositRandom = RandomSource.create(depositSeed);
                    Deposit deposit = plan(sourceX, sourceZ, config, depositRandom);
                    if (allowedBiome(level, config.biomeTag(), deposit.center())) {
                        placed |= rasterise(level, target, config, deposit, depositSeed);
                    }
                }
            }
        }
        return placed;
    }

    private static int occurrenceCount(float frequency, RandomSource random) {
        int count = Mth.floor(frequency);
        return count + (random.nextFloat() < frequency - count ? 1 : 0);
    }

    private static Deposit plan(int chunkX, int chunkZ, MotherlodeConfiguration config, RandomSource random) {
        double x = chunkX * 16.0 + random.nextDouble() * 16.0;
        double z = chunkZ * 16.0 + random.nextDouble() * 16.0;
        double y = config.height().sample(random);
        Vec center = new Vec(x, y, z);
        VeinShape shape = config.shape();
        List<Primitive> primitives = new ArrayList<>();
        primitives.add(new Ellipsoid(
            center,
            Math.max(0.25, shape.motherlodeSize().sample(random)),
            Math.max(0.25, shape.motherlodeSize().sample(random)),
            Math.max(0.25, shape.motherlodeSize().sample(random))
        ));

        int branches = Math.max(0, shape.branchCount().sampleInt(random));
        for (int branch = 0; branch < branches && primitives.size() < MAX_COMPONENTS; branch++) {
            double yaw = random.nextDouble() * Mth.TWO_PI;
            double pitch = shape.inclination().sample(random);
            growBranch(primitives, center, yaw, pitch,
                Math.max(0, shape.branchLength().sample(random)), center.y(), shape, random, 0);
        }
        return new Deposit(center, primitives);
    }

    private static void growBranch(List<Primitive> output, Vec start, double yaw, double pitch,
                                   double remaining, double motherY, VeinShape shape,
                                   RandomSource random, int depth) {
        Vec cursor = start;
        while (remaining > 0.01 && output.size() < MAX_COMPONENTS) {
            double length = Math.min(remaining, Math.max(0.25, shape.segmentLength().sample(random)));
            double radius = Math.max(0.1, shape.segmentRadius().sample(random));
            Vec direction = Vec.direction(yaw, pitch);
            Vec end = cursor.add(direction.scale(length));
            if (Math.abs(end.y() - motherY) > shape.verticalLimit()) return;
            output.add(new Tube(cursor, end, radius));
            remaining -= length;

            int forks = Math.max(0, shape.forkCount().sampleInt(random));
            if (depth < MAX_FORK_DEPTH && remaining > 0.01) {
                for (int fork = 0; fork < forks && output.size() < MAX_COMPONENTS; fork++) {
                    RandomSource forkRandom = RandomSource.create(random.nextLong());
                    double multiplier = Mth.clamp(shape.forkLengthMultiplier().sample(forkRandom), 0, 1);
                    double forkYaw = yaw + forkRandom.nextDouble() * Mth.TWO_PI;
                    double forkPitch = pitch + signed(shape.segmentTurn().sample(forkRandom), forkRandom);
                    growBranch(output, end, forkYaw, forkPitch, remaining * multiplier,
                        motherY, shape, forkRandom, depth + 1);
                }
            }

            cursor = end;
            yaw += signed(shape.segmentTurn().sample(random), random);
            pitch = Mth.clamp(pitch + signed(shape.segmentPitch().sample(random), random),
                -Math.PI / 2.0, Math.PI / 2.0);
        }
    }

    private static double signed(float value, RandomSource random) {
        return random.nextBoolean() ? value : -value;
    }

    private static boolean allowedBiome(WorldGenLevel level, java.util.Optional<ResourceLocation> biomeTag, Vec center) {
        if (biomeTag.isEmpty()) return true;
        TagKey<Biome> tag = TagKey.create(Registries.BIOME, biomeTag.get());
        Holder<Biome> biome = level.getBiome(BlockPos.containing(center.x(), center.y(), center.z()));
        return biome.is(tag);
    }

    private static boolean rasterise(WorldGenLevel level, ChunkPos chunk, MotherlodeConfiguration config,
                                     Deposit deposit, long depositSeed) {
        int minX = chunk.getMinBlockX();
        int maxX = chunk.getMaxBlockX();
        int minZ = chunk.getMinBlockZ();
        int maxZ = chunk.getMaxBlockZ();
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - 1;
        TagKey<Block> replaceable = TagKey.create(Registries.BLOCK, config.replaceableTag());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean placed = false;

        for (Primitive primitive : deposit.primitives()) {
            Bounds bounds = primitive.bounds().intersect(minX, minY, minZ, maxX, maxY, maxZ);
            if (bounds.empty()) continue;
            for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
                for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
                    for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
                        if (!primitive.contains(x + 0.5, y + 0.5, z + 0.5)) continue;
                        pos.set(x, y, z);
                        if (!level.getBlockState(pos).is(replaceable)) continue;
                        RandomSource blockRandom = RandomSource.create(mixBlock(depositSeed, x, y, z));
                        if (blockRandom.nextFloat() > config.shape().density()) continue;
                        level.setBlock(pos, WeightedBlock.choose(config.blocks(), blockRandom), 2);
                        placed = true;
                    }
                }
            }
        }
        return placed;
    }

    private static long mixChunk(long seed, int x, int z, int salt) {
        long value = seed ^ ((long) salt * 0x9E3779B97F4A7C15L);
        value ^= (long) x * 341873128712L;
        value ^= (long) z * 132897987541L;
        return avalanche(value);
    }

    private static long mixBlock(long seed, int x, int y, int z) {
        return avalanche(seed ^ (long) x * 341873128712L ^ (long) y * 42317861L ^ (long) z * 132897987541L);
    }

    private static long avalanche(long value) {
        value ^= value >>> 30;
        value *= 0xBF58476D1CE4E5B9L;
        value ^= value >>> 27;
        value *= 0x94D049BB133111EBL;
        return value ^ value >>> 31;
    }

    private record Deposit(Vec center, List<Primitive> primitives) {}
    private record Vec(double x, double y, double z) {
        Vec add(Vec other) { return new Vec(x + other.x, y + other.y, z + other.z); }
        Vec scale(double factor) { return new Vec(x * factor, y * factor, z * factor); }
        static Vec direction(double yaw, double pitch) {
            double horizontal = Math.cos(pitch);
            return new Vec(Math.sin(yaw) * horizontal, Math.sin(pitch), Math.cos(yaw) * horizontal);
        }
    }

    private sealed interface Primitive permits Ellipsoid, Tube {
        Bounds bounds();
        boolean contains(double x, double y, double z);
    }

    private record Ellipsoid(Vec center, double radiusX, double radiusY, double radiusZ) implements Primitive {
        public Bounds bounds() {
            return Bounds.around(center, radiusX, radiusY, radiusZ);
        }
        public boolean contains(double x, double y, double z) {
            double dx = (x - center.x) / radiusX;
            double dy = (y - center.y) / radiusY;
            double dz = (z - center.z) / radiusZ;
            return dx * dx + dy * dy + dz * dz <= 1.0;
        }
    }

    private record Tube(Vec start, Vec end, double radius) implements Primitive {
        public Bounds bounds() {
            return new Bounds(
                Mth.floor(Math.min(start.x, end.x) - radius), Mth.floor(Math.min(start.y, end.y) - radius),
                Mth.floor(Math.min(start.z, end.z) - radius), Mth.floor(Math.max(start.x, end.x) + radius),
                Mth.floor(Math.max(start.y, end.y) + radius), Mth.floor(Math.max(start.z, end.z) + radius)
            );
        }
        public boolean contains(double x, double y, double z) {
            double abX = end.x - start.x, abY = end.y - start.y, abZ = end.z - start.z;
            double apX = x - start.x, apY = y - start.y, apZ = z - start.z;
            double lengthSquared = abX * abX + abY * abY + abZ * abZ;
            double t = lengthSquared == 0 ? 0 : Mth.clamp((apX * abX + apY * abY + apZ * abZ) / lengthSquared, 0, 1);
            double dx = apX - abX * t, dy = apY - abY * t, dz = apZ - abZ * t;
            return dx * dx + dy * dy + dz * dz <= radius * radius;
        }
    }

    private record Bounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        static Bounds around(Vec center, double rx, double ry, double rz) {
            return new Bounds(Mth.floor(center.x - rx), Mth.floor(center.y - ry), Mth.floor(center.z - rz),
                Mth.floor(center.x + rx), Mth.floor(center.y + ry), Mth.floor(center.z + rz));
        }
        Bounds intersect(int x1, int y1, int z1, int x2, int y2, int z2) {
            return new Bounds(Math.max(minX, x1), Math.max(minY, y1), Math.max(minZ, z1),
                Math.min(maxX, x2), Math.min(maxY, y2), Math.min(maxZ, z2));
        }
        boolean empty() { return minX > maxX || minY > maxY || minZ > maxZ; }
    }
}
