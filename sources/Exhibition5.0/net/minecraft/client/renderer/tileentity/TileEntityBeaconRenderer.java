// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation beaconBeam;
    private static final String __OBFID = "CL_00000962";
    
    public void func_180536_a(final TileEntityBeacon p_180536_1_, final double p_180536_2_, final double p_180536_4_, final double p_180536_6_, final float p_180536_8_, final int p_180536_9_) {
        final float var10 = p_180536_1_.shouldBeamRender();
        GlStateManager.alphaFunc(516, 0.1f);
        if (var10 > 0.0f) {
            final Tessellator var11 = Tessellator.getInstance();
            final WorldRenderer var12 = var11.getWorldRenderer();
            final List var13 = p_180536_1_.func_174907_n();
            int var14 = 0;
            for (int var15 = 0; var15 < var13.size(); ++var15) {
                final TileEntityBeacon.BeamSegment var16 = var13.get(var15);
                final int var17 = var14 + var16.func_177264_c();
                this.bindTexture(TileEntityBeaconRenderer.beaconBeam);
                GL11.glTexParameterf(3553, 10242, 10497.0f);
                GL11.glTexParameterf(3553, 10243, 10497.0f);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                final float var18 = p_180536_1_.getWorld().getTotalWorldTime() + p_180536_8_;
                final float var19 = -var18 * 0.2f - MathHelper.floor_float(-var18 * 0.1f);
                double var20 = var18 * 0.025 * -1.5;
                var12.startDrawingQuads();
                double var21 = 0.2;
                double var22 = 0.5 + Math.cos(var20 + 2.356194490192345) * var21;
                double var23 = 0.5 + Math.sin(var20 + 2.356194490192345) * var21;
                double var24 = 0.5 + Math.cos(var20 + 0.7853981633974483) * var21;
                double var25 = 0.5 + Math.sin(var20 + 0.7853981633974483) * var21;
                double var26 = 0.5 + Math.cos(var20 + 3.9269908169872414) * var21;
                double var27 = 0.5 + Math.sin(var20 + 3.9269908169872414) * var21;
                double var28 = 0.5 + Math.cos(var20 + 5.497787143782138) * var21;
                double var29 = 0.5 + Math.sin(var20 + 5.497787143782138) * var21;
                double var30 = 0.0;
                double var31 = 1.0;
                final double var32 = -1.0f + var19;
                final double var33 = var16.func_177264_c() * var10 * (0.5 / var21) + var32;
                var12.setColorRGBA(var16.func_177263_b()[0], var16.func_177263_b()[1], var16.func_177263_b()[2], 0.125f);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var17, p_180536_6_ + var23, var31, var33);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var14, p_180536_6_ + var23, var31, var32);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var25, var30, var32);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var25, var30, var33);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var17, p_180536_6_ + var29, var31, var33);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var14, p_180536_6_ + var29, var31, var32);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var14, p_180536_6_ + var27, var30, var32);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var17, p_180536_6_ + var27, var30, var33);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var25, var31, var33);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var25, var31, var32);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var14, p_180536_6_ + var29, var30, var32);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var17, p_180536_6_ + var29, var30, var33);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var17, p_180536_6_ + var27, var31, var33);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var14, p_180536_6_ + var27, var31, var32);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var14, p_180536_6_ + var23, var30, var32);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var17, p_180536_6_ + var23, var30, var33);
                var11.draw();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                var12.startDrawingQuads();
                var12.setColorRGBA(var16.func_177263_b()[0], var16.func_177263_b()[1], var16.func_177263_b()[2], 0.125f);
                var20 = 0.2;
                var21 = 0.2;
                var22 = 0.8;
                var23 = 0.2;
                var24 = 0.2;
                var25 = 0.8;
                var26 = 0.8;
                var27 = 0.8;
                var28 = 0.0;
                var29 = 1.0;
                var30 = -1.0f + var19;
                var31 = var16.func_177264_c() * var10 + var30;
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var17, p_180536_6_ + var21, var29, var31);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var14, p_180536_6_ + var21, var29, var30);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var14, p_180536_6_ + var23, var28, var30);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var17, p_180536_6_ + var23, var28, var31);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var17, p_180536_6_ + var27, var29, var31);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var14, p_180536_6_ + var27, var29, var30);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var25, var28, var30);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var25, var28, var31);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var17, p_180536_6_ + var23, var29, var31);
                var12.addVertexWithUV(p_180536_2_ + var22, p_180536_4_ + var14, p_180536_6_ + var23, var29, var30);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var14, p_180536_6_ + var27, var28, var30);
                var12.addVertexWithUV(p_180536_2_ + var26, p_180536_4_ + var17, p_180536_6_ + var27, var28, var31);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var25, var29, var31);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var25, var29, var30);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var14, p_180536_6_ + var21, var28, var30);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var17, p_180536_6_ + var21, var28, var31);
                var11.draw();
                GlStateManager.enableLighting();
                GlStateManager.enableTextures();
                GlStateManager.depthMask(true);
                var14 = var17;
            }
        }
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity p_180535_1_, final double p_180535_2_, final double p_180535_4_, final double p_180535_6_, final float p_180535_8_, final int p_180535_9_) {
        this.func_180536_a((TileEntityBeacon)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
    
    static {
        beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
    }
}
