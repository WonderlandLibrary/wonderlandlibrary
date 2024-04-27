/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.profiler.Profiler
 */
package net.dev.important.injection.forge.mixins.client;

import net.dev.important.Client;
import net.dev.important.event.Render2DEvent;
import net.dev.important.utils.ClassUtils;
import net.minecraft.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Profiler.class})
public class MixinProfiler {
    @Inject(method={"startSection"}, at={@At(value="HEAD")})
    private void startSection(String name, CallbackInfo callbackInfo) {
        if (name.equals("bossHealth") && ClassUtils.hasClass("net.labymod.api.LabyModAPI")) {
            Client.eventManager.callEvent(new Render2DEvent(0.0f));
        }
    }
}

