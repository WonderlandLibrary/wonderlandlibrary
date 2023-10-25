/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.ResourceLocation;

public class RenderSquid
extends RenderLiving<EntitySquid> {
    private static final ResourceLocation squidTextures = new ResourceLocation("textures/entity/squid.png");

    @Override
    protected ResourceLocation getEntityTexture(EntitySquid entitySquid) {
        return squidTextures;
    }

    public RenderSquid(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }

    @Override
    protected float handleRotationFloat(EntitySquid entitySquid, float f) {
        return entitySquid.lastTentacleAngle + (entitySquid.tentacleAngle - entitySquid.lastTentacleAngle) * f;
    }

    @Override
    protected void rotateCorpse(EntitySquid entitySquid, float f, float f2, float f3) {
        float f4 = entitySquid.prevSquidPitch + (entitySquid.squidPitch - entitySquid.prevSquidPitch) * f3;
        float f5 = entitySquid.prevSquidYaw + (entitySquid.squidYaw - entitySquid.prevSquidYaw) * f3;
        GlStateManager.translate(0.0f, 0.5f, 0.0f);
        GlStateManager.rotate(180.0f - f2, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f5, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, -1.2f, 0.0f);
    }
}

