package com.qwq117458866249.multiblocklib.common.template.fluid_port;

import com.mojang.serialization.MapCodec;
import com.qwq117458866249.multiblocklib.api.IAbleToForm;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class FluidPortBlock extends BaseEntityBlock implements IAbleToForm {
    public static final ArrayList<Block> allThis = new ArrayList<>();
    public static final HashMap<Block,Integer> allSlotCounts = new HashMap<>();
    public static final HashMap<Block,Integer> allSize = new HashMap<>();
    public static final HashMap<Block,IOMode> allIOMode = new HashMap<>();
    public int eachSlotSize = 0;

    public FluidPortBlock(Properties properties, int eachSlotSize, int slotCount, IOMode ioMode) {
        super(properties);
        this.eachSlotSize = eachSlotSize;
        allIOMode.put(this,ioMode);
        allThis.add(this);
        allSlotCounts.put(this,slotCount);
        allSize.put(this,eachSlotSize);
    }

    private FluidPortBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(FluidPortBlock::new);
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
    public boolean isDirectional() {
        return true;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidPortBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if ((!isDirectional()) || hitResult.getDirection().equals(state.getValue(FACING))) {
            FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, Register.FLUID_PORT_BE.get(), FluidPortBlockEntity::tick);
    }
}
