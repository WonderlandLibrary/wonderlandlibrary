package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
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

    public void doRender(EntityPainting entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)x, (double)y, (double)z);
        GlStateManager.rotate((float)(180.0f - entityYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture((Entity)entity);
        EntityPainting.EnumArt entitypainting$enumart = entity.art;
        float f = 0.0625f;
        GlStateManager.scale((float)f, (float)f, (float)f);
        this.renderPainting(entity, entitypainting$enumart.sizeX, entitypainting$enumart.sizeY, entitypainting$enumart.offsetX, entitypainting$enumart.offsetY);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
    }

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
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                worldrenderer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f20, (double)f21).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f19, (double)f21).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f19, (double)f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f20, (double)f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)f2).tex((double)f3, (double)f5).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)f2).tex((double)f4, (double)f5).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)f2).tex((double)f4, (double)f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)f2).tex((double)f3, (double)f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f7, (double)f9).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f8, (double)f9).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)f2).tex((double)f8, (double)f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)f2).tex((double)f7, (double)f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)f2).tex((double)f7, (double)f9).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)f2).tex((double)f8, (double)f9).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f8, (double)f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f7, (double)f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)f2).tex((double)f12, (double)f13).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)f2).tex((double)f12, (double)f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f11, (double)f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f11, (double)f13).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f12, (double)f13).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f12, (double)f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)f2).tex((double)f11, (double)f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)f2).tex((double)f11, (double)f13).normal(1.0f, 0.0f, 0.0f).endVertex();
                tessellator.draw();
            }
        }
    }

    private void setLightmap(EntityPainting painting, float p_77008_2_, float p_77008_3_) {
        int i = MathHelper.floor_double((double)painting.posX);
        int j = MathHelper.floor_double((double)(painting.posY + (double)(p_77008_3_ / 16.0f)));
        int k = MathHelper.floor_double((double)painting.posZ);
        EnumFacing enumfacing = painting.facingDirection;
        if (enumfacing == EnumFacing.NORTH) {
            i = MathHelper.floor_double((double)(painting.posX + (double)(p_77008_2_ / 16.0f)));
        }
        if (enumfacing == EnumFacing.WEST) {
            k = MathHelper.floor_double((double)(painting.posZ - (double)(p_77008_2_ / 16.0f)));
        }
        if (enumfacing == EnumFacing.SOUTH) {
            i = MathHelper.floor_double((double)(painting.posX - (double)(p_77008_2_ / 16.0f)));
        }
        if (enumfacing == EnumFacing.EAST) {
            k = MathHelper.floor_double((double)(painting.posZ + (double)(p_77008_2_ / 16.0f)));
        }
        int l = this.renderManager.worldObj.getCombinedLight(new BlockPos(i, j, k), 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)i1, (float)j1);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
    }
}
