package com.qwq117458866249.multiblocklib.api.interfaces;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface IControllerBlock {
    EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    BooleanProperty WORKING = BooleanProperty.create("working");
    BooleanProperty FORMED = BooleanProperty.create("formed");

    BlockState iDefaultBlockState();

    default void iCreateBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(FORMED);
        builder.add(WORKING);
    }

    default BlockState iGetStateForPlacement(BlockPlaceContext context) {
        Direction playerDirection = context.getHorizontalDirection();
        Direction facing = playerDirection.getOpposite();
        return this.iDefaultBlockState().setValue(FACING, facing).setValue(FORMED, false).setValue(WORKING, false);
    }
}
