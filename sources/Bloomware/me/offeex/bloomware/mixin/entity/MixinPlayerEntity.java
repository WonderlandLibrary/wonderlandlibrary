package me.offeex.bloomware.mixin.entity;

import me.offeex.bloomware.api.util.MixinUtil;
import me.offeex.bloomware.event.events.EventClipAtLedge;
import me.offeex.bloomware.event.events.EventDamage;
import me.offeex.bloomware.event.events.EventPlayerTravel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
    public MixinPlayerEntity(World worldIn) {
        super(EntityType.PLAYER, worldIn);
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d vec3d, CallbackInfo ci) {
        if (MixinUtil.isClientEntity(this)) new EventPlayerTravel(vec3d).post(ci);
    }

    @Inject(method = "clipAtLedge", at = @At("RETURN"), cancellable = true)
    private void clipAtLedge(CallbackInfoReturnable<Boolean> cir) {
        EventClipAtLedge event = new EventClipAtLedge(cir.getReturnValue()).post();
        cir.setReturnValue(event.getClip());
    }

    @Inject(method = "damageArmor", at = @At("HEAD"))
    void damageArmor(DamageSource source, float amount, CallbackInfo ci) {
        new EventDamage.Armor(source, amount).post(ci);
    }
}
