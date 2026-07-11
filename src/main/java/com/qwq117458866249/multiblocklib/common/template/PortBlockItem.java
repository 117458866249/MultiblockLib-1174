package com.qwq117458866249.multiblocklib.common.template;

import com.qwq117458866249.multiblocklib.api.IOMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class PortBlockItem extends BlockItem {
    public PortBlockItem(Block block, Properties properties, IOMode io) {
        super(block, properties);
        this.io = io;
    }

    public IOMode io;

    public boolean hasToolTip() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (hasToolTip()) {
            switch (io) {
                case INPUT -> builder.accept(Component.translatable("tooltip.multiblocklib.input").withColor(0x3939b9));
                case OUTPUT ->
                        builder.accept(Component.translatable("tooltip.multiblocklib.output").withColor(0x30b075));
                case BOTH -> builder.accept(Component.translatable("tooltip.multiblocklib.both").withColor(0x2eb7ca));
            }
        }
    }
}
