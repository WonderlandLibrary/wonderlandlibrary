package me.offeex.bloomware.mixin.entity;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.player.AutoFish;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class MixinFishingBobberEntity {
    @Inject(method = "onTrackedDataSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;setVelocity(DDD)V"))
    private void onTrackerDataSet(TrackedData<?> data, CallbackInfo ci) {
        AutoFish module = AutoFish.INSTANCE;
        if (module.getEnabled() && AutoFish.INSTANCE.getTrigger().like("Hook")) Bloomware.INSTANCE.getMc().interactionManager.interactItem(Bloomware.INSTANCE.getMc().player, Hand.MAIN_HAND);
    }
}
