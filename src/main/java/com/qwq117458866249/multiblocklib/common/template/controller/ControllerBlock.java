package com.qwq117458866249.multiblocklib.common.template.controller;

import com.qwq117458866249.multiblocklib.api.interfaces.IControllerBlock;
import com.qwq117458866249.multiblocklib.util.Info;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jspecify.annotations.Nullable;

public abstract class ControllerBlock extends BaseEntityBlock implements IControllerBlock {
    public ControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        iCreateBlockStateDefinition(builder);
    }

    @Override
    public BlockState iDefaultBlockState() {
        return defaultBlockState();
    }

    @Override
    @org.jetbrains.annotations.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return iGetStateForPlacement(context);
    }

    @Info(m = "@Override                                                                                                                             ")
    @Info(m = "public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {")
    @Info(m = "    return createTickerHelper(type,Register.TEST_CONTROLLER_BE.get(), TestControllerBlockEntity::tick);                               ")
    @Info(m = "}                                                                                                                                     ")
    public abstract @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type);
}
