package com.qwq117458866249.multiblocklib.api.structure_requirements;

import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.recipes.StructureRequirement;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MaxBlocksStructureRequirement extends StructureRequirement {

    public ArrayList<String> blocks;
    public int value;

    public MaxBlocksStructureRequirement(ArrayList<String> blocks, int value) {
        this.blocks = blocks;
        this.value = value;
    }

    @Override
    public boolean cantForm(BlockPos pos, Level level, Direction face, Structure structure) {
        AtomicInteger availableBlocks = new AtomicInteger();
        structure.blocks().forEach((eachPos, z) -> {
            blocks.forEach(block -> {
                if (block.charAt(0) == '#') {
                    if (
                            level.getBlockState(
                                    Util.getAbsPos(
                                            pos, Util.getDirectionPos(eachPos, face)
                                    )
                            ).is(
                                    TagKey.create(
                                            BuiltInRegistries.BLOCK.key(),
                                            Identifier.parse(
                                                    Util.getPath(block)
                                            )
                                    ))
                    ) {
                        availableBlocks.getAndIncrement();
                    }
                } else {
                    if (
                            level.getBlockState(
                                    Util.getAbsPos(
                                            pos, Util.getDirectionPos(eachPos, face)
                                    )
                            ).is(
                                    BuiltInRegistries.BLOCK.getValue(Identifier.parse(block))
                            )
                    ) {
                        availableBlocks.getAndIncrement();
                    }
                }
            });
        });
        return availableBlocks.get() > value;
    }

    @Override
    public Component getDesc() {
        return Component.literal(Component.translatable("requirement.multiblocklib.max.f").getString() + value + Component.translatable("requirement.multiblocklib.max.b").getString());
    }
}
