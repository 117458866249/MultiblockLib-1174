package com.qwq117458866249.multiblocklib.common.template.build;

import com.qwq117458866249.multiblocklib.api.interfaces.IControllerBlock;
import com.qwq117458866249.multiblocklib.api.interfaces.IControllerBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class MultiblockDetector extends Item {
    public MultiblockDetector(Properties properties) {
        super(properties);
    }

    public static void execute(BlockPos pos, Level level, Player player, IControllerBlockEntity controller) {
        if (!controller.getFormedAs().isEmpty()) {
            return;
        }

        controller.allStructures().forEach(structure -> {
            player.sendSystemMessage(Component.translatable("multiblock_structure." + structure.structureId()).append(":"));
            structure.blocks().forEach((eachPos, blocks) -> {
                for (String block : blocks) {
                    if (block.charAt(0) == '#') {
                        if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING)))).is(TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.parse(block)))) {
                            return;
                        }
                    } else {
                        if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING)))).is(BuiltInRegistries.BLOCK.getValue(Identifier.parse(block)))) {
                            return;
                        }
                    }
                }

                if (blocks.getFirst().charAt(0) == '#'){
                    player.sendSystemMessage(
                            Component.literal("[ ")
                                    .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getX())).withStyle(ChatFormatting.DARK_BLUE))
                                    .append(" , ")
                                    .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getY())).withStyle(ChatFormatting.BLUE))
                                    .append(" , ")
                                    .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getZ())).withStyle(ChatFormatting.AQUA))
                                    .append(" ] , Tag: ")
                                    .append(Component.literal(blocks.getFirst()).withStyle(ChatFormatting.DARK_AQUA))
                    );
                } else {
                    player.sendSystemMessage(
                            Component.literal("[ ")
                                    .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getX())).withStyle(ChatFormatting.DARK_BLUE))
                                    .append(" , ")
                                    .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getY())).withStyle(ChatFormatting.BLUE))
                                    .append(" , ")
                                    .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getZ())).withStyle(ChatFormatting.AQUA))
                                    .append(" ] , ")
                                    .append(Component.translatable(BuiltInRegistries.BLOCK.getValue(Identifier.parse(blocks.getFirst())).getDescriptionId()).withStyle(ChatFormatting.DARK_AQUA))
                    );
                }
            });
        });
    }
}
