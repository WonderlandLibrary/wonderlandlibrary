// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.util.ResourceLocation;

public class RenderSnowMan extends RenderLiving
{
    private static final ResourceLocation snowManTextures;
    private static final String __OBFID = "CL_00001025";
    
    public RenderSnowMan(final RenderManager p_i46140_1_) {
        super(p_i46140_1_, new ModelSnowMan(), 0.5f);
        this.addLayer(new LayerSnowmanHead(this));
    }
    
    protected ResourceLocation func_180587_a(final EntitySnowman p_180587_1_) {
        return RenderSnowMan.snowManTextures;
    }
    
    public ModelSnowMan func_177123_g() {
        return (ModelSnowMan)super.getMainModel();
    }
    
    @Override
    public ModelBase getMainModel() {
        return this.func_177123_g();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180587_a((EntitySnowman)p_110775_1_);
    }
    
    static {
        snowManTextures = new ResourceLocation("textures/entity/snowman.png");
    }
}
