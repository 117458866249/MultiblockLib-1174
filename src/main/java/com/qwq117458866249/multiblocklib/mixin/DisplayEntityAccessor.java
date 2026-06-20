package com.qwq117458866249.multiblocklib.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.TextDisplay.class)
public interface DisplayEntityAccessor {

    @Invoker("setText")
    void invokerSetText(Component text);
}
