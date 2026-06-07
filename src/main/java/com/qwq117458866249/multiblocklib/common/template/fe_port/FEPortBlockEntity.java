package com.qwq117458866249.multiblocklib.common.template.fe_port;

import com.qwq117458866249.multiblocklib.api.IAbleToForm;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.register.Register;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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

    public static void tick(Level level, BlockPos pos, BlockState state, FEPortBlockEntity entity) {
        entity.entityTick(level, pos, state);
    }

    public void entityTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        if (level.getBlockState(pos).getBlock() instanceof FEPortBlock port && (!port.isDirectional())) {
            return;
        }

        EnergyHandler nearHandler = level.getCapability(
                Capabilities.Energy.BLOCK,
                Util.getAbsPos(pos, Util.getDirectionalPos(state.getValue(IAbleToForm.FACING))),
                state.getValue(IAbleToForm.FACING).getOpposite()
        );

        if (nearHandler == null) {
            return;
        }

        try (Transaction transaction = Transaction.openRoot()) {
            if (ioMode.equals(IOMode.INPUT)) {
                int canInsert;
                try (Transaction discard = Transaction.open(transaction)) {
                    canInsert = handler.insert(nearHandler.getAmountAsInt(), discard);
                }
                try (Transaction t = Transaction.open(transaction)) {
                    handler.insert(canInsert, t);
                    t.commit();
                }
                try (Transaction t = Transaction.open(transaction)) {
                    nearHandler.extract(canInsert, t);
                    t.commit();
                }
            } else if (ioMode.equals(IOMode.OUTPUT)) {
                int canInsert;
                try (Transaction discard = Transaction.open(transaction)) {
                    canInsert = nearHandler.insert(handler.getAmountAsInt(), discard);
                }
                try (Transaction t = Transaction.open(transaction)) {
                    handler.extract(canInsert, t);
                    t.commit();
                }
                try (Transaction t = Transaction.open(transaction)) {
                    nearHandler.insert(canInsert, t);
                    t.commit();
                }
            }
            transaction.commit();
        }
    }
}
