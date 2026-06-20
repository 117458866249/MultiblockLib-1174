package com.qwq117458866249.multiblocklib.common.template.controller;

import com.qwq117458866249.multiblocklib.api.EmptyRecipe;
import com.qwq117458866249.multiblocklib.api.EmptyStructure;
import com.qwq117458866249.multiblocklib.common.recipes.Recipe;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.mixin.DisplayEntityAccessor;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class ControllerBlockEntity extends BlockEntity {
    public ArrayList<Structure> allStructures() {
        ArrayList<Structure> temp = new ArrayList<>();
        Structure.allStructures.forEach((_, structure) -> {
            if (structure.controllerId().equals(BuiltInRegistries.BLOCK.wrapAsHolder(getBlockState().getBlock()).getRegisteredName())) {
                temp.add(structure);
            }
        });
        return temp;
    }

    public int tick = 0;
    public int recipeParallels = 0;
    public List<Display.TextDisplay> allDisplays = new ArrayList<>();
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

    public int getParallels() {
        return 1;
    }

    public int getParseSpeed() {
        return 1;
    }

    public Component display() {
        return Component.translatable("key.multiblocklib.formedas").append(Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).structureId().isEmpty() ? Component.translatable("key.multiblocklib.none") : Component.translatable("multiblock_structure" + Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).structureId())).append("\n")
                .append(Component.translatable("key.multiblocklib.progress")).append((Recipe.allRecipes.getOrDefault(parsingRecipe, new EmptyRecipe()).parsingTime() == 0 ? 0 : ((Number) (((Number) tick).floatValue() / ((Number) Recipe.allRecipes.getOrDefault(parsingRecipe, new EmptyRecipe()).parsingTime()).floatValue() * 100)).intValue()) + "%").append("\n")
                .append(Component.translatable("key.multiblocklib.parsespeed")).append(getParseSpeed() + "x").append("\n")
                .append(Component.translatable("key.multiblocklib.parallels")).append(getParallels() + "x");
    }

    public void entityTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        // 1. Progress
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
            if (parsingRecipe.isEmpty() && (!Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()), false).recipeId().isEmpty())) {
                parsingRecipe = Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()), false).recipeId();
                for (int i = 0; i < getParallels(); i++) {
                    if (!Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()), true).recipeId().isEmpty()) {
                        Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()), true).inputRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()), i == 0);
                        recipeParallels++;
                    } else break;
                }
            }

            if (!parsingRecipe.isEmpty()) {
                work(level, pos);
                tick += getParseSpeed();
            } else {
                noWork(level, pos);
                tick = 0;
            }

            if (tick >= Recipe.allRecipes.getOrDefault(parsingRecipe, new EmptyRecipe()).parsingTime() && !parsingRecipe.isEmpty()) {
                for (int i = 0; i < recipeParallels; i++) {
                    Recipe.allRecipes.getOrDefault(parsingRecipe, new EmptyRecipe()).outputRecipe(pos, level, state.getValue(ControllerBlock.FACING), Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()), i == 0);
                }
                parsingRecipe = "";
                tick = 0;
                recipeParallels = 0;
            }
        } else {
            noWork(level, pos);
            tick = 0;
            recipeParallels = 0;
        }

        // 2. Sound
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

        // 3. Dashboard
        allDisplays = level.getEntitiesOfClass(Display.TextDisplay.class, new AABB(
                pos.getX() - 3,
                pos.getY() - 3,
                pos.getZ() - 3,
                pos.getX() + 3,
                pos.getY() + 3,
                pos.getZ() + 3
        ));

        if (allDisplays.isEmpty()) {
            Display.TextDisplay textDisplay = new Display.TextDisplay(EntityType.TEXT_DISPLAY, level);
            DisplayEntityAccessor accessor = (DisplayEntityAccessor) textDisplay;
            textDisplay.setPos(
                    new Vec3(
                            pos.getX() + 0.5 + Util.getDirectionalPos(state.getValue(ControllerBlock.FACING)).getX(),
                            pos.getY() + 1.5 + Util.getDirectionalPos(state.getValue(ControllerBlock.FACING)).getY(),
                            pos.getZ() + 0.5 + Util.getDirectionalPos(state.getValue(ControllerBlock.FACING)).getZ()
                    )
            );
            accessor.invokerSetText(display());
            switch (state.getValue(ControllerBlock.FACING)) {
                case WEST -> textDisplay.setYRot(90);
                case NORTH -> textDisplay.setYRot(180);
                case EAST -> textDisplay.setYRot(270);
            }
            level.addFreshEntity(textDisplay);
        } else {
            DisplayEntityAccessor accessor = (DisplayEntityAccessor) allDisplays.getFirst();
            accessor.invokerSetText(display());
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("entityTicker", tick);
        output.putInt("recipeParallels", recipeParallels);
        output.putString("formedAs", formedAs);
        output.putString("parsingRecipe", parsingRecipe);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        tick = input.getInt("entityTicker").get();
        recipeParallels = input.getInt("recipeParallels").get();
        formedAs = input.getString("formedAs").get();
        parsingRecipe = input.getString("parsingRecipe").get();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        Structure.allStructures.getOrDefault(formedAs, new EmptyStructure()).unForm(pos, level, state.getValue(ControllerBlock.FACING));
        allDisplays.forEach((display) -> display.remove(Entity.RemovalReason.KILLED));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ControllerBlockEntity entity) {
        entity.entityTick(level, pos, state);
    }
}
