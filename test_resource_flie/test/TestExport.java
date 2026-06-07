package com.qwq117458866249.multiblocklib.test;

import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class TestExport extends Structure {
    @Override
    public HashMap<BlockPos, ArrayList<String>> blocks() {
        HashMap<BlockPos, ArrayList<String>> t =new HashMap<>();
        
        t.put(new BlockPos(1, 0, 0), Util.getStringList("multiblocklibes:test_item_port_in"));
        t.put(new BlockPos(-1, 0, 0), Util.getStringList("multiblocklibes:test_item_port_ou"));
        t.put(new BlockPos(1, 1, 0), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(0, 1, 0), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(-1, 1, 0), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(1, 3, 0), Util.getStringList("minecraft:stripped_jungle_log"));
        t.put(new BlockPos(0, 3, 0), Util.getStringList("minecraft:stripped_jungle_log"));
        t.put(new BlockPos(-1, 3, 0), Util.getStringList("minecraft:stripped_jungle_log"));
        t.put(new BlockPos(1, 0, 1), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(0, 0, 1), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(-1, 0, 1), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(1, 1, 1), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(-1, 1, 1), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(1, 2, 1), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(0, 2, 1), Util.getStringList("minecraft:birch_planks"));
        t.put(new BlockPos(-1, 2, 1), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(0, 3, 1), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(1, 0, 2), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(0, 0, 2), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(-1, 0, 2), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(1, 1, 2), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(-1, 1, 2), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(0, 3, 2), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(1, 0, 3), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(0, 0, 3), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(-1, 0, 3), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(1, 1, 3), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(-1, 1, 3), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(1, 2, 3), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(0, 2, 3), Util.getStringList("minecraft:birch_planks"));
        t.put(new BlockPos(-1, 2, 3), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(0, 3, 3), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(1, 0, 4), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(0, 0, 4), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(-1, 0, 4), Util.getStringList("minecraft:oxidized_cut_copper"));
        t.put(new BlockPos(1, 1, 4), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(0, 1, 4), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(-1, 1, 4), Util.getStringList("minecraft:diamond_block"));
        t.put(new BlockPos(1, 3, 4), Util.getStringList("minecraft:stripped_jungle_log"));
        t.put(new BlockPos(0, 3, 4), Util.getStringList("minecraft:stripped_jungle_log"));
        t.put(new BlockPos(-1, 3, 4), Util.getStringList("minecraft:stripped_jungle_log"));
        
        return t;
    }

    @Override
    public String structureId() {
        return "blyat";
    }

    @Override
    public ArrayList<StructureRequirement> requirements() {
        ArrayList<StructureRequirement> t = new ArrayList<>();
        return t;
    }

    @Override
    public ArrayList<Recipe> recipes() {
        ArrayList<Recipe> t = new ArrayList<>();
        return t;
    }

    static {
        allStructures.put(new TestExport().structureId(), new TestExport());
    }
}