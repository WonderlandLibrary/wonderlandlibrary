/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import optifine.Config;
import shadersmod.client.Shaders;

public abstract class RenderLiving
extends RendererLivingEntity {
    private static final String __OBFID = "CL_00001015";

    public RenderLiving(RenderManager p_i46153_1_, ModelBase p_i46153_2_, float p_i46153_3_) {
        super(p_i46153_1_, p_i46153_2_, p_i46153_3_);
    }

    protected boolean canRenderName(EntityLiving targetEntity) {
        return super.canRenderName(targetEntity) && (targetEntity.getAlwaysRenderNameTagForRender() || targetEntity.hasCustomName() && targetEntity == this.renderManager.field_147941_i);
    }

    public boolean func_177104_a(EntityLiving p_177104_1_, ICamera p_177104_2_, double p_177104_3_, double p_177104_5_, double p_177104_7_) {
        if (super.func_177071_a(p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_)) {
            return true;
        }
        if (p_177104_1_.getLeashed() && p_177104_1_.getLeashedToEntity() != null) {
            Entity var9 = p_177104_1_.getLeashedToEntity();
            return p_177104_2_.isBoundingBoxInFrustum(var9.getEntityBoundingBox());
        }
        return false;
    }

    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        this.func_110827_b(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void func_177105_a(EntityLiving p_177105_1_, float p_177105_2_) {
        int var3 = p_177105_1_.getBrightnessForRender(p_177105_2_);
        int var4 = var3 % 65536;
        int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var4 / 1.0f, (float)var5 / 1.0f);
    }

    private double func_110828_a(double start, double end, double pct) {
        return start + (end - start) * pct;
    }

    protected void func_110827_b(EntityLiving p_110827_1_, double p_110827_2_, double p_110827_4_, double p_110827_6_, float p_110827_8_, float p_110827_9_) {
        Entity var10;
        if (!(Config.isShaders() && Shaders.isShadowPass || (var10 = p_110827_1_.getLeashedToEntity()) == null)) {
            float var49;
            int var48;
            p_110827_4_ -= (1.6 - (double)p_110827_1_.height) * 0.5;
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();
            double var13 = this.func_110828_a(var10.prevRotationYaw, var10.rotationYaw, p_110827_9_ * 0.5f) * 0.01745329238474369;
            double var15 = this.func_110828_a(var10.prevRotationPitch, var10.rotationPitch, p_110827_9_ * 0.5f) * 0.01745329238474369;
            double var17 = Math.cos(var13);
            double var19 = Math.sin(var13);
            double var21 = Math.sin(var15);
            if (var10 instanceof EntityHanging) {
                var17 = 0.0;
                var19 = 0.0;
                var21 = -1.0;
            }
            double var23 = Math.cos(var15);
            double var25 = this.func_110828_a(var10.prevPosX, var10.posX, p_110827_9_) - var17 * 0.7 - var19 * 0.5 * var23;
            double var27 = this.func_110828_a(var10.prevPosY + (double)var10.getEyeHeight() * 0.7, var10.posY + (double)var10.getEyeHeight() * 0.7, p_110827_9_) - var21 * 0.5 - 0.25;
            double var29 = this.func_110828_a(var10.prevPosZ, var10.posZ, p_110827_9_) - var19 * 0.7 + var17 * 0.5 * var23;
            double var31 = this.func_110828_a(p_110827_1_.prevRenderYawOffset, p_110827_1_.renderYawOffset, p_110827_9_) * 0.01745329238474369 + 1.5707963267948966;
            var17 = Math.cos(var31) * (double)p_110827_1_.width * 0.4;
            var19 = Math.sin(var31) * (double)p_110827_1_.width * 0.4;
            double var33 = this.func_110828_a(p_110827_1_.prevPosX, p_110827_1_.posX, p_110827_9_) + var17;
            double var35 = this.func_110828_a(p_110827_1_.prevPosY, p_110827_1_.posY, p_110827_9_);
            double var37 = this.func_110828_a(p_110827_1_.prevPosZ, p_110827_1_.posZ, p_110827_9_) + var19;
            p_110827_2_ += var17;
            p_110827_6_ += var19;
            double var39 = (float)(var25 - var33);
            double var41 = (float)(var27 - var35);
            double var43 = (float)(var29 - var37);
            GlStateManager.func_179090_x();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            if (Config.isShaders()) {
                Shaders.beginLeash();
            }
            boolean var45 = true;
            double var46 = 0.025;
            var12.startDrawing(5);
            for (var48 = 0; var48 <= 24; ++var48) {
                if (var48 % 2 == 0) {
                    var12.func_178960_a(0.5f, 0.4f, 0.3f, 1.0f);
                } else {
                    var12.func_178960_a(0.35f, 0.28f, 0.21000001f, 1.0f);
                }
                var49 = (float)var48 / 24.0f;
                var12.addVertex(p_110827_2_ + var39 * (double)var49 + 0.0, p_110827_4_ + var41 * (double)(var49 * var49 + var49) * 0.5 + (double)((24.0f - (float)var48) / 18.0f + 0.125f), p_110827_6_ + var43 * (double)var49);
                var12.addVertex(p_110827_2_ + var39 * (double)var49 + 0.025, p_110827_4_ + var41 * (double)(var49 * var49 + var49) * 0.5 + (double)((24.0f - (float)var48) / 18.0f + 0.125f) + 0.025, p_110827_6_ + var43 * (double)var49);
            }
            var11.draw();
            var12.startDrawing(5);
            for (var48 = 0; var48 <= 24; ++var48) {
                if (var48 % 2 == 0) {
                    var12.func_178960_a(0.5f, 0.4f, 0.3f, 1.0f);
                } else {
                    var12.func_178960_a(0.35f, 0.28f, 0.21000001f, 1.0f);
                }
                var49 = (float)var48 / 24.0f;
                var12.addVertex(p_110827_2_ + var39 * (double)var49 + 0.0, p_110827_4_ + var41 * (double)(var49 * var49 + var49) * 0.5 + (double)((24.0f - (float)var48) / 18.0f + 0.125f) + 0.025, p_110827_6_ + var43 * (double)var49);
                var12.addVertex(p_110827_2_ + var39 * (double)var49 + 0.025, p_110827_4_ + var41 * (double)(var49 * var49 + var49) * 0.5 + (double)((24.0f - (float)var48) / 18.0f + 0.125f), p_110827_6_ + var43 * (double)var49 + 0.025);
            }
            var11.draw();
            if (Config.isShaders()) {
                Shaders.endLeash();
            }
            GlStateManager.enableLighting();
            GlStateManager.func_179098_w();
            GlStateManager.enableCull();
        }
    }

    @Override
    protected boolean canRenderName(EntityLivingBase targetEntity) {
        return this.canRenderName((EntityLiving)targetEntity);
    }

    @Override
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    protected boolean func_177070_b(Entity p_177070_1_) {
        return this.canRenderName((EntityLiving)p_177070_1_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    public boolean func_177071_a(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_) {
        return this.func_177104_a((EntityLiving)p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }
}

