package com.qwq117458866249.multiblocklib.api.recipe_requirements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

import java.util.ArrayList;
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
    public boolean onlyDetectOnce() {
        return true;
    }

    public static void register() {
    }

    static {
        allRecipeRequirements.put("block_recipe_requirement", obj -> {
            ArrayList<String> temp = new ArrayList<>();
            ((JsonArray) obj[1]).asList().forEach(each -> temp.add(each.getAsString()));
            return new BlockRecipeRequirement(
                    IOMode.get(((JsonElement) obj[0]).getAsString()),
                    temp,
                    new BlockPos(
                            ((JsonElement) obj[2]).getAsInt(),
                            ((JsonElement) obj[3]).getAsInt(),
                            ((JsonElement) obj[4]).getAsInt()
                    )
            );
        });
    }
}
