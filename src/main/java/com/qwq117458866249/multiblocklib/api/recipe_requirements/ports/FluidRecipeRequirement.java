package com.qwq117458866249.multiblocklib.api.recipe_requirements.ports;

import com.qwq117458866249.multiblocklib.api.IOMode;
import com.qwq117458866249.multiblocklib.api.ParseResult;
import com.qwq117458866249.multiblocklib.common.recipes.RecipeRequirement;
import com.qwq117458866249.multiblocklib.common.recipes.Structure;
import com.qwq117458866249.multiblocklib.common.template.fluid_port.FluidPortBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.concurrent.atomic.AtomicInteger;

public class FluidRecipeRequirement extends RecipeRequirement {
    public Fluid fluid = null;
    public TagKey<Fluid> fluidTagKey = null;
    public boolean isTag = false;
    public final int count;
    private Block port = null;

    public FluidRecipeRequirement setPort(Block port) {
        this.port = port;
        return this;
    }

    public FluidRecipeRequirement(IOMode io, String fluid, int count) {
        super(io);
        if (fluid.charAt(0) == '#') {
            this.fluidTagKey = TagKey.create(BuiltInRegistries.FLUID.key(), Identifier.parse(Util.getPath(fluid)));
            isTag = true;
        } else {
            this.fluid = BuiltInRegistries.FLUID.getValue(Identifier.parse(fluid));
        }
        this.count = count;
    }

    @Override
    public ParseResult canParseRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        AtomicInteger waitForProgress = new AtomicInteger(count);

        structure.blocks().forEach((eachPos, _) -> {
            if (
                    level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof FluidPortBlockEntity portEntity && (!portEntity.ioMode.equals(IOMode.OUTPUT)) &&
                            (port == null || level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).getBlock().equals(port))
            ) {
                portEntity.handler.copyToList().forEach(eachStack -> {
                    if (
                            (isTag && eachStack.is(fluidTagKey)) ||
                                    ((!isTag) && eachStack.is(fluid))
                    ) {
                        if (waitForProgress.get() < eachStack.getAmount()) {
                            waitForProgress.set(0);
                        } else {
                            waitForProgress.addAndGet(-eachStack.getAmount());
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
    public void inputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        try (Transaction rtTransaction = Transaction.openRoot()) {
            AtomicInteger waitForProgress = new AtomicInteger(count);
            structure.blocks().forEach((eachPos, _) -> {
                if (
                        level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof FluidPortBlockEntity portEntity && (!portEntity.ioMode.equals(IOMode.OUTPUT)) &&
                                (port == null || level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).getBlock().equals(port))
                ) {
                    for (int i = 0; i < portEntity.slotCount; i++) {
                        if (isTag) {
                            if (portEntity.handler.getResource(i).toStack(1).is(fluidTagKey)) {
                                try (Transaction transaction = Transaction.open(rtTransaction)) {
                                    waitForProgress.addAndGet(-portEntity.handler.extract(portEntity.handler.getResource(i), waitForProgress.get(), transaction));
                                    transaction.commit();
                                }
                            }
                        } else {
                            if (portEntity.handler.getResource(i).toStack(1).is(fluid)) {
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
    public void outputRequirement(BlockPos pos, Level level, Direction face, Structure structure) {
        if (Math.random() >= chance) return;
        try (Transaction rtTransaction = Transaction.openRoot()) {
            AtomicInteger waitForProgress = new AtomicInteger(count);
            structure.blocks().forEach((eachPos, _) -> {
                if (
                        level.getBlockEntity(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))) instanceof FluidPortBlockEntity portEntity && (!portEntity.ioMode.equals(IOMode.INPUT)) &&
                                (port == null || level.getBlockState(Util.getAbsPos(pos, Util.getDirectionPos(eachPos, face))).getBlock().equals(port))
                ) {
                    try (Transaction transaction = Transaction.open(rtTransaction)) {
                        waitForProgress.addAndGet(-portEntity.handler.insert(FluidResource.of(fluid), waitForProgress.get(), transaction));
                        transaction.commit();
                    }
                }
            });
            rtTransaction.commit();
        }
    }

    @Override
    public Component getDesc() {
        return Component.empty();
    }
}
