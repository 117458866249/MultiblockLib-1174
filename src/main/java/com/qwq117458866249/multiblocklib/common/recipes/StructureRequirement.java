package com.qwq117458866249.multiblocklib.common.recipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public abstract class StructureRequirement {
    public abstract boolean cantForm(BlockPos pos, Level level, Direction face, Structure structure);

    public abstract Component getDesc();
}