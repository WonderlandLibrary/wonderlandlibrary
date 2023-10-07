// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class LayerCape implements LayerRenderer
{
    private final RenderPlayer playerRenderer;
    private static final String __OBFID = "CL_00002425";
    
    public LayerCape(final RenderPlayer p_i46123_1_) {
        this.playerRenderer = p_i46123_1_;
    }
    
    public void doRenderLayer(final AbstractClientPlayer p_177166_1_, final float p_177166_2_, final float p_177166_3_, final float p_177166_4_, final float p_177166_5_, final float p_177166_6_, final float p_177166_7_, final float p_177166_8_) {
        if (p_177166_1_.hasCape() && !p_177166_1_.isInvisible() && p_177166_1_.func_175148_a(EnumPlayerModelParts.CAPE) && p_177166_1_.getLocationCape() != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.playerRenderer.bindTexture(p_177166_1_.getLocationCape());
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            final double var9 = p_177166_1_.field_71091_bM + (p_177166_1_.field_71094_bP - p_177166_1_.field_71091_bM) * p_177166_4_ - (p_177166_1_.prevPosX + (p_177166_1_.posX - p_177166_1_.prevPosX) * p_177166_4_);
            final double var10 = p_177166_1_.field_71096_bN + (p_177166_1_.field_71095_bQ - p_177166_1_.field_71096_bN) * p_177166_4_ - (p_177166_1_.prevPosY + (p_177166_1_.posY - p_177166_1_.prevPosY) * p_177166_4_);
            final double var11 = p_177166_1_.field_71097_bO + (p_177166_1_.field_71085_bR - p_177166_1_.field_71097_bO) * p_177166_4_ - (p_177166_1_.prevPosZ + (p_177166_1_.posZ - p_177166_1_.prevPosZ) * p_177166_4_);
            final float var12 = p_177166_1_.prevRenderYawOffset + (p_177166_1_.renderYawOffset - p_177166_1_.prevRenderYawOffset) * p_177166_4_;
            final double var13 = MathHelper.sin(var12 * 3.1415927f / 180.0f);
            final double var14 = -MathHelper.cos(var12 * 3.1415927f / 180.0f);
            float var15 = (float)var10 * 10.0f;
            var15 = MathHelper.clamp_float(var15, -6.0f, 32.0f);
            float var16 = (float)(var9 * var13 + var11 * var14) * 100.0f;
            final float var17 = (float)(var9 * var14 - var11 * var13) * 100.0f;
            if (var16 < 0.0f) {
                var16 = 0.0f;
            }
            if (var16 > 165.0f) {
                var16 = 165.0f;
            }
            final float var18 = p_177166_1_.prevCameraYaw + (p_177166_1_.cameraYaw - p_177166_1_.prevCameraYaw) * p_177166_4_;
            var15 += MathHelper.sin((p_177166_1_.prevDistanceWalkedModified + (p_177166_1_.distanceWalkedModified - p_177166_1_.prevDistanceWalkedModified) * p_177166_4_) * 6.0f) * 32.0f * var18;
            if (p_177166_1_.isSneaking()) {
                var15 += 25.0f;
                GlStateManager.translate(0.0f, 0.142f, -0.0178f);
            }
            GlStateManager.rotate(6.0f + var16 / 2.0f + var15, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(var17 / 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(-var17 / 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            this.playerRenderer.func_177136_g().func_178728_c(0.0625f);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        this.doRenderLayer((AbstractClientPlayer)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}
