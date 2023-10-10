package net.minecraft.world.gen.layer;

public class GenLayerZoom extends GenLayer {
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int var5 = var1 >> 1;
      int var6 = var2 >> 1;
      int var7 = (var3 >> 1) + 2;
      int var8 = (var4 >> 1) + 2;
      int[] var9 = this.parent.getInts(var5, var6, var7, var8);
      int var10 = var7 - 1 << 1;
      int var11 = var8 - 1 << 1;
      int[] var12 = IntCache.getIntCache(var10 * var11);

      int var14;
      for(int var13 = 0; var13 < var8 - 1; ++var13) {
         var14 = (var13 << 1) * var10;
         int var15 = 0;
         int var16 = var9[var15 + 0 + (var13 + 0) * var7];

         for(int var17 = var9[var15 + 0 + (var13 + 1) * var7]; var15 < var7 - 1; ++var15) {
            this.initChunkSeed((long)(var15 + var5 << 1), (long)(var13 + var6 << 1));
            int var18 = var9[var15 + 1 + (var13 + 0) * var7];
            int var19 = var9[var15 + 1 + (var13 + 1) * var7];
            var12[var14] = var16;
            var12[var14++ + var10] = this.selectRandom(new int[]{var16, var17});
            var12[var14] = this.selectRandom(new int[]{var16, var18});
            var12[var14++ + var10] = this.selectModeOrRandom(var16, var18, var17, var19);
            var16 = var18;
            var17 = var19;
         }
      }

      int[] var20 = IntCache.getIntCache(var3 * var4);

      for(var14 = 0; var14 < var4; ++var14) {
         System.arraycopy(var12, (var14 + (var2 & 1)) * var10 + (var1 & 1), var20, var14 * var3, var3);
      }

      return var20;
   }

   public static GenLayer magnify(long var0, GenLayer var2, int var3) {
      Object var4 = var2;

      for(int var5 = 0; var5 < var3; ++var5) {
         var4 = new GenLayerZoom(var0 + (long)var5, (GenLayer)var4);
      }

      return (GenLayer)var4;
   }

   public GenLayerZoom(long var1, GenLayer var3) {
      super(var1);
      super.parent = var3;
   }
}
