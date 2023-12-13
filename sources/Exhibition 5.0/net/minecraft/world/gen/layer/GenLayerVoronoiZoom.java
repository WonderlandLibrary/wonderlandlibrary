// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.layer;

public class GenLayerVoronoiZoom extends GenLayer
{
    private static final String __OBFID = "CL_00000571";
    
    public GenLayerVoronoiZoom(final long p_i2133_1_, final GenLayer p_i2133_3_) {
        super(p_i2133_1_);
        super.parent = p_i2133_3_;
    }
    
    @Override
    public int[] getInts(int areaX, int areaY, final int areaWidth, final int areaHeight) {
        areaX -= 2;
        areaY -= 2;
        final int var5 = areaX >> 2;
        final int var6 = areaY >> 2;
        final int var7 = (areaWidth >> 2) + 2;
        final int var8 = (areaHeight >> 2) + 2;
        final int[] var9 = this.parent.getInts(var5, var6, var7, var8);
        final int var10 = var7 - 1 << 2;
        final int var11 = var8 - 1 << 2;
        final int[] var12 = IntCache.getIntCache(var10 * var11);
        for (int var13 = 0; var13 < var8 - 1; ++var13) {
            int var14 = 0;
            int var15 = var9[var14 + 0 + (var13 + 0) * var7];
            int var16 = var9[var14 + 0 + (var13 + 1) * var7];
            while (var14 < var7 - 1) {
                final double var17 = 3.6;
                this.initChunkSeed(var14 + var5 << 2, var13 + var6 << 2);
                final double var18 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                final double var19 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(var14 + var5 + 1 << 2, var13 + var6 << 2);
                final double var20 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final double var21 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                this.initChunkSeed(var14 + var5 << 2, var13 + var6 + 1 << 2);
                final double var22 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6;
                final double var23 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                this.initChunkSeed(var14 + var5 + 1 << 2, var13 + var6 + 1 << 2);
                final double var24 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final double var25 = (this.nextInt(1024) / 1024.0 - 0.5) * 3.6 + 4.0;
                final int var26 = var9[var14 + 1 + (var13 + 0) * var7] & 0xFF;
                final int var27 = var9[var14 + 1 + (var13 + 1) * var7] & 0xFF;
                for (int var28 = 0; var28 < 4; ++var28) {
                    int var29 = ((var13 << 2) + var28) * var10 + (var14 << 2);
                    for (int var30 = 0; var30 < 4; ++var30) {
                        final double var31 = (var28 - var19) * (var28 - var19) + (var30 - var18) * (var30 - var18);
                        final double var32 = (var28 - var21) * (var28 - var21) + (var30 - var20) * (var30 - var20);
                        final double var33 = (var28 - var23) * (var28 - var23) + (var30 - var22) * (var30 - var22);
                        final double var34 = (var28 - var25) * (var28 - var25) + (var30 - var24) * (var30 - var24);
                        if (var31 < var32 && var31 < var33 && var31 < var34) {
                            var12[var29++] = var15;
                        }
                        else if (var32 < var31 && var32 < var33 && var32 < var34) {
                            var12[var29++] = var26;
                        }
                        else if (var33 < var31 && var33 < var32 && var33 < var34) {
                            var12[var29++] = var16;
                        }
                        else {
                            var12[var29++] = var27;
                        }
                    }
                }
                var15 = var26;
                var16 = var27;
                ++var14;
            }
        }
        final int[] var35 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int var14 = 0; var14 < areaHeight; ++var14) {
            System.arraycopy(var12, (var14 + (areaY & 0x3)) * var10 + (areaX & 0x3), var35, var14 * areaWidth, areaWidth);
        }
        return var35;
    }
}
