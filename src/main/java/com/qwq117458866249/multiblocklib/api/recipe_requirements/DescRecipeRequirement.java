package com.qwq117458866249.multiblocklib.api.recipe_requirements;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class DescRecipeRequirement extends RecipeRequirement {
    public final Component desc;
    public DescRecipeRequirement(IOMode io, Component desc) {
        super(io);
        this.desc = desc;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        return ParseResult.SUCCESS;
    }

    @Override
    public void inputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {

    }

    @Override
    public void outputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {

    }

    @Override
    public Component getDesc() {
        return desc;
    }
}
