package com.qwq117458866249.multiblocklib.common.recipes;

import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;

public final class MultiblockStructureSimpleViewer {
    private HashMap<BlockPos, ArrayList<String>> blocks = new HashMap<>();
    private int x = 0;
    private int y = 0;
    private int z = 0;

    public MultiblockStructureSimpleViewer(HashMap<BlockPos, ArrayList<String>> blocks, String controller) {
        blocks.forEach((pos, _) -> {
            setX(Math.min(x, pos.getX()));
            setY(Math.min(y, pos.getY()));
            setZ(Math.min(z, pos.getZ()));
        });

        this.blocks.put(new BlockPos(-x, -y, -z), Util.getStringList(controller));

        blocks.forEach((pos, strings) -> this.blocks.put(Util.getDifPos(pos, new BlockPos(x, y, z)), strings));

        x = 0;
        y = 0;
        z = 0;

        this.blocks.forEach((pos, _) -> {
            setX(Math.max(x, pos.getX()));
            setY(Math.max(y, pos.getY()));
            setZ(Math.max(z, pos.getZ()));
        });
    }

    public HashMap<BlockPos, ArrayList<String>> getBlocks() {
        return blocks;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    private void setX(int value) {
        x = value;
    }

    private void setY(int value) {
        y = value;
    }

    private void setZ(int value) {
        z = value;
    }
}
