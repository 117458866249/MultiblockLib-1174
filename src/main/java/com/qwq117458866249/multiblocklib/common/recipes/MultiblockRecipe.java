package com.qwq117458866249.multiblocklib.common.recipes;

import com.google.common.collect.HashBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MultiblockRecipe {
    public static HashBiMap<String, MultiblockRecipe> allRecipes = HashBiMap.create();

    public abstract ArrayList<RecipeRequirement> recipeRequirements();

    public abstract int parsingTime();

    public abstract String recipeId();

    public abstract String structureId();

    public boolean canParseRecipe(BlockPos pos, Level level, Direction face, MultiblockStructure structure, boolean isFirst) {
        AtomicBoolean temp = new AtomicBoolean(true);
        recipeRequirements().forEach(p -> {
            if (temp.get() && (!p.isOutput) && ((!p.onlyDetectOnce()) || isFirst)) {
                temp.set(false);
                switch (p.canParseRequirement(pos, level, face, structure)) {
                    case SUCCESS -> temp.set(true);
                    case WE_DON_T_PARSE_THIS ->
                            throw new RuntimeException("A invalid requirement has been progressed!");
                }
            }
        });
        return temp.get();
    }

    public void inputRecipe(BlockPos pos, Level level, Direction face, MultiblockStructure structure, boolean isFirst) {
        recipeRequirements().forEach(p -> {
            if ((!p.isOutput) && ((!p.onlyDetectOnce()) || isFirst)) {
                p.inputRequirement(pos, level, face, structure);
            }
        });
    }

    public void outputRecipe(BlockPos pos, Level level, Direction face, MultiblockStructure structure, boolean isFirst) {
        recipeRequirements().forEach(p -> {
            if (p.isOutput && ((!p.onlyDetectOnce()) || isFirst)) {
                p.outputRequirement(pos, level, face, structure);
            }
        });
    }

    @Override
    public String toString() {
        return recipeId();
    }
}
