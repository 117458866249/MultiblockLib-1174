package com.qwq117458866249.multiblocklib.api.interfaces;

import com.qwq117458866249.multiblocklib.api.EmptyRecipe;
import com.qwq117458866249.multiblocklib.api.EmptyStructure;
import com.qwq117458866249.multiblocklib.common.recipes.MultiblockRecipe;
import com.qwq117458866249.multiblocklib.common.recipes.MultiblockStructure;
import com.qwq117458866249.multiblocklib.mixin.TextDisplayEntityAccessor;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public interface IControllerBlockEntity {
    int getTick();

    void setTick(int tick);

    int getRecipeParallels();

    void setRecipeParallels(int recipeParallels);

    List<Display.TextDisplay> getAllDisplays();

    void setAllDisplays(List<Display.TextDisplay> allDisplays);

    String getFormedAs();

    void setFormedAs(String formedAs);

    String getParsingRecipe();

    void setParsingRecipe(String parsingRecipe);

    BlockState iGetBlockState();

    Level iGetLevel();

    default ArrayList<MultiblockStructure> allStructures() {
        ArrayList<MultiblockStructure> temp = new ArrayList<>();
        MultiblockStructure.allStructures.forEach((_, structure) -> {
            if (structure.controllerId().equals(BuiltInRegistries.BLOCK.wrapAsHolder(iGetBlockState().getBlock()).getRegisteredName())) {
                temp.add(structure);
            }
        });
        return temp;
    }

    default void form(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(IControllerBlock.FORMED, true), 3);
    }

    default void unForm(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(IControllerBlock.FORMED, false), 3);
    }

    default void work(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(IControllerBlock.WORKING, true), 3);
    }

    default void noWork(Level level, BlockPos pos) {
        level.setBlock(pos, level.getBlockState(pos).setValue(IControllerBlock.WORKING, false), 3);
    }

    default int getParallels() {
        return 1;
    }

    default int getParseSpeed() {
        return 1;
    }

    default Component display() {
        return Component.translatable("key.multiblocklib.formedas").append(MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()).structureId().isEmpty() ? Component.translatable("key.multiblocklib.none") : Component.translatable("multiblock_structure." + MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()).structureId())).append("\n")
                .append(Component.translatable("key.multiblocklib.progress")).append((MultiblockRecipe.allRecipes.getOrDefault(getParsingRecipe(), new EmptyRecipe()).parsingTime() == 0 ? 0 : ((Number) (((Number) getTick()).floatValue() / ((Number) MultiblockRecipe.allRecipes.getOrDefault(getParsingRecipe(), new EmptyRecipe()).parsingTime()).floatValue() * 100)).intValue()) + "%").append("\n")
                .append(Component.translatable("key.multiblocklib.parsespeed")).append(getParseSpeed() + "x").append("\n")
                .append(Component.translatable("key.multiblocklib.parallels")).append(getParallels() + "x");
    }

    default void entityTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        // 1. Progress
        if (getFormedAs().isEmpty()) {
            allStructures().forEach(p -> {
                if (getFormedAs().isEmpty()) {
                    String temp = p.formedAs(pos, level, state.getValue(IControllerBlock.FACING));
                    setFormedAs(temp);
                    if (!getFormedAs().isEmpty()) {
                        p.form(pos, level, state.getValue(IControllerBlock.FACING));
                        form(level, pos);
                    }
                }
            });
        } else {
            if (
                    MultiblockStructure.allStructures
                            .getOrDefault(getFormedAs(), new EmptyStructure())
                            .formedAs(pos, level, state.getValue(IControllerBlock.FACING))
                            .isEmpty()
            ) {
                MultiblockStructure.allStructures
                        .getOrDefault(getFormedAs(), new EmptyStructure())
                        .unForm(pos, level, state.getValue(IControllerBlock.FACING));
                unForm(level, pos);
                setFormedAs("");
            }
        }


        if (state.getValue(IControllerBlock.FORMED)) {
            if (
                    getParsingRecipe().isEmpty() &&
                            (
                                    !MultiblockStructure.allStructures
                                            .getOrDefault(getFormedAs(), new EmptyStructure())
                                            .parseAbleRecipe(pos, level, state.getValue(IControllerBlock.FACING), MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()), false)
                                            .recipeId().isEmpty()
                            )
            ) {
                setParsingRecipe(
                        MultiblockStructure.allStructures
                                .getOrDefault(getFormedAs(), new EmptyStructure())
                                .parseAbleRecipe(pos, level, state.getValue(IControllerBlock.FACING), MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()), false)
                                .recipeId()
                );
                for (int i = 0; i < getParallels(); i++) {
                    if (!MultiblockStructure.allStructures
                            .getOrDefault(getFormedAs(), new EmptyStructure())
                            .parseAbleRecipe(pos, level, state.getValue(IControllerBlock.FACING), MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()), true)
                            .recipeId()
                            .isEmpty()) {
                        MultiblockStructure.allStructures
                                .getOrDefault(getFormedAs(), new EmptyStructure()).parseAbleRecipe(pos, level, state.getValue(IControllerBlock.FACING), MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()), true)
                                .inputRecipe(pos, level, state.getValue(IControllerBlock.FACING), MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()), i == 0);
                        setRecipeParallels(getRecipeParallels() + 1);
                    } else break;
                }
            }

            if (!getParsingRecipe().isEmpty()) {
                work(level, pos);
                setTick(getTick() + getParseSpeed());
            } else {
                noWork(level, pos);
                setTick(0);
            }

            if (getTick() >= MultiblockRecipe.allRecipes
                    .getOrDefault(getParsingRecipe(), new EmptyRecipe())
                    .parsingTime() &&
                    !getParsingRecipe()
                            .isEmpty()) {
                for (int i = 0; i < getRecipeParallels(); i++) {
                    MultiblockRecipe.allRecipes
                            .getOrDefault(getParsingRecipe(), new EmptyRecipe())
                            .outputRecipe(pos, level, state.getValue(IControllerBlock.FACING), MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()), i == 0);
                }
                setParsingRecipe("");
                setTick(0);
                setRecipeParallels(0);
            }
        } else {
            noWork(level, pos);
            setTick(0);
            setRecipeParallels(0);
        }

        // 2. Sound
        if (getTick() % 10 == 1) {
            level.playSound(
                    null,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    SoundEvent.createVariableRangeEvent(Identifier.parse("multiblocklibes:" + getFormedAs())),
                    SoundSource.VOICE,
                    1,
                    1
            );
        }

        // 3. Dashboard
        setAllDisplays(level.getEntitiesOfClass(Display.TextDisplay.class, new AABB(
                pos.getX() - 3,
                pos.getY() - 3,
                pos.getZ() - 3,
                pos.getX() + 3,
                pos.getY() + 3,
                pos.getZ() + 3
        )));

        if (getAllDisplays().isEmpty()) {
            Display.TextDisplay textDisplay = new Display.TextDisplay(EntityType.TEXT_DISPLAY, level);
            TextDisplayEntityAccessor accessor = (TextDisplayEntityAccessor) textDisplay;
            textDisplay.setPos(
                    new Vec3(
                            pos.getX() + 0.5 + Util.getDirectionalPos(state.getValue(IControllerBlock.FACING)).getX(),
                            pos.getY() + 1.5 + Util.getDirectionalPos(state.getValue(IControllerBlock.FACING)).getY(),
                            pos.getZ() + 0.5 + Util.getDirectionalPos(state.getValue(IControllerBlock.FACING)).getZ()
                    )
            );
            accessor.invokerSetText(display());
            switch (state.getValue(IControllerBlock.FACING)) {
                case WEST -> textDisplay.setYRot(90);
                case NORTH -> textDisplay.setYRot(180);
                case EAST -> textDisplay.setYRot(270);
            }
            level.addFreshEntity(textDisplay);
        } else {
            TextDisplayEntityAccessor accessor = (TextDisplayEntityAccessor) getAllDisplays().getFirst();
            accessor.invokerSetText(display());
        }
    }

    default void iSaveAdditional(ValueOutput output) {
        output.putInt("entityTicker", getTick());
        output.putInt("recipeParallels", getRecipeParallels());
        output.putString("formedAs", getFormedAs());
        output.putString("parsingRecipe", getParsingRecipe());
    }

    default void iLoadAdditional(ValueInput input) {
        setTick(input.getInt("entityTicker").get());
        setRecipeParallels(input.getInt("recipeParallels").get());
        setFormedAs(input.getString("formedAs").get());
        setParsingRecipe(input.getString("parsingRecipe").get());
    }

    default void iPreRemoveSideEffects(BlockPos pos, BlockState state) {
        MultiblockStructure.allStructures.getOrDefault(getFormedAs(), new EmptyStructure()).unForm(pos, iGetLevel(), state.getValue(IControllerBlock.FACING));
        getAllDisplays().forEach((display) -> display.remove(Entity.RemovalReason.KILLED));
    }
}
