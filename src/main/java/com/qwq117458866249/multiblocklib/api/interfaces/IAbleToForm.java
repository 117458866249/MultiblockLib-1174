package com.qwq117458866249.multiblocklib.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface IAbleToForm {
    default void form(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(FORMED, true), 3);
    }

    default void unForm(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(FORMED, false), 3);
    }

    boolean isDirectional();

    BooleanProperty FORMED = BooleanProperty.create("formed");

    EnumProperty<Direction> FACING = BlockStateProperties.FACING;
}
