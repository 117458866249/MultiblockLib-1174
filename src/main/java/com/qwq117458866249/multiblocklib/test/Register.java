package com.qwq117458866249.multiblocklib.test;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.template.PortBlockItem;
import com.qwq117458866249.multiblocklib.common.template.fe_port.FEPortBlock;
import com.qwq117458866249.multiblocklib.common.template.fluid_port.FluidPortBlock;
import com.qwq117458866249.multiblocklib.common.template.item_port.ItemPortBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Register {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MultiblockLib.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MultiblockLib.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MultiblockLib.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MultiblockLib.MOD_ID);

    // Test Controller
    public static final DeferredBlock<Block> TEST_CONTROLLER_B = BLOCKS.register(
            "test_controller",
            registryName -> new TestControllerBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
            ));

    public static final DeferredItem<Item> TEST_CONTROLLER =
            ITEMS.register("test_controller",
                    registryName -> new BlockItem(TEST_CONTROLLER_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName))
                    ));

    public static final Supplier<BlockEntityType<TestControllerBlockEntity>> TEST_CONTROLLER_BE = BLOCK_ENTITY_TYPES.register(
            "test_controller",
            () -> new BlockEntityType<>(
                    TestControllerBlockEntity::new,
                    false,
                    TEST_CONTROLLER_B.get()
            )
    );

    // Test Normal Item Port In
    public static final DeferredBlock<Block> TEST_ITEM_PORT_IN_B = BLOCKS.register(
            "test_item_port_in",
            registryName -> new ItemPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    4, IOMode.INPUT
            ));

    public static final DeferredItem<Item> TEST_ITEM_PORT_IN =
            ITEMS.register("test_item_port_in",
                    registryName -> new PortBlockItem(TEST_ITEM_PORT_IN_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.INPUT
                    ));

    // Test Normal Item Port Ou
    public static final DeferredBlock<Block> TEST_ITEM_PORT_OU_B = BLOCKS.register(
            "test_item_port_ou",
            registryName -> new ItemPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    4, IOMode.OUTPUT
            ));

    public static final DeferredItem<Item> TEST_ITEM_PORT_OU =
            ITEMS.register("test_item_port_ou",
                    registryName -> new PortBlockItem(TEST_ITEM_PORT_OU_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.OUTPUT
                    ));

    // Waste
    public static final DeferredBlock<Block> TEST_WASTE_B = BLOCKS.register(
            "test_waste",
            registryName -> new ItemPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    1, IOMode.OUTPUT
            ) {
                @Override
                public boolean isDirectional() {
                    return false;
                }
            });

    public static final DeferredItem<Item> TEST_WASTE =
            ITEMS.register("test_waste",
                    registryName -> new PortBlockItem(TEST_WASTE_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.OUTPUT
                    ));

    // Fluid In
    public static final DeferredBlock<Block> TEST_FLUID_PORT_IN_B = BLOCKS.register(
            "test_fluid_port_in",
            registryName -> new FluidPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    4000, 2, IOMode.INPUT
            ));

    public static final DeferredItem<Item> TEST_FLUID_PORT_IN =
            ITEMS.register("test_fluid_port_in",
                    registryName -> new PortBlockItem(TEST_FLUID_PORT_IN_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.INPUT
                    ));

    // Fluid Ou
    public static final DeferredBlock<Block> TEST_FLUID_PORT_OU_B = BLOCKS.register(
            "test_fluid_port_ou",
            registryName -> new FluidPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    4000, 2, IOMode.OUTPUT
            ));

    public static final DeferredItem<Item> TEST_FLUID_PORT_OU =
            ITEMS.register("test_fluid_port_ou",
                    registryName -> new PortBlockItem(TEST_FLUID_PORT_OU_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.OUTPUT
                    ));

    // FE
    public static final DeferredBlock<Block> TEST_FE_PORT_B = BLOCKS.register(
            "test_fe_port",
            registryName -> new FEPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    15000, IOMode.INPUT
            ));

    public static final DeferredItem<Item> TEST_FE_PORT =
            ITEMS.register("test_fe_port",
                    registryName -> new PortBlockItem(TEST_FE_PORT_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.INPUT
                    ));

    public static final DeferredBlock<Block> TEST_FE_OUTPUT_PORT_B = BLOCKS.register(
            "test_fe_output_port",
            registryName -> new FEPortBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName)),
                    15000, IOMode.OUTPUT
            ));

    public static final DeferredItem<Item> TEST_FE_OUTPUT_PORT =
            ITEMS.register("test_fe_output_port",
                    registryName -> new PortBlockItem(TEST_FE_OUTPUT_PORT_B.get(), new Item.Properties()
                            .setId(ResourceKey.create(Registries.ITEM, registryName)),
                            IOMode.OUTPUT
                    ));

    // Tab
    public static final Supplier<CreativeModeTab> TAB =
            TABS.register("tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(TEST_CONTROLLER.get()))
                    .title(Component.literal("Test XwX"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(TEST_CONTROLLER);
                        pOutput.accept(TEST_ITEM_PORT_IN);
                        pOutput.accept(TEST_ITEM_PORT_OU);
                        pOutput.accept(TEST_WASTE);
                        pOutput.accept(TEST_FLUID_PORT_IN);
                        pOutput.accept(TEST_FLUID_PORT_OU);
                        pOutput.accept(TEST_FE_PORT);
                        pOutput.accept(TEST_FE_OUTPUT_PORT);
                    })
                    .build()
            );

    // EventBus
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        TABS.register(eventBus);
    }
}
