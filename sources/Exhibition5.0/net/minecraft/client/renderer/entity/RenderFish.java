// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.ResourceLocation;

public class RenderFish extends Render
{
    private static final ResourceLocation field_110792_a;
    private static final String __OBFID = "CL_00000996";
    
    public RenderFish(final RenderManager p_i46175_1_) {
        super(p_i46175_1_);
    }
    
    public void func_180558_a(final EntityFishHook p_180558_1_, final double p_180558_2_, final double p_180558_4_, final double p_180558_6_, final float p_180558_8_, final float p_180558_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180558_2_, (float)p_180558_4_, (float)p_180558_6_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        this.bindEntityTexture(p_180558_1_);
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        final byte var12 = 1;
        final byte var13 = 2;
        final float var14 = (var12 * 8 + 0) / 128.0f;
        final float var15 = (var12 * 8 + 8) / 128.0f;
        final float var16 = (var13 * 8 + 0) / 128.0f;
        final float var17 = (var13 * 8 + 8) / 128.0f;
        final float var18 = 1.0f;
        final float var19 = 0.5f;
        final float var20 = 0.5f;
        final float n = 180.0f;
        final RenderManager renderManager = this.renderManager;
        GlStateManager.rotate(n - RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        final RenderManager renderManager2 = this.renderManager;
        GlStateManager.rotate(-RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        var11.startDrawingQuads();
        var11.func_178980_d(0.0f, 1.0f, 0.0f);
        var11.addVertexWithUV(0.0f - var19, 0.0f - var20, 0.0, var14, var17);
        var11.addVertexWithUV(var18 - var19, 0.0f - var20, 0.0, var15, var17);
        var11.addVertexWithUV(var18 - var19, 1.0f - var20, 0.0, var15, var16);
        var11.addVertexWithUV(0.0f - var19, 1.0f - var20, 0.0, var14, var16);
        var10.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        if (p_180558_1_.angler != null) {
            final float var21 = p_180558_1_.angler.getSwingProgress(p_180558_9_);
            final float var22 = MathHelper.sin(MathHelper.sqrt_float(var21) * 3.1415927f);
            Vec3 var23 = new Vec3(-0.36, 0.03, 0.35);
            var23 = var23.rotatePitch(-(p_180558_1_.angler.prevRotationPitch + (p_180558_1_.angler.rotationPitch - p_180558_1_.angler.prevRotationPitch) * p_180558_9_) * 3.1415927f / 180.0f);
            var23 = var23.rotateYaw(-(p_180558_1_.angler.prevRotationYaw + (p_180558_1_.angler.rotationYaw - p_180558_1_.angler.prevRotationYaw) * p_180558_9_) * 3.1415927f / 180.0f);
            var23 = var23.rotateYaw(var22 * 0.5f);
            var23 = var23.rotatePitch(-var22 * 0.7f);
            double var24 = p_180558_1_.angler.prevPosX + (p_180558_1_.angler.posX - p_180558_1_.angler.prevPosX) * p_180558_9_ + var23.xCoord;
            double var25 = p_180558_1_.angler.prevPosY + (p_180558_1_.angler.posY - p_180558_1_.angler.prevPosY) * p_180558_9_ + var23.yCoord;
            double var26 = p_180558_1_.angler.prevPosZ + (p_180558_1_.angler.posZ - p_180558_1_.angler.prevPosZ) * p_180558_9_ + var23.zCoord;
            double var27 = p_180558_1_.angler.getEyeHeight();
            if ((this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0) || p_180558_1_.angler != Minecraft.getMinecraft().thePlayer) {
                final float var28 = (p_180558_1_.angler.prevRenderYawOffset + (p_180558_1_.angler.renderYawOffset - p_180558_1_.angler.prevRenderYawOffset) * p_180558_9_) * 3.1415927f / 180.0f;
                final double var29 = MathHelper.sin(var28);
                final double var30 = MathHelper.cos(var28);
                final double var31 = 0.35;
                final double var32 = 0.8;
                var24 = p_180558_1_.angler.prevPosX + (p_180558_1_.angler.posX - p_180558_1_.angler.prevPosX) * p_180558_9_ - var30 * 0.35 - var29 * 0.8;
                var25 = p_180558_1_.angler.prevPosY + var27 + (p_180558_1_.angler.posY - p_180558_1_.angler.prevPosY) * p_180558_9_ - 0.45;
                var26 = p_180558_1_.angler.prevPosZ + (p_180558_1_.angler.posZ - p_180558_1_.angler.prevPosZ) * p_180558_9_ - var29 * 0.35 + var30 * 0.8;
                var27 = (p_180558_1_.angler.isSneaking() ? -0.1875 : 0.0);
            }
            final double var33 = p_180558_1_.prevPosX + (p_180558_1_.posX - p_180558_1_.prevPosX) * p_180558_9_;
            final double var34 = p_180558_1_.prevPosY + (p_180558_1_.posY - p_180558_1_.prevPosY) * p_180558_9_ + 0.25;
            final double var35 = p_180558_1_.prevPosZ + (p_180558_1_.posZ - p_180558_1_.prevPosZ) * p_180558_9_;
            final double var36 = (float)(var24 - var33);
            final double var37 = (float)(var25 - var34) + var27;
            final double var38 = (float)(var26 - var35);
            GlStateManager.disableTextures();
            GlStateManager.disableLighting();
            var11.startDrawing(3);
            var11.func_178991_c(0);
            final byte var39 = 16;
            for (int var40 = 0; var40 <= var39; ++var40) {
                final float var41 = var40 / var39;
                var11.addVertex(p_180558_2_ + var36 * var41, p_180558_4_ + var37 * (var41 * var41 + var41) * 0.5 + 0.25, p_180558_6_ + var38 * var41);
            }
            var10.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTextures();
            super.doRender(p_180558_1_, p_180558_2_, p_180558_4_, p_180558_6_, p_180558_8_, p_180558_9_);
        }
    }
    
    protected ResourceLocation getEntityTexture(final EntityFishHook p_110775_1_) {
        return RenderFish.field_110792_a;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityFishHook)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_180558_a((EntityFishHook)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        field_110792_a = new ResourceLocation("textures/particle/particles.png");
    }
}
