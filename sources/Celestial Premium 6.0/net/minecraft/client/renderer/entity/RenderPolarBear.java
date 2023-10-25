/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelPolarBear;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.util.ResourceLocation;

public class RenderPolarBear
extends RenderLiving<EntityPolarBear> {
    private static final ResourceLocation POLAR_BEAR_TEXTURE = new ResourceLocation("textures/entity/bear/polarbear.png");

    public RenderPolarBear(RenderManager p_i47197_1_) {
        super(p_i47197_1_, new ModelPolarBear(), 0.7f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPolarBear entity) {
        return POLAR_BEAR_TEXTURE;
    }

    @Override
    public void doRender(EntityPolarBear entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(EntityPolarBear entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(1.2f, 1.2f, 1.2f);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}

