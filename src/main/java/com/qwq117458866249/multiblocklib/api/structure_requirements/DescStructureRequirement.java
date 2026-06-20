package com.qwq117458866249.multiblocklib.api.structure_requirements;

import com.google.gson.JsonElement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class DescStructureRequirement extends StructureRequirement {
    public Component desc;

    public DescStructureRequirement(Component desc) {
        this.desc = desc;
    }

    @Override
    public boolean cantForm(BlockPos pos, Level level, Direction face, Structure structure) {
        return false;
    }

    @Override
    public Component getDesc() {
        return desc;
    }

    public static void register() {
    }

    static {
        allStructureRequirements.put("desc_structure_requirement", obj -> new DescStructureRequirement(
                switch (((JsonElement) obj[0]).getAsString()) {
                    case "tran" -> Component.translatable(((JsonElement) obj[1]).getAsString());
                    default -> Component.literal(((JsonElement) obj[1]).getAsString());
                }
        ));
    }
}
