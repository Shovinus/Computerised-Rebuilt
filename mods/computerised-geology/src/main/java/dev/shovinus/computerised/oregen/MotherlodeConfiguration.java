package dev.shovinus.computerised.oregen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Optional;

public record MotherlodeConfiguration(
    float frequency,
    Distribution height,
    VeinShape shape,
    List<WeightedBlock> blocks,
    ResourceLocation replaceableTag,
    Optional<ResourceLocation> biomeTag,
    int salt
) implements FeatureConfiguration {
    public static final Codec<MotherlodeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.floatRange(0.0F, 64.0F).fieldOf("frequency").forGetter(MotherlodeConfiguration::frequency),
        Distribution.CODEC.fieldOf("height").forGetter(MotherlodeConfiguration::height),
        VeinShape.CODEC.fieldOf("shape").forGetter(MotherlodeConfiguration::shape),
        WeightedBlock.CODEC.listOf().fieldOf("blocks").forGetter(MotherlodeConfiguration::blocks),
        ResourceLocation.CODEC.fieldOf("replaceable_tag").forGetter(MotherlodeConfiguration::replaceableTag),
        ResourceLocation.CODEC.optionalFieldOf("biome_tag").forGetter(MotherlodeConfiguration::biomeTag),
        Codec.INT.optionalFieldOf("salt", 0).forGetter(MotherlodeConfiguration::salt)
    ).apply(instance, MotherlodeConfiguration::new));

    public MotherlodeConfiguration {
        if (blocks.isEmpty()) throw new IllegalArgumentException("A motherlode requires at least one output block");
    }
}
