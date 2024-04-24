package me.offeex.bloomware.mixin.world;

import me.offeex.bloomware.event.events.EventCamera;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void clipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> cir) {
        new EventCamera.ClipToSpace(desiredCameraDistance).post(cir);
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void onSetRot(Args args) {
        EventCamera.Rotation event = new EventCamera.Rotation(args.get(0), args.get(1)).post();
        args.setAll(event.getYaw(), event.getPitch());
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void onSetPos(Args args) {
        EventCamera.Position event = new EventCamera.Position(args.get(0), args.get(1), args.get(2)).post();
        args.setAll(event.getX(), event.getY(), event.getZ());
    }
}
