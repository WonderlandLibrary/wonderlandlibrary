// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityFireball;

public class RenderFireball extends Render
{
    private float scale;
    private static final String __OBFID = "CL_00000995";
    
    public RenderFireball(final RenderManager p_i46176_1_, final float p_i46176_2_) {
        super(p_i46176_1_);
        this.scale = p_i46176_2_;
    }
    
    public void doRender(final EntityFireball p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(p_76986_1_);
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        GlStateManager.enableRescaleNormal();
        final float var10 = this.scale;
        GlStateManager.scale(var10 / 1.0f, var10 / 1.0f, var10 / 1.0f);
        final TextureAtlasSprite var11 = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
        final Tessellator var12 = Tessellator.getInstance();
        final WorldRenderer var13 = var12.getWorldRenderer();
        final float var14 = var11.getMinU();
        final float var15 = var11.getMaxU();
        final float var16 = var11.getMinV();
        final float var17 = var11.getMaxV();
        final float var18 = 1.0f;
        final float var19 = 0.5f;
        final float var20 = 0.25f;
        final float n = 180.0f;
        final RenderManager renderManager = this.renderManager;
        GlStateManager.rotate(n - RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        final RenderManager renderManager2 = this.renderManager;
        GlStateManager.rotate(-RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        var13.startDrawingQuads();
        var13.func_178980_d(0.0f, 1.0f, 0.0f);
        var13.addVertexWithUV(0.0f - var19, 0.0f - var20, 0.0, var14, var17);
        var13.addVertexWithUV(var18 - var19, 0.0f - var20, 0.0, var15, var17);
        var13.addVertexWithUV(var18 - var19, 1.0f - var20, 0.0, var15, var16);
        var13.addVertexWithUV(0.0f - var19, 1.0f - var20, 0.0, var14, var16);
        var12.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    protected ResourceLocation func_180556_a(final EntityFireball p_180556_1_) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_180556_a((EntityFireball)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityFireball)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
