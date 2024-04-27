package me.aquavit.liquidsense.injection.forge.mixins.client;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.utils.mc.ClassUtils;
import net.minecraft.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Profiler.class)
public class MixinProfiler {

    @Inject(method = "startSection", at = @At("HEAD"))
    private void startSection(String name, CallbackInfo callbackInfo) {
        if(name.equals("bossHealth") && ClassUtils.hasClass("net.labymod.api.LabyModAPI"))
            LiquidSense.eventManager.callEvent(new Render2DEvent(0F));
    }
}