/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.tileentity;

import javax.annotation.Nullable;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.NoRender;

public class TileEntityBannerRenderer
extends TileEntitySpecialRenderer<TileEntityBanner> {
    private final ModelBanner bannerModel = new ModelBanner();

    @Override
    public void func_192841_a(TileEntityBanner p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.banner.getCurrentValue()) {
            return;
        }
        boolean flag = p_192841_1_.getWorld() != null;
        boolean flag1 = !flag || p_192841_1_.getBlockType() == Blocks.STANDING_BANNER;
        int i = flag ? p_192841_1_.getBlockMetadata() : 0;
        long j = flag ? p_192841_1_.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float f = 0.6666667f;
        if (flag1) {
            GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 0.5f, (float)p_192841_6_ + 0.5f);
            float f1 = (float)(i * 360) / 16.0f;
            GlStateManager.rotate(-f1, 0.0f, 1.0f, 0.0f);
            this.bannerModel.bannerStand.showModel = true;
        } else {
            float f2 = 0.0f;
            if (i == 2) {
                f2 = 180.0f;
            }
            if (i == 4) {
                f2 = 90.0f;
            }
            if (i == 5) {
                f2 = -90.0f;
            }
            GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ - 0.16666667f, (float)p_192841_6_ + 0.5f);
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.bannerModel.bannerStand.showModel = false;
        }
        BlockPos blockpos = p_192841_1_.getPos();
        float f3 = (float)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float)j + p_192841_8_;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125f + 0.01f * MathHelper.cos(f3 * (float)Math.PI * 0.02f)) * (float)Math.PI;
        GlStateManager.enableRescaleNormal();
        ResourceLocation resourcelocation = this.getBannerResourceLocation(p_192841_1_);
        if (resourcelocation != null) {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.6666667f, -0.6666667f, -0.6666667f);
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_192841_10_);
        GlStateManager.popMatrix();
    }

    @Nullable
    private ResourceLocation getBannerResourceLocation(TileEntityBanner bannerObj) {
        return BannerTextures.BANNER_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(), bannerObj.getPatternList(), bannerObj.getColorList());
    }
}

