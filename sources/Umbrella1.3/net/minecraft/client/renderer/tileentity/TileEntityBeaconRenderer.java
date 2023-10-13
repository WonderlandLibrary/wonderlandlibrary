/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityBeaconRenderer
extends TileEntitySpecialRenderer {
    private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
    private static final String __OBFID = "CL_00000962";

    public void func_180536_a(TileEntityBeacon p_180536_1_, double p_180536_2_, double p_180536_4_, double p_180536_6_, float p_180536_8_, int p_180536_9_) {
        float var10 = p_180536_1_.shouldBeamRender();
        GlStateManager.alphaFunc(516, 0.1f);
        if (var10 > 0.0f) {
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();
            List var13 = p_180536_1_.func_174907_n();
            int var14 = 0;
            for (int var15 = 0; var15 < var13.size(); ++var15) {
                TileEntityBeacon.BeamSegment var16 = (TileEntityBeacon.BeamSegment)var13.get(var15);
                int var17 = var14 + var16.func_177264_c();
                this.bindTexture(beaconBeam);
                GL11.glTexParameterf((int)3553, (int)10242, (float)10497.0f);
                GL11.glTexParameterf((int)3553, (int)10243, (float)10497.0f);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                float var18 = (float)p_180536_1_.getWorld().getTotalWorldTime() + p_180536_8_;
                float var19 = -var18 * 0.2f - (float)MathHelper.floor_float(-var18 * 0.1f);
                double var20 = (double)var18 * 0.025 * -1.5;
                var12.startDrawingQuads();
                double var22 = 0.2;
                double var24 = 0.5 + Math.cos(var20 + 2.356194490192345) * var22;
                double var26 = 0.5 + Math.sin(var20 + 2.356194490192345) * var22;
                double var28 = 0.5 + Math.cos(var20 + 0.7853981633974483) * var22;
                double var30 = 0.5 + Math.sin(var20 + 0.7853981633974483) * var22;
                double var32 = 0.5 + Math.cos(var20 + 3.9269908169872414) * var22;
                double var34 = 0.5 + Math.sin(var20 + 3.9269908169872414) * var22;
                double var36 = 0.5 + Math.cos(var20 + 5.497787143782138) * var22;
                double var38 = 0.5 + Math.sin(var20 + 5.497787143782138) * var22;
                double var40 = 0.0;
                double var42 = 1.0;
                double var44 = -1.0f + var19;
                double var46 = (double)((float)var16.func_177264_c() * var10) * (0.5 / var22) + var44;
                var12.func_178960_a(var16.func_177263_b()[0], var16.func_177263_b()[1], var16.func_177263_b()[2], 0.125f);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var17, p_180536_6_ + var26, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var14, p_180536_6_ + var26, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var14, p_180536_6_ + var30, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var17, p_180536_6_ + var30, var40, var46);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + (double)var17, p_180536_6_ + var38, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + (double)var14, p_180536_6_ + var38, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var14, p_180536_6_ + var34, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var17, p_180536_6_ + var34, var40, var46);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var17, p_180536_6_ + var30, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var14, p_180536_6_ + var30, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + (double)var14, p_180536_6_ + var38, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + (double)var17, p_180536_6_ + var38, var40, var46);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var17, p_180536_6_ + var34, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var14, p_180536_6_ + var34, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var14, p_180536_6_ + var26, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var17, p_180536_6_ + var26, var40, var46);
                var11.draw();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                var12.startDrawingQuads();
                var12.func_178960_a(var16.func_177263_b()[0], var16.func_177263_b()[1], var16.func_177263_b()[2], 0.125f);
                var20 = 0.2;
                var22 = 0.2;
                var24 = 0.8;
                var26 = 0.2;
                var28 = 0.2;
                var30 = 0.8;
                var32 = 0.8;
                var34 = 0.8;
                var36 = 0.0;
                var38 = 1.0;
                var40 = -1.0f + var19;
                var42 = (double)((float)var16.func_177264_c() * var10) + var40;
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + (double)var17, p_180536_6_ + var22, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + (double)var14, p_180536_6_ + var22, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var14, p_180536_6_ + var26, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var17, p_180536_6_ + var26, var36, var42);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var17, p_180536_6_ + var34, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var14, p_180536_6_ + var34, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var14, p_180536_6_ + var30, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var17, p_180536_6_ + var30, var36, var42);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var17, p_180536_6_ + var26, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + (double)var14, p_180536_6_ + var26, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var14, p_180536_6_ + var34, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + (double)var17, p_180536_6_ + var34, var36, var42);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var17, p_180536_6_ + var30, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + (double)var14, p_180536_6_ + var30, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + (double)var14, p_180536_6_ + var22, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + (double)var17, p_180536_6_ + var22, var36, var42);
                var11.draw();
                GlStateManager.enableLighting();
                GlStateManager.func_179098_w();
                GlStateManager.depthMask(true);
                var14 = var17;
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity p_180535_1_, double p_180535_2_, double p_180535_4_, double p_180535_6_, float p_180535_8_, int p_180535_9_) {
        this.func_180536_a((TileEntityBeacon)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
}

