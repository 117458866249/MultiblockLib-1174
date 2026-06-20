package com.qwq117458866249.multiblocklib.api;

import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;

import java.util.ArrayList;

public final class EmptyRecipe extends Recipe {
    @Override
    public ArrayList<RecipeRequirement> recipeRequirements() {
        return new ArrayList<>();
    }

    @Override
    public int parsingTime() {
        return 0;
    }

    @Override
    public String recipeId() {
        return "";
    }

    @Override
    public String structureId() {
        return "";
    }

    static {
        allRecipes.put(new EmptyRecipe().recipeId(), new EmptyRecipe());
    }
}
