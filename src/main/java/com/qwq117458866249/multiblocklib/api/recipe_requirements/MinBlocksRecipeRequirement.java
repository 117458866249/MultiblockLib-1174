package com.qwq117458866249.multiblocklib.api.recipe_requirements;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.api.structure_requirements.MinBlocksStructureRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class MinBlocksRecipeRequirement extends RecipeRequirement {
    public final int value;
    public final ArrayList<String> blocks;

    public MinBlocksRecipeRequirement(IOMode io, int value, ArrayList<String> blocks) {
        super(io);
        this.value = value;
        this.blocks = blocks;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        if (new MinBlocksStructureRequirement(blocks,value).cantForm(pos, level, face, structure)){
            return ParseResult.FAILED;
        } else {
            return ParseResult.SUCCESS;
        }
    }

    @Override
    public void inputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {

    }

    @Override
    public void outputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {

    }

    @Override
    public Component getDesc() {
        return Component.literal(Component.translatable("requirement.multiblocklib.min.f").getString() + value + Component.translatable("requirement.multiblocklib.min.b").getString());
    }
}
