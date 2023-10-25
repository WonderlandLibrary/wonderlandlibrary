package net.minecraft.world.gen.layer;

public class GenLayerVoronoiZoom extends GenLayer {
  public GenLayerVoronoiZoom(long p_i2133_1_, GenLayer p_i2133_3_) {
    super(p_i2133_1_);
    this.parent = p_i2133_3_;
  }
  
  public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
    areaX -= 2;
    areaY -= 2;
    int i = areaX >> 2;
    int j = areaY >> 2;
    int k = (areaWidth >> 2) + 2;
    int l = (areaHeight >> 2) + 2;
    int[] aint = this.parent.getInts(i, j, k, l);
    int i1 = k - 1 << 2;
    int j1 = l - 1 << 2;
    int[] aint1 = IntCache.getIntCache(i1 * j1);
    for (int k1 = 0; k1 < l - 1; k1++) {
      int l1 = 0;
      int i2 = aint[l1 + 0 + (k1 + 0) * k];
      for (int j2 = aint[l1 + 0 + (k1 + 1) * k]; l1 < k - 1; l1++) {
        double d0 = 3.6D;
        initChunkSeed((l1 + i << 2), (k1 + j << 2));
        double d1 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        double d2 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        initChunkSeed((l1 + i + 1 << 2), (k1 + j << 2));
        double d3 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double d4 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        initChunkSeed((l1 + i << 2), (k1 + j + 1 << 2));
        double d5 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
        double d6 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        initChunkSeed((l1 + i + 1 << 2), (k1 + j + 1 << 2));
        double d7 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double d8 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        int k2 = aint[l1 + 1 + (k1 + 0) * k] & 0xFF;
        int l2 = aint[l1 + 1 + (k1 + 1) * k] & 0xFF;
        for (int i3 = 0; i3 < 4; i3++) {
          int j3 = ((k1 << 2) + i3) * i1 + (l1 << 2);
          for (int k3 = 0; k3 < 4; k3++) {
            double d9 = (i3 - d2) * (i3 - d2) + (k3 - d1) * (k3 - d1);
            double d10 = (i3 - d4) * (i3 - d4) + (k3 - d3) * (k3 - d3);
            double d11 = (i3 - d6) * (i3 - d6) + (k3 - d5) * (k3 - d5);
            double d12 = (i3 - d8) * (i3 - d8) + (k3 - d7) * (k3 - d7);
            if (d9 < d10 && d9 < d11 && d9 < d12) {
              aint1[j3++] = i2;
            } else if (d10 < d9 && d10 < d11 && d10 < d12) {
              aint1[j3++] = k2;
            } else if (d11 < d9 && d11 < d10 && d11 < d12) {
              aint1[j3++] = j2;
            } else {
              aint1[j3++] = l2;
            } 
          } 
        } 
        i2 = k2;
        j2 = l2;
      } 
    } 
    int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
    for (int l3 = 0; l3 < areaHeight; l3++)
      System.arraycopy(aint1, (l3 + (areaY & 0x3)) * i1 + (areaX & 0x3), aint2, l3 * areaWidth, areaWidth); 
    return aint2;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\gen\layer\GenLayerVoronoiZoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */