/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;

public class RenderLightningBolt
extends Render<EntityLightningBolt> {
    public RenderLightningBolt(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityLightningBolt entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);
        double[] adouble = new double[8];
        double[] adouble1 = new double[8];
        double d0 = 0.0;
        double d1 = 0.0;
        Random random = new Random(entity.boltVertex);
        for (int i2 = 7; i2 >= 0; --i2) {
            adouble[i2] = d0;
            adouble1[i2] = d1;
            d0 += (double)(random.nextInt(11) - 5);
            d1 += (double)(random.nextInt(11) - 5);
        }
        for (int k1 = 0; k1 < 4; ++k1) {
            Random random1 = new Random(entity.boltVertex);
            for (int j2 = 0; j2 < 3; ++j2) {
                int k2 = 7;
                int l2 = 0;
                if (j2 > 0) {
                    k2 = 7 - j2;
                }
                if (j2 > 0) {
                    l2 = k2 - 2;
                }
                double d2 = adouble[k2] - d0;
                double d3 = adouble1[k2] - d1;
                for (int i1 = k2; i1 >= l2; --i1) {
                    double d4 = d2;
                    double d5 = d3;
                    if (j2 == 0) {
                        d2 += (double)(random1.nextInt(11) - 5);
                        d3 += (double)(random1.nextInt(11) - 5);
                    } else {
                        d2 += (double)(random1.nextInt(31) - 15);
                        d3 += (double)(random1.nextInt(31) - 15);
                    }
                    worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    float f2 = 0.5f;
                    float f1 = 0.45f;
                    float f22 = 0.45f;
                    float f3 = 0.5f;
                    double d6 = 0.1 + (double)k1 * 0.2;
                    if (j2 == 0) {
                        d6 *= (double)i1 * 0.1 + 1.0;
                    }
                    double d7 = 0.1 + (double)k1 * 0.2;
                    if (j2 == 0) {
                        d7 *= (double)(i1 - 1) * 0.1 + 1.0;
                    }
                    for (int j1 = 0; j1 < 5; ++j1) {
                        double d8 = x2 + 0.5 - d6;
                        double d9 = z2 + 0.5 - d6;
                        if (j1 == 1 || j1 == 2) {
                            d8 += d6 * 2.0;
                        }
                        if (j1 == 2 || j1 == 3) {
                            d9 += d6 * 2.0;
                        }
                        double d10 = x2 + 0.5 - d7;
                        double d11 = z2 + 0.5 - d7;
                        if (j1 == 1 || j1 == 2) {
                            d10 += d7 * 2.0;
                        }
                        if (j1 == 2 || j1 == 3) {
                            d11 += d7 * 2.0;
                        }
                        worldrenderer.pos(d10 + d2, y2 + (double)(i1 * 16), d11 + d3).color(0.45f, 0.45f, 0.5f, 0.3f).endVertex();
                        worldrenderer.pos(d8 + d4, y2 + (double)((i1 + 1) * 16), d9 + d5).color(0.45f, 0.45f, 0.5f, 0.3f).endVertex();
                    }
                    tessellator.draw();
                }
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLightningBolt entity) {
        return null;
    }
}

