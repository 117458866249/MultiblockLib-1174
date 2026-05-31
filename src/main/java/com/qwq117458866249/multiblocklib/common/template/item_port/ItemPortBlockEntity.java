package com.qwq117458866249.multiblocklib.common.template.item_port;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

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
}
