/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderCaveSpider
extends RenderSpider {
    private static final ResourceLocation caveSpiderTextures = new ResourceLocation("textures/entity/spider/cave_spider.png");
    private static final String __OBFID = "CL_00000982";

    public RenderCaveSpider(RenderManager p_i46189_1_) {
        super(p_i46189_1_);
        this.shadowSize *= 0.7f;
    }

    protected void func_180585_a(EntityCaveSpider p_180585_1_, float p_180585_2_) {
        GlStateManager.scale(0.7f, 0.7f, 0.7f);
    }

    protected ResourceLocation func_180586_a(EntityCaveSpider p_180586_1_) {
        return caveSpiderTextures;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySpider p_110775_1_) {
        return this.func_180586_a((EntityCaveSpider)p_110775_1_);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        this.func_180585_a((EntityCaveSpider)p_77041_1_, p_77041_2_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_180586_a((EntityCaveSpider)p_110775_1_);
    }
}

