/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;

public class TileEntityEndPortalRenderer
extends TileEntitySpecialRenderer<TileEntityEndPortal> {
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final Random field_147527_e;
    private static final ResourceLocation END_PORTAL_TEXTURE;
    FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);

    @Override
    public void renderTileEntityAt(TileEntityEndPortal tileEntityEndPortal, double d, double d2, double d3, float f, int n) {
        float f2 = (float)this.rendererDispatcher.entityX;
        float f3 = (float)this.rendererDispatcher.entityY;
        float f4 = (float)this.rendererDispatcher.entityZ;
        GlStateManager.disableLighting();
        field_147527_e.setSeed(31100L);
        float f5 = 0.75f;
        int n2 = 0;
        while (n2 < 16) {
            GlStateManager.pushMatrix();
            float f6 = 16 - n2;
            float f7 = 0.0625f;
            float f8 = 1.0f / (f6 + 1.0f);
            if (n2 == 0) {
                this.bindTexture(END_SKY_TEXTURE);
                f8 = 0.1f;
                f6 = 65.0f;
                f7 = 0.125f;
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
            }
            if (n2 >= 1) {
                this.bindTexture(END_PORTAL_TEXTURE);
            }
            if (n2 == 1) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(1, 1);
                f7 = 0.5f;
            }
            float f9 = (float)(-(d2 + (double)f5));
            float f10 = f9 + (float)ActiveRenderInfo.getPosition().yCoord;
            float f11 = f9 + f6 + (float)ActiveRenderInfo.getPosition().yCoord;
            float f12 = f10 / f11;
            f12 = (float)(d2 + (double)f5) + f12;
            GlStateManager.translate(f2, f12, f4);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
            GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
            GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9473, this.func_147525_a(1.0f, 0.0f, 0.0f, 0.0f));
            GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9473, this.func_147525_a(0.0f, 0.0f, 1.0f, 0.0f));
            GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9473, this.func_147525_a(0.0f, 0.0f, 0.0f, 1.0f));
            GlStateManager.func_179105_a(GlStateManager.TexGen.Q, 9474, this.func_147525_a(0.0f, 1.0f, 0.0f, 0.0f));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0f, 0.0f);
            GlStateManager.scale(f7, f7, f7);
            GlStateManager.translate(0.5f, 0.5f, 0.0f);
            GlStateManager.rotate((float)(n2 * n2 * 4321 + n2 * 9) * 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(-0.5f, -0.5f, 0.0f);
            GlStateManager.translate(-f2, -f4, -f3);
            f10 = f9 + (float)ActiveRenderInfo.getPosition().yCoord;
            GlStateManager.translate((float)ActiveRenderInfo.getPosition().xCoord * f6 / f10, (float)ActiveRenderInfo.getPosition().zCoord * f6 / f10, -f3);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f13 = (field_147527_e.nextFloat() * 0.5f + 0.1f) * f8;
            float f14 = (field_147527_e.nextFloat() * 0.5f + 0.4f) * f8;
            float f15 = (field_147527_e.nextFloat() * 0.5f + 0.5f) * f8;
            if (n2 == 0) {
                f14 = f15 = 1.0f * f8;
                f13 = f15;
            }
            worldRenderer.pos(d, d2 + (double)f5, d3).color(f13, f14, f15, 1.0f).endVertex();
            worldRenderer.pos(d, d2 + (double)f5, d3 + 1.0).color(f13, f14, f15, 1.0f).endVertex();
            worldRenderer.pos(d + 1.0, d2 + (double)f5, d3 + 1.0).color(f13, f14, f15, 1.0f).endVertex();
            worldRenderer.pos(d + 1.0, d2 + (double)f5, d3).color(f13, f14, f15, 1.0f).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            this.bindTexture(END_SKY_TEXTURE);
            ++n2;
        }
        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
        GlStateManager.enableLighting();
    }

    private FloatBuffer func_147525_a(float f, float f2, float f3, float f4) {
        this.field_147528_b.clear();
        this.field_147528_b.put(f).put(f2).put(f3).put(f4);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }

    static {
        END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
        field_147527_e = new Random(31100L);
    }
}

