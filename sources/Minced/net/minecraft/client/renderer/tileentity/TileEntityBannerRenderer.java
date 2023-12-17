// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.tileentity.TileEntityBanner;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner>
{
    private final ModelBanner bannerModel;
    
    public TileEntityBannerRenderer() {
        this.bannerModel = new ModelBanner();
    }
    
    @Override
    public void render(final TileEntityBanner te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        final boolean flag = te.getWorld() != null;
        final boolean flag2 = !flag || te.getBlockType() == Blocks.STANDING_BANNER;
        final int i = flag ? te.getBlockMetadata() : 0;
        final long j = flag ? te.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        final float f = 0.6666667f;
        if (flag2) {
            GlStateManager.translate((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);
            final float f2 = i * 360 / 16.0f;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            this.bannerModel.bannerStand.showModel = true;
        }
        else {
            float f3 = 0.0f;
            if (i == 2) {
                f3 = 180.0f;
            }
            if (i == 4) {
                f3 = 90.0f;
            }
            if (i == 5) {
                f3 = -90.0f;
            }
            GlStateManager.translate((float)x + 0.5f, (float)y - 0.16666667f, (float)z + 0.5f);
            GlStateManager.rotate(-f3, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.bannerModel.bannerStand.showModel = false;
        }
        final BlockPos blockpos = te.getPos();
        final float f4 = blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13 + (float)j + partialTicks;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125f + 0.01f * MathHelper.cos(f4 * 3.1415927f * 0.02f)) * 3.1415927f;
        GlStateManager.enableRescaleNormal();
        final ResourceLocation resourcelocation = this.getBannerResourceLocation(te);
        if (resourcelocation != null) {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.6666667f, -0.6666667f, -0.6666667f);
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        GlStateManager.popMatrix();
    }
    
    @Nullable
    private ResourceLocation getBannerResourceLocation(final TileEntityBanner bannerObj) {
        return BannerTextures.BANNER_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(), bannerObj.getPatternList(), bannerObj.getColorList());
    }
}
