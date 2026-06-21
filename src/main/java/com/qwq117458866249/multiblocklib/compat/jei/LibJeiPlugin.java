package com.qwq117458866249.multiblocklib.compat.jei;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.common.recipes.json.JsonRecipe;
import com.qwq117458866249.multiblocklib.common.recipes.json.JsonStructure;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class LibJeiPlugin implements IModPlugin {

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(MultiblockLib.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new StructureCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(IRecipeType.create(Identifier.parse("multiblocklibes:recipe"), JsonRecipe.class), JsonRecipe.recipes);
        registration.addRecipes(IRecipeType.create(Identifier.parse("multiblocklibes:structure"), JsonStructure.class), JsonStructure.recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

    }
}
