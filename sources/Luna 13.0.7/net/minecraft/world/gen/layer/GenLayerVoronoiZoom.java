package net.minecraft.world.gen.layer;

public class GenLayerVoronoiZoom
  extends GenLayer
{
  private static final String __OBFID = "CL_00000571";
  
  public GenLayerVoronoiZoom(long p_i2133_1_, GenLayer p_i2133_3_)
  {
    super(p_i2133_1_);
    this.parent = p_i2133_3_;
  }
  
  public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
  {
    areaX -= 2;
    areaY -= 2;
    int var5 = areaX >> 2;
    int var6 = areaY >> 2;
    int var7 = (areaWidth >> 2) + 2;
    int var8 = (areaHeight >> 2) + 2;
    int[] var9 = this.parent.getInts(var5, var6, var7, var8);
    int var10 = var7 - 1 << 2;
    int var11 = var8 - 1 << 2;
    int[] var12 = IntCache.getIntCache(var10 * var11);
    for (int var13 = 0; var13 < var8 - 1; var13++)
    {
      int var14 = 0;
      int var15 = var9[(var14 + 0 + (var13 + 0) * var7)];
      for (int var16 = var9[(var14 + 0 + (var13 + 1) * var7)]; var14 < var7 - 1; var14++)
      {
        double var17 = 3.6D;
        initChunkSeed(var14 + var5 << 2, var13 + var6 << 2);
        double var19 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        double var21 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        initChunkSeed(var14 + var5 + 1 << 2, var13 + var6 << 2);
        double var23 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double var25 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        initChunkSeed(var14 + var5 << 2, var13 + var6 + 1 << 2);
        double var27 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        double var29 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        initChunkSeed(var14 + var5 + 1 << 2, var13 + var6 + 1 << 2);
        double var31 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double var33 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        int var35 = var9[(var14 + 1 + (var13 + 0) * var7)] & 0xFF;
        int var36 = var9[(var14 + 1 + (var13 + 1) * var7)] & 0xFF;
        for (int var37 = 0; var37 < 4; var37++)
        {
          int var38 = ((var13 << 2) + var37) * var10 + (var14 << 2);
          for (int var39 = 0; var39 < 4; var39++)
          {
            double var40 = (var37 - var21) * (var37 - var21) + (var39 - var19) * (var39 - var19);
            double var42 = (var37 - var25) * (var37 - var25) + (var39 - var23) * (var39 - var23);
            double var44 = (var37 - var29) * (var37 - var29) + (var39 - var27) * (var39 - var27);
            double var46 = (var37 - var33) * (var37 - var33) + (var39 - var31) * (var39 - var31);
            if ((var40 < var42) && (var40 < var44) && (var40 < var46)) {
              var12[(var38++)] = var15;
            } else if ((var42 < var40) && (var42 < var44) && (var42 < var46)) {
              var12[(var38++)] = var35;
            } else if ((var44 < var40) && (var44 < var42) && (var44 < var46)) {
              var12[(var38++)] = var16;
            } else {
              var12[(var38++)] = var36;
            }
          }
        }
        var15 = var35;
        var16 = var36;
      }
    }
    int[] var48 = IntCache.getIntCache(areaWidth * areaHeight);
    for (int var14 = 0; var14 < areaHeight; var14++) {
      System.arraycopy(var12, (var14 + (areaY & 0x3)) * var10 + (areaX & 0x3), var48, var14 * areaWidth, areaWidth);
    }
    return var48;
  }
}
