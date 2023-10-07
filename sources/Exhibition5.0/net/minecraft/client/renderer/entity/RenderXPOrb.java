// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;

public class RenderXPOrb extends Render
{
    private static final ResourceLocation experienceOrbTextures;
    private static final String __OBFID = "CL_00000993";
    
    public RenderXPOrb(final RenderManager p_i46178_1_) {
        super(p_i46178_1_);
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }
    
    public void doRender(final EntityXPOrb p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        this.bindEntityTexture(p_76986_1_);
        final int var10 = p_76986_1_.getTextureByXP();
        final float var11 = (var10 % 4 * 16 + 0) / 64.0f;
        final float var12 = (var10 % 4 * 16 + 16) / 64.0f;
        final float var13 = (var10 / 4 * 16 + 0) / 64.0f;
        final float var14 = (var10 / 4 * 16 + 16) / 64.0f;
        final float var15 = 1.0f;
        final float var16 = 0.5f;
        final float var17 = 0.25f;
        final int var18 = p_76986_1_.getBrightnessForRender(p_76986_9_);
        final int var19 = var18 % 65536;
        int var20 = var18 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var19 / 1.0f, var20 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float var21 = 255.0f;
        final float var22 = (p_76986_1_.xpColor + p_76986_9_) / 2.0f;
        var20 = (int)((MathHelper.sin(var22 + 0.0f) + 1.0f) * 0.5f * var21);
        final int var23 = (int)var21;
        final int var24 = (int)((MathHelper.sin(var22 + 4.1887903f) + 1.0f) * 0.1f * var21);
        final int var25 = var20 << 16 | var23 << 8 | var24;
        final float n = 180.0f;
        final RenderManager renderManager = this.renderManager;
        GlStateManager.rotate(n - RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        final RenderManager renderManager2 = this.renderManager;
        GlStateManager.rotate(-RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        final float var26 = 0.3f;
        GlStateManager.scale(var26, var26, var26);
        final Tessellator var27 = Tessellator.getInstance();
        final WorldRenderer var28 = var27.getWorldRenderer();
        var28.startDrawingQuads();
        var28.drawColor(var25, 128);
        var28.func_178980_d(0.0f, 1.0f, 0.0f);
        var28.addVertexWithUV(0.0f - var16, 0.0f - var17, 0.0, var11, var14);
        var28.addVertexWithUV(var15 - var16, 0.0f - var17, 0.0, var12, var14);
        var28.addVertexWithUV(var15 - var16, 1.0f - var17, 0.0, var12, var13);
        var28.addVertexWithUV(0.0f - var16, 1.0f - var17, 0.0, var11, var13);
        var27.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    protected ResourceLocation getTexture(final EntityXPOrb p_180555_1_) {
        return RenderXPOrb.experienceOrbTextures;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getTexture((EntityXPOrb)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityXPOrb)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");
    }
}
