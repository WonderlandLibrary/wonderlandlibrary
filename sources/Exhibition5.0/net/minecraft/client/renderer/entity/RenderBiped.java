// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;

public class RenderBiped extends RenderLiving
{
    private static final ResourceLocation field_177118_j;
    protected ModelBiped modelBipedMain;
    protected float field_77070_b;
    private static final String __OBFID = "CL_00001001";
    
    public RenderBiped(final RenderManager p_i46168_1_, final ModelBiped p_i46168_2_, final float p_i46168_3_) {
        this(p_i46168_1_, p_i46168_2_, p_i46168_3_, 1.0f);
        this.addLayer(new LayerHeldItem(this));
    }
    
    public RenderBiped(final RenderManager p_i46169_1_, final ModelBiped p_i46169_2_, final float p_i46169_3_, final float p_i46169_4_) {
        super(p_i46169_1_, p_i46169_2_, p_i46169_3_);
        this.modelBipedMain = p_i46169_2_;
        this.field_77070_b = p_i46169_4_;
        this.addLayer(new LayerCustomHead(p_i46169_2_.bipedHead));
    }
    
    protected ResourceLocation getEntityTexture(final EntityLiving p_110775_1_) {
        return RenderBiped.field_177118_j;
    }
    
    @Override
    public void func_82422_c() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityLiving)p_110775_1_);
    }
    
    static {
        field_177118_j = new ResourceLocation("textures/entity/steve.png");
    }
}
