package com.qwq117458866249.multiblocklib.common.template.fluid_port;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

public class FluidPortBlockEntity extends BlockEntity {
    public int slotCount;
    public int eachSlotSize;
    public IOMode ioMode;
    public FluidStacksResourceHandler handler;
    public FluidStacksResourceHandler temp = null;

    public FluidPortBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Register.FLUID_PORT_BE.get(), worldPosition, blockState);
        eachSlotSize = FluidPortBlock.allSize.get(blockState.getBlock());
        slotCount = FluidPortBlock.allSlotCounts.get(blockState.getBlock());
        ioMode = FluidPortBlock.allIOMode.get(blockState.getBlock());
        handler = new FluidStacksResourceHandler(FluidPortBlock.allSlotCounts.get(getBlockState().getBlock()), FluidPortBlock.allSize.get(getBlockState().getBlock()));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (handler != null) {
            handler.serialize(output);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        handler = new FluidStacksResourceHandler(FluidPortBlock.allSlotCounts.get(getBlockState().getBlock()), FluidPortBlock.allSize.get(getBlockState().getBlock()));
        handler.deserialize(input);
    }
}
