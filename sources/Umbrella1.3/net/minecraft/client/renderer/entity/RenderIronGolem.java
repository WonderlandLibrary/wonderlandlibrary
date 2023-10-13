/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderIronGolem
extends RenderLiving {
    private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");
    private static final String __OBFID = "CL_00001031";

    public RenderIronGolem(RenderManager p_i46133_1_) {
        super(p_i46133_1_, new ModelIronGolem(), 0.5f);
        this.addLayer(new LayerIronGolemFlower(this));
    }

    protected ResourceLocation getEntityTexture(EntityIronGolem p_110775_1_) {
        return ironGolemTextures;
    }

    protected void func_180588_a(EntityIronGolem p_180588_1_, float p_180588_2_, float p_180588_3_, float p_180588_4_) {
        super.rotateCorpse(p_180588_1_, p_180588_2_, p_180588_3_, p_180588_4_);
        if ((double)p_180588_1_.limbSwingAmount >= 0.01) {
            float var5 = 13.0f;
            float var6 = p_180588_1_.limbSwing - p_180588_1_.limbSwingAmount * (1.0f - p_180588_4_) + 6.0f;
            float var7 = (Math.abs(var6 % var5 - var5 * 0.5f) - var5 * 0.25f) / (var5 * 0.25f);
            GlStateManager.rotate(6.5f * var7, 0.0f, 0.0f, 1.0f);
        }
    }

    @Override
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        this.func_180588_a((EntityIronGolem)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityIronGolem)p_110775_1_);
    }
}

