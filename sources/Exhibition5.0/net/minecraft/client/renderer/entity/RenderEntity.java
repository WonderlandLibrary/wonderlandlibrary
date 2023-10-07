// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class RenderEntity extends Render
{
    private static final String __OBFID = "CL_00000986";
    
    public RenderEntity(final RenderManager p_i46185_1_) {
        super(p_i46185_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        GlStateManager.pushMatrix();
        Render.renderOffsetAABB(p_76986_1_.getEntityBoundingBox(), p_76986_2_ - p_76986_1_.lastTickPosX, p_76986_4_ - p_76986_1_.lastTickPosY, p_76986_6_ - p_76986_1_.lastTickPosZ);
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return null;
    }
}
