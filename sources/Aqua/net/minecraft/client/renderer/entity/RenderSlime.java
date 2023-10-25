package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

public class RenderSlime
extends RenderLiving<EntitySlime> {
    private static final ResourceLocation slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");

    public RenderSlime(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer((LayerRenderer)new LayerSlimeGel(this));
    }

    public void doRender(EntitySlime entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = 0.25f * (float)entity.getSlimeSize();
        super.doRender((EntityLiving)entity, x, y, z, entityYaw, partialTicks);
    }

    protected void preRenderCallback(EntitySlime entitylivingbaseIn, float partialTickTime) {
        float f = entitylivingbaseIn.getSlimeSize();
        float f1 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f * 0.5f + 1.0f);
        float f2 = 1.0f / (f1 + 1.0f);
        GlStateManager.scale((float)(f2 * f), (float)(1.0f / f2 * f), (float)(f2 * f));
    }

    protected ResourceLocation getEntityTexture(EntitySlime entity) {
        return slimeTextures;
    }
}
