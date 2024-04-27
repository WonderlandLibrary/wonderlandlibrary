/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.potion.PotionEffect
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={EntityLivingBase.class})
public class EntityLivingBaseMixin_ResolveCrash {
    @Inject(method={"updatePotionEffects"}, at={@At(value="INVOKE", target="Lnet/minecraft/potion/PotionEffect;onUpdate(Lnet/minecraft/entity/EntityLivingBase;)Z")}, locals=LocalCapture.CAPTURE_FAILSOFT, cancellable=true)
    private void patcher$checkPotionEffect(CallbackInfo ci, Iterator<Integer> iterator2, Integer integer, PotionEffect potioneffect) {
        if (potioneffect == null) {
            ci.cancel();
        }
    }
}

