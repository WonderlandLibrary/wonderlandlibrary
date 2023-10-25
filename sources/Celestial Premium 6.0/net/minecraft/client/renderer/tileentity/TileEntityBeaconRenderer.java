/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import optifine.Config;
import shadersmod.client.Shaders;

public class TileEntityBeaconRenderer
extends TileEntitySpecialRenderer<TileEntityBeacon> {
    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    @Override
    public void func_192841_a(TileEntityBeacon p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
        this.renderBeacon(p_192841_2_, p_192841_4_, p_192841_6_, p_192841_8_, p_192841_1_.shouldBeamRender(), p_192841_1_.getBeamSegments(), p_192841_1_.getWorld().getTotalWorldTime());
    }

    public void renderBeacon(double p_188206_1_, double p_188206_3_, double p_188206_5_, double p_188206_7_, double p_188206_9_, List<TileEntityBeacon.BeamSegment> p_188206_11_, double p_188206_12_) {
        if (p_188206_9_ > 0.0 && p_188206_11_.size() > 0) {
            if (Config.isShaders()) {
                Shaders.beginBeacon();
            }
            GlStateManager.alphaFunc(516, 0.1f);
            this.bindTexture(TEXTURE_BEACON_BEAM);
            if (p_188206_9_ > 0.0) {
                GlStateManager.disableFog();
                int i = 0;
                for (int j = 0; j < p_188206_11_.size(); ++j) {
                    TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = p_188206_11_.get(j);
                    TileEntityBeaconRenderer.renderBeamSegment(p_188206_1_, p_188206_3_, p_188206_5_, p_188206_7_, p_188206_9_, p_188206_12_, i, tileentitybeacon$beamsegment.getHeight(), tileentitybeacon$beamsegment.getColors());
                    i += tileentitybeacon$beamsegment.getHeight();
                }
                GlStateManager.enableFog();
            }
            if (Config.isShaders()) {
                Shaders.endBeacon();
            }
        }
    }

    public static void renderBeamSegment(double x, double y, double z, double partialTicks, double textureScale, double totalWorldTime, int yOffset, int height, float[] colors) {
        TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, yOffset, height, colors, 0.2, 0.25);
    }

    public static void renderBeamSegment(double x, double y, double z, double partialTicks, double textureScale, double totalWorldTime, int yOffset, int height, float[] colors, double beamRadius, double glowRadius) {
        int i = yOffset + height;
        GlStateManager.glTexParameteri(3553, 10242, 10497);
        GlStateManager.glTexParameteri(3553, 10243, 10497);
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double d0 = totalWorldTime + partialTicks;
        double d1 = height < 0 ? d0 : -d0;
        double d2 = MathHelper.frac(d1 * 0.2 - (double)MathHelper.floor(d1 * 0.1));
        float f = colors[0];
        float f1 = colors[1];
        float f2 = colors[2];
        double d3 = d0 * 0.025 * -1.5;
        double d4 = 0.5 + Math.cos(d3 + 2.356194490192345) * beamRadius;
        double d5 = 0.5 + Math.sin(d3 + 2.356194490192345) * beamRadius;
        double d6 = 0.5 + Math.cos(d3 + 0.7853981633974483) * beamRadius;
        double d7 = 0.5 + Math.sin(d3 + 0.7853981633974483) * beamRadius;
        double d8 = 0.5 + Math.cos(d3 + 3.9269908169872414) * beamRadius;
        double d9 = 0.5 + Math.sin(d3 + 3.9269908169872414) * beamRadius;
        double d10 = 0.5 + Math.cos(d3 + 5.497787143782138) * beamRadius;
        double d11 = 0.5 + Math.sin(d3 + 5.497787143782138) * beamRadius;
        double d12 = 0.0;
        double d13 = 1.0;
        double d14 = -1.0 + d2;
        double d15 = (double)height * textureScale * (0.5 / beamRadius) + d14;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x + d4, y + (double)i, z + d5).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d4, y + (double)yOffset, z + d5).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)yOffset, z + d7).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)i, z + d7).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)i, z + d11).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)yOffset, z + d11).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)yOffset, z + d9).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)i, z + d9).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)i, z + d7).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d6, y + (double)yOffset, z + d7).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)yOffset, z + d11).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d10, y + (double)i, z + d11).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)i, z + d9).tex(1.0, d15).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d8, y + (double)yOffset, z + d9).tex(1.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d4, y + (double)yOffset, z + d5).tex(0.0, d14).color(f, f1, f2, 1.0f).endVertex();
        bufferbuilder.pos(x + d4, y + (double)i, z + d5).tex(0.0, d15).color(f, f1, f2, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.depthMask(false);
        d3 = 0.5 - glowRadius;
        d4 = 0.5 - glowRadius;
        d5 = 0.5 + glowRadius;
        d6 = 0.5 - glowRadius;
        d7 = 0.5 - glowRadius;
        d8 = 0.5 + glowRadius;
        d9 = 0.5 + glowRadius;
        d10 = 0.5 + glowRadius;
        d11 = 0.0;
        d12 = 1.0;
        d13 = -1.0 + d2;
        d14 = (double)height * textureScale + d13;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x + d3, y + (double)i, z + d4).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d3, y + (double)yOffset, z + d4).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)yOffset, z + d6).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)i, z + d6).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)i, z + d10).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)yOffset, z + d10).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)yOffset, z + d8).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)i, z + d8).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)i, z + d6).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d5, y + (double)yOffset, z + d6).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)yOffset, z + d10).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d9, y + (double)i, z + d10).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)i, z + d8).tex(1.0, d14).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d7, y + (double)yOffset, z + d8).tex(1.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d3, y + (double)yOffset, z + d4).tex(0.0, d13).color(f, f1, f2, 0.125f).endVertex();
        bufferbuilder.pos(x + d3, y + (double)i, z + d4).tex(0.0, d14).color(f, f1, f2, 0.125f).endVertex();
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }

    @Override
    public boolean isGlobalRenderer(TileEntityBeacon te) {
        return true;
    }
}

