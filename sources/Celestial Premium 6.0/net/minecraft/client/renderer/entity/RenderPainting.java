/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RenderPainting
extends Render<EntityPainting> {
    private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");

    public RenderPainting(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityPainting entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);
        EntityPainting.EnumArt entitypainting$enumart = entity.art;
        float f = 0.0625f;
        GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.renderPainting(entity, entitypainting$enumart.sizeX, entitypainting$enumart.sizeY, entitypainting$enumart.offsetX, entitypainting$enumart.offsetY);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPainting entity) {
        return KRISTOFFER_PAINTING_TEXTURE;
    }

    private void renderPainting(EntityPainting painting, int width, int height, int textureU, int textureV) {
        float f = (float)(-width) / 2.0f;
        float f1 = (float)(-height) / 2.0f;
        float f2 = 0.5f;
        float f3 = 0.75f;
        float f4 = 0.8125f;
        float f5 = 0.0f;
        float f6 = 0.0625f;
        float f7 = 0.75f;
        float f8 = 0.8125f;
        float f9 = 0.001953125f;
        float f10 = 0.001953125f;
        float f11 = 0.7519531f;
        float f12 = 0.7519531f;
        float f13 = 0.0f;
        float f14 = 0.0625f;
        for (int i = 0; i < width / 16; ++i) {
            for (int j = 0; j < height / 16; ++j) {
                float f15 = f + (float)((i + 1) * 16);
                float f16 = f + (float)(i * 16);
                float f17 = f1 + (float)((j + 1) * 16);
                float f18 = f1 + (float)(j * 16);
                this.setLightmap(painting, (f15 + f16) / 2.0f, (f17 + f18) / 2.0f);
                float f19 = (float)(textureU + width - i * 16) / 256.0f;
                float f20 = (float)(textureU + width - (i + 1) * 16) / 256.0f;
                float f21 = (float)(textureV + height - j * 16) / 256.0f;
                float f22 = (float)(textureV + height - (j + 1) * 16) / 256.0f;
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                bufferbuilder.pos(f15, f18, -0.5).tex(f20, f21).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f16, f18, -0.5).tex(f19, f21).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f16, f17, -0.5).tex(f19, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f15, f17, -0.5).tex(f20, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f15, f17, 0.5).tex(0.75, 0.0).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f16, f17, 0.5).tex(0.8125, 0.0).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f16, f18, 0.5).tex(0.8125, 0.0625).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f15, f18, 0.5).tex(0.75, 0.0625).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f15, f17, -0.5).tex(0.75, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f17, -0.5).tex(0.8125, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f17, 0.5).tex(0.8125, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f15, f17, 0.5).tex(0.75, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f15, f18, 0.5).tex(0.75, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f18, 0.5).tex(0.8125, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f18, -0.5).tex(0.8125, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f15, f18, -0.5).tex(0.75, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f15, f17, 0.5).tex(0.751953125, 0.0).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f15, f18, 0.5).tex(0.751953125, 0.0625).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f15, f18, -0.5).tex(0.751953125, 0.0625).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f15, f17, -0.5).tex(0.751953125, 0.0).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f17, -0.5).tex(0.751953125, 0.0).normal(1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f18, -0.5).tex(0.751953125, 0.0625).normal(1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f18, 0.5).tex(0.751953125, 0.0625).normal(1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f17, 0.5).tex(0.751953125, 0.0).normal(1.0f, 0.0f, 0.0f).endVertex();
                tessellator.draw();
            }
        }
    }

    private void setLightmap(EntityPainting painting, float p_77008_2_, float p_77008_3_) {
        int i = MathHelper.floor(painting.posX);
        int j = MathHelper.floor(painting.posY + (double)(p_77008_3_ / 16.0f));
        int k = MathHelper.floor(painting.posZ);
        EnumFacing enumfacing = painting.facingDirection;
        if (enumfacing == EnumFacing.NORTH) {
            i = MathHelper.floor(painting.posX + (double)(p_77008_2_ / 16.0f));
        }
        if (enumfacing == EnumFacing.WEST) {
            k = MathHelper.floor(painting.posZ - (double)(p_77008_2_ / 16.0f));
        }
        if (enumfacing == EnumFacing.SOUTH) {
            i = MathHelper.floor(painting.posX - (double)(p_77008_2_ / 16.0f));
        }
        if (enumfacing == EnumFacing.EAST) {
            k = MathHelper.floor(painting.posZ + (double)(p_77008_2_ / 16.0f));
        }
        int l = this.renderManager.worldObj.getCombinedLight(new BlockPos(i, j, k), 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
}

