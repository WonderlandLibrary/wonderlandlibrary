package net.minecraft.world.gen.layer;

public class GenLayerSmooth extends GenLayer {
  public GenLayerSmooth(long p_i2131_1_, GenLayer p_i2131_3_) {
    super(p_i2131_1_);
    this.parent = p_i2131_3_;
  }
  
  public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
    int i = areaX - 1;
    int j = areaY - 1;
    int k = areaWidth + 2;
    int l = areaHeight + 2;
    int[] aint = this.parent.getInts(i, j, k, l);
    int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
    for (int i1 = 0; i1 < areaHeight; i1++) {
      for (int j1 = 0; j1 < areaWidth; j1++) {
        int k1 = aint[j1 + 0 + (i1 + 1) * k];
        int l1 = aint[j1 + 2 + (i1 + 1) * k];
        int i2 = aint[j1 + 1 + (i1 + 0) * k];
        int j2 = aint[j1 + 1 + (i1 + 2) * k];
        int k2 = aint[j1 + 1 + (i1 + 1) * k];
        if (k1 == l1 && i2 == j2) {
          initChunkSeed((j1 + areaX), (i1 + areaY));
          if (nextInt(2) == 0) {
            k2 = k1;
          } else {
            k2 = i2;
          } 
        } else {
          if (k1 == l1)
            k2 = k1; 
          if (i2 == j2)
            k2 = i2; 
        } 
        aint1[j1 + i1 * areaWidth] = k2;
      } 
    } 
    return aint1;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\gen\layer\GenLayerSmooth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */