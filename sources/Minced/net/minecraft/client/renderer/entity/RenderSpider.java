// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntitySpider;

public class RenderSpider<T extends EntitySpider> extends RenderLiving<T>
{
    private static final ResourceLocation SPIDER_TEXTURES;
    
    public RenderSpider(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSpider(), 1.0f);
        this.addLayer(new LayerSpiderEyes(this));
    }
    
    @Override
    protected float getDeathMaxRotation(final T entityLivingBaseIn) {
        return 180.0f;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final T entity) {
        return RenderSpider.SPIDER_TEXTURES;
    }
    
    static {
        SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/spider.png");
    }
}
