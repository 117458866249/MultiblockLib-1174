package com.qwq117458866249.multiblocklib.api.recipe_requirements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.api.structure_requirements.MaxBlocksStructureRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.MultiblockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class MaxBlocksRecipeRequirement extends RecipeRequirement {
    public final int value;
    public final ArrayList<String> blocks;

    public MaxBlocksRecipeRequirement(IOMode io, int value, ArrayList<String> blocks) {
        super(io);
        this.value = value;
        this.blocks = blocks;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure) {
        if (new MaxBlocksStructureRequirement(blocks, value).cantForm(pos, level, face, structure)) {
            return ParseResult.FAILED;
        } else {
            return ParseResult.SUCCESS;
        }
    }

    @Override
    public void inputRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure) {

    }

    @Override
    public void outputRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure) {

    }

    @Override
    public Component getDesc() {
        return Component.literal(Component.translatable("requirement.multiblocklib.max.f").getString() + value + " * " + Component.translatable(BuiltInRegistries.BLOCK.getValue(Identifier.parse(blocks.getFirst())).getDescriptionId()).getString() + Component.translatable("requirement.multiblocklib.max.b").getString());
    }

    @Override
    public boolean onlyDetectOnce() {
        return true;
    }

    public static void register() {
    }

    static {
        allRecipeRequirements.put("max_blocks_recipe_requirement", obj -> {
            ArrayList<String> temp = new ArrayList<>();
            ((JsonArray) obj[2]).asList().forEach(each -> temp.add(each.getAsString()));
            return new MaxBlocksRecipeRequirement(
                    IOMode.get(((JsonElement) obj[0]).getAsString()),
                    ((JsonElement) obj[1]).getAsInt(),
                    temp
            );
        });
    }
}
