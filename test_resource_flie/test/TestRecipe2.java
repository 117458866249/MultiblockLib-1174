package com.qwq117458866249.multiblocklib.test;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.FluidRecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;

import java.util.ArrayList;

public class TestRecipe2 extends Recipe {
    @Override
    public ArrayList<RecipeRequirement> recipeRequirements() {
        ArrayList<RecipeRequirement> t = new ArrayList<>();
        t.add(new FluidRecipeRequirement(IOMode.INPUT,"minecraft:water",1000));
        t.add(new FluidRecipeRequirement(IOMode.OUTPUT,"minecraft:lava",500));
        return t;
    }

    @Override
    public int parsingTime() {
        return 40;
    }

    @Override
    public String recipeId() {
        return "waaaaaaagh";
    }

    static {
        Recipe.allRecipes.put(new TestRecipe2().recipeId(),new TestRecipe2());
    }
}
