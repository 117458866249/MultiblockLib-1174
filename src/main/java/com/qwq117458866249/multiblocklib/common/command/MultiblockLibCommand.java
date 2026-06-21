package com.qwq117458866249.multiblocklib.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.qwq117458866249.multiblocklib.common.template.controller.ControllerBlock;
import com.qwq117458866249.multiblocklib.common.template.controller.ControllerBlockEntity;
import com.qwq117458866249.multiblocklib.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class MultiblockLibCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("multiblocklibes").then(
                Commands.literal("structure").then(
                        Commands.argument("controllerPos", BlockPosArgument.blockPos()).then(
                                Commands.argument("cornerI", BlockPosArgument.blockPos()).then(
                                        Commands.argument("cornerII", BlockPosArgument.blockPos()).then(
                                                Commands.argument("fileName", StringArgumentType.string()).executes(ctx -> {
                                                    if (ctx.getSource().getLevel().getBlockEntity(BlockPosArgument.getBlockPos(ctx, "controllerPos")) instanceof ControllerBlockEntity) {

                                                        BlockPos controller = BlockPosArgument.getBlockPos(ctx, "controllerPos");
                                                        BlockPos cornerI = BlockPosArgument.getBlockPos(ctx, "cornerI");
                                                        BlockPos cornerII = BlockPosArgument.getBlockPos(ctx, "cornerII");
                                                        String fileName = StringArgumentType.getString(ctx, "fileName");
                                                        String before = """
{
    "type": "multiblocklibes:structure",
    "structure_id": ,
    "controller_id": ,
    "pattern": [""";
                                                        String after = """
    
    ],
    "requirements": []
}""";
                                                        BlockPos temp;

                                                        for (int i = Math.min(cornerI.getX(), cornerII.getX()); i <= Math.max(cornerI.getX(), cornerII.getX()); i++) {
                                                            for (int j = Math.min(cornerI.getY(), cornerII.getY()); j <= Math.max(cornerI.getY(), cornerII.getY()); j++) {
                                                                for (int k = Math.min(cornerI.getZ(), cornerII.getZ()); k <= Math.max(cornerI.getZ(), cornerII.getZ()); k++) {
                                                                    temp = Util.getDirectionPos(Util.getDifPos(new BlockPos(i, j, k), controller), ctx.getSource().getLevel().getBlockState(controller).getValue(ControllerBlock.FACING).getOpposite());
                                                                    before = before + (!(temp.equals(new BlockPos(0, 0, 0)) || ctx.getSource().getLevel().getBlockState(new BlockPos(i, j, k)).getBlock().equals(Blocks.AIR) || ctx.getSource().getLevel().getBlockState(new BlockPos(i, j, k)).getBlock().equals(Blocks.CAVE_AIR) || ctx.getSource().getLevel().getBlockState(new BlockPos(i, j, k)).getBlock().equals(Blocks.VOID_AIR)) ?
                                                                            "\n            [" + temp.getX() + ", " + temp.getY() + ", " + temp.getZ() + ", [\"" + BuiltInRegistries.BLOCK.wrapAsHolder(ctx.getSource().getLevel().getBlockState(new BlockPos(i, j, k)).getBlock()).getRegisteredName() + "\"]]," : "");
                                                                }
                                                            }
                                                        }

                                                        try {
                                                            Util.writeToGameDir("multiblocklibes/" + fileName + ".json", before + after);
                                                        } catch (Exception e) {
                                                            throw new RuntimeException(e);
                                                        }

                                                        ctx.getSource().sendSuccess(() -> Component.literal("Your structure file is at " + FMLPaths.GAMEDIR.get().resolve("multiblocklibes/" + fileName) + ".json now QwQ"), true);
                                                        return 1;
                                                    } else {
                                                        ctx.getSource().sendFailure(Component.literal("You didn't choose a controller block and it's a " + ctx.getSource().getLevel().getBlockState(BlockPosArgument.getBlockPos(ctx, "controllerPos")).getBlock() + " !"));
                                                        return 0;
                                                    }
                                                })
                                        )
                                )
                        )
                )
        ).then(
                Commands.literal("aSimpleBp").then(
                        Commands.argument("bp", BlockPosArgument.blockPos()).executes(ctx -> {
                            ctx.getSource().sendSuccess(() -> Component.literal(BlockPosArgument.getBlockPos(ctx, "bp").toShortString()), true);
                            return 1;
                        })
                )
        );

        dispatcher.register(command);
    }
}