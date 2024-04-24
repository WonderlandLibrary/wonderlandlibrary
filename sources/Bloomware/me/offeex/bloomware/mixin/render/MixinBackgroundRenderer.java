package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.client.module.visuals.FullBright;
import me.offeex.bloomware.client.module.visuals.NoRender;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void removeFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if (NoRender.INSTANCE.getEnabled() && NoRender.INSTANCE.getFog().getToggled()) ci.cancel();
    }

    @ModifyVariable(
            method = "render",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F", ordinal = 0)),
            index = 9)
    private static float modifySkyBrightness(float value) {
        return FullBright.INSTANCE.getEnabled() && FullBright.INSTANCE.getMode().like("Lightmap")
                ? (float) FullBright.INSTANCE.getSkyBrightness().getValue() : value;
    }
}
