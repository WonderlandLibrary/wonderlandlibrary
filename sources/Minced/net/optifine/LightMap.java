// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import net.minecraft.world.World;

public class LightMap
{
    private CustomColormap lightMapRgb;
    private float[][] sunRgbs;
    private float[][] torchRgbs;
    
    public LightMap(final CustomColormap lightMapRgb) {
        this.lightMapRgb = null;
        this.sunRgbs = new float[16][3];
        this.torchRgbs = new float[16][3];
        this.lightMapRgb = lightMapRgb;
    }
    
    public CustomColormap getColormap() {
        return this.lightMapRgb;
    }
    
    public boolean updateLightmap(final World world, final float torchFlickerX, final int[] lmColors, final boolean nightvision) {
        if (this.lightMapRgb == null) {
            return false;
        }
        final int i = this.lightMapRgb.getHeight();
        if (nightvision && i < 64) {
            return false;
        }
        final int j = this.lightMapRgb.getWidth();
        if (j < 16) {
            warn("Invalid lightmap width: " + j);
            this.lightMapRgb = null;
            return false;
        }
        int k = 0;
        if (nightvision) {
            k = j * 16 * 2;
        }
        float f = 1.1666666f * (world.getSunBrightness(1.0f) - 0.2f);
        if (world.getLastLightningBolt() > 0) {
            f = 1.0f;
        }
        f = Config.limitTo1(f);
        final float f2 = f * (j - 1);
        final float f3 = Config.limitTo1(torchFlickerX + 0.5f) * (j - 1);
        final float f4 = Config.limitTo1(Config.getGameSettings().gammaSetting);
        final boolean flag = f4 > 1.0E-4f;
        final float[][] afloat = this.lightMapRgb.getColorsRgb();
        this.getLightMapColumn(afloat, f2, k, j, this.sunRgbs);
        this.getLightMapColumn(afloat, f3, k + 16 * j, j, this.torchRgbs);
        final float[] afloat2 = new float[3];
        for (int l = 0; l < 16; ++l) {
            for (int i2 = 0; i2 < 16; ++i2) {
                for (int j2 = 0; j2 < 3; ++j2) {
                    float f5 = Config.limitTo1(this.sunRgbs[l][j2] + this.torchRgbs[i2][j2]);
                    if (flag) {
                        float f6 = 1.0f - f5;
                        f6 = 1.0f - f6 * f6 * f6 * f6;
                        f5 = f4 * f6 + (1.0f - f4) * f5;
                    }
                    afloat2[j2] = f5;
                }
                final int k2 = (int)(afloat2[0] * 255.0f);
                final int l2 = (int)(afloat2[1] * 255.0f);
                final int i3 = (int)(afloat2[2] * 255.0f);
                lmColors[l * 16 + i2] = (0xFF000000 | k2 << 16 | l2 << 8 | i3);
            }
        }
        return true;
    }
    
    private void getLightMapColumn(final float[][] origMap, final float x, final int offset, final int width, final float[][] colRgb) {
        final int i = (int)Math.floor(x);
        final int j = (int)Math.ceil(x);
        if (i == j) {
            for (int i2 = 0; i2 < 16; ++i2) {
                final float[] afloat3 = origMap[offset + i2 * width + i];
                final float[] afloat4 = colRgb[i2];
                for (int j2 = 0; j2 < 3; ++j2) {
                    afloat4[j2] = afloat3[j2];
                }
            }
        }
        else {
            final float f = 1.0f - (x - i);
            final float f2 = 1.0f - (j - x);
            for (int k = 0; k < 16; ++k) {
                final float[] afloat5 = origMap[offset + k * width + i];
                final float[] afloat6 = origMap[offset + k * width + j];
                final float[] afloat7 = colRgb[k];
                for (int l = 0; l < 3; ++l) {
                    afloat7[l] = afloat5[l] * f + afloat6[l] * f2;
                }
            }
        }
    }
    
    private static void dbg(final String str) {
        Config.dbg("CustomColors: " + str);
    }
    
    private static void warn(final String str) {
        Config.warn("CustomColors: " + str);
    }
}
