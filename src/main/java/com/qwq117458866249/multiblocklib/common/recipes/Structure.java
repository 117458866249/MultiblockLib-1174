package com.qwq117458866249.multiblocklib.common.recipes;

import com.google.common.collect.HashBiMap;
import com.qwq117458866249.multiblocklib.api.EmptyRecipe;
import com.qwq117458866249.multiblocklib.api.IAbleToForm;
import com.qwq117458866249.multiblocklib.util.Info;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Info(m = "We should add these code to your structure code")
@Info(m = "static {                                                                           ")
@Info(m = "    allStructures.put(new ExampleStructure().structureId(),new ExampleStructure());")
@Info(m = "}                                                                                  ")
public abstract class Structure {
    public static final HashBiMap<String, Structure> allStructures = HashBiMap.create();

    public abstract HashMap<BlockPos, ArrayList<String>> blocks();

    public abstract String structureId();

    public abstract ArrayList<StructureRequirement> requirements();

    public abstract ArrayList<Recipe> recipes();

    public String formedAs(BlockPos pos, Level level, Direction face) {
        AtomicBoolean ableToReturn = new AtomicBoolean(true);
        blocks().forEach((eachPos, availableBlocks) -> {
            if (ableToReturn.get()) {
                ableToReturn.set(false);
                availableBlocks.forEach(availableBlock -> {
                    if (availableBlock.charAt(0) == '#') {
                        if (
                                level.getBlockState(
                                        Util.getAbsPos(
                                                pos, Util.getDirectionPos(eachPos, face)
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
                                                pos, Util.getDirectionPos(eachPos, face)
                                        )
                                ).is(
                                        BuiltInRegistries.BLOCK.getValue(Identifier.parse(availableBlock))
                                )
                        ) {
                            ableToReturn.set(true);
                        }
                    }
                });
            }
        });
        for (StructureRequirement p : requirements()) {
            if (ableToReturn.get() && p.cantForm(pos, level, face, this)) {
                ableToReturn.set(false);
            }
        }
        if (ableToReturn.get()) {
            return structureId();
        }
        return "";
    }

    public void form(BlockPos pos, Level level, Direction face) {
        blocks().forEach((eachPos, availableBlocks) -> {
            if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).getBlock() instanceof IAbleToForm block) {
                block.form(level, Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face)));
            }
        });
    }

    public void unForm(BlockPos pos, Level level, Direction face) {
        blocks().forEach((eachPos, availableBlocks) -> {
            if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).getBlock() instanceof IAbleToForm block) {
                block.unForm(level, Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face)));
            }
        });
    }

    public Recipe parseAbleRecipe(BlockPos pos, Level level, Direction face, Structure structure) {
        for (Recipe recipe : recipes()) {
            if (recipe.canParseRecipe(pos, level, face, structure)) {
                return recipe;
            }
        }
        return new EmptyRecipe();
    }
}
