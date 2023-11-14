package net.ccbluex.LiquidBase.injection.mixins;

import net.ccbluex.LiquidBase.event.EventManager;
import net.ccbluex.LiquidBase.event.events.MotionUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void preMotion(CallbackInfo callbackInfo) {
        EventManager.callEvent(new MotionUpdateEvent(MotionUpdateEvent.State.PRE));
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    private void postMotion(CallbackInfo callbackInfo) {
        EventManager.callEvent(new MotionUpdateEvent(MotionUpdateEvent.State.POST));
    }
}