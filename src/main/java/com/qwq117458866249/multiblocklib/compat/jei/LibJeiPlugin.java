package com.qwq117458866249.multiblocklib.compat.jei;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.util.SomeProvider;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class LibJeiPlugin implements IModPlugin {

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(MultiblockLib.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = new RecipeManager(new SomeProvider());

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

    }
}
