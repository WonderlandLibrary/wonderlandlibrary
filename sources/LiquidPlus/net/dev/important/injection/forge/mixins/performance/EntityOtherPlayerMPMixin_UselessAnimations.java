/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityOtherPlayerMP.class})
public class EntityOtherPlayerMPMixin_UselessAnimations {
    @Inject(method={"onLivingUpdate"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityOtherPlayerMP;updateArmSwingProgress()V", shift=At.Shift.AFTER)}, cancellable=true)
    private void patcher$removeUselessAnimations(CallbackInfo ci) {
        ci.cancel();
    }
}

