package dev.shovinus.computerised.oregen;

import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
    private static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Block> TRACE_IRON_ORE = oreBlock("trace_iron_ore");
    public static final RegistryObject<Block> POOR_IRON_ORE = oreBlock("poor_iron_ore");
    public static final RegistryObject<Block> POOR_NICKEL_ORE = oreBlock("poor_nickel_ore");
    public static final RegistryObject<Item> IRON_FINES = item("iron_fines");
    public static final RegistryObject<Item> NICKEL_FINES = item("nickel_fines");
    public static final RegistryObject<Item> NICKEL_NUGGET = item("nickel_nugget");
    public static final RegistryObject<Item> NICKEL_INGOT = item("nickel_ingot");
    public static final RegistryObject<Feature<?>> MOTHERLODE = FEATURES.register(
        "motherlode", () -> new MotherlodeFeature(MotherlodeConfiguration.CODEC));
    public static final RegistryObject<Feature<?>> ORE_REVEAL = FEATURES.register(
        "ore_reveal", OreRevealFeature::new);

    public ComputerisedOreGen() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        FEATURES.register(modBus);
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        modBus.addListener(ComputerisedOreGen::addCreativeTabContents);
    }

    private static RegistryObject<Block> oreBlock(String name) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    private static RegistryObject<Item> item(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    private static void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(TRACE_IRON_ORE);
            event.accept(POOR_IRON_ORE);
            event.accept(POOR_NICKEL_ORE);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(IRON_FINES);
            event.accept(NICKEL_FINES);
            event.accept(NICKEL_NUGGET);
            event.accept(NICKEL_INGOT);
        }
    }
}
