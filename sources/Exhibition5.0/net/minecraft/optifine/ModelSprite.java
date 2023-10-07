// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.renderer.WorldRenderer;
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
        float rMinU = this.minU;
        float rMaxU = this.maxU;
        float rMinV = this.minV;
        float rMaxV = this.maxV;
        if (this.modelRenderer.mirror) {
            rMinU = this.maxU;
            rMaxU = this.minU;
        }
        if (this.modelRenderer.mirrorV) {
            rMinV = this.maxV;
            rMaxV = this.minV;
        }
        renderItemIn2D(tessellator, rMinU, rMinV, rMaxU, rMaxV, this.sizeX, this.sizeY, scale * this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
        GlStateManager.translate(-this.posX * scale, -this.posY * scale, -this.posZ * scale);
    }
    
    public static void renderItemIn2D(final Tessellator tess, final float minU, final float minV, final float maxU, final float maxV, final int sizeX, final int sizeY, float width, final float texWidth, final float texHeight) {
        if (width < 6.25E-4f) {
            width = 6.25E-4f;
        }
        final float dU = maxU - minU;
        final float dV = maxV - minV;
        final double dimX = MathHelper.abs(dU) * (texWidth / 16.0f);
        final double dimY = MathHelper.abs(dV) * (texHeight / 16.0f);
        final WorldRenderer tessellator = tess.getWorldRenderer();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(0.0f, 0.0f, -1.0f);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, minU, minV);
        tessellator.addVertexWithUV(dimX, 0.0, 0.0, maxU, minV);
        tessellator.addVertexWithUV(dimX, dimY, 0.0, maxU, maxV);
        tessellator.addVertexWithUV(0.0, dimY, 0.0, minU, maxV);
        tess.draw();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(0.0f, 0.0f, 1.0f);
        tessellator.addVertexWithUV(0.0, dimY, width, minU, maxV);
        tessellator.addVertexWithUV(dimX, dimY, width, maxU, maxV);
        tessellator.addVertexWithUV(dimX, 0.0, width, maxU, minV);
        tessellator.addVertexWithUV(0.0, 0.0, width, minU, minV);
        tess.draw();
        final float var8 = 0.5f * dU / sizeX;
        final float var9 = 0.5f * dV / sizeY;
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(-1.0f, 0.0f, 0.0f);
        for (int var10 = 0; var10 < sizeX; ++var10) {
            final float var11 = var10 / sizeX;
            final float var12 = minU + dU * var11 + var8;
            tessellator.addVertexWithUV(var11 * dimX, 0.0, width, var12, minV);
            tessellator.addVertexWithUV(var11 * dimX, 0.0, 0.0, var12, minV);
            tessellator.addVertexWithUV(var11 * dimX, dimY, 0.0, var12, maxV);
            tessellator.addVertexWithUV(var11 * dimX, dimY, width, var12, maxV);
        }
        tess.draw();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(1.0f, 0.0f, 0.0f);
        for (int var10 = 0; var10 < sizeX; ++var10) {
            final float var11 = var10 / sizeX;
            final float var12 = minU + dU * var11 + var8;
            final float var13 = var11 + 1.0f / sizeX;
            tessellator.addVertexWithUV(var13 * dimX, dimY, width, var12, maxV);
            tessellator.addVertexWithUV(var13 * dimX, dimY, 0.0, var12, maxV);
            tessellator.addVertexWithUV(var13 * dimX, 0.0, 0.0, var12, minV);
            tessellator.addVertexWithUV(var13 * dimX, 0.0, width, var12, minV);
        }
        tess.draw();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(0.0f, 1.0f, 0.0f);
        for (int var10 = 0; var10 < sizeY; ++var10) {
            final float var11 = var10 / sizeY;
            final float var12 = minV + dV * var11 + var9;
            final float var13 = var11 + 1.0f / sizeY;
            tessellator.addVertexWithUV(0.0, var13 * dimY, 0.0, minU, var12);
            tessellator.addVertexWithUV(dimX, var13 * dimY, 0.0, maxU, var12);
            tessellator.addVertexWithUV(dimX, var13 * dimY, width, maxU, var12);
            tessellator.addVertexWithUV(0.0, var13 * dimY, width, minU, var12);
        }
        tess.draw();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(0.0f, -1.0f, 0.0f);
        for (int var10 = 0; var10 < sizeY; ++var10) {
            final float var11 = var10 / sizeY;
            final float var12 = minV + dV * var11 + var9;
            tessellator.addVertexWithUV(dimX, var11 * dimY, 0.0, maxU, var12);
            tessellator.addVertexWithUV(0.0, var11 * dimY, 0.0, minU, var12);
            tessellator.addVertexWithUV(0.0, var11 * dimY, width, minU, var12);
            tessellator.addVertexWithUV(dimX, var11 * dimY, width, maxU, var12);
        }
        tess.draw();
    }
}
