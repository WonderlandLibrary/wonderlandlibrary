// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityTNTPrimed;

public class RenderTNTPrimed extends Render
{
    private static final String __OBFID = "CL_00001030";
    
    public RenderTNTPrimed(final RenderManager p_i46134_1_) {
        super(p_i46134_1_);
        this.shadowSize = 0.5f;
    }
    
    public void doRender(final EntityTNTPrimed p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        final BlockRendererDispatcher var10 = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_ + 0.5f, (float)p_76986_6_);
        if (p_76986_1_.fuse - p_76986_9_ + 1.0f < 10.0f) {
            float var11 = 1.0f - (p_76986_1_.fuse - p_76986_9_ + 1.0f) / 10.0f;
            var11 = MathHelper.clamp_float(var11, 0.0f, 1.0f);
            var11 *= var11;
            var11 *= var11;
            final float var12 = 1.0f + var11 * 0.3f;
            GlStateManager.scale(var12, var12, var12);
        }
        float var11 = (1.0f - (p_76986_1_.fuse - p_76986_9_ + 1.0f) / 100.0f) * 0.8f;
        this.bindEntityTexture(p_76986_1_);
        GlStateManager.translate(-0.5f, -0.5f, 0.5f);
        var10.func_175016_a(Blocks.tnt.getDefaultState(), p_76986_1_.getBrightness(p_76986_9_));
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (p_76986_1_.fuse / 5 % 2 == 0) {
            GlStateManager.disableTextures();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0f, 1.0f, 1.0f, var11);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            var10.func_175016_a(Blocks.tnt.getDefaultState(), 1.0f);
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTextures();
        }
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    protected ResourceLocation func_180563_a(final EntityTNTPrimed p_180563_1_) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180563_a((EntityTNTPrimed)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityTNTPrimed)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
