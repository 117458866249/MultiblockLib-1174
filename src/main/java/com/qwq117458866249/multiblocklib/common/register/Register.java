package com.qwq117458866249.multiblocklib.common.register;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.common.recipes.json.MultiblockJsonRecipe;
import com.qwq117458866249.multiblocklib.common.recipes.json.MultiblockJsonStructure;
import com.qwq117458866249.multiblocklib.common.template.fe_port.FEPortBlock;
import com.qwq117458866249.multiblocklib.common.template.fe_port.FEPortBlockEntity;
import com.qwq117458866249.multiblocklib.common.template.fluid_port.FluidPortBlock;
import com.qwq117458866249.multiblocklib.common.template.fluid_port.FluidPortBlockEntity;
import com.qwq117458866249.multiblocklib.common.template.item_port.ItemPortBlock;
import com.qwq117458866249.multiblocklib.common.template.item_port.ItemPortBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Register {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MultiblockLib.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MultiblockLib.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MultiblockLib.MOD_ID);

    // Block Entities
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

    // Recipe
    public static final Supplier<RecipeType<MultiblockJsonRecipe>> RECIPE_TYPE = RECIPE_TYPES.register(
            "recipe",
            () -> RecipeType.simple(Identifier.parse("multiblocklibes:recipe"))
    );

    public static final Supplier<RecipeType<MultiblockJsonStructure>> STRUCTURE_TYPE = RECIPE_TYPES.register(
            "structure",
            () -> RecipeType.simple(Identifier.parse("multiblocklibes:structure"))
    );

    public static final Supplier<RecipeSerializer<MultiblockJsonRecipe>> RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            "recipe",
            () -> MultiblockJsonRecipe.SERIALIZER
    );

    public static final Supplier<RecipeSerializer<MultiblockJsonStructure>> STRUCTURE_SERIALIZER = RECIPE_SERIALIZERS.register(
            "structure",
            () -> MultiblockJsonStructure.SERIALIZER
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
