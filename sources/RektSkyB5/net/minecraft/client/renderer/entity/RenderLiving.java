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
    public RenderLiving(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Override
    protected boolean canRenderName(T entity) {
        return super.canRenderName(entity) && (((EntityLivingBase)entity).getAlwaysRenderNameTagForRender() || ((Entity)entity).hasCustomName() && entity == this.renderManager.pointedEntity);
    }

    @Override
    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (((EntityLiving)livingEntity).getLeashed() && ((EntityLiving)livingEntity).getLeashedToEntity() != null) {
            Entity entity = ((EntityLiving)livingEntity).getLeashedToEntity();
            return camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
        }
        return false;
    }

    @Override
    public void doRender(T entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
        this.renderLeash(entity, x2, y2, z2, entityYaw, partialTicks);
    }

    public void func_177105_a(T entityLivingIn, float partialTicks) {
        int i2 = ((Entity)entityLivingIn).getBrightnessForRender(partialTicks);
        int j2 = i2 % 65536;
        int k2 = i2 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j2 / 1.0f, (float)k2 / 1.0f);
    }

    private double interpolateValue(double start, double end, double pct) {
        return start + (end - start) * pct;
    }

    protected void renderLeash(T entityLivingIn, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        Entity entity = ((EntityLiving)entityLivingIn).getLeashedToEntity();
        if (entity != null) {
            y2 -= (1.6 - (double)((EntityLiving)entityLivingIn).height) * 0.5;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            double d0 = this.interpolateValue(entity.prevRotationYaw, entity.rotationYaw, partialTicks * 0.5f) * 0.01745329238474369;
            double d1 = this.interpolateValue(entity.prevRotationPitch, entity.rotationPitch, partialTicks * 0.5f) * 0.01745329238474369;
            double d2 = Math.cos(d0);
            double d3 = Math.sin(d0);
            double d4 = Math.sin(d1);
            if (entity instanceof EntityHanging) {
                d2 = 0.0;
                d3 = 0.0;
                d4 = -1.0;
            }
            double d5 = Math.cos(d1);
            double d6 = this.interpolateValue(entity.prevPosX, entity.posX, partialTicks) - d2 * 0.7 - d3 * 0.5 * d5;
            double d7 = this.interpolateValue(entity.prevPosY + (double)entity.getEyeHeight() * 0.7, entity.posY + (double)entity.getEyeHeight() * 0.7, partialTicks) - d4 * 0.5 - 0.25;
            double d8 = this.interpolateValue(entity.prevPosZ, entity.posZ, partialTicks) - d3 * 0.7 + d2 * 0.5 * d5;
            double d9 = this.interpolateValue(((EntityLiving)entityLivingIn).prevRenderYawOffset, ((EntityLiving)entityLivingIn).renderYawOffset, partialTicks) * 0.01745329238474369 + 1.5707963267948966;
            d2 = Math.cos(d9) * (double)((EntityLiving)entityLivingIn).width * 0.4;
            d3 = Math.sin(d9) * (double)((EntityLiving)entityLivingIn).width * 0.4;
            double d10 = this.interpolateValue(((EntityLiving)entityLivingIn).prevPosX, ((EntityLiving)entityLivingIn).posX, partialTicks) + d2;
            double d11 = this.interpolateValue(((EntityLiving)entityLivingIn).prevPosY, ((EntityLiving)entityLivingIn).posY, partialTicks);
            double d12 = this.interpolateValue(((EntityLiving)entityLivingIn).prevPosZ, ((EntityLiving)entityLivingIn).posZ, partialTicks) + d3;
            x2 += d2;
            z2 += d3;
            double d13 = (float)(d6 - d10);
            double d14 = (float)(d7 - d11);
            double d15 = (float)(d8 - d12);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            int i2 = 24;
            double d16 = 0.025;
            worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
            for (int j2 = 0; j2 <= 24; ++j2) {
                float f2 = 0.5f;
                float f1 = 0.4f;
                float f22 = 0.3f;
                if (j2 % 2 == 0) {
                    f2 *= 0.7f;
                    f1 *= 0.7f;
                    f22 *= 0.7f;
                }
                float f3 = (float)j2 / 24.0f;
                worldrenderer.pos(x2 + d13 * (double)f3 + 0.0, y2 + d14 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)j2) / 18.0f + 0.125f), z2 + d15 * (double)f3).color(f2, f1, f22, 1.0f).endVertex();
                worldrenderer.pos(x2 + d13 * (double)f3 + 0.025, y2 + d14 * (double)(f3 * f3 + f3) * 0.5 + (double)((24.0f - (float)j2) / 18.0f + 0.125f) + 0.025, z2 + d15 * (double)f3).color(f2, f1, f22, 1.0f).endVertex();
            }
            tessellator.draw();
            worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
            for (int k2 = 0; k2 <= 24; ++k2) {
                float f4 = 0.5f;
                float f5 = 0.4f;
                float f6 = 0.3f;
                if (k2 % 2 == 0) {
                    f4 *= 0.7f;
                    f5 *= 0.7f;
                    f6 *= 0.7f;
                }
                float f7 = (float)k2 / 24.0f;
                worldrenderer.pos(x2 + d13 * (double)f7 + 0.0, y2 + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0f - (float)k2) / 18.0f + 0.125f) + 0.025, z2 + d15 * (double)f7).color(f4, f5, f6, 1.0f).endVertex();
                worldrenderer.pos(x2 + d13 * (double)f7 + 0.025, y2 + d14 * (double)(f7 * f7 + f7) * 0.5 + (double)((24.0f - (float)k2) / 18.0f + 0.125f), z2 + d15 * (double)f7 + 0.025).color(f4, f5, f6, 1.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }
}

