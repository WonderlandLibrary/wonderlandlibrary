/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderPainting
extends Render<EntityPainting> {
    private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");

    public RenderPainting(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityPainting entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x2, y2, z2);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);
        EntityPainting.EnumArt entitypainting$enumart = entity.art;
        float f2 = 0.0625f;
        GlStateManager.scale(f2, f2, f2);
        this.renderPainting(entity, entitypainting$enumart.sizeX, entitypainting$enumart.sizeY, entitypainting$enumart.offsetX, entitypainting$enumart.offsetY);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPainting entity) {
        return KRISTOFFER_PAINTING_TEXTURE;
    }

    private void renderPainting(EntityPainting painting, int width, int height, int textureU, int textureV) {
        float f2 = (float)(-width) / 2.0f;
        float f1 = (float)(-height) / 2.0f;
        float f22 = 0.5f;
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
        for (int i2 = 0; i2 < width / 16; ++i2) {
            for (int j2 = 0; j2 < height / 16; ++j2) {
                float f15 = f2 + (float)((i2 + 1) * 16);
                float f16 = f2 + (float)(i2 * 16);
                float f17 = f1 + (float)((j2 + 1) * 16);
                float f18 = f1 + (float)(j2 * 16);
                this.setLightmap(painting, (f15 + f16) / 2.0f, (f17 + f18) / 2.0f);
                float f19 = (float)(textureU + width - i2 * 16) / 256.0f;
                float f20 = (float)(textureU + width - (i2 + 1) * 16) / 256.0f;
                float f21 = (float)(textureV + height - j2 * 16) / 256.0f;
                float f222 = (float)(textureV + height - (j2 + 1) * 16) / 256.0f;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                worldrenderer.pos(f15, f18, -f22).tex(f20, f21).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f16, f18, -f22).tex(f19, f21).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f16, f17, -f22).tex(f19, f222).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f15, f17, -f22).tex(f20, f222).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos(f15, f17, f22).tex(f3, f5).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f16, f17, f22).tex(f4, f5).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f16, f18, f22).tex(f4, f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f15, f18, f22).tex(f3, f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos(f15, f17, -f22).tex(f7, f9).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f17, -f22).tex(f8, f9).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f17, f22).tex(f8, f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f15, f17, f22).tex(f7, f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos(f15, f18, f22).tex(f7, f9).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f18, f22).tex(f8, f9).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f18, -f22).tex(f8, f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f15, f18, -f22).tex(f7, f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos(f15, f17, f22).tex(f12, f13).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f15, f18, f22).tex(f12, f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f15, f18, -f22).tex(f11, f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f15, f17, -f22).tex(f11, f13).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f17, -f22).tex(f12, f13).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f18, -f22).tex(f12, f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f18, f22).tex(f11, f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos(f16, f17, f22).tex(f11, f13).normal(1.0f, 0.0f, 0.0f).endVertex();
                tessellator.draw();
            }
        }
    }

    private void setLightmap(EntityPainting painting, float p_77008_2_, float p_77008_3_) {
        int i2 = MathHelper.floor_double(painting.posX);
        int j2 = MathHelper.floor_double(painting.posY + (double)(p_77008_3_ / 16.0f));
        int k2 = MathHelper.floor_double(painting.posZ);
        EnumFacing enumfacing = painting.facingDirection;
        if (enumfacing == EnumFacing.NORTH) {
            i2 = MathHelper.floor_double(painting.posX + (double)(p_77008_2_ / 16.0f));
        }
        if (enumfacing == EnumFacing.WEST) {
            k2 = MathHelper.floor_double(painting.posZ - (double)(p_77008_2_ / 16.0f));
        }
        if (enumfacing == EnumFacing.SOUTH) {
            i2 = MathHelper.floor_double(painting.posX - (double)(p_77008_2_ / 16.0f));
        }
        if (enumfacing == EnumFacing.EAST) {
            k2 = MathHelper.floor_double(painting.posZ + (double)(p_77008_2_ / 16.0f));
        }
        int l2 = this.renderManager.worldObj.getCombinedLight(new BlockPos(i2, j2, k2), 0);
        int i1 = l2 % 65536;
        int j1 = l2 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
}

