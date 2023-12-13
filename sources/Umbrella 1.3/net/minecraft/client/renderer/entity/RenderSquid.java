/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.ResourceLocation;

public class RenderSquid
extends RenderLiving {
    private static final ResourceLocation squidTextures = new ResourceLocation("textures/entity/squid.png");
    private static final String __OBFID = "CL_00001028";

    public RenderSquid(RenderManager p_i46138_1_, ModelBase p_i46138_2_, float p_i46138_3_) {
        super(p_i46138_1_, p_i46138_2_, p_i46138_3_);
    }

    protected ResourceLocation getEntityTexture(EntitySquid p_110775_1_) {
        return squidTextures;
    }

    protected void rotateCorpse(EntitySquid p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        float var5 = p_77043_1_.prevSquidPitch + (p_77043_1_.squidPitch - p_77043_1_.prevSquidPitch) * p_77043_4_;
        float var6 = p_77043_1_.prevSquidYaw + (p_77043_1_.squidYaw - p_77043_1_.prevSquidYaw) * p_77043_4_;
        GlStateManager.translate(0.0f, 0.5f, 0.0f);
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(var5, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(var6, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, -1.2f, 0.0f);
    }

    protected float handleRotationFloat(EntitySquid p_77044_1_, float p_77044_2_) {
        return p_77044_1_.lastTentacleAngle + (p_77044_1_.tentacleAngle - p_77044_1_.lastTentacleAngle) * p_77044_2_;
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_) {
        return this.handleRotationFloat((EntitySquid)p_77044_1_, p_77044_2_);
    }

    @Override
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        this.rotateCorpse((EntitySquid)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntitySquid)p_110775_1_);
    }
}

