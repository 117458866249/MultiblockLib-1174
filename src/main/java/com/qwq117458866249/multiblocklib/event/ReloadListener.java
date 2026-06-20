package com.qwq117458866249.multiblocklib.event;

import com.google.common.collect.HashBiMap;
import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.json.JsonRecipe;
import com.qwq117458866249.multiblocklib.common.recipes.json.JsonStructure;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.util.ArrayList;
import java.util.HashMap;

@EventBusSubscriber(modid = MultiblockLib.MOD_ID)
public class ReloadListener {
    public static void load(RecipeManager manager) {
        Structure.allStructures = HashBiMap.create();
        Recipe.allRecipes = HashBiMap.create();

        manager.recipeMap().byType(Register.RECIPE_TYPE.get()).forEach(jsonRecipeRecipeHolder -> {
            JsonRecipe recipe = jsonRecipeRecipeHolder.value();
            Recipe.allRecipes.put(recipe.recipeId, new Recipe() {
                @Override
                public ArrayList<RecipeRequirement> recipeRequirements() {
                    ArrayList<RecipeRequirement> t = new ArrayList<>();
                    recipe.jsonObjects.forEach(jsonObject -> {
                        t.add(RecipeRequirement.fromJson(jsonObject));
                    });
                    return t;
                }

                @Override
                public int parsingTime() {
                    return recipe.time;
                }

                @Override
                public String recipeId() {
                    return recipe.recipeId;
                }

                @Override
                public String structureId() {
                    return recipe.structureId;
                }
            });
        });

        manager.recipeMap().byType(Register.STRUCTURE_TYPE.get()).forEach(jsonStructureRecipeHolder -> {
            JsonStructure structure = jsonStructureRecipeHolder.value();
            Structure.allStructures.put(structure.structureId, new Structure() {
                @Override
                public HashMap<BlockPos, ArrayList<String>> blocks() {
                    return structure.blocks;
                }

                @Override
                public String structureId() {
                    return structure.structureId;
                }

                @Override
                public String controllerId() {
                    return structure.controllerId;
                }

                @Override
                public ArrayList<StructureRequirement> requirements() {
                    ArrayList<StructureRequirement> t = new ArrayList<>();
                    structure.jsonObjects.forEach(jsonObject -> {
                        t.add(StructureRequirement.fromJson(jsonObject));
                    });
                    return t;
                }
            });
        });
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent e) {
        load(e.getServer().getRecipeManager());
    }

    @SubscribeEvent
    public static void onReloadServerResources(AddServerReloadListenersEvent e) {
        e.addListener(Identifier.parse("multiblocklibes:listener"), new SimplePreparableReloadListener<Void>() {
            @Override
            protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(Void object, ResourceManager resourceManager, ProfilerFiller profiler) {
                load(e.getServerResources().getRecipeManager());
            }
        });
    }
}
