package net.smoothboot.client.mixin;

import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.smoothboot.client.module.render.PlayerESP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.smoothboot.client.hud.frame.*;
import static net.smoothboot.client.module.Mod.mc;
import static net.smoothboot.client.module.render.PlayerESP.ESPTeamcheck;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (PlayerESP.outliningEntities && vertexConsumers instanceof OutlineVertexConsumerProvider && entity.getType() == EntityType.PLAYER) {
            OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider) vertexConsumers;
            if (ESPTeamcheck.isEnabled() && !entity.isTeammate(mc.player)) {
                outlineVertexConsumers.setColor(menured, menugreen, menublue, 255);
            }
            else if (!ESPTeamcheck.isEnabled()) {
                outlineVertexConsumers.setColor(menured, menugreen, menublue, 255);
            }
        }
    }
}