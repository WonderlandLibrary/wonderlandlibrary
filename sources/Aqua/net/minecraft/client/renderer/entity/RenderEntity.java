package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

public class RenderEntity
extends Render<Entity> {
    public RenderEntity(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        RenderEntity.renderOffsetAABB((AxisAlignedBB)entity.getEntityBoundingBox(), (double)(x - entity.lastTickPosX), (double)(y - entity.lastTickPosY), (double)(z - entity.lastTickPosZ));
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
