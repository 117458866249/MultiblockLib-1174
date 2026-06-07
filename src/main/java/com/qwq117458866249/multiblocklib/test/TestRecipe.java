package com.qwq117458866249.multiblocklib.test;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.FERecipeRequirement;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.ItemRecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;

import java.util.ArrayList;

public class TestRecipe extends Recipe {
    @Override
    public ArrayList<RecipeRequirement> recipeRequirements() {
        ArrayList<RecipeRequirement> t = new ArrayList<>();
        t.add(new ItemRecipeRequirement(IOMode.INPUT,"minecraft:iron_ingot",3));
        t.add(new ItemRecipeRequirement(IOMode.OUTPUT,"minecraft:netherite_ingot",1).setPort(Register.TEST_ITEM_PORT_OU_B.get()));
        t.add(new ItemRecipeRequirement(IOMode.OUTPUT,"minecraft:gunpowder",2).setPort(Register.TEST_WASTE_B.get()));
        t.add(new FERecipeRequirement(IOMode.INPUT,5000));
        return t;
    }

    @Override
    public int parsingTime() {
        return 100;
    }

    @Override
    public String recipeId() {
        return "blast_revipe_test";
    }

    static {
        allRecipes.put(new TestRecipe().recipeId(), new TestRecipe());
    }
}
