/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.ResourceLocation;

public class RenderSquid
extends RenderLiving<EntitySquid> {
    private static final ResourceLocation SQUID_TEXTURES = new ResourceLocation("textures/entity/squid.png");

    public RenderSquid(RenderManager p_i47192_1_) {
        super(p_i47192_1_, new ModelSquid(), 0.7f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySquid entity) {
        return SQUID_TEXTURES;
    }

    @Override
    protected void rotateCorpse(EntitySquid entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        float f = entityLiving.prevSquidPitch + (entityLiving.squidPitch - entityLiving.prevSquidPitch) * partialTicks;
        float f1 = entityLiving.prevSquidYaw + (entityLiving.squidYaw - entityLiving.prevSquidYaw) * partialTicks;
        GlStateManager.translate(0.0f, 0.5f, 0.0f);
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f1, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, -1.2f, 0.0f);
    }

    @Override
    protected float handleRotationFloat(EntitySquid livingBase, float partialTicks) {
        return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
    }
}

