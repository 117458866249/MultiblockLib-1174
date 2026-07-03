package com.qwq117458866249.multiblocklib.api.recipe_requirements.ports;

import com.google.gson.JsonElement;
import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.MultiblockStructure;
import com.qwq117458866249.multiblocklib.common.template.item_port.ItemPortBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemRecipeRequirement extends RecipeRequirement {
    public Item item = null;
    public TagKey<Item> itemTagKey = null;
    public boolean isTag = false;
    public final int count;
    private List<JsonElement> port = null;

    public ItemRecipeRequirement setPort(List<JsonElement> port) {
        this.port = port;
        return this;
    }

    public ItemRecipeRequirement(IOMode io, String item, int count) {
        super(io);
        if (item.charAt(0) == '#') {
            this.itemTagKey = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.parse(Util.getPath(item)));
            isTag = true;
        } else {
            this.item = BuiltInRegistries.ITEM.getValue(Identifier.parse(item));
        }
        this.count = count;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, MultiblockStructure structure) {
        AtomicInteger waitForProgress = new AtomicInteger(count);

        structure.blocks().forEach((eachPos, _) -> {
            AtomicBoolean availablePort = new AtomicBoolean(false);
            if (port != null) {
                port.forEach(jsonElement -> {
                    if (jsonElement.getAsString().charAt(0) == '#') {
                        if (level.getBlockState(eachPos).is(TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.parse(Util.getPath(jsonElement.getAsString()))))) {
                            availablePort.set(true);
                        }
                    } else {
                        if (level.getBlockState(eachPos).is(BuiltInRegistries.BLOCK.getValue(Identifier.parse(jsonElement.getAsString())))) {
                            availablePort.set(true);
                        }
                    }
                });
            }

            if (
                    level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof ItemPortBlockEntity portEntity && (!portEntity.ioMode.equals(IOMode.OUTPUT)) &&
                            (port == null || availablePort.get())
            ) {
                portEntity.handler.copyToList().forEach(eachStack -> {
                    if (
                            (isTag && eachStack.is(itemTagKey)) ||
                                    ((!isTag) && eachStack.is(item))
                    ) {
                        if (waitForProgress.get() < eachStack.getCount()) {
                            waitForProgress.set(0);
                        } else {
                            waitForProgress.addAndGet(-eachStack.getCount());
                        }
                    }
                });
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
                        level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof ItemPortBlockEntity portEntity && (!portEntity.ioMode.equals(IOMode.OUTPUT)) &&
                                (port == null || availablePort.get())
                ) {
                    for (int i = 0; i < portEntity.slotAmount; i++) {
                        if (isTag) {
                            if (portEntity.handler.getResource(i).toStack().is(itemTagKey)) {
                                try (Transaction transaction = Transaction.open(rtTransaction)) {
                                    waitForProgress.addAndGet(-portEntity.handler.extract(portEntity.handler.getResource(i), waitForProgress.get(), transaction));
                                    transaction.commit();
                                }
                            }
                        } else {
                            if (portEntity.handler.getResource(i).toStack().is(item)) {
                                try (Transaction transaction = Transaction.open(rtTransaction)) {
                                    waitForProgress.addAndGet(-portEntity.handler.extract(portEntity.handler.getResource(i), waitForProgress.get(), transaction));
                                    transaction.commit();
                                }
                            }
                        }
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
                        level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof ItemPortBlockEntity portEntity && portEntity.ioMode.equals(IOMode.OUTPUT) &&
                                (port == null || availablePort.get())
                ) {
                    try (Transaction transaction = Transaction.open(rtTransaction)) {
                        waitForProgress.addAndGet(-portEntity.handler.insert(ItemResource.of(item), waitForProgress.get(), transaction));
                        transaction.commit();
                    }
                }
            });
            rtTransaction.commit();
        }
    }

    public static void register() {
    }

    static {
        allRecipeRequirements.put("item_recipe_requirement", obj -> {
            if (obj.length >= 4) {
                return new ItemRecipeRequirement(
                        IOMode.get(((JsonElement) obj[0]).getAsString()),
                        ((JsonElement) obj[1]).getAsString(),
                        ((JsonElement) obj[2]).getAsInt()
                ).setPort(((JsonElement) obj[3]).getAsJsonArray().asList());
            }
            return new ItemRecipeRequirement(
                    IOMode.get(((JsonElement) obj[0]).getAsString()),
                    ((JsonElement) obj[1]).getAsString(),
                    ((JsonElement) obj[2]).getAsInt()
            );
        });
    }
}
