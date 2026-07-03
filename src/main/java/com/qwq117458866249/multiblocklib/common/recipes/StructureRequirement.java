package com.qwq117458866249.multiblocklib.common.recipes;

import com.google.gson.JsonObject;
import com.qwq117458866249.multiblocklib.api.structure_requirements.DescStructureRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public abstract class StructureRequirement {
    public abstract boolean cantForm(BlockPos pos, Level level, Direction face, MultiblockStructure structure);

    public Component getDesc(){
        return Component.empty();
    }

    @Override
    public String toString() {
        return getDesc().getString();
    }

    // Json
    public static final HashMap<String, StructureRequirement.AbstractCreator> allStructureRequirements = new HashMap<>();

    public static StructureRequirement fromJson(JsonObject json) {
        try {
            return allStructureRequirements.get(json.get("id").getAsString()).get(json.get("property").getAsJsonArray().asList().toArray());
        } catch (Exception ignored) {
            return new DescStructureRequirement(Component.empty());
        }
    }

    public interface AbstractCreator {
        StructureRequirement get(Object... obj);
    }
}