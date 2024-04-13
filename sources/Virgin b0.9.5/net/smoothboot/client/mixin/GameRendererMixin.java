package net.smoothboot.client.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.combat.Reach;
import net.smoothboot.client.module.modmanager;
import net.smoothboot.client.module.render.Nohurtcam;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
    public class GameRendererMixin {
        @Inject(method = "tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("HEAD"), cancellable = true)
        public void bobViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
            if (Nohurtcam.hurtCam) {
                ci.cancel();
            }
        }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {"ldc=hand"}), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        for (Mod m : modmanager.INSTANCE.getModule()) {
            if (m.isEnabled()) {
                m.onWorldRender(matrices);
            }
        }
    }

    @ModifyConstant(method = {"updateTargetedEntity"}, constant = {@Constant(doubleValue = 9.0)})
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
        return ((Reach.reachHack() * ((Reach.reachHack()))));
    }

    @Inject(
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0),
            method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V")
    private void onRender(float partialTicks, long finishTimeNano, MatrixStack matrices, CallbackInfo ci) {
    }
}