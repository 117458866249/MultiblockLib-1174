package com.qwq117458866249.multiblocklib.common.template.fe_port;

import com.mojang.serialization.MapCodec;
import com.qwq117458866249.multiblocklib.api.IAbleToForm;
import com.qwq117458866249.multiblocklib.api.IOMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class FEPortBlock extends BaseEntityBlock implements IAbleToForm {
    public static final ArrayList<Block> allThis = new ArrayList<>();
    public static final HashMap<Block, Integer> allAmounts = new HashMap<>();
    public static final HashMap<Block,IOMode> allIOMode = new HashMap<>();

    public FEPortBlock(Properties properties, int feAmount, IOMode ioMode) {
        super(properties);
        allIOMode.put(this,ioMode);
        allThis.add(this);
        allAmounts.put(this, feAmount);
    }

    private FEPortBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(FEPortBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        if (isDirectional()) {
            builder.add(FACING);
        }
        builder.add(FORMED);
    }

    @Override
    @org.jetbrains.annotations.Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (isDirectional()) {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(FORMED, false);
        } else {
            return this.defaultBlockState().setValue(FORMED, false);
        }
    }

    @Override
    public boolean isDirectional() {
        return true;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FEPortBlockEntity(blockPos, blockState);
    }
}
