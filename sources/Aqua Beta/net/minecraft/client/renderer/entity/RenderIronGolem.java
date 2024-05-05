package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderIronGolem
extends RenderLiving<EntityIronGolem> {
    private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");

    public RenderIronGolem(RenderManager renderManagerIn) {
        super(renderManagerIn, (ModelBase)new ModelIronGolem(), 0.5f);
        this.addLayer((LayerRenderer)new LayerIronGolemFlower(this));
    }

    protected ResourceLocation getEntityTexture(EntityIronGolem entity) {
        return ironGolemTextures;
    }

    protected void rotateCorpse(EntityIronGolem bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        super.rotateCorpse((EntityLivingBase)bat, p_77043_2_, p_77043_3_, partialTicks);
        if ((double)bat.limbSwingAmount >= 0.01) {
            float f = 13.0f;
            float f1 = bat.limbSwing - bat.limbSwingAmount * (1.0f - partialTicks) + 6.0f;
            float f2 = (Math.abs((float)(f1 % f - f * 0.5f)) - f * 0.25f) / (f * 0.25f);
            GlStateManager.rotate((float)(6.5f * f2), (float)0.0f, (float)0.0f, (float)1.0f);
        }
    }
}
