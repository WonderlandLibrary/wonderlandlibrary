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

    private void setLightmap(EntityPainting entityPainting, float f, float f2) {
        int n = MathHelper.floor_double(entityPainting.posX);
        int n2 = MathHelper.floor_double(entityPainting.posY + (double)(f2 / 16.0f));
        int n3 = MathHelper.floor_double(entityPainting.posZ);
        EnumFacing enumFacing = entityPainting.facingDirection;
        if (enumFacing == EnumFacing.NORTH) {
            n = MathHelper.floor_double(entityPainting.posX + (double)(f / 16.0f));
        }
        if (enumFacing == EnumFacing.WEST) {
            n3 = MathHelper.floor_double(entityPainting.posZ - (double)(f / 16.0f));
        }
        if (enumFacing == EnumFacing.SOUTH) {
            n = MathHelper.floor_double(entityPainting.posX - (double)(f / 16.0f));
        }
        if (enumFacing == EnumFacing.EAST) {
            n3 = MathHelper.floor_double(entityPainting.posZ + (double)(f / 16.0f));
        }
        int n4 = this.renderManager.worldObj.getCombinedLight(new BlockPos(n, n2, n3), 0);
        int n5 = n4 % 65536;
        int n6 = n4 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, n5, n6);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    public RenderPainting(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPainting entityPainting) {
        return KRISTOFFER_PAINTING_TEXTURE;
    }

    @Override
    public void doRender(EntityPainting entityPainting, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(d, d2, d3);
        GlStateManager.rotate(180.0f - f, 0.0f, 1.0f, 0.0f);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entityPainting);
        EntityPainting.EnumArt enumArt = entityPainting.art;
        float f3 = 0.0625f;
        GlStateManager.scale(f3, f3, f3);
        this.renderPainting(entityPainting, enumArt.sizeX, enumArt.sizeY, enumArt.offsetX, enumArt.offsetY);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entityPainting, d, d2, d3, f, f2);
    }

    private void renderPainting(EntityPainting entityPainting, int n, int n2, int n3, int n4) {
        float f = (float)(-n) / 2.0f;
        float f2 = (float)(-n2) / 2.0f;
        float f3 = 0.5f;
        float f4 = 0.75f;
        float f5 = 0.8125f;
        float f6 = 0.0f;
        float f7 = 0.0625f;
        float f8 = 0.75f;
        float f9 = 0.8125f;
        float f10 = 0.001953125f;
        float f11 = 0.001953125f;
        float f12 = 0.7519531f;
        float f13 = 0.7519531f;
        float f14 = 0.0f;
        float f15 = 0.0625f;
        int n5 = 0;
        while (n5 < n / 16) {
            int n6 = 0;
            while (n6 < n2 / 16) {
                float f16 = f + (float)((n5 + 1) * 16);
                float f17 = f + (float)(n5 * 16);
                float f18 = f2 + (float)((n6 + 1) * 16);
                float f19 = f2 + (float)(n6 * 16);
                this.setLightmap(entityPainting, (f16 + f17) / 2.0f, (f18 + f19) / 2.0f);
                float f20 = (float)(n3 + n - n5 * 16) / 256.0f;
                float f21 = (float)(n3 + n - (n5 + 1) * 16) / 256.0f;
                float f22 = (float)(n4 + n2 - n6 * 16) / 256.0f;
                float f23 = (float)(n4 + n2 - (n6 + 1) * 16) / 256.0f;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                worldRenderer.pos(f16, f19, -f3).tex(f21, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldRenderer.pos(f17, f19, -f3).tex(f20, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldRenderer.pos(f17, f18, -f3).tex(f20, f23).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldRenderer.pos(f16, f18, -f3).tex(f21, f23).normal(0.0f, 0.0f, -1.0f).endVertex();
                worldRenderer.pos(f16, f18, f3).tex(f4, f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldRenderer.pos(f17, f18, f3).tex(f5, f6).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldRenderer.pos(f17, f19, f3).tex(f5, f7).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldRenderer.pos(f16, f19, f3).tex(f4, f7).normal(0.0f, 0.0f, 1.0f).endVertex();
                worldRenderer.pos(f16, f18, -f3).tex(f8, f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f18, -f3).tex(f9, f10).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f18, f3).tex(f9, f11).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldRenderer.pos(f16, f18, f3).tex(f8, f11).normal(0.0f, 1.0f, 0.0f).endVertex();
                worldRenderer.pos(f16, f19, f3).tex(f8, f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f19, f3).tex(f9, f10).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f19, -f3).tex(f9, f11).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldRenderer.pos(f16, f19, -f3).tex(f8, f11).normal(0.0f, -1.0f, 0.0f).endVertex();
                worldRenderer.pos(f16, f18, f3).tex(f13, f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldRenderer.pos(f16, f19, f3).tex(f13, f15).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldRenderer.pos(f16, f19, -f3).tex(f12, f15).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldRenderer.pos(f16, f18, -f3).tex(f12, f14).normal(-1.0f, 0.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f18, -f3).tex(f13, f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f19, -f3).tex(f13, f15).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f19, f3).tex(f12, f15).normal(1.0f, 0.0f, 0.0f).endVertex();
                worldRenderer.pos(f17, f18, f3).tex(f12, f14).normal(1.0f, 0.0f, 0.0f).endVertex();
                tessellator.draw();
                ++n6;
            }
            ++n5;
        }
    }
}

