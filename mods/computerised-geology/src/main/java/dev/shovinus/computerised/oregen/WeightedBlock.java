package dev.shovinus.computerised.oregen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public record WeightedBlock(BlockState state, int weight) {
    public static final Codec<WeightedBlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockState.CODEC.fieldOf("state").forGetter(WeightedBlock::state),
        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(WeightedBlock::weight)
    ).apply(instance, WeightedBlock::new));

    public static BlockState choose(java.util.List<WeightedBlock> blocks, RandomSource random) {
        int total = blocks.stream().mapToInt(WeightedBlock::weight).sum();
        int selection = random.nextInt(total);
        for (WeightedBlock block : blocks) {
            selection -= block.weight;
            if (selection < 0) return block.state;
        }
        return blocks.get(blocks.size() - 1).state;
    }
}
