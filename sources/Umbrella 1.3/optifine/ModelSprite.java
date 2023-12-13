/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;

public class ModelSprite {
    private ModelRenderer modelRenderer = null;
    private int textureOffsetX = 0;
    private int textureOffsetY = 0;
    private float posX = 0.0f;
    private float posY = 0.0f;
    private float posZ = 0.0f;
    private int sizeX = 0;
    private int sizeY = 0;
    private int sizeZ = 0;
    private float sizeAdd = 0.0f;
    private float minU = 0.0f;
    private float minV = 0.0f;
    private float maxU = 0.0f;
    private float maxV = 0.0f;

    public ModelSprite(ModelRenderer modelRenderer, int textureOffsetX, int textureOffsetY, float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd) {
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
        this.minU = (float)textureOffsetX / modelRenderer.textureWidth;
        this.minV = (float)textureOffsetY / modelRenderer.textureHeight;
        this.maxU = (float)(textureOffsetX + sizeX) / modelRenderer.textureWidth;
        this.maxV = (float)(textureOffsetY + sizeY) / modelRenderer.textureHeight;
    }

    public void render(Tessellator tessellator, float scale) {
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
        ModelSprite.renderItemIn2D(tessellator, rMinU, rMinV, rMaxU, rMaxV, this.sizeX, this.sizeY, scale * (float)this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
        GlStateManager.translate(-this.posX * scale, -this.posY * scale, -this.posZ * scale);
    }

    public static void renderItemIn2D(Tessellator tess, float minU, float minV, float maxU, float maxV, int sizeX, int sizeY, float width, float texWidth, float texHeight) {
        float var13;
        float var12;
        float var11;
        int var10;
        if (width < 6.25E-4f) {
            width = 6.25E-4f;
        }
        float dU = maxU - minU;
        float dV = maxV - minV;
        double dimX = MathHelper.abs(dU) * (texWidth / 16.0f);
        double dimY = MathHelper.abs(dV) * (texHeight / 16.0f);
        WorldRenderer tessellator = tess.getWorldRenderer();
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
        float var8 = 0.5f * dU / (float)sizeX;
        float var9 = 0.5f * dV / (float)sizeY;
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(-1.0f, 0.0f, 0.0f);
        for (var10 = 0; var10 < sizeX; ++var10) {
            var11 = (float)var10 / (float)sizeX;
            var12 = minU + dU * var11 + var8;
            tessellator.addVertexWithUV((double)var11 * dimX, 0.0, width, var12, minV);
            tessellator.addVertexWithUV((double)var11 * dimX, 0.0, 0.0, var12, minV);
            tessellator.addVertexWithUV((double)var11 * dimX, dimY, 0.0, var12, maxV);
            tessellator.addVertexWithUV((double)var11 * dimX, dimY, width, var12, maxV);
        }
        tess.draw();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(1.0f, 0.0f, 0.0f);
        for (var10 = 0; var10 < sizeX; ++var10) {
            var11 = (float)var10 / (float)sizeX;
            var12 = minU + dU * var11 + var8;
            var13 = var11 + 1.0f / (float)sizeX;
            tessellator.addVertexWithUV((double)var13 * dimX, dimY, width, var12, maxV);
            tessellator.addVertexWithUV((double)var13 * dimX, dimY, 0.0, var12, maxV);
            tessellator.addVertexWithUV((double)var13 * dimX, 0.0, 0.0, var12, minV);
            tessellator.addVertexWithUV((double)var13 * dimX, 0.0, width, var12, minV);
        }
        tess.draw();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(0.0f, 1.0f, 0.0f);
        for (var10 = 0; var10 < sizeY; ++var10) {
            var11 = (float)var10 / (float)sizeY;
            var12 = minV + dV * var11 + var9;
            var13 = var11 + 1.0f / (float)sizeY;
            tessellator.addVertexWithUV(0.0, (double)var13 * dimY, 0.0, minU, var12);
            tessellator.addVertexWithUV(dimX, (double)var13 * dimY, 0.0, maxU, var12);
            tessellator.addVertexWithUV(dimX, (double)var13 * dimY, width, maxU, var12);
            tessellator.addVertexWithUV(0.0, (double)var13 * dimY, width, minU, var12);
        }
        tess.draw();
        tessellator.startDrawingQuads();
        tessellator.func_178980_d(0.0f, -1.0f, 0.0f);
        for (var10 = 0; var10 < sizeY; ++var10) {
            var11 = (float)var10 / (float)sizeY;
            var12 = minV + dV * var11 + var9;
            tessellator.addVertexWithUV(dimX, (double)var11 * dimY, 0.0, maxU, var12);
            tessellator.addVertexWithUV(0.0, (double)var11 * dimY, 0.0, minU, var12);
            tessellator.addVertexWithUV(0.0, (double)var11 * dimY, width, minU, var12);
            tessellator.addVertexWithUV(dimX, (double)var11 * dimY, width, maxU, var12);
        }
        tess.draw();
    }
}

