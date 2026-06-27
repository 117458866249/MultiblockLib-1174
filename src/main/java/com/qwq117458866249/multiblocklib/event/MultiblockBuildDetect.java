package com.qwq117458866249.multiblocklib.event;

import com.qwq117458866249.multiblocklib.MultiblockLib;
import com.qwq117458866249.multiblocklib.common.template.build.MultiblockBuilder;
import com.qwq117458866249.multiblocklib.common.template.build.MultiblockDetector;
import com.qwq117458866249.multiblocklib.common.template.controller.ControllerBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = MultiblockLib.MOD_ID)
public class MultiblockBuildDetect {

    @SubscribeEvent
    public static void onUse(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity().isShiftKeyDown() && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().getBlockEntity(event.getPos()) instanceof ControllerBlockEntity controller) {
            if (event.getItemStack().getItem() instanceof MultiblockBuilder) {
                CustomData data = event.getItemStack().get(DataComponents.CUSTOM_DATA);
                if (data != null && data.copyTag().getInt("index").isPresent()) {
                    MultiblockBuilder.execute(event.getPos(), event.getLevel(), event.getEntity(), controller, data.copyTag().getInt("index").get() >= 0 && data.copyTag().getInt("index").get() < controller.allStructures().size() ? data.copyTag().getInt("index").get() : 0);
                } else {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("index", 0);
                    event.getItemStack().set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    MultiblockBuilder.execute(event.getPos(), event.getLevel(), event.getEntity(), controller, 0);
                }
            } else if (event.getItemStack().getItem() instanceof MultiblockDetector) {
                MultiblockDetector.execute(event.getPos(), event.getLevel(), event.getEntity(), controller);
            }
        } else if (event.getItemStack().getItem() instanceof MultiblockBuilder && event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().getBlockEntity(event.getPos()) instanceof ControllerBlockEntity controller) {
            CustomData data = event.getItemStack().get(DataComponents.CUSTOM_DATA);
            if (data != null && data.copyTag().getInt("index").isPresent()) {
                if (data.copyTag().getInt("index").get() < 0 || data.copyTag().getInt("index").get() >= controller.allStructures().size() - 1) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("index", 0);
                    event.getItemStack().set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    event.getEntity().sendSystemMessage(Component.translatable("multiblock_structure." + controller.allStructures().getFirst()));
                } else {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("index", data.copyTag().getInt("index").get() + 1);
                    event.getItemStack().set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    event.getEntity().sendSystemMessage(Component.translatable("multiblock_structure." + controller.allStructures().get(data.copyTag().getInt("index").get() + 1)));
                }
            } else {
                CompoundTag tag = new CompoundTag();
                tag.putInt("index", 0);
                event.getItemStack().set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                event.getEntity().sendSystemMessage(Component.translatable("multiblock_structure." + controller.allStructures().getFirst()));
            }
        }
    }
}
