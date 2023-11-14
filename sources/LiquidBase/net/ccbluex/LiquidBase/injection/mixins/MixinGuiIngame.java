package net.ccbluex.LiquidBase.injection.mixins;

import net.ccbluex.LiquidBase.event.EventManager;
import net.ccbluex.LiquidBase.event.events.Render2DEvent;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderTooltip", at = @At("RETURN"))
    private void render2D(CallbackInfo callbackInfo) {
        EventManager.callEvent(new Render2DEvent());
    }
}