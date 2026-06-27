package com.qwq117458866249.multiblocklib.common.template.item_port;

import com.qwq117458866249.multiblocklib.api.interfaces.IAbleToForm;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.register.Register;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ItemPortBlockEntity extends BlockEntity {
    public int slotAmount;
    public IOMode ioMode;
    public ItemStacksResourceHandler handler;

    public ItemPortBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Register.ITEM_PORT_BE.get(), worldPosition, blockState);
        slotAmount = ItemPortBlock.allSize.get(blockState.getBlock());
        ioMode = ItemPortBlock.allIOMode.get(blockState.getBlock());
        handler = new ItemStacksResourceHandler(ItemPortBlock.allSize.get(blockState.getBlock()));
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        SimpleContainer inventory = new SimpleContainer(ItemPortBlock.allSize.get(getBlockState().getBlock()));
        for (int i = 0; i < ItemPortBlock.allSize.get(getBlockState().getBlock()); i++) {
            inventory.setItem(i, handler.copyToList().get(i));
        }
        Containers.dropContents(this.level, pos, inventory);
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
        handler = new ItemStacksResourceHandler(ItemPortBlock.allSize.get(getBlockState().getBlock()));
        handler.deserialize(input);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ItemPortBlockEntity entity) {
        entity.entityTick(level, pos, state);
    }

    public void entityTick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        if (level.getBlockState(pos).getBlock() instanceof ItemPortBlock port && (!port.isDirectional())) {
            return;
        }

        ResourceHandler nearHandler = level.getCapability(
                Capabilities.Item.BLOCK,
                Util.getAbsPos(pos, Util.getDirectionalPos(state.getValue(IAbleToForm.FACING))),
                state.getValue(IAbleToForm.FACING).getOpposite()
        );

        if (nearHandler == null) {
            return;
        }

        try (Transaction transaction = Transaction.openRoot()) {
            if (ioMode.equals(IOMode.INPUT)) {
                for (int i = 0; i < nearHandler.size(); i++) {
                    if (nearHandler.getResource(i) instanceof ItemResource resource && (!resource.isEmpty())) {
                        int canInsert;
                        try (Transaction discard = Transaction.open(transaction)) {
                            canInsert = handler.insert(resource, nearHandler.getAmountAsInt(i), discard);
                        }
                        try (Transaction t = Transaction.open(transaction)) {
                            handler.insert(resource, canInsert, t);
                            t.commit();
                        }
                        try (Transaction t = Transaction.open(transaction)) {
                            nearHandler.extract(resource, canInsert, t);
                            t.commit();
                        }
                    }
                }
            } else if (ioMode.equals(IOMode.OUTPUT)) {
                for (int i = 0; i < handler.size(); i++) {
                    if (handler.getResource(i) instanceof ItemResource resource && (!resource.isEmpty())) {
                        int canInsert;
                        try (Transaction discard = Transaction.open(transaction)) {
                            canInsert = nearHandler.insert(resource, handler.getAmountAsInt(i), discard);
                        }
                        try (Transaction t = Transaction.open(transaction)) {
                            nearHandler.insert(resource, canInsert, t);
                            t.commit();
                        }
                        try (Transaction t = Transaction.open(transaction)) {
                            handler.extract(resource, canInsert, t);
                            t.commit();
                        }
                    }
                }
            }
            transaction.commit();
        }
    }
}
