package dev.shovinus.computerised.oregen;

import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Modern world-generation host for the CustomOreGen distribution engine.
 *
 * <p>The engine deliberately registers no ore blocks. Configurations refer to
 * blocks through registry IDs, allowing packs to supply poor and rich ores.</p>
 */
@Mod(ComputerisedOreGen.MOD_ID)
public final class ComputerisedOreGen {
    public static final String MOD_ID = "computerised_oregen";
    private static final DeferredRegister<Feature<?>> FEATURES =
        DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);
    public static final RegistryObject<Feature<?>> MOTHERLODE = FEATURES.register(
        "motherlode", () -> new MotherlodeFeature(MotherlodeConfiguration.CODEC));

    public ComputerisedOreGen() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        FEATURES.register(modBus);
    }
}
