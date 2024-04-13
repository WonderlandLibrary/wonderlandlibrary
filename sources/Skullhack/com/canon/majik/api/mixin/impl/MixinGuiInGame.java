package com.canon.majik.api.mixin.impl;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.event.events.Render2DEvent;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiInGame {

    @Inject(method = "renderHotbar", at = @At(value = "HEAD"))
    private void renderTooltip(ScaledResolution two, float one, CallbackInfo info) {
        Render2DEvent render2DEvent = new Render2DEvent(one);
        Initializer.eventBus.invoke(render2DEvent);
    }
}
