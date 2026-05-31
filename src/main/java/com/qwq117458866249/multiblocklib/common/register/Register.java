package com.qwq117458866249.multiblocklib.common.register;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.common.template.fe_port.FEPortBlock;
import com.qwq117458866249.multiblocklib.common.template.fe_port.FEPortBlockEntity;
import com.qwq117458866249.multiblocklib.common.template.fluid_port.FluidPortBlock;
import com.qwq117458866249.multiblocklib.common.template.fluid_port.FluidPortBlockEntity;
import com.qwq117458866249.multiblocklib.common.template.item_port.ItemPortBlock;
import com.qwq117458866249.multiblocklib.common.template.item_port.ItemPortBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Register {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MultiblockLib.MOD_ID);

    public static final Supplier<BlockEntityType<ItemPortBlockEntity>> ITEM_PORT_BE = BLOCK_ENTITY_TYPES.register(
            "item_port_be",
            () -> new BlockEntityType<>(ItemPortBlockEntity::new, Util.toSet(ItemPortBlock.allThis))
    );

    public static final Supplier<BlockEntityType<FluidPortBlockEntity>> FLUID_PORT_BE = BLOCK_ENTITY_TYPES.register(
            "fluid_port_be",
            () -> new BlockEntityType<>(FluidPortBlockEntity::new, Util.toSet(FluidPortBlock.allThis))
    );

    public static final Supplier<BlockEntityType<FEPortBlockEntity>> FE_PORT_BE = BLOCK_ENTITY_TYPES.register(
            "fe_port_be",
            () -> new BlockEntityType<>(FEPortBlockEntity::new, Util.toSet(FEPortBlock.allThis))
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
