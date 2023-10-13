/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerVoronoiZoom
extends GenLayer {
    private static final String __OBFID = "CL_00000571";

    public GenLayerVoronoiZoom(long p_i2133_1_, GenLayer p_i2133_3_) {
        super(p_i2133_1_);
        this.parent = p_i2133_3_;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int var14;
        int var5 = (areaX -= 2) >> 2;
        int var6 = (areaY -= 2) >> 2;
        int var7 = (areaWidth >> 2) + 2;
        int var8 = (areaHeight >> 2) + 2;
        int[] var9 = this.parent.getInts(var5, var6, var7, var8);
        int var10 = var7 - 1 << 2;
        int var11 = var8 - 1 << 2;
        int[] var12 = IntCache.getIntCache(var10 * var11);
        for (int var13 = 0; var13 < var8 - 1; ++var13) {
            int var15 = var9[var14 + 0 + (var13 + 0) * var7];
            int var16 = var9[var14 + 0 + (var13 + 1) * var7];
            for (var14 = 0; var14 < var7 - 1; ++var14) {
                double var17 = 3.6;
                this.initChunkSeed(var14 + var5 << 2, var13 + var6 << 2);
                double var19 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                double var21 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(var14 + var5 + 1 << 2, var13 + var6 << 2);
                double var23 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                double var25 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(var14 + var5 << 2, var13 + var6 + 1 << 2);
                double var27 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                double var29 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                this.initChunkSeed(var14 + var5 + 1 << 2, var13 + var6 + 1 << 2);
                double var31 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                double var33 = ((double)this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                int var35 = var9[var14 + 1 + (var13 + 0) * var7] & 0xFF;
                int var36 = var9[var14 + 1 + (var13 + 1) * var7] & 0xFF;
                for (int var37 = 0; var37 < 4; ++var37) {
                    int var38 = ((var13 << 2) + var37) * var10 + (var14 << 2);
                    for (int var39 = 0; var39 < 4; ++var39) {
                        double var40 = ((double)var37 - var21) * ((double)var37 - var21) + ((double)var39 - var19) * ((double)var39 - var19);
                        double var42 = ((double)var37 - var25) * ((double)var37 - var25) + ((double)var39 - var23) * ((double)var39 - var23);
                        double var44 = ((double)var37 - var29) * ((double)var37 - var29) + ((double)var39 - var27) * ((double)var39 - var27);
                        double var46 = ((double)var37 - var33) * ((double)var37 - var33) + ((double)var39 - var31) * ((double)var39 - var31);
                        var12[var38++] = var40 < var42 && var40 < var44 && var40 < var46 ? var15 : (var42 < var40 && var42 < var44 && var42 < var46 ? var35 : (var44 < var40 && var44 < var42 && var44 < var46 ? var16 : var36));
                    }
                }
                var15 = var35;
                var16 = var36;
            }
        }
        int[] var48 = IntCache.getIntCache(areaWidth * areaHeight);
        for (var14 = 0; var14 < areaHeight; ++var14) {
            System.arraycopy(var12, (var14 + (areaY & 3)) * var10 + (areaX & 3), var48, var14 * areaWidth, areaWidth);
        }
        return var48;
    }
}

