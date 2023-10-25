/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public abstract class RenderLiving<T extends EntityLiving>
extends RendererLivingEntity<T> {
    @Override
    protected boolean canRenderName(T t) {
        return super.canRenderName(t) && (((EntityLivingBase)t).getAlwaysRenderNameTagForRender() || ((Entity)t).hasCustomName() && t == this.renderManager.pointedEntity);
    }

    public void func_177105_a(T t, float f) {
        int n = ((Entity)t).getBrightnessForRender(f);
        int n2 = n % 65536;
        int n3 = n / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n2 / 1.0f, (float)n3 / 1.0f);
    }

    @Override
    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        super.doRender(t, d, d2, d3, f, f2);
        this.renderLeash(t, d, d2, d3, f, f2);
    }

    protected void renderLeash(T t, double d, double d2, double d3, float f, float f2) {
        Entity entity = ((EntityLiving)t).getLeashedToEntity();
        if (entity != null) {
            float f3;
            float f4;
            float f5;
            float f6;
            d2 -= (1.6 - (double)((EntityLiving)t).height) * 0.5;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            double d4 = this.interpolateValue(entity.prevRotationYaw, entity.rotationYaw, f2 * 0.5f) * 0.01745329238474369;
            double d5 = this.interpolateValue(entity.prevRotationPitch, entity.rotationPitch, f2 * 0.5f) * 0.01745329238474369;
            double d6 = Math.cos(d4);
            double d7 = Math.sin(d4);
            double d8 = Math.sin(d5);
            if (entity instanceof EntityHanging) {
                d6 = 0.0;
                d7 = 0.0;
                d8 = -1.0;
            }
            double d9 = Math.cos(d5);
            double d10 = this.interpolateValue(entity.prevPosX, entity.posX, f2) - d6 * 0.7 - d7 * 0.5 * d9;
            double d11 = this.interpolateValue(entity.prevPosY + (double)entity.getEyeHeight() * 0.7, entity.posY + (double)entity.getEyeHeight() * 0.7, f2) - d8 * 0.5 - 0.25;
            double d12 = this.interpolateValue(entity.prevPosZ, entity.posZ, f2) - d7 * 0.7 + d6 * 0.5 * d9;
            double d13 = this.interpolateValue(((EntityLiving)t).prevRenderYawOffset, ((EntityLiving)t).renderYawOffset, f2) * 0.01745329238474369 + 1.5707963267948966;
            d6 = Math.cos(d13) * (double)((EntityLiving)t).width * 0.4;
            d7 = Math.sin(d13) * (double)((EntityLiving)t).width * 0.4;
            double d14 = this.interpolateValue(((EntityLiving)t).prevPosX, ((EntityLiving)t).posX, f2) + d6;
            double d15 = this.interpolateValue(((EntityLiving)t).prevPosY, ((EntityLiving)t).posY, f2);
            double d16 = this.interpolateValue(((EntityLiving)t).prevPosZ, ((EntityLiving)t).posZ, f2) + d7;
            d += d6;
            d3 += d7;
            double d17 = (float)(d10 - d14);
            double d18 = (float)(d11 - d15);
            double d19 = (float)(d12 - d16);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            int n = 24;
            double d20 = 0.025;
            worldRenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
            int n2 = 0;
            while (n2 <= 24) {
                f6 = 0.5f;
                f5 = 0.4f;
                f4 = 0.3f;
                if (n2 % 2 == 0) {
                    f6 *= 0.7f;
                    f5 *= 0.7f;
                    f4 *= 0.7f;
                }
                f3 = (float)n2 / 24.0f;
                worldRenderer.pos(d + d17 * (double)f3 + 0.0, d2 + d18 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)n2) / 18.0f + 0.125f), d3 + d19 * (double)f3).color(f6, f5, f4, 1.0f).endVertex();
                worldRenderer.pos(d + d17 * (double)f3 + 0.025, d2 + d18 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)n2) / 18.0f + 0.125f) + 0.025, d3 + d19 * (double)f3).color(f6, f5, f4, 1.0f).endVertex();
                ++n2;
            }
            tessellator.draw();
            worldRenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
            n2 = 0;
            while (n2 <= 24) {
                f6 = 0.5f;
                f5 = 0.4f;
                f4 = 0.3f;
                if (n2 % 2 == 0) {
                    f6 *= 0.7f;
                    f5 *= 0.7f;
                    f4 *= 0.7f;
                }
                f3 = (float)n2 / 24.0f;
                worldRenderer.pos(d + d17 * (double)f3 + 0.0, d2 + d18 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)n2) / 18.0f + 0.125f) + 0.025, d3 + d19 * (double)f3).color(f6, f5, f4, 1.0f).endVertex();
                worldRenderer.pos(d + d17 * (double)f3 + 0.025, d2 + d18 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)n2) / 18.0f + 0.125f), d3 + d19 * (double)f3 + 0.025).color(f6, f5, f4, 1.0f).endVertex();
                ++n2;
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }

    @Override
    public boolean shouldRender(T t, ICamera iCamera, double d, double d2, double d3) {
        if (super.shouldRender(t, iCamera, d, d2, d3)) {
            return true;
        }
        if (((EntityLiving)t).getLeashed() && ((EntityLiving)t).getLeashedToEntity() != null) {
            Entity entity = ((EntityLiving)t).getLeashedToEntity();
            return iCamera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
        }
        return false;
    }

    public RenderLiving(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }

    private double interpolateValue(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }
}

