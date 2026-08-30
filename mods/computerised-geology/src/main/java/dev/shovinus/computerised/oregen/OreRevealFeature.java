package dev.shovinus.computerised.oregen;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;

/** Development-only pass that leaves tagged ores floating in an otherwise empty chunk. */
public final class OreRevealFeature extends Feature<NoneFeatureConfiguration> {
    public OreRevealFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        ChunkPos chunkPos = new ChunkPos(context.origin());
        ChunkAccess chunk = context.level().getChunk(chunkPos.x, chunkPos.z);
        boolean removed = false;

        // This runs during chunk generation. Mutating sections directly avoids
        // scheduling tens of thousands of asynchronous light updates against a
        // ProtoChunk whose lighting DataLayers do not exist yet.
        for (LevelChunkSection section : chunk.getSections()) {
            if (section.hasOnlyAir()) continue;
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        var state = section.getBlockState(x, y, z);
                        if (!state.isAir() && !state.is(Tags.Blocks.ORES)) {
                            section.setBlockState(x, y, z, Blocks.AIR.defaultBlockState(), false);
                            removed = true;
                        }
                    }
                }
            }
        }
        return removed;
    }
}
