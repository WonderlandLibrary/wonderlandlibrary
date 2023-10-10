package me.pianopenguin471.mixins;

import net.minecraft.client.renderer.RenderGlobal;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ravenweave.client.event.impl.DrawBlockHighlightEvent;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {
    @Inject(method = "drawSelectionBox", at = @At("HEAD"))
    public void onDrawSelectionBox(CallbackInfo ci) {
        EventBus.callEvent(new DrawBlockHighlightEvent());
    }
}
