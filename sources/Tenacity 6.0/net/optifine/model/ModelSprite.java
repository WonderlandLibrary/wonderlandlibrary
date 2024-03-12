// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.model;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.model.ModelRenderer;

public class ModelSprite
{
    private ModelRenderer modelRenderer;
    private int textureOffsetX;
    private int textureOffsetY;
    private float posX;
    private float posY;
    private float posZ;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private float sizeAdd;
    private float minU;
    private float minV;
    private float maxU;
    private float maxV;
    
    public ModelSprite(final ModelRenderer modelRenderer, final int textureOffsetX, final int textureOffsetY, final float posX, final float posY, final float posZ, final int sizeX, final int sizeY, final int sizeZ, final float sizeAdd) {
        this.modelRenderer = null;
        this.textureOffsetX = 0;
        this.textureOffsetY = 0;
        this.posX = 0.0f;
        this.posY = 0.0f;
        this.posZ = 0.0f;
        this.sizeX = 0;
        this.sizeY = 0;
        this.sizeZ = 0;
        this.sizeAdd = 0.0f;
        this.minU = 0.0f;
        this.minV = 0.0f;
        this.maxU = 0.0f;
        this.maxV = 0.0f;
        this.modelRenderer = modelRenderer;
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.sizeAdd = sizeAdd;
        this.minU = textureOffsetX / modelRenderer.textureWidth;
        this.minV = textureOffsetY / modelRenderer.textureHeight;
        this.maxU = (textureOffsetX + sizeX) / modelRenderer.textureWidth;
        this.maxV = (textureOffsetY + sizeY) / modelRenderer.textureHeight;
    }
    
    public void render(final Tessellator tessellator, final float scale) {
        GlStateManager.translate(this.posX * scale, this.posY * scale, this.posZ * scale);
        float f = this.minU;
        float f2 = this.maxU;
        float f3 = this.minV;
        float f4 = this.maxV;
        if (this.modelRenderer.mirror) {
            f = this.maxU;
            f2 = this.minU;
        }
        if (this.modelRenderer.mirrorV) {
            f3 = this.maxV;
            f4 = this.minV;
        }
        renderItemIn2D(tessellator, f, f3, f2, f4, this.sizeX, this.sizeY, scale * this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
        GlStateManager.translate(-this.posX * scale, -this.posY * scale, -this.posZ * scale);
    }
    
    public static void renderItemIn2D(final Tessellator tess, final float minU, final float minV, final float maxU, final float maxV, final int sizeX, final int sizeY, float width, final float texWidth, final float texHeight) {
        if (width < 6.25E-4f) {
            width = 6.25E-4f;
        }
        final float f = maxU - minU;
        final float f2 = maxV - minV;
        final double d0 = MathHelper.abs(f) * (texWidth / 16.0f);
        final double d2 = MathHelper.abs(f2) * (texHeight / 16.0f);
        final WorldRenderer worldrenderer = tess.getWorldRenderer();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, d2, 0.0).tex(minU, maxV).endVertex();
        worldrenderer.pos(d0, d2, 0.0).tex(maxU, maxV).endVertex();
        worldrenderer.pos(d0, 0.0, 0.0).tex(maxU, minV).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(minU, minV).endVertex();
        tess.draw();
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, 0.0, width).tex(minU, minV).endVertex();
        worldrenderer.pos(d0, 0.0, width).tex(maxU, minV).endVertex();
        worldrenderer.pos(d0, d2, width).tex(maxU, maxV).endVertex();
        worldrenderer.pos(0.0, d2, width).tex(minU, maxV).endVertex();
        tess.draw();
        final float f3 = 0.5f * f / sizeX;
        final float f4 = 0.5f * f2 / sizeY;
        GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int i = 0; i < sizeX; ++i) {
            final float f5 = i / (float)sizeX;
            final float f6 = minU + f * f5 + f3;
            worldrenderer.pos(f5 * d0, d2, width).tex(f6, maxV).endVertex();
            worldrenderer.pos(f5 * d0, d2, 0.0).tex(f6, maxV).endVertex();
            worldrenderer.pos(f5 * d0, 0.0, 0.0).tex(f6, minV).endVertex();
            worldrenderer.pos(f5 * d0, 0.0, width).tex(f6, minV).endVertex();
        }
        tess.draw();
        GL11.glNormal3f(1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int j = 0; j < sizeX; ++j) {
            final float f7 = j / (float)sizeX;
            final float f8 = minU + f * f7 + f3;
            final float f9 = f7 + 1.0f / sizeX;
            worldrenderer.pos(f9 * d0, 0.0, width).tex(f8, minV).endVertex();
            worldrenderer.pos(f9 * d0, 0.0, 0.0).tex(f8, minV).endVertex();
            worldrenderer.pos(f9 * d0, d2, 0.0).tex(f8, maxV).endVertex();
            worldrenderer.pos(f9 * d0, d2, width).tex(f8, maxV).endVertex();
        }
        tess.draw();
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int k = 0; k < sizeY; ++k) {
            final float f10 = k / (float)sizeY;
            final float f11 = minV + f2 * f10 + f4;
            final float f12 = f10 + 1.0f / sizeY;
            worldrenderer.pos(0.0, f12 * d2, width).tex(minU, f11).endVertex();
            worldrenderer.pos(d0, f12 * d2, width).tex(maxU, f11).endVertex();
            worldrenderer.pos(d0, f12 * d2, 0.0).tex(maxU, f11).endVertex();
            worldrenderer.pos(0.0, f12 * d2, 0.0).tex(minU, f11).endVertex();
        }
        tess.draw();
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int l = 0; l < sizeY; ++l) {
            final float f13 = l / (float)sizeY;
            final float f14 = minV + f2 * f13 + f4;
            worldrenderer.pos(d0, f13 * d2, width).tex(maxU, f14).endVertex();
            worldrenderer.pos(0.0, f13 * d2, width).tex(minU, f14).endVertex();
            worldrenderer.pos(0.0, f13 * d2, 0.0).tex(minU, f14).endVertex();
            worldrenderer.pos(d0, f13 * d2, 0.0).tex(maxU, f14).endVertex();
        }
        tess.draw();
    }
}
