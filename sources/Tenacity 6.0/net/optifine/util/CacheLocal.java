// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class CacheLocal
{
    private int maxX;
    private int maxY;
    private int maxZ;
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private int[][][] cache;
    private int[] lastZs;
    private int lastDz;
    
    public CacheLocal(final int maxX, final int maxY, final int maxZ) {
        this.maxX = 18;
        this.maxY = 128;
        this.maxZ = 18;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.cache = null;
        this.lastZs = null;
        this.lastDz = 0;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.cache = new int[maxX][maxY][maxZ];
        this.resetCache();
    }
    
    public void resetCache() {
        for (int i = 0; i < this.maxX; ++i) {
            final int[][] aint = this.cache[i];
            for (int j = 0; j < this.maxY; ++j) {
                final int[] aint2 = aint[j];
                for (int k = 0; k < this.maxZ; ++k) {
                    aint2[k] = -1;
                }
            }
        }
    }
    
    public void setOffset(final int x, final int y, final int z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.resetCache();
    }
    
    public int get(final int x, final int y, final int z) {
        try {
            this.lastZs = this.cache[x - this.offsetX][y - this.offsetY];
            this.lastDz = z - this.offsetZ;
            return this.lastZs[this.lastDz];
        }
        catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
            arrayindexoutofboundsexception.printStackTrace();
            return -1;
        }
    }
    
    public void setLast(final int val) {
        try {
            this.lastZs[this.lastDz] = val;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
