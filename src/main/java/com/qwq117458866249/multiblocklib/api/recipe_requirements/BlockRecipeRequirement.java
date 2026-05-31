package com.qwq117458866249.multiblocklib.api.recipe_requirements;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockRecipeRequirement extends RecipeRequirement {
    public final List<String> block;
    public final BlockPos absPos;

    public BlockRecipeRequirement(IOMode io, List<String> block, BlockPos absPos) {
        super(io);
        this.block = block;
        this.absPos = absPos;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        AtomicBoolean ableToReturn = new AtomicBoolean(false);
        block.forEach(availableBlock -> {
            if (availableBlock.charAt(0) == '#') {
                if (
                        level.getBlockState(
                                Util.getAbsPos(
                                        pos, Util.getDirectionPos(absPos, face)
                                )
                        ).is(
                                TagKey.create(
                                        BuiltInRegistries.BLOCK.key(),
                                        Identifier.parse(
                                                Util.getPath(availableBlock)
                                        )
                                ))
                ) {
                    ableToReturn.set(true);
                }
            } else {
                if (
                        level.getBlockState(
                                Util.getAbsPos(
                                        pos, Util.getDirectionPos(absPos, face)
                                )
                        ).is(
                                BuiltInRegistries.BLOCK.getValue(Identifier.parse(availableBlock))
                        )
                ) {
                    ableToReturn.set(true);
                }
            }
        });
        if (ableToReturn.get()) {
            return ParseResult.SUCCESS;
        } else {
            return ParseResult.FAILED;
        }
    }

    @Override
    public void inputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        // Attack the D point!
    }

    @Override
    public void outputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        if (Math.random() >= chance) return;
        block.forEach(availableBlock -> {
            level.setBlock(
                    Util.getAbsPos(
                            pos, Util.getDirectionPos(absPos, face)
                    ),
                    BuiltInRegistries.BLOCK.getValue(Identifier.parse(availableBlock)).defaultBlockState(),
                    3
            );
        });
    }

    @Override
    public Component getDesc() {
        return Component.empty();
    }
}
