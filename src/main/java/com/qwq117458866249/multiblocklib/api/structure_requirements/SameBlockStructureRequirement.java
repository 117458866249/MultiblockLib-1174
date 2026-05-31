package com.qwq117458866249.multiblocklib.api.structure_requirements;

import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SameBlockStructureRequirement extends StructureRequirement {
    public ArrayList<BlockPos> sameBlocks;

    public SameBlockStructureRequirement(ArrayList<BlockPos> sameBlocks) {
        this.sameBlocks = sameBlocks;
    }

    @Override
    public boolean cantForm(BlockPos pos, Level level, Direction face, Structure structure) {
        AtomicReference<BlockPos> temp = new AtomicReference<>();
        temp.set(null);
        AtomicBoolean cant = new AtomicBoolean(false);
        sameBlocks.forEach(samePos -> {
            if (temp.get() != null) {
                if (
                        !level.getBlockState(
                                Util.getAbsPos(
                                        pos, Util.getDirectionPos(samePos, face)
                                )
                        ).is(
                                level.getBlockState(
                                        Util.getAbsPos(
                                                pos, Util.getDirectionPos(temp.get(), face)
                                        )
                                ).getBlock()
                        )
                ) {
                    cant.set(true);
                }
            }
            temp.set(samePos);
        });
        return cant.get();
    }

    @Override
    public Component getDesc() {
        return Component.empty();
    }
}
