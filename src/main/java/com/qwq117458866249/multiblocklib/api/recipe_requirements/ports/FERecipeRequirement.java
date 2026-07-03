package com.qwq117458866249.multiblocklib.api.recipe_requirements.ports;

import com.google.gson.JsonElement;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.MultiblockStructure;
import com.qwq117458866249.multiblocklib.common.template.fe_port.FEPortBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FERecipeRequirement extends RecipeRequirement {
    public final int count;
    private List<JsonElement> port = null;

    public FERecipeRequirement setPort(List<JsonElement> port) {
        this.port = port;
        return this;
    }

    public FERecipeRequirement(IOMode io, int count) {
        super(io);
        this.count = count;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure) {
        AtomicInteger waitForProgress = new AtomicInteger(count);

        structure.blocks().forEach((eachPos, _) -> {
            if (
                    level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof FEPortBlockEntity portEntity && (!portEntity.ioMode.equals(IOMode.OUTPUT)) &&
                            (port == null || level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).getBlock().equals(port))
            ) {
                waitForProgress.addAndGet(-portEntity.handler.getAmountAsInt());
            }
        });
        if (waitForProgress.get() <= 0) {
            return ParseResult.SUCCESS;
        } else {
            return ParseResult.FAILED;
        }
    }

    @Override
    public void inputRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure) {
        try (Transaction rtTransaction = Transaction.openRoot()) {
            AtomicInteger waitForProgress = new AtomicInteger(count);
            structure.blocks().forEach((eachPos, _) -> {
                AtomicBoolean availablePort = new AtomicBoolean(false);
                if (port != null) {
                    port.forEach(jsonElement -> {
                        if (jsonElement.getAsString().charAt(0) == '#') {
                            if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).is(TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.parse(Util.getPath(jsonElement.getAsString()))))) {
                                availablePort.set(true);
                            }
                        } else {
                            if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).is(BuiltInRegistries.BLOCK.getValue(Identifier.parse(jsonElement.getAsString())))) {
                                availablePort.set(true);
                            }
                        }
                    });
                }

                if (
                        level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof FEPortBlockEntity portEntity && (!portEntity.ioMode.equals(IOMode.OUTPUT)) &&
                                (port == null || availablePort.get())
                ) {
                    try (Transaction transaction = Transaction.open(rtTransaction)) {
                        waitForProgress.addAndGet(-portEntity.handler.extract(waitForProgress.get(), transaction));
                        transaction.commit();
                    }
                }
            });
            rtTransaction.commit();
        }
    }

    @Override
    public void outputRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure) {
        if (Math.random() >= chance) return;
        try (Transaction rtTransaction = Transaction.openRoot()) {
            AtomicInteger waitForProgress = new AtomicInteger(count);
            structure.blocks().forEach((eachPos, _) -> {
                AtomicBoolean availablePort = new AtomicBoolean(false);
                if (port != null) {
                    port.forEach(jsonElement -> {
                        if (jsonElement.getAsString().charAt(0) == '#') {
                            if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).is(TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.parse(Util.getPath(jsonElement.getAsString()))))) {
                                availablePort.set(true);
                            }
                        } else {
                            if (level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).is(BuiltInRegistries.BLOCK.getValue(Identifier.parse(jsonElement.getAsString())))) {
                                availablePort.set(true);
                            }
                        }
                    });
                }

                if (
                        level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof FEPortBlockEntity portEntity && portEntity.ioMode.equals(IOMode.OUTPUT) &&
                                (port == null || availablePort.get())
                ) {
                    try (Transaction transaction = Transaction.open(rtTransaction)) {
                        waitForProgress.addAndGet(-portEntity.handler.insert(waitForProgress.get(), transaction));
                        transaction.commit();
                    }
                }
            });
            rtTransaction.commit();
        }
    }

    @Override
    public Component getDesc() {
        return Component.literal(count + " FE");
    }

    public static void register() {
    }

    static {
        allRecipeRequirements.put("fe_recipe_requirement", obj -> {
            if (obj.length >= 3) {
                return new FERecipeRequirement(
                        IOMode.get(((JsonElement) obj[0]).getAsString()),
                        ((JsonElement) obj[1]).getAsInt()
                ).setPort(((JsonElement) obj[2]).getAsJsonArray().asList());
            }
            return new FERecipeRequirement(
                    IOMode.get(((JsonElement) obj[0]).getAsString()),
                    ((JsonElement) obj[1]).getAsInt()
            );
        });
    }
}
