/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class RenderFireball
extends Render {
    private float scale;
    private static final String __OBFID = "CL_00000995";

    public RenderFireball(RenderManager p_i46176_1_, float p_i46176_2_) {
        super(p_i46176_1_);
        this.scale = p_i46176_2_;
    }

    public void doRender(EntityFireball p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(p_76986_1_);
        GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        GlStateManager.enableRescaleNormal();
        float var10 = this.scale;
        GlStateManager.scale(var10 / 1.0f, var10 / 1.0f, var10 / 1.0f);
        TextureAtlasSprite var11 = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
        Tessellator var12 = Tessellator.getInstance();
        WorldRenderer var13 = var12.getWorldRenderer();
        float var14 = var11.getMinU();
        float var15 = var11.getMaxU();
        float var16 = var11.getMinV();
        float var17 = var11.getMaxV();
        float var18 = 1.0f;
        float var19 = 0.5f;
        float var20 = 0.25f;
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
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

    protected ResourceLocation func_180556_a(EntityFireball p_180556_1_) {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_180556_a((EntityFireball)p_110775_1_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityFireball)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}

