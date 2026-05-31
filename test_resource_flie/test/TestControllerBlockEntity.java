package com.qwq117458866249.multiblocklib.test;

import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.template.controller.ControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class TestControllerBlockEntity extends ControllerBlockEntity {
    public TestControllerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Register.TEST_CONTROLLER_BE.get(), worldPosition, blockState);
    }

    @Override
    public ArrayList<Structure> allStructures() {
        ArrayList<Structure> t = new ArrayList<>();
        t.add(new TestStructure());
        return t;
    }
}
