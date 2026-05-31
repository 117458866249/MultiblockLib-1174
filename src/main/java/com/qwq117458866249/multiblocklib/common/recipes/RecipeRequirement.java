package com.qwq117458866249.multiblocklib.common.recipes;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.util.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public abstract class RecipeRequirement {
    public final boolean isOutput;

    public RecipeRequirement setChance(int value) {
        chance = value;
        return this;
    }

    protected double chance = ((Number) 1).doubleValue();

    public RecipeRequirement(IOMode io) {
        this.isOutput = io.equals(IOMode.OUTPUT);
    }

    public abstract ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, Structure structure);

    public abstract void inputRequirement(BlockPos pos, Level level, Direction face, Structure structure);

    @Info(m = "Add this code to your Requirement")
    @Info(m = "if (Math.random() >= chance) return;")
    public abstract void outputRequirement(BlockPos pos, Level level, Direction face, Structure structure);

    public abstract Component getDesc();
}
