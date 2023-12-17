// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityCaveSpider;

public class RenderCaveSpider extends RenderSpider<EntityCaveSpider>
{
    private static final ResourceLocation CAVE_SPIDER_TEXTURES;
    
    public RenderCaveSpider(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize *= 0.7f;
    }
    
    @Override
    protected void preRenderCallback(final EntityCaveSpider entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(0.7f, 0.7f, 0.7f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityCaveSpider entity) {
        return RenderCaveSpider.CAVE_SPIDER_TEXTURES;
    }
    
    static {
        CAVE_SPIDER_TEXTURES = new ResourceLocation("textures/entity/spider/cave_spider.png");
    }
}
