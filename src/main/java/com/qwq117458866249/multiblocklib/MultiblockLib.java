package com.qwq117458866249.multiblocklib;

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
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
