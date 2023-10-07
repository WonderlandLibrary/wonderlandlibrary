// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityHanging;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.model.ModelBase;

public abstract class RenderLiving extends RendererLivingEntity
{
    private static final String __OBFID = "CL_00001015";
    
    public RenderLiving(final RenderManager p_i46153_1_, final ModelBase p_i46153_2_, final float p_i46153_3_) {
        super(p_i46153_1_, p_i46153_2_, p_i46153_3_);
    }
    
    protected boolean canRenderName(final EntityLiving targetEntity) {
        return super.canRenderName(targetEntity) && (targetEntity.getAlwaysRenderNameTagForRender() || (targetEntity.hasCustomName() && targetEntity == this.renderManager.field_147941_i));
    }
    
    public boolean func_177104_a(final EntityLiving p_177104_1_, final ICamera p_177104_2_, final double p_177104_3_, final double p_177104_5_, final double p_177104_7_) {
        if (super.func_177071_a(p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_)) {
            return true;
        }
        if (p_177104_1_.getLeashed() && p_177104_1_.getLeashedToEntity() != null) {
            final Entity var9 = p_177104_1_.getLeashedToEntity();
            return p_177104_2_.isBoundingBoxInFrustum(var9.getEntityBoundingBox());
        }
        return false;
    }
    
    public void doRender(final EntityLiving p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        this.func_110827_b(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    public void func_177105_a(final EntityLiving p_177105_1_, final float p_177105_2_) {
        final int var3 = p_177105_1_.getBrightnessForRender(p_177105_2_);
        final int var4 = var3 % 65536;
        final int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var4 / 1.0f, var5 / 1.0f);
    }
    
    private double func_110828_a(final double p_110828_1_, final double p_110828_3_, final double p_110828_5_) {
        return p_110828_1_ + (p_110828_3_ - p_110828_1_) * p_110828_5_;
    }
    
    protected void func_110827_b(final EntityLiving p_110827_1_, double p_110827_2_, double p_110827_4_, double p_110827_6_, final float p_110827_8_, final float p_110827_9_) {
        final Entity var10 = p_110827_1_.getLeashedToEntity();
        if (var10 != null) {
            p_110827_4_ -= (1.6 - p_110827_1_.height) * 0.5;
            final Tessellator var11 = Tessellator.getInstance();
            final WorldRenderer var12 = var11.getWorldRenderer();
            final double var13 = this.func_110828_a(var10.prevRotationYaw, var10.rotationYaw, p_110827_9_ * 0.5f) * 0.01745329238474369;
            final double var14 = this.func_110828_a(var10.prevRotationPitch, var10.rotationPitch, p_110827_9_ * 0.5f) * 0.01745329238474369;
            double var15 = Math.cos(var13);
            double var16 = Math.sin(var13);
            double var17 = Math.sin(var14);
            if (var10 instanceof EntityHanging) {
                var15 = 0.0;
                var16 = 0.0;
                var17 = -1.0;
            }
            final double var18 = Math.cos(var14);
            final double var19 = this.func_110828_a(var10.prevPosX, var10.posX, p_110827_9_) - var15 * 0.7 - var16 * 0.5 * var18;
            final double var20 = this.func_110828_a(var10.prevPosY + var10.getEyeHeight() * 0.7, var10.posY + var10.getEyeHeight() * 0.7, p_110827_9_) - var17 * 0.5 - 0.25;
            final double var21 = this.func_110828_a(var10.prevPosZ, var10.posZ, p_110827_9_) - var16 * 0.7 + var15 * 0.5 * var18;
            final double var22 = this.func_110828_a(p_110827_1_.prevRenderYawOffset, p_110827_1_.renderYawOffset, p_110827_9_) * 0.01745329238474369 + 1.5707963267948966;
            var15 = Math.cos(var22) * p_110827_1_.width * 0.4;
            var16 = Math.sin(var22) * p_110827_1_.width * 0.4;
            final double var23 = this.func_110828_a(p_110827_1_.prevPosX, p_110827_1_.posX, p_110827_9_) + var15;
            final double var24 = this.func_110828_a(p_110827_1_.prevPosY, p_110827_1_.posY, p_110827_9_);
            final double var25 = this.func_110828_a(p_110827_1_.prevPosZ, p_110827_1_.posZ, p_110827_9_) + var16;
            p_110827_2_ += var15;
            p_110827_6_ += var16;
            final double var26 = (float)(var19 - var23);
            final double var27 = (float)(var20 - var24);
            final double var28 = (float)(var21 - var25);
            GlStateManager.disableTextures();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            final boolean var29 = true;
            final double var30 = 0.025;
            var12.startDrawing(5);
            for (int var31 = 0; var31 <= 24; ++var31) {
                if (var31 % 2 == 0) {
                    var12.setColorRGBA(0.5f, 0.4f, 0.3f, 1.0f);
                }
                else {
                    var12.setColorRGBA(0.35f, 0.28f, 0.21000001f, 1.0f);
                }
                final float var32 = var31 / 24.0f;
                var12.addVertex(p_110827_2_ + var26 * var32 + 0.0, p_110827_4_ + var27 * (var32 * var32 + var32) * 0.5 + ((24.0f - var31) / 18.0f + 0.125f), p_110827_6_ + var28 * var32);
                var12.addVertex(p_110827_2_ + var26 * var32 + 0.025, p_110827_4_ + var27 * (var32 * var32 + var32) * 0.5 + ((24.0f - var31) / 18.0f + 0.125f) + 0.025, p_110827_6_ + var28 * var32);
            }
            var11.draw();
            var12.startDrawing(5);
            for (int var31 = 0; var31 <= 24; ++var31) {
                if (var31 % 2 == 0) {
                    var12.setColorRGBA(0.5f, 0.4f, 0.3f, 1.0f);
                }
                else {
                    var12.setColorRGBA(0.35f, 0.28f, 0.21000001f, 1.0f);
                }
                final float var32 = var31 / 24.0f;
                var12.addVertex(p_110827_2_ + var26 * var32 + 0.0, p_110827_4_ + var27 * (var32 * var32 + var32) * 0.5 + ((24.0f - var31) / 18.0f + 0.125f) + 0.025, p_110827_6_ + var28 * var32);
                var12.addVertex(p_110827_2_ + var26 * var32 + 0.025, p_110827_4_ + var27 * (var32 * var32 + var32) * 0.5 + ((24.0f - var31) / 18.0f + 0.125f), p_110827_6_ + var28 * var32 + 0.025);
            }
            var11.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTextures();
            GlStateManager.enableCull();
        }
    }
    
    @Override
    protected boolean canRenderName(final EntityLivingBase targetEntity) {
        return this.canRenderName((EntityLiving)targetEntity);
    }
    
    @Override
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    protected boolean func_177070_b(final Entity p_177070_1_) {
        return this.canRenderName((EntityLiving)p_177070_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    @Override
    public boolean func_177071_a(final Entity p_177071_1_, final ICamera p_177071_2_, final double p_177071_3_, final double p_177071_5_, final double p_177071_7_) {
        return this.func_177104_a((EntityLiving)p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
    }
}
