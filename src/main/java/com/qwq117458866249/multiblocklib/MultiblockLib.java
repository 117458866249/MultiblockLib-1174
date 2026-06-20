package com.qwq117458866249.multiblocklib;

import com.qwq117458866249.multiblocklib.api.recipe_requirements.*;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.FERecipeRequirement;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.FluidRecipeRequirement;
import com.qwq117458866249.multiblocklib.api.recipe_requirements.ports.ItemRecipeRequirement;
import com.qwq117458866249.multiblocklib.api.structure_requirements.*;
import com.qwq117458866249.multiblocklib.common.register.Register;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(MultiblockLib.MOD_ID)
public class MultiblockLib {
    public static final String MOD_ID = "multiblocklibes";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MultiblockLib(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        Register.register(modEventBus);
        com.qwq117458866249.multiblocklib.test.Register.register(modEventBus);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        FERecipeRequirement.register();
        FluidRecipeRequirement.register();
        ItemRecipeRequirement.register();
        BlockRecipeRequirement.register();
        CommandRecipeRequirement.register();
        DescRecipeRequirement.register();
        MaxBlocksRecipeRequirement.register();
        MinBlocksRecipeRequirement.register();
        RightCountBlocksRecipeRequirement.register();

        DescStructureRequirement.register();
        MaxBlocksStructureRequirement.register();
        MinBlocksStructureRequirement.register();
        RightCountBlockStructureRequirement.register();
        SameBlockStructureRequirement.register();
    }
}