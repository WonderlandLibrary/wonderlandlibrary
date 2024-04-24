package me.offeex.bloomware.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.offeex.bloomware.api.util.MathUtil;
import me.offeex.bloomware.client.module.motion.NoSlow;
import me.offeex.bloomware.event.events.EventMovement;
import me.offeex.bloomware.event.events.EventUpdate;
import me.offeex.bloomware.event.events.EventVelocity;
import me.offeex.bloomware.api.util.MixinUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @ModifyReturnValue(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "RETURN", ordinal = 0))
    Vec3d onStep(Vec3d original) {
        if (MixinUtil.isClientEntity(this)) {
            EventMovement.Step event = new EventMovement.Step(original).post();
            return event.getVec3d();
        }
        return original;
    }

    @Inject(method = "changeLookDirection", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setPitch(F)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci, float deltaY, float deltaX) {
        new EventUpdate.Rotation.Both(deltaX, deltaY).post(ci);
    }

    @ModifyVariable(method = "changeLookDirection", at = @At(value = "STORE"), ordinal = 1)
    private float onYaw(float value) {
        EventUpdate.Rotation.Yaw event = new EventUpdate.Rotation.Yaw(value).post();
        return event.getDelta();
    }

    @ModifyVariable(method = "changeLookDirection", at = @At("STORE"), ordinal = 0)
    private float onPitch(float value) {
        EventUpdate.Rotation.Pitch event = new EventUpdate.Rotation.Pitch(value).post();
        return event.getDelta();
    }

    @Redirect(method = "pushAwayFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void pushAwayFrom(Entity instance, double deltaX, double deltaY, double deltaZ) {
        MathUtil.INSTANCE.protection();
        if (instance instanceof ClientPlayerEntity p) {
            EventVelocity.Push event = new EventVelocity.Push.Entities(p, deltaX, deltaY, deltaZ).post();
            instance.addVelocity(event.getX(), event.getY(), event.getZ());
        } else instance.addVelocity(deltaX, deltaY, deltaZ);
    }

    @Redirect(method = "getVelocityMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
    private Block getVelocityMultiplierGetBlockProxy(BlockState blockState) {
        NoSlow module = NoSlow.INSTANCE;
        if (blockState.getBlock() == Blocks.SOUL_SAND && NoSlow.INSTANCE.getSoulSand().getToggled() && module.getEnabled())
            return Blocks.STONE;
        return blockState.getBlock();
    }

    @Redirect(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    void updateMovementInFluid(Entity instance, Vec3d velocity) {
        if (instance instanceof ClientPlayerEntity) {
            Vec3d d = velocity.subtract(instance.getVelocity());
            EventVelocity.Fluid event = new EventVelocity.Fluid(d.x, d.y, d.z).post();
            instance.addVelocity(event.getX(), event.getY(), event.getZ());
        } else instance.setVelocity(velocity);
    }
}
