// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.util.ResourceLocation;

public class RenderLeashKnot extends Render
{
    private static final ResourceLocation leashKnotTextures;
    private ModelLeashKnot leashKnotModel;
    private static final String __OBFID = "CL_00001010";
    
    public RenderLeashKnot(final RenderManager p_i46158_1_) {
        super(p_i46158_1_);
        this.leashKnotModel = new ModelLeashKnot();
    }
    
    public void func_180559_a(final EntityLeashKnot p_180559_1_, final double p_180559_2_, final double p_180559_4_, final double p_180559_6_, final float p_180559_8_, final float p_180559_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float)p_180559_2_, (float)p_180559_4_, (float)p_180559_6_);
        final float var10 = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(p_180559_1_);
        this.leashKnotModel.render(p_180559_1_, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, var10);
        GlStateManager.popMatrix();
        super.doRender(p_180559_1_, p_180559_2_, p_180559_4_, p_180559_6_, p_180559_8_, p_180559_9_);
    }
    
    protected ResourceLocation getEntityTexture(final EntityLeashKnot p_110775_1_) {
        return RenderLeashKnot.leashKnotTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityLeashKnot)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180559_a((EntityLeashKnot)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        leashKnotTextures = new ResourceLocation("textures/entity/lead_knot.png");
    }
}
