package com.qwq117458866249.multiblocklib.common.recipes;

import com.google.common.collect.HashBiMap;
import com.qwq117458866249.multiblocklib.util.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Info(m = "We should add these code to your structure code")
@Info(m = "static {                                                                ")
@Info(m = "    allRecipes.put(new ExampleRecipe().recipeId(), new ExampleRecipe());")
@Info(m = "}                                                                       ")
public abstract class Recipe {
    public static HashBiMap<String, Recipe> allRecipes = HashBiMap.create();

    public abstract ArrayList<RecipeRequirement> recipeRequirements();

    public abstract int parsingTime();

    public abstract String recipeId();

    public abstract String structureId();

    public boolean canParseRecipe(BlockPos pos, Level level, Direction face, Structure structure, boolean isParallels) {
        AtomicBoolean temp = new AtomicBoolean(true);
        recipeRequirements().forEach(p -> {
            if (temp.get() && (!p.isOutput) && (!(p.onlyDetectOnce() && isParallels))) {
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

    public void inputRecipe(BlockPos pos, Level level, Direction face, Structure structure, boolean isFirst) {
        recipeRequirements().forEach(p -> {
            if (!p.isOutput && ((!p.onlyDetectOnce()) || isFirst)) {
                p.inputRequirement(pos, level, face, structure);
            }
        });
    }

    public void outputRecipe(BlockPos pos, Level level, Direction face, Structure structure, boolean isFirst) {
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
