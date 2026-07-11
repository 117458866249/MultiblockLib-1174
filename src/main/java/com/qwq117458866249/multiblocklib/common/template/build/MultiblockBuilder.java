package com.qwq117458866249.multiblocklib.common.template.build;

import com.qwq117458866249.multiblocklib.api.interfaces.IControllerBlock;
import com.qwq117458866249.multiblocklib.api.interfaces.IControllerBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class MultiblockBuilder extends Item {
    public MultiblockBuilder(Properties properties) {
        super(properties);
    }

    public static void execute(BlockPos pos, Level level, Player player, IControllerBlockEntity controller, int index) {
        if (player.isCreative()) {
            controller.allStructures().get(index).blocks().forEach((eachPos, blocks) -> {
                String identifier;
                if (blocks.getFirst().charAt(0) == '#') {
                    identifier = BuiltInRegistries.BLOCK.getKey(
                            BuiltInRegistries.BLOCK.stream()
                                    .filter(block -> block.defaultBlockState().is(TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.parse(Util.getPath(blocks.getFirst())))))
                                    .toList()
                                    .getFirst()
                    ).toString();
                } else {
                    identifier = blocks.getFirst();
                }
                level.setBlock(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))), BuiltInRegistries.BLOCK.getValue(Identifier.parse(identifier)).defaultBlockState(), 3);
            });
            return;
        }

        controller.allStructures().get(index).blocks().forEach((eachPos, blocks) -> {
            if (
                    (!level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING)))).is(Blocks.AIR)) &&
                            (!level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING)))).is(Blocks.CAVE_AIR)) &&
                            !level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING)))).is(Blocks.VOID_AIR)
            ) {
                player.sendSystemMessage(
                        Component.translatable("key.multiblockes.pls_destory")
                                .append(Component.literal("[ "))
                                .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getX())).withStyle(ChatFormatting.DARK_BLUE))
                                .append(" , ")
                                .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getY())).withStyle(ChatFormatting.BLUE))
                                .append(" , ")
                                .append(Component.literal(String.valueOf(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))).getZ())).withStyle(ChatFormatting.AQUA))
                                .append(Component.literal(" ]"))
                );
                return;
            }

            AtomicReference<String> identifier = new AtomicReference<>("minecraft:air");

            for (String block : blocks) {
                if (block.charAt(0) == '#') {
                    for (
                            Block eachTag :
                            BuiltInRegistries.BLOCK
                                    .stream()
                                    .filter(filter -> filter.defaultBlockState().is(TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.parse(Util.getPath(block)))))
                                    .toList()
                    ) {
                        if (player.getInventory().hasAnyOf(Set.of(eachTag.asItem()))) {
                            Util.removeItem(player.getInventory(), eachTag.asItem());
                            identifier.set(
                                    BuiltInRegistries
                                            .BLOCK
                                            .getKey(eachTag)
                                            .toString()
                            );
                            break;
                        }
                    }
                } else {
                    if (player.getInventory().hasAnyOf(Set.of(BuiltInRegistries.BLOCK.getValue(Identifier.parse(block)).asItem()))) {
                        Util.removeItem(player.getInventory(), BuiltInRegistries.BLOCK.getValue(Identifier.parse(block)).asItem());
                        identifier.set(block);
                    }
                }
                if (!identifier.get().equals("minecraft:air")) break;
            }

            level.setBlock(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, level.getBlockState(pos).getValue(IControllerBlock.FACING))), BuiltInRegistries.BLOCK.getValue(Identifier.parse(identifier.get())).defaultBlockState(), 3);
        });
    }
}
