/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.EffectRenderer
 *  net.minecraft.client.particle.EntityFX
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.entity.Entity
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.patcher.util.world.ParticleCulling;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={EffectRenderer.class})
public class EffectRendererMixin_ParticleCulling {
    @Redirect(method={"renderParticles"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/particle/EntityFX;renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void patcher$cullParticles(EntityFX instance, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (ParticleCulling.shouldRender(instance)) {
            instance.func_180434_a(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        }
    }

    @ModifyVariable(method={"updateEffectAlphaLayer"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/particle/EffectRenderer;tickParticle(Lnet/minecraft/client/particle/EntityFX;)V", shift=At.Shift.AFTER))
    private EntityFX patcher$checkIfCulled(EntityFX entityFX) {
        if (ParticleCulling.camera != null) {
            entityFX.field_70140_Q = ParticleCulling.camera.func_78546_a(entityFX.func_174813_aQ()) ? 1.0f : -1.0f;
        }
        return entityFX;
    }
}

