package me.pianopenguin471.mixins;

import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ravenweave.client.event.impl.GameLoopEvent;

@Mixin(priority = 1005, value = Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "runTick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        EventBus.callEvent(new GameLoopEvent());
    }

}
