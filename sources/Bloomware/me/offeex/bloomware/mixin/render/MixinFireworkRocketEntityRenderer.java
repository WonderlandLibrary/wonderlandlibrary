package me.offeex.bloomware.mixin.render;

import me.offeex.bloomware.client.module.visuals.NoRender;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FireworkRocketEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntityRenderer.class)
public class MixinFireworkRocketEntityRenderer {
    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    public void renderFirework(FireworkRocketEntity fireworkRocketEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        NoRender module = NoRender.INSTANCE;
        if (module.getEnabled() && NoRender.INSTANCE.getFireworks().getToggled()) info.cancel();
    }
}
