// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public class RenderBoat extends Render
{
    private static final ResourceLocation boatTextures;
    protected ModelBase modelBoat;
    private static final String __OBFID = "CL_00000981";
    
    public RenderBoat(final RenderManager p_i46190_1_) {
        super(p_i46190_1_);
        this.modelBoat = new ModelBoat();
        this.shadowSize = 0.5f;
    }
    
    public void func_180552_a(final EntityBoat p_180552_1_, final double p_180552_2_, final double p_180552_4_, final double p_180552_6_, final float p_180552_8_, final float p_180552_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180552_2_, (float)p_180552_4_ + 0.25f, (float)p_180552_6_);
        GlStateManager.rotate(180.0f - p_180552_8_, 0.0f, 1.0f, 0.0f);
        final float var10 = p_180552_1_.getTimeSinceHit() - p_180552_9_;
        float var11 = p_180552_1_.getDamageTaken() - p_180552_9_;
        if (var11 < 0.0f) {
            var11 = 0.0f;
        }
        if (var10 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(var10) * var10 * var11 / 10.0f * p_180552_1_.getForwardDirection(), 1.0f, 0.0f, 0.0f);
        }
        final float var12 = 0.75f;
        GlStateManager.scale(var12, var12, var12);
        GlStateManager.scale(1.0f / var12, 1.0f / var12, 1.0f / var12);
        this.bindEntityTexture(p_180552_1_);
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelBoat.render(p_180552_1_, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(p_180552_1_, p_180552_2_, p_180552_4_, p_180552_6_, p_180552_8_, p_180552_9_);
    }
    
    protected ResourceLocation func_180553_a(final EntityBoat p_180553_1_) {
        return RenderBoat.boatTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180553_a((EntityBoat)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180552_a((EntityBoat)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        boatTextures = new ResourceLocation("textures/entity/boat.png");
    }
}
