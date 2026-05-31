package com.qwq117458866249.multiblocklib.test;

import com.qwq117458866249.multiblocklib.api.structure_requirements.RightCountBlockStructureRequirement;
import com.qwq117458866249.multiblocklib.api.structure_requirements.SameBlockStructureRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public class TestStructure extends Structure {
    @Override
    public HashMap<BlockPos, ArrayList<String>> blocks() {
        HashMap<BlockPos, ArrayList<String>> t = new HashMap<>();
        ArrayList<String> coil = Util.getStringList("minecraft:diamond_block", "minecraft:emerald_block");
        ArrayList<String> struc = Util.getStringList("minecraft:oxidized_cut_copper", "multiblocklibes:test_item_port_in", "multiblocklibes:test_item_port_ou", "multiblocklibes:test_fluid_port_in", "multiblocklibes:test_fluid_port_ou","multiblocklibes:test_fe_port");
        t.put(new BlockPos(1, 0, 0), struc);
        t.put(new BlockPos(-1, 0, 0), struc);
        t.put(new BlockPos(1, 0, 1), struc);
        t.put(new BlockPos(0, 0, 1), struc);
        t.put(new BlockPos(-1, 0, 1), struc);
        t.put(new BlockPos(1, 0, 2), struc);
        t.put(new BlockPos(0, 0, 2), struc);
        t.put(new BlockPos(-1, 0, 2), struc);

        t.put(new BlockPos(1, 3, 0), struc);
        t.put(new BlockPos(0, 3, 0), struc);
        t.put(new BlockPos(-1, 3, 0), struc);
        t.put(new BlockPos(1, 3, 1), struc);
        t.put(new BlockPos(0, 3, 1), Util.getStringList("multiblocklibes:test_waste"));
        t.put(new BlockPos(-1, 3, 1), struc);
        t.put(new BlockPos(1, 3, 2), struc);
        t.put(new BlockPos(0, 3, 2), struc);
        t.put(new BlockPos(-1, 3, 2), struc);

        t.put(new BlockPos(0, 1, 0), coil);
        t.put(new BlockPos(1, 1, 0), coil);
        t.put(new BlockPos(-1, 1, 0), coil);
        t.put(new BlockPos(1, 1, 1), coil);
        t.put(new BlockPos(-1, 1, 1), coil);
        t.put(new BlockPos(1, 1, 2), coil);
        t.put(new BlockPos(0, 1, 2), coil);
        t.put(new BlockPos(-1, 1, 2), coil);

        t.put(new BlockPos(0, 2, 0), coil);
        t.put(new BlockPos(1, 2, 0), coil);
        t.put(new BlockPos(-1, 2, 0), coil);
        t.put(new BlockPos(1, 2, 1), coil);
        t.put(new BlockPos(-1, 2, 1), coil);
        t.put(new BlockPos(1, 2, 2), coil);
        t.put(new BlockPos(0, 2, 2), coil);
        t.put(new BlockPos(-1, 2, 2), coil);

        return t;
    }

    @Override
    public String structureId() {
        return "blast";
    }

    @Override
    public ArrayList<StructureRequirement> requirements() {
        ArrayList<StructureRequirement> t = new ArrayList<>();
        ArrayList<BlockPos> t1 = new ArrayList<>();

        t1.add(new BlockPos(0, 1, 0));
        t1.add(new BlockPos(1, 1, 0));
        t1.add(new BlockPos(-1, 1, 0));
        t1.add(new BlockPos(1, 1, 1));
        t1.add(new BlockPos(-1, 1, 1));
        t1.add(new BlockPos(1, 1, 2));
        t1.add(new BlockPos(0, 1, 2));
        t1.add(new BlockPos(-1, 1, 2));

        t1.add(new BlockPos(0, 2, 0));
        t1.add(new BlockPos(1, 2, 0));
        t1.add(new BlockPos(-1, 2, 0));
        t1.add(new BlockPos(1, 2, 1));
        t1.add(new BlockPos(-1, 2, 1));
        t1.add(new BlockPos(1, 2, 2));
        t1.add(new BlockPos(0, 2, 2));
        t1.add(new BlockPos(-1, 2, 2));

        t.add(new RightCountBlockStructureRequirement(Util.getStringList("multiblocklibes:test_item_port_in"), 1));
        t.add(new RightCountBlockStructureRequirement(Util.getStringList("multiblocklibes:test_item_port_ou"), 1));
        t.add(new RightCountBlockStructureRequirement(Util.getStringList("multiblocklibes:test_fluid_port_in"), 1));
        t.add(new RightCountBlockStructureRequirement(Util.getStringList("multiblocklibes:test_fluid_port_ou"), 1));
        t.add(new RightCountBlockStructureRequirement(Util.getStringList("multiblocklibes:test_fe_port"), 1));
        t.add(new SameBlockStructureRequirement(t1));
        return t;
    }

    @Override
    public ArrayList<Recipe> recipes() {
        ArrayList<Recipe> t = new ArrayList<>();
        t.add(new TestRecipe());
        return t;
    }

    static {
        allStructures.put(new TestStructure().structureId(), new TestStructure());
    }
}
