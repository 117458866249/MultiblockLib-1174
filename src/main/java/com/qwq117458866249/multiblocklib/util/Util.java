package com.qwq117458866249.multiblocklib.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Util {
    public static BlockPos getAbsPos(BlockPos pos, int x, int y, int z) {
        return new BlockPos(
                pos.getX() + x,
                pos.getY() + y,
                pos.getZ() + z
        );
    }

    public static BlockPos getAbsPos(BlockPos pos, BlockPos abs) {
        return new BlockPos(
                pos.getX() + abs.getX(),
                pos.getY() + abs.getY(),
                pos.getZ() + abs.getZ()
        );
    }

    public static BlockPos getDifPos(BlockPos pos, BlockPos dif) {
        return new BlockPos(
                pos.getX() - dif.getX(),
                pos.getY() - dif.getY(),
                pos.getZ() - dif.getZ()
        );
    }

    public static void runCommand(String command, Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }

        CommandSourceStack src = Objects.requireNonNull(
                level.getServer()
        ).createCommandSourceStack().withSuppressedOutput();

        level.getServer().getCommands().performPrefixedCommand(
                src,
                "execute positioned "
                        +
                        pos.getX()
                        +
                        " "
                        +
                        pos.getY()
                        +
                        " "
                        +
                        pos.getZ()
                        +
                        " in "
                        +
                        level.dimension().identifier()
                        +
                        " run "
                        +
                        command
        );
    }

    public static BlockPos getDirectionPos(BlockPos pos, Direction direction) {
        switch (direction) {
            case SOUTH -> {
                return new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
            }
            case WEST -> {
                return new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
            }
            case EAST -> {
                return new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
            }
            case null, default -> {
                return pos;
            }
        }
    }

    public static String getPath(String string) {
        String temp = "";
        for (int i = 1; i < string.length(); i++) {
            temp = temp + string.charAt(i);
        }
        return temp;
    }

    public static ArrayList<String> getStringList(String... strs) {
        ArrayList<String> temp = new ArrayList<>();
        Collections.addAll(temp, strs);
        return temp;
    }

    public static Set<Block> toSet(ArrayList<Block> list) {
        Set<Block> temp = new HashSet();
        for (Block block : list) {
            temp.add(block);
        }
        return temp;
    }

    public static void writeToGameDir(String subPath, String content) throws Exception {
        Path gameDir = FMLPaths.GAMEDIR.get();
        Path targetPath = gameDir.resolve(subPath);
        Files.createDirectories(targetPath.getParent());
        Files.writeString(targetPath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static BlockPos getDirectionalPos(Direction direction) {
        return switch (direction) {
            case UP -> new BlockPos(0, 1, 0);
            case DOWN -> new BlockPos(0, -1, 0);
            case NORTH -> new BlockPos(0, 0, -1);
            case SOUTH -> new BlockPos(0, 0, 1);
            case EAST -> new BlockPos(1, 0, 0);
            case WEST -> new BlockPos(-1, 0, 0);
        };
    }

    public static ArrayList<ItemStack> expandWithCount(Ingredient ingredient, int count) {
        ArrayList<ItemStack> temp = new ArrayList<>();
        for (Holder<Item> i : ingredient.getValues()) {
            temp.add(new ItemStack(i.value(), count));
        }
        return temp;
    }

    public static void removeItem(Inventory inv, Item item) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (inv.getItem(i).getItem().equals(item)) {
                inv.removeItem(i, 1);
                break;
            }
        }
    }
}