package com.canon.majik.api.mixin.impl;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.Globals;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer implements Globals {
    private float renderPitch, renderHeadYaw;

    @Inject(method = "doRender*", at = @At("HEAD"))
    public void doRenderPre(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (entity.equals(mc.player) && mc.currentScreen == null) {
            renderPitch = entity.rotationPitch;
            renderHeadYaw = entity.rotationYawHead;

            entity.rotationPitch = Initializer.rotationManager.getSpoofedPitch();
            entity.prevRotationPitch = Initializer.rotationManager.getSpoofedPitch();

            entity.rotationYawHead = Initializer.rotationManager.getSpoofedYaw();
            entity.prevRotationYaw = Initializer.rotationManager.getSpoofedYaw();
            entity.prevRotationYawHead = Initializer.rotationManager.getSpoofedYaw();
        }
    }

    @Inject(method = "doRender*", at = @At("RETURN"))
    public void rotateEnd(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (entity.equals(mc.player) && mc.currentScreen == null) {
            entity.rotationPitch = renderPitch;
            entity.prevRotationPitch = renderPitch;

            entity.rotationYawHead = renderHeadYaw;
            entity.prevRotationYaw = renderHeadYaw;
            entity.prevRotationYawHead = renderHeadYaw;
        }
    }
}
