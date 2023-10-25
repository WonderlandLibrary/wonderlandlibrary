/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityBeaconRenderer
extends TileEntitySpecialRenderer<TileEntityBeacon> {
    private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");

    @Override
    public boolean func_181055_a() {
        return true;
    }

    @Override
    public void renderTileEntityAt(TileEntityBeacon tileEntityBeacon, double d, double d2, double d3, float f, int n) {
        float f2 = tileEntityBeacon.shouldBeamRender();
        GlStateManager.alphaFunc(516, 0.1f);
        if (f2 > 0.0f) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            GlStateManager.disableFog();
            List<TileEntityBeacon.BeamSegment> list = tileEntityBeacon.getBeamSegments();
            int n2 = 0;
            int n3 = 0;
            while (n3 < list.size()) {
                TileEntityBeacon.BeamSegment beamSegment = list.get(n3);
                int n4 = n2 + beamSegment.getHeight();
                this.bindTexture(beaconBeam);
                GL11.glTexParameterf((int)3553, (int)10242, (float)10497.0f);
                GL11.glTexParameterf((int)3553, (int)10243, (float)10497.0f);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                double d4 = (double)tileEntityBeacon.getWorld().getTotalWorldTime() + (double)f;
                double d5 = MathHelper.func_181162_h(-d4 * 0.2 - (double)MathHelper.floor_double(-d4 * 0.1));
                float f3 = beamSegment.getColors()[0];
                float f4 = beamSegment.getColors()[1];
                float f5 = beamSegment.getColors()[2];
                double d6 = d4 * 0.025 * -1.5;
                double d7 = 0.2;
                double d8 = 0.5 + Math.cos(d6 + 2.356194490192345) * 0.2;
                double d9 = 0.5 + Math.sin(d6 + 2.356194490192345) * 0.2;
                double d10 = 0.5 + Math.cos(d6 + 0.7853981633974483) * 0.2;
                double d11 = 0.5 + Math.sin(d6 + 0.7853981633974483) * 0.2;
                double d12 = 0.5 + Math.cos(d6 + 3.9269908169872414) * 0.2;
                double d13 = 0.5 + Math.sin(d6 + 3.9269908169872414) * 0.2;
                double d14 = 0.5 + Math.cos(d6 + 5.497787143782138) * 0.2;
                double d15 = 0.5 + Math.sin(d6 + 5.497787143782138) * 0.2;
                double d16 = 0.0;
                double d17 = 1.0;
                double d18 = -1.0 + d5;
                double d19 = (double)((float)beamSegment.getHeight() * f2) * 2.5 + d18;
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(d + d8, d2 + (double)n4, d3 + d9).tex(1.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d8, d2 + (double)n2, d3 + d9).tex(1.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d10, d2 + (double)n2, d3 + d11).tex(0.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d10, d2 + (double)n4, d3 + d11).tex(0.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d14, d2 + (double)n4, d3 + d15).tex(1.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d14, d2 + (double)n2, d3 + d15).tex(1.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d12, d2 + (double)n2, d3 + d13).tex(0.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d12, d2 + (double)n4, d3 + d13).tex(0.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d10, d2 + (double)n4, d3 + d11).tex(1.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d10, d2 + (double)n2, d3 + d11).tex(1.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d14, d2 + (double)n2, d3 + d15).tex(0.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d14, d2 + (double)n4, d3 + d15).tex(0.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d12, d2 + (double)n4, d3 + d13).tex(1.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d12, d2 + (double)n2, d3 + d13).tex(1.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d8, d2 + (double)n2, d3 + d9).tex(0.0, d18).color(f3, f4, f5, 1.0f).endVertex();
                worldRenderer.pos(d + d8, d2 + (double)n4, d3 + d9).tex(0.0, d19).color(f3, f4, f5, 1.0f).endVertex();
                tessellator.draw();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                d6 = 0.2;
                d7 = 0.2;
                d8 = 0.8;
                d9 = 0.2;
                d10 = 0.2;
                d11 = 0.8;
                d12 = 0.8;
                d13 = 0.8;
                d14 = 0.0;
                d15 = 1.0;
                d16 = -1.0 + d5;
                d17 = (double)((float)beamSegment.getHeight() * f2) + d16;
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(d + 0.2, d2 + (double)n4, d3 + 0.2).tex(1.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.2, d2 + (double)n2, d3 + 0.2).tex(1.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n2, d3 + 0.2).tex(0.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n4, d3 + 0.2).tex(0.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n4, d3 + 0.8).tex(1.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n2, d3 + 0.8).tex(1.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.2, d2 + (double)n2, d3 + 0.8).tex(0.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.2, d2 + (double)n4, d3 + 0.8).tex(0.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n4, d3 + 0.2).tex(1.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n2, d3 + 0.2).tex(1.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n2, d3 + 0.8).tex(0.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.8, d2 + (double)n4, d3 + 0.8).tex(0.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.2, d2 + (double)n4, d3 + 0.8).tex(1.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.2, d2 + (double)n2, d3 + 0.8).tex(1.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.2, d2 + (double)n2, d3 + 0.2).tex(0.0, d16).color(f3, f4, f5, 0.125f).endVertex();
                worldRenderer.pos(d + 0.2, d2 + (double)n4, d3 + 0.2).tex(0.0, d17).color(f3, f4, f5, 0.125f).endVertex();
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
                n2 = n4;
                ++n3;
            }
            GlStateManager.enableFog();
        }
    }
}

