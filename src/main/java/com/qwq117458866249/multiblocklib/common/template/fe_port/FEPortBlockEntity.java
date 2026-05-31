package com.qwq117458866249.multiblocklib.common.template.fe_port;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

public class FEPortBlockEntity extends BlockEntity {
    public int feAmount;
    public IOMode ioMode;
    public SimpleEnergyHandler handler;

    public FEPortBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Register.FE_PORT_BE.get(), worldPosition, blockState);
        feAmount = FEPortBlock.allAmounts.get(blockState.getBlock());
        ioMode = FEPortBlock.allIOMode.get(blockState.getBlock());
        handler = new SimpleEnergyHandler(FEPortBlock.allAmounts.get(blockState.getBlock()));
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
        handler = new SimpleEnergyHandler(FEPortBlock.allAmounts.get(getBlockState().getBlock()));
        handler.deserialize(input);
    }
}
