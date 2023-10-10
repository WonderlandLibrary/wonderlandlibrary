/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEntity
extends Render<Entity> {
    public RenderEntity(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(Entity entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        RenderEntity.renderOffsetAABB(entity.getEntityBoundingBox(), x2 - entity.lastTickPosX, y2 - entity.lastTickPosY, z2 - entity.lastTickPosZ);
        GlStateManager.popMatrix();
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}

