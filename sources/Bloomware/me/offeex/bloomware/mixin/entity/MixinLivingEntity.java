package me.offeex.bloomware.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.offeex.bloomware.client.module.visuals.Particles;
import me.offeex.bloomware.event.events.EventMovement;
import me.offeex.bloomware.api.util.MixinUtil;
import me.offeex.bloomware.event.events.EventSprint;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @ModifyExpressionValue(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    float jumpYaw(float original) {
        if (MixinUtil.isClientEntity(this)) {
            EventSprint.JumpAcceleration event = new EventSprint.JumpAcceleration(original).post();
            return event.getYaw();
        } else return original;
    }

    @ModifyReturnValue(method = "isClimbing", at = @At("RETURN"))
    boolean onIsClimbing(boolean original) {
        if (MixinUtil.isClientEntity(this)) {
            EventMovement.IsClimbing event = new EventMovement.IsClimbing(original).post();
            return event.getClimbing();
        }
        return original;
    }

    @ModifyVariable(method = "applyMovementInput", at = @At("STORE"), ordinal = 1)
    private Vec3d onApplyMovementInput(Vec3d value) {
        if (MixinUtil.isClientEntity(this)) {
            EventMovement.Climbing event = new EventMovement.Climbing(value).post();
            return event.getVec3d();
        }
        return value;
    }

    @ModifyArg(method = "spawnConsumptionEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;spawnItemParticles(Lnet/minecraft/item/ItemStack;I)V"), index = 1)
    public int spawnConsumptionEffects(int i) {
        Particles module = Particles.INSTANCE;
        return module.getEnabled() && Particles.INSTANCE.getCrack().getToggled() ? (int) Particles.INSTANCE.getCAmount().getValue() : i;
    }
}
