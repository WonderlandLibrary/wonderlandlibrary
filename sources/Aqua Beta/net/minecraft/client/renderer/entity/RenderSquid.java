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

    public RenderSquid(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected ResourceLocation getEntityTexture(EntitySquid entity) {
        return squidTextures;
    }

    protected void rotateCorpse(EntitySquid bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        float f = bat.prevSquidPitch + (bat.squidPitch - bat.prevSquidPitch) * partialTicks;
        float f1 = bat.prevSquidYaw + (bat.squidYaw - bat.prevSquidYaw) * partialTicks;
        GlStateManager.translate((float)0.0f, (float)0.5f, (float)0.0f);
        GlStateManager.rotate((float)(180.0f - p_77043_3_), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)f1, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)-1.2f, (float)0.0f);
    }

    protected float handleRotationFloat(EntitySquid livingBase, float partialTicks) {
        return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
    }
}
