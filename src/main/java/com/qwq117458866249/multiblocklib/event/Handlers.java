package com.qwq117458866249.multiblocklib.event;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.api.IAbleToForm;
import com.qwq117458866249.multiblocklib.common.register.Register;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

@EventBusSubscriber(modid = MultiblockLib.MOD_ID)
public class Handlers {

    @SubscribeEvent
    public static void registerCapabilitiesItem(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                Register.ITEM_PORT_BE.get(),
                (blockEntity, side) -> {
                    if (side == null || blockEntity.getLevel().isClientSide()) {
                        return blockEntity.handler;
                    }
                    if (blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getBlock() instanceof IAbleToForm be) {
                        if (be.isDirectional()) {
                            if (side.equals(blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(IAbleToForm.FACING))) {
                                return blockEntity.handler;
                            } else {
                                return new ItemStacksResourceHandler(0);
                            }
                        } else {
                            return blockEntity.handler;
                        }
                    }
                    return new ItemStacksResourceHandler(0);
                }
        );
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                Register.FLUID_PORT_BE.get(),
                (blockEntity, side) -> {
                    if (side == null || blockEntity.getLevel().isClientSide()) {
                        return blockEntity.handler;
                    }
                    if (blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getBlock() instanceof IAbleToForm be) {
                        if (be.isDirectional()) {
                            if (side.equals(blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(IAbleToForm.FACING))) {
                                return blockEntity.handler;
                            } else {
                                return new FluidStacksResourceHandler(0, 0);
                            }
                        } else {
                            return blockEntity.handler;
                        }
                    }
                    return new FluidStacksResourceHandler(0, 0);
                }
        );
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                Register.FE_PORT_BE.get(),
                (blockEntity, side) -> {
                    if (side == null || blockEntity.getLevel().isClientSide()) {
                        return blockEntity.handler;
                    }
                    if (blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getBlock() instanceof IAbleToForm be) {
                        if (be.isDirectional()) {
                            if (side.equals(blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(IAbleToForm.FACING))) {
                                return blockEntity.handler;
                            } else {
                                return new SimpleEnergyHandler(0);
                            }
                        } else {
                            return blockEntity.handler;
                        }
                    }
                    return new SimpleEnergyHandler(0);
                }
        );
    }
}
