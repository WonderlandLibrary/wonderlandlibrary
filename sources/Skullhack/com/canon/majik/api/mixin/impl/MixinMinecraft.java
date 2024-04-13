package com.canon.majik.api.mixin.impl;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.event.events.LoopEvent;
import com.canon.majik.api.event.events.MultiTaskEvent;
import com.canon.majik.api.event.events.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "runTick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci){
        TickEvent eventUpdate = new TickEvent();
        Initializer.eventBus.invoke(eventUpdate);
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void onLoop(CallbackInfo ci){
        LoopEvent loopEvent = new LoopEvent();
        Initializer.eventBus.invoke(loopEvent);
    }

    @Redirect(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    public boolean multiTask(EntityPlayerSP entityPlayerSP){
        MultiTaskEvent event = new MultiTaskEvent();
        Initializer.eventBus.invoke(event);
        return event.isCancelled();
    }

}
