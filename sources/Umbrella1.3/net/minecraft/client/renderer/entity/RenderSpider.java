/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderSpider
extends RenderLiving {
    private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");
    private static final String __OBFID = "CL_00001027";

    public RenderSpider(RenderManager p_i46139_1_) {
        super(p_i46139_1_, new ModelSpider(), 1.0f);
        this.addLayer(new LayerSpiderEyes(this));
    }

    protected float getDeathMaxRotation(EntitySpider p_77037_1_) {
        return 180.0f;
    }

    protected ResourceLocation getEntityTexture(EntitySpider p_110775_1_) {
        return spiderTextures;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_) {
        return this.getDeathMaxRotation((EntitySpider)p_77037_1_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntitySpider)p_110775_1_);
    }
}

