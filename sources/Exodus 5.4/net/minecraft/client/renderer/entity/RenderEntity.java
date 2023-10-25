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
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

    public RenderEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        RenderEntity.renderOffsetAABB(entity.getEntityBoundingBox(), d - entity.lastTickPosX, d2 - entity.lastTickPosY, d3 - entity.lastTickPosZ);
        GlStateManager.popMatrix();
        super.doRender(entity, d, d2, d3, f, f2);
    }
}

