// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class CacheLocalByte
{
    private int maxX;
    private int maxY;
    private int maxZ;
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private byte[][][] cache;
    private byte[] lastZs;
    private int lastDz;
    
    public CacheLocalByte(final int maxX, final int maxY, final int maxZ) {
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
        this.cache = new byte[maxX][maxY][maxZ];
        this.resetCache();
    }
    
    public void resetCache() {
        for (int i = 0; i < this.maxX; ++i) {
            final byte[][] abyte = this.cache[i];
            for (int j = 0; j < this.maxY; ++j) {
                final byte[] abyte2 = abyte[j];
                for (int k = 0; k < this.maxZ; ++k) {
                    abyte2[k] = -1;
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
    
    public byte get(final int x, final int y, final int z) {
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
    
    public void setLast(final byte val) {
        try {
            this.lastZs[this.lastDz] = val;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
