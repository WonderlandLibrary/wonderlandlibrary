// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.util.ResourceLocation;

public class RenderSpider extends RenderLiving
{
    private static final ResourceLocation spiderTextures;
    private static final String __OBFID = "CL_00001027";
    
    public RenderSpider(final RenderManager p_i46139_1_) {
        super(p_i46139_1_, new ModelSpider(), 1.0f);
        this.addLayer(new LayerSpiderEyes(this));
    }
    
    protected float getDeathMaxRotation(final EntitySpider p_77037_1_) {
        return 180.0f;
    }
    
    protected ResourceLocation getEntityTexture(final EntitySpider p_110775_1_) {
        return RenderSpider.spiderTextures;
    }
    
    @Override
    protected float getDeathMaxRotation(final EntityLivingBase p_77037_1_) {
        return this.getDeathMaxRotation((EntitySpider)p_77037_1_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntitySpider)p_110775_1_);
    }
    
    static {
        spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");
    }
}
