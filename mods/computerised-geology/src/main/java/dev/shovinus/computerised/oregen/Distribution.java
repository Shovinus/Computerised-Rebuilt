package dev.shovinus.computerised.oregen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

/** A bounded probability distribution compatible with CustomOreGen's PDist. */
public record Distribution(float mean, float range, Type type) {
    public static final Codec<Distribution> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("mean").forGetter(Distribution::mean),
        Codec.FLOAT.optionalFieldOf("range", 0.0F).forGetter(Distribution::range),
        Type.CODEC.optionalFieldOf("type", Type.UNIFORM).forGetter(Distribution::type)
    ).apply(instance, Distribution::new));

    public Distribution {
        range = Math.abs(range);
    }

    public float sample(RandomSource random) {
        if (range == 0.0F) return mean;
        float unit = switch (type) {
            case UNIFORM -> random.nextFloat() * 2.0F - 1.0F;
            case NORMAL -> Math.max(-1.0F, Math.min(1.0F, (float) random.nextGaussian() / 2.5F));
        };
        return mean + unit * range;
    }

    public int sampleInt(RandomSource random) {
        float value = sample(random);
        int whole = (int) value;
        float fraction = value - whole;
        if (fraction > 0 && random.nextFloat() < fraction) whole++;
        if (fraction < 0 && random.nextFloat() < -fraction) whole--;
        return whole;
    }

    public float maximum() {
        return mean + range;
    }

    public enum Type {
        UNIFORM,
        NORMAL;

        static final Codec<Type> CODEC = Codec.STRING.xmap(
            name -> valueOf(name.toUpperCase()),
            type -> type.name().toLowerCase()
        );
    }
}
