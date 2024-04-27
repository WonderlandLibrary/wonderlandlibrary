/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.EntityFX
 */
package net.dev.important.injection.forge.mixins.optimize;

import net.dev.important.modules.module.modules.misc.Performance;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={EntityFX.class})
public class MixinEntityFX {
    @Redirect(method={"renderParticle"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/particle/EntityFX;getBrightnessForRender(F)I"))
    private int renderParticle(EntityFX entityFX, float f) {
        return (Boolean)Performance.staticParticleColorValue.get() != false ? 0xF000F0 : entityFX.func_70070_b(f);
    }
}

