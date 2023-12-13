/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderBat
extends RenderLiving {
    private static final ResourceLocation batTextures = new ResourceLocation("textures/entity/bat.png");
    private static final String __OBFID = "CL_00000979";

    public RenderBat(RenderManager p_i46192_1_) {
        super(p_i46192_1_, new ModelBat(), 0.25f);
    }

    protected ResourceLocation func_180566_a(EntityBat p_180566_1_) {
        return batTextures;
    }

    protected void func_180567_a(EntityBat p_180567_1_, float p_180567_2_) {
        GlStateManager.scale(0.35f, 0.35f, 0.35f);
    }

    protected void rotateCorpse(EntityBat p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        if (!p_77043_1_.getIsBatHanging()) {
            GlStateManager.translate(0.0f, MathHelper.cos(p_77043_2_ * 0.3f) * 0.1f, 0.0f);
        } else {
            GlStateManager.translate(0.0f, -0.1f, 0.0f);
        }
        super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        this.func_180567_a((EntityBat)p_77041_1_, p_77041_2_);
    }

    @Override
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        this.rotateCorpse((EntityBat)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_180566_a((EntityBat)p_110775_1_);
    }
}

