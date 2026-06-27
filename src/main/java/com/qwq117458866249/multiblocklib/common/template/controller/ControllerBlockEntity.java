package com.qwq117458866249.multiblocklib.common.template.controller;

import com.qwq117458866249.multiblocklib.api.interfaces.IControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public abstract class ControllerBlockEntity extends BlockEntity implements IControllerBlockEntity {
    public int tick = 0;
    public int recipeParallels = 0;
    public List<Display.TextDisplay> allDisplays = new ArrayList<>();
    public String formedAs = "";
    public String parsingRecipe = "";

    public ControllerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        iSaveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        iLoadAdditional(input);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        iPreRemoveSideEffects(pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, IControllerBlockEntity entity) {
        entity.entityTick(level, pos, state);
    }

    @Override
    public int getTick() {
        return tick;
    }

    @Override
    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public int getRecipeParallels() {
        return recipeParallels;
    }

    @Override
    public void setRecipeParallels(int recipeParallels) {
        this.recipeParallels = recipeParallels;
    }

    @Override
    public List<Display.TextDisplay> getAllDisplays() {
        return allDisplays;
    }

    @Override
    public void setAllDisplays(List<Display.TextDisplay> allDisplays) {
        this.allDisplays = allDisplays;
    }

    @Override
    public String getFormedAs() {
        return formedAs;
    }

    @Override
    public void setFormedAs(String formedAs) {
        this.formedAs = formedAs;
    }

    @Override
    public String getParsingRecipe() {
        return parsingRecipe;
    }

    @Override
    public void setParsingRecipe(String parsingRecipe) {
        this.parsingRecipe = parsingRecipe;
    }

    @Override
    public BlockState iGetBlockState() {
        return getBlockState();
    }

    @Override
    public Level iGetLevel() {
        return getLevel();
    }
}
