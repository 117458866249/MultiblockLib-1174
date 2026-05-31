package com.qwq117458866249.multiblocklib.api.recipe_requirements;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class CommandRecipeRequirement extends RecipeRequirement {
    public final String command;
    public final BlockPos absPos;

    public CommandRecipeRequirement(IOMode io, String command, BlockPos pos) {
        super(io);
        if (command.charAt(0) == '/') {
            this.command = command;
        } else {
            this.command = Util.getPath(command);
        }
        this.absPos = pos;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        return ParseResult.SUCCESS;
    }

    @Override
    public void inputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        Util.runCommand(command, level, Util.getAbsPos(pos, Util.getDirectionPos(absPos, face)));
    }

    @Override
    public void outputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        if (Math.random() >= chance) return;
        Util.runCommand(command, level, Util.getAbsPos(pos, Util.getDirectionPos(absPos, face)));
    }

    @Override
    public Component getDesc() {
        return Component.empty();
    }
}
