package com.qwq117458866249.multiblocklib.common.template.controller;

import com.qwq117458866249.multiblocklib.api.EmptyRecipe;
import com.qwq117458866249.multiblocklib.api.EmptyStructure;
import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;

public abstract class ControllerBlockEntity extends BlockEntity {
    public abstract ArrayList<Structure> allStructures();

    public int tick = 0;
    public String formedAs = "";
    public String parsingRecipe = "";

    public ControllerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    public void form(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(ControllerBlock.FORMED, true), 3);
    }

    public void unForm(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(ControllerBlock.FORMED, false), 3);
    }

    public void work(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(ControllerBlock.WORKING, true), 3);
    }

    public void noWork(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(ControllerBlock.WORKING, false), 3);
    }

    public void entityTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }
        if (formedAs.isEmpty()) {
            allStructures().forEach(p -> {
                if (formedAs.isEmpty()) {
                    String temp = p.formedAs(pos, level, state.getValue(ControllerBlock.FACING));
                    formedAs = temp;
                    if (!formedAs.isEmpty()) {
                        p.form(pos, level, state.getValue(ControllerBlock.FACING));
                        form(level, pos);
                    }
                }
            });
        } else {
            if (Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).formedAs(pos, level, state.getValue(ControllerBlock.FACING)).isEmpty()) {
                Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).unForm(pos, level, state.getValue(ControllerBlock.FACING));
                unForm(level, pos);
                formedAs = "";
            }
        }


        if (state.getValue(ControllerBlock.FORMED)) {
            if (parsingRecipe.isEmpty() && (!Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure())).recipeId().isEmpty())) {
                parsingRecipe = Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure())).recipeId();
                Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure())).inputRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()));
            }

            if (!parsingRecipe.isEmpty()) {
                work(level, pos);
                tick++;
            } else {
                noWork(level, pos);
                tick = 0;
            }

            if (tick >= Recipe.allRecipes.getOrDefault(parsingRecipe, new EmptyRecipe()).parsingTime() && !parsingRecipe.isEmpty()) {
                Recipe.allRecipes.getOrDefault(parsingRecipe, new EmptyRecipe()).outputRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()));
                parsingRecipe = "";
                tick = 0;
                if (!Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure())).recipeId().isEmpty()) {
                    parsingRecipe = Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure())).recipeId();
                    Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure())).inputRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()));
                }
            }
        } else {
            noWork(level, pos);
            tick = 0;
        }

        if (tick % 10 == 1) {
            level.playSound(
                    null,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    SoundEvent.createVariableRangeEvent(Identifier.parse("multiblocklibes:" + formedAs)),
                    SoundSource.VOICE,
                    1,
                    1
            );
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("entityTicker", tick);
        output.putString("formedAs", formedAs);
        output.putString("parsingRecipe", parsingRecipe);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        tick = input.getInt("entityTicker").get();
        formedAs = input.getString("formedAs").get();
        parsingRecipe = input.getString("parsingRecipe").get();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).unForm(pos, level, state.getValue(ControllerBlock.FACING));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ControllerBlockEntity entity) {
        entity.entityTick(level, pos, state);
    }
}
