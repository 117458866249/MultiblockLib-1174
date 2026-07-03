package com.qwq117458866249.multiblocklib.api;

import com.qwq117458866249.multiblocklib.common.recipes.MultiblockRecipe;
import com.qwq117458866249.multiblocklib.common.recipes.MultiblockStructure;
import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public final class EmptyStructure extends MultiblockStructure {
    @Override
    public HashMap<BlockPos, ArrayList<String>> blocks() {
        return new HashMap<>();
    }

    @Override
    public String structureId() {
        return "";
    }

    @Override
    public String controllerId() {
        return "minecraft:air";
    }

    @Override
    public ArrayList<StructureRequirement> requirements() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<MultiblockRecipe> recipes() {
        return new ArrayList<>();
    }

    static {
        allStructures.put(new EmptyStructure().structureId(), new EmptyStructure());
    }
}
