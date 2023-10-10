/*
 * Decompiled with CFR 0.152.
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
    public void renderTileEntityAt(TileEntityBeacon te, double x2, double y2, double z2, float partialTicks, int destroyStage) {
        float f2 = te.shouldBeamRender();
        GlStateManager.alphaFunc(516, 0.1f);
        if (f2 > 0.0f) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableFog();
            List<TileEntityBeacon.BeamSegment> list = te.getBeamSegments();
            int i2 = 0;
            for (int j2 = 0; j2 < list.size(); ++j2) {
                TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = list.get(j2);
                int k2 = i2 + tileentitybeacon$beamsegment.getHeight();
                this.bindTexture(beaconBeam);
                GL11.glTexParameterf(3553, 10242, 10497.0f);
                GL11.glTexParameterf(3553, 10243, 10497.0f);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                double d0 = (double)te.getWorld().getTotalWorldTime() + (double)partialTicks;
                double d1 = MathHelper.func_181162_h(-d0 * 0.2 - (double)MathHelper.floor_double(-d0 * 0.1));
                float f1 = tileentitybeacon$beamsegment.getColors()[0];
                float f22 = tileentitybeacon$beamsegment.getColors()[1];
                float f3 = tileentitybeacon$beamsegment.getColors()[2];
                double d2 = d0 * 0.025 * -1.5;
                double d3 = 0.2;
                double d4 = 0.5 + Math.cos(d2 + 2.356194490192345) * 0.2;
                double d5 = 0.5 + Math.sin(d2 + 2.356194490192345) * 0.2;
                double d6 = 0.5 + Math.cos(d2 + 0.7853981633974483) * 0.2;
                double d7 = 0.5 + Math.sin(d2 + 0.7853981633974483) * 0.2;
                double d8 = 0.5 + Math.cos(d2 + 3.9269908169872414) * 0.2;
                double d9 = 0.5 + Math.sin(d2 + 3.9269908169872414) * 0.2;
                double d10 = 0.5 + Math.cos(d2 + 5.497787143782138) * 0.2;
                double d11 = 0.5 + Math.sin(d2 + 5.497787143782138) * 0.2;
                double d12 = 0.0;
                double d13 = 1.0;
                double d14 = -1.0 + d1;
                double d15 = (double)((float)tileentitybeacon$beamsegment.getHeight() * f2) * 2.5 + d14;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(x2 + d4, y2 + (double)k2, z2 + d5).tex(1.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d4, y2 + (double)i2, z2 + d5).tex(1.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d6, y2 + (double)i2, z2 + d7).tex(0.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d6, y2 + (double)k2, z2 + d7).tex(0.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d10, y2 + (double)k2, z2 + d11).tex(1.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d10, y2 + (double)i2, z2 + d11).tex(1.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d8, y2 + (double)i2, z2 + d9).tex(0.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d8, y2 + (double)k2, z2 + d9).tex(0.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d6, y2 + (double)k2, z2 + d7).tex(1.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d6, y2 + (double)i2, z2 + d7).tex(1.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d10, y2 + (double)i2, z2 + d11).tex(0.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d10, y2 + (double)k2, z2 + d11).tex(0.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d8, y2 + (double)k2, z2 + d9).tex(1.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d8, y2 + (double)i2, z2 + d9).tex(1.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d4, y2 + (double)i2, z2 + d5).tex(0.0, d14).color(f1, f22, f3, 1.0f).endVertex();
                worldrenderer.pos(x2 + d4, y2 + (double)k2, z2 + d5).tex(0.0, d15).color(f1, f22, f3, 1.0f).endVertex();
                tessellator.draw();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                d2 = 0.2;
                d3 = 0.2;
                d4 = 0.8;
                d5 = 0.2;
                d6 = 0.2;
                d7 = 0.8;
                d8 = 0.8;
                d9 = 0.8;
                d10 = 0.0;
                d11 = 1.0;
                d12 = -1.0 + d1;
                d13 = (double)((float)tileentitybeacon$beamsegment.getHeight() * f2) + d12;
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldrenderer.pos(x2 + 0.2, y2 + (double)k2, z2 + 0.2).tex(1.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.2, y2 + (double)i2, z2 + 0.2).tex(1.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)i2, z2 + 0.2).tex(0.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)k2, z2 + 0.2).tex(0.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)k2, z2 + 0.8).tex(1.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)i2, z2 + 0.8).tex(1.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.2, y2 + (double)i2, z2 + 0.8).tex(0.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.2, y2 + (double)k2, z2 + 0.8).tex(0.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)k2, z2 + 0.2).tex(1.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)i2, z2 + 0.2).tex(1.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)i2, z2 + 0.8).tex(0.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.8, y2 + (double)k2, z2 + 0.8).tex(0.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.2, y2 + (double)k2, z2 + 0.8).tex(1.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.2, y2 + (double)i2, z2 + 0.8).tex(1.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.2, y2 + (double)i2, z2 + 0.2).tex(0.0, d12).color(f1, f22, f3, 0.125f).endVertex();
                worldrenderer.pos(x2 + 0.2, y2 + (double)k2, z2 + 0.2).tex(0.0, d13).color(f1, f22, f3, 0.125f).endVertex();
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
                i2 = k2;
            }
            GlStateManager.enableFog();
        }
    }

    @Override
    public boolean func_181055_a() {
        return true;
    }
}

