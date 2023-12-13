// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.event.events.EventThrow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityThrowable.class })
public class MixinEntityThrowable
{
    @Inject(method = { "shoot(Lnet/minecraft/entity/Entity;FFFFF)V" }, at = { @At("HEAD") }, cancellable = true)
    private void shoot(final Entity entityThrower, final float rotationPitchIn, final float rotationYawIn, final float pitchOffset, final float velocity, final float inaccuracy, final CallbackInfo info) {
        final EventThrow eventThrow = new EventThrow(entityThrower, EntityThrowable.class.cast(this), rotationYawIn);
        eventThrow.call();
        if (eventThrow.isCancelled()) {
            info.cancel();
        }
    }
}
