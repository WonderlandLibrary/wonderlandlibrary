// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.WorldRenderer;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.effect.EntityLightningBolt;

public class RenderLightningBolt extends Render
{
    private static final String __OBFID = "CL_00001011";
    
    public RenderLightningBolt(final RenderManager p_i46157_1_) {
        super(p_i46157_1_);
    }
    
    public void doRender(final EntityLightningBolt p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        GlStateManager.disableTextures();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);
        final double[] var12 = new double[8];
        final double[] var13 = new double[8];
        double var14 = 0.0;
        double var15 = 0.0;
        final Random var16 = new Random(p_76986_1_.boltVertex);
        for (int var17 = 7; var17 >= 0; --var17) {
            var12[var17] = var14;
            var13[var17] = var15;
            var14 += var16.nextInt(11) - 5;
            var15 += var16.nextInt(11) - 5;
        }
        for (int var18 = 0; var18 < 4; ++var18) {
            final Random var19 = new Random(p_76986_1_.boltVertex);
            for (int var20 = 0; var20 < 3; ++var20) {
                int var21 = 7;
                int var22 = 0;
                if (var20 > 0) {
                    var21 = 7 - var20;
                }
                if (var20 > 0) {
                    var22 = var21 - 2;
                }
                double var23 = var12[var21] - var14;
                double var24 = var13[var21] - var15;
                for (int var25 = var21; var25 >= var22; --var25) {
                    final double var26 = var23;
                    final double var27 = var24;
                    if (var20 == 0) {
                        var23 += var19.nextInt(11) - 5;
                        var24 += var19.nextInt(11) - 5;
                    }
                    else {
                        var23 += var19.nextInt(31) - 15;
                        var24 += var19.nextInt(31) - 15;
                    }
                    var11.startDrawing(5);
                    final float var28 = 0.5f;
                    var11.setColorRGBA(0.9f * var28, 0.9f * var28, 1.0f * var28, 0.3f);
                    double var29 = 0.1 + var18 * 0.2;
                    if (var20 == 0) {
                        var29 *= var25 * 0.1 + 1.0;
                    }
                    double var30 = 0.1 + var18 * 0.2;
                    if (var20 == 0) {
                        var30 *= (var25 - 1) * 0.1 + 1.0;
                    }
                    for (int var31 = 0; var31 < 5; ++var31) {
                        double var32 = p_76986_2_ + 0.5 - var29;
                        double var33 = p_76986_6_ + 0.5 - var29;
                        if (var31 == 1 || var31 == 2) {
                            var32 += var29 * 2.0;
                        }
                        if (var31 == 2 || var31 == 3) {
                            var33 += var29 * 2.0;
                        }
                        double var34 = p_76986_2_ + 0.5 - var30;
                        double var35 = p_76986_6_ + 0.5 - var30;
                        if (var31 == 1 || var31 == 2) {
                            var34 += var30 * 2.0;
                        }
                        if (var31 == 2 || var31 == 3) {
                            var35 += var30 * 2.0;
                        }
                        var11.addVertex(var34 + var23, p_76986_4_ + var25 * 16, var35 + var24);
                        var11.addVertex(var32 + var26, p_76986_4_ + (var25 + 1) * 16, var33 + var27);
                    }
                    var10.draw();
                }
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTextures();
    }
    
    protected ResourceLocation getEntityTexture(final EntityLightningBolt p_110775_1_) {
        return null;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityLightningBolt)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityLightningBolt)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
