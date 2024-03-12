// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.world.World;

public class LightMapPack
{
    private LightMap lightMap;
    private LightMap lightMapRain;
    private LightMap lightMapThunder;
    private int[] colorBuffer1;
    private int[] colorBuffer2;
    
    public LightMapPack(final LightMap lightMap, LightMap lightMapRain, LightMap lightMapThunder) {
        this.colorBuffer1 = new int[0];
        this.colorBuffer2 = new int[0];
        if (lightMapRain != null || lightMapThunder != null) {
            if (lightMapRain == null) {
                lightMapRain = lightMap;
            }
            if (lightMapThunder == null) {
                lightMapThunder = lightMapRain;
            }
        }
        this.lightMap = lightMap;
        this.lightMapRain = lightMapRain;
        this.lightMapThunder = lightMapThunder;
    }
    
    public boolean updateLightmap(final World world, final float torchFlickerX, final int[] lmColors, final boolean nightvision, final float partialTicks) {
        if (this.lightMapRain == null && this.lightMapThunder == null) {
            return this.lightMap.updateLightmap(world, torchFlickerX, lmColors, nightvision);
        }
        final int i = world.provider.getDimensionId();
        if (i == 1 || i == -1) {
            return this.lightMap.updateLightmap(world, torchFlickerX, lmColors, nightvision);
        }
        final float f = world.getRainStrength(partialTicks);
        float f2 = world.getThunderStrength(partialTicks);
        final float f3 = 1.0E-4f;
        final boolean flag = f > f3;
        final boolean flag2 = f2 > f3;
        if (!flag && !flag2) {
            return this.lightMap.updateLightmap(world, torchFlickerX, lmColors, nightvision);
        }
        if (f > 0.0f) {
            f2 /= f;
        }
        final float f4 = 1.0f - f;
        final float f5 = f - f2;
        if (this.colorBuffer1.length != lmColors.length) {
            this.colorBuffer1 = new int[lmColors.length];
            this.colorBuffer2 = new int[lmColors.length];
        }
        int j = 0;
        final int[][] aint = { lmColors, this.colorBuffer1, this.colorBuffer2 };
        final float[] afloat = new float[3];
        if (f4 > f3 && this.lightMap.updateLightmap(world, torchFlickerX, aint[j], nightvision)) {
            afloat[j] = f4;
            ++j;
        }
        if (f5 > f3 && this.lightMapRain != null && this.lightMapRain.updateLightmap(world, torchFlickerX, aint[j], nightvision)) {
            afloat[j] = f5;
            ++j;
        }
        if (f2 > f3 && this.lightMapThunder != null && this.lightMapThunder.updateLightmap(world, torchFlickerX, aint[j], nightvision)) {
            afloat[j] = f2;
            ++j;
        }
        return (j == 2) ? this.blend(aint[0], afloat[0], aint[1], afloat[1]) : (j != 3 || this.blend(aint[0], afloat[0], aint[1], afloat[1], aint[2], afloat[2]));
    }
    
    private boolean blend(final int[] cols0, final float br0, final int[] cols1, final float br1) {
        if (cols1.length != cols0.length) {
            return false;
        }
        for (int i = 0; i < cols0.length; ++i) {
            final int j = cols0[i];
            final int k = j >> 16 & 0xFF;
            final int l = j >> 8 & 0xFF;
            final int i2 = j & 0xFF;
            final int j2 = cols1[i];
            final int k2 = j2 >> 16 & 0xFF;
            final int l2 = j2 >> 8 & 0xFF;
            final int i3 = j2 & 0xFF;
            final int j3 = (int)(k * br0 + k2 * br1);
            final int k3 = (int)(l * br0 + l2 * br1);
            final int l3 = (int)(i2 * br0 + i3 * br1);
            cols0[i] = (0xFF000000 | j3 << 16 | k3 << 8 | l3);
        }
        return true;
    }
    
    private boolean blend(final int[] cols0, final float br0, final int[] cols1, final float br1, final int[] cols2, final float br2) {
        if (cols1.length == cols0.length && cols2.length == cols0.length) {
            for (int i = 0; i < cols0.length; ++i) {
                final int j = cols0[i];
                final int k = j >> 16 & 0xFF;
                final int l = j >> 8 & 0xFF;
                final int i2 = j & 0xFF;
                final int j2 = cols1[i];
                final int k2 = j2 >> 16 & 0xFF;
                final int l2 = j2 >> 8 & 0xFF;
                final int i3 = j2 & 0xFF;
                final int j3 = cols2[i];
                final int k3 = j3 >> 16 & 0xFF;
                final int l3 = j3 >> 8 & 0xFF;
                final int i4 = j3 & 0xFF;
                final int j4 = (int)(k * br0 + k2 * br1 + k3 * br2);
                final int k4 = (int)(l * br0 + l2 * br1 + l3 * br2);
                final int l4 = (int)(i2 * br0 + i3 * br1 + i4 * br2);
                cols0[i] = (0xFF000000 | j4 << 16 | k4 << 8 | l4);
            }
            return true;
        }
        return false;
    }
}
