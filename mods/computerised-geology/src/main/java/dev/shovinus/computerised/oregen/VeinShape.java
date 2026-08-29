package dev.shovinus.computerised.oregen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record VeinShape(
    Distribution motherlodeSize,
    Distribution branchCount,
    Distribution branchLength,
    Distribution inclination,
    Distribution segmentLength,
    Distribution segmentRadius,
    Distribution segmentTurn,
    Distribution segmentPitch,
    Distribution forkCount,
    Distribution forkLengthMultiplier,
    float verticalLimit,
    float density
) {
    public static final Codec<VeinShape> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Distribution.CODEC.fieldOf("motherlode_size").forGetter(VeinShape::motherlodeSize),
        Distribution.CODEC.fieldOf("branch_count").forGetter(VeinShape::branchCount),
        Distribution.CODEC.fieldOf("branch_length").forGetter(VeinShape::branchLength),
        Distribution.CODEC.fieldOf("branch_inclination").forGetter(VeinShape::inclination),
        Distribution.CODEC.fieldOf("segment_length").forGetter(VeinShape::segmentLength),
        Distribution.CODEC.fieldOf("segment_radius").forGetter(VeinShape::segmentRadius),
        Distribution.CODEC.fieldOf("segment_turn").forGetter(VeinShape::segmentTurn),
        Distribution.CODEC.fieldOf("segment_pitch").forGetter(VeinShape::segmentPitch),
        Distribution.CODEC.fieldOf("fork_count").forGetter(VeinShape::forkCount),
        Distribution.CODEC.fieldOf("fork_length_multiplier").forGetter(VeinShape::forkLengthMultiplier),
        Codec.FLOAT.fieldOf("vertical_limit").forGetter(VeinShape::verticalLimit),
        Codec.floatRange(0.0F, 1.0F).optionalFieldOf("density", 1.0F).forGetter(VeinShape::density)
    ).apply(instance, VeinShape::new));

    public int searchRadiusChunks() {
        float reach = motherlodeSize.maximum() + branchLength.maximum() + segmentRadius.maximum() + 16;
        return Math.max(1, (int) Math.ceil(reach / 16.0));
    }
}
