package sudo.mixins;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import sudo.Client;
import sudo.module.render.Chams;

@Mixin(EntityRenderDispatcher.class)
public class RenderEntityDispatcherMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public <E extends Entity> void onRenderPre(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Chams.get.shouldRender(entity)) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public <E extends Entity> void onRenderPost(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Chams.get.shouldRender(entity)) {
            GL11.glPolygonOffset(1.0f, 1100000.0f);
            Client.logger.info("e");
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }
}