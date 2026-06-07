package com.qwq117458866249.multiblocklib.common.template.item_port;

import com.mojang.serialization.MapCodec;
import com.qwq117458866249.multiblocklib.api.IAbleToForm;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.core.BlockPos;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ItemPortBlock extends BaseEntityBlock implements IAbleToForm {
    public static final ArrayList<Block> allThis = new ArrayList<>();
    public static final HashMap<Block,Integer> allSize = new HashMap<>();
    public static final HashMap<Block,IOMode> allIOMode = new HashMap<>();
    public boolean noOnBreak = false;

    public ItemPortBlock(Properties properties, int slotAmount, IOMode ioMode) {
        super(properties);
        allIOMode.put(this,ioMode);
        allThis.add(this);
        allSize.put(this,slotAmount);
    }

    private ItemPortBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ItemPortBlock::new);
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
            return this.defaultBlockState().setValue(FACING, (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) ? context.getNearestLookingDirection() : context.getNearestLookingDirection().getOpposite()).setValue(FORMED, false);
        } else {
            return this.defaultBlockState().setValue(FORMED, false);
        }
    }

    @Override
    public void form(Level level, BlockPos pos) {
        noOnBreak = true;
        IAbleToForm.super.form(level, pos);
        noOnBreak = false;
    }

    @Override
    public void unForm(Level level, BlockPos pos) {
        noOnBreak = true;
        IAbleToForm.super.unForm(level, pos);
        noOnBreak = false;
    }

    @Override
    public boolean isDirectional() {
        return true;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPortBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, Register.ITEM_PORT_BE.get(), ItemPortBlockEntity::tick);
    }
}
