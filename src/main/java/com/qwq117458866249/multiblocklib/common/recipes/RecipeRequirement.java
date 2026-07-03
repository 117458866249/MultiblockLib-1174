package com.qwq117458866249.multiblocklib.common.recipes;

import com.google.gson.JsonObject;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.DescRecipeRequirement;
import com.qwq117458866249.multiblocklib.util.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public abstract class RecipeRequirement {
    public final boolean isOutput;

    public RecipeRequirement setChance(int value) {
        chance = value;
        return this;
    }

    protected double chance = ((Number) 1).doubleValue();

    public RecipeRequirement(IOMode io) {
        this.isOutput = io.equals(IOMode.OUTPUT);
    }

    public abstract ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure);

    public abstract void inputRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure);

    @Info(m = "Add this code to your Requirement")
    @Info(m = "if (Math.random() >= chance) return;")
    public abstract void outputRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure);

    public Component getDesc(){
        return Component.empty();
    }

    // Parallels
    public boolean detectOnce = false;

    public boolean onlyDetectOnce() {
        return detectOnce;
    }

    public RecipeRequirement setDetectOnce() {
        detectOnce = true;
        return this;
    }

    // Json
    public static final HashMap<String, AbstractCreator> allRecipeRequirements = new HashMap<>();

    public static RecipeRequirement fromJson(JsonObject json) {
        try {
            try {
                if (json.get("once").isJsonObject() && json.get("once").getAsBoolean()) {
                    return allRecipeRequirements.get(json.get("id").getAsString()).get(json.get("property").getAsJsonArray().asList().toArray()).setDetectOnce();
                }
            } catch (Exception _) {
            }
            return allRecipeRequirements.get(json.get("id").getAsString()).get(json.get("property").getAsJsonArray().asList().toArray());
        } catch (Exception _) {
            return new DescRecipeRequirement(IOMode.BOTH, Component.empty());
        }
    }

    public interface AbstractCreator {
        RecipeRequirement get(Object... obj);
    }
}
