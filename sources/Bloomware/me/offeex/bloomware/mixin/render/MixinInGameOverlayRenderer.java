package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.client.module.visuals.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class MixinInGameOverlayRenderer {
    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderFire(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (NoRender.INSTANCE.getFire().getToggled() && module.getEnabled()) ci.cancel();
    }

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderUnderWater(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (NoRender.INSTANCE.getWater().getToggled() && module.getEnabled()) ci.cancel();
    }

    @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
    private static void renderInWall(Sprite sprite, MatrixStack matrixStack, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (NoRender.INSTANCE.getWalls().getToggled() && module.getEnabled()) ci.cancel();
    }
}
