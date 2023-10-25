package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderBat
extends RenderLiving<EntityBat> {
    private static final ResourceLocation batTextures = new ResourceLocation("textures/entity/bat.png");

    public RenderBat(RenderManager renderManagerIn) {
        super(renderManagerIn, (ModelBase)new ModelBat(), 0.25f);
    }

    protected ResourceLocation getEntityTexture(EntityBat entity) {
        return batTextures;
    }

    protected void preRenderCallback(EntityBat entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale((float)0.35f, (float)0.35f, (float)0.35f);
    }

    protected void rotateCorpse(EntityBat bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        if (!bat.getIsBatHanging()) {
            GlStateManager.translate((float)0.0f, (float)(MathHelper.cos((float)(p_77043_2_ * 0.3f)) * 0.1f), (float)0.0f);
        } else {
            GlStateManager.translate((float)0.0f, (float)-0.1f, (float)0.0f);
        }
        super.rotateCorpse((EntityLivingBase)bat, p_77043_2_, p_77043_3_, partialTicks);
    }
}
