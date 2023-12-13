// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.Render;

public class RenderEnderCrystal extends Render
{
    private static final ResourceLocation enderCrystalTextures;
    private ModelBase field_76995_b;
    private static final String __OBFID = "CL_00000987";
    
    public RenderEnderCrystal(final RenderManager p_i46184_1_) {
        super(p_i46184_1_);
        this.field_76995_b = new ModelEnderCrystal(0.0f, true);
        this.shadowSize = 0.5f;
    }
    
    public void doRender(final EntityEnderCrystal p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        final float var10 = p_76986_1_.innerRotation + p_76986_9_;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        this.bindTexture(RenderEnderCrystal.enderCrystalTextures);
        float var11 = MathHelper.sin(var10 * 0.2f) / 2.0f + 0.5f;
        var11 += var11 * var11;
        this.field_76995_b.render(p_76986_1_, 0.0f, var10 * 3.0f, var11 * 0.2f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    protected ResourceLocation func_180554_a(final EntityEnderCrystal p_180554_1_) {
        return RenderEnderCrystal.enderCrystalTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180554_a((EntityEnderCrystal)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityEnderCrystal)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        enderCrystalTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    }
}
