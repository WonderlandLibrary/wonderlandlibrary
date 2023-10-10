package net.minecraft.world.gen.layer;

public class GenLayerAddIsland extends GenLayer {
   public GenLayerAddIsland(long var1, GenLayer var3) {
      super(var1);
      this.parent = var3;
   }

   public int[] getInts(int var1, int var2, int var3, int var4) {
      int var5 = var1 - 1;
      int var6 = var2 - 1;
      int var7 = var3 + 2;
      int var8 = var4 + 2;
      int[] var9 = this.parent.getInts(var5, var6, var7, var8);
      int[] var10 = IntCache.getIntCache(var3 * var4);

      for(int var11 = 0; var11 < var4; ++var11) {
         for(int var12 = 0; var12 < var3; ++var12) {
            int var13 = var9[var12 + 0 + (var11 + 0) * var7];
            int var14 = var9[var12 + 2 + (var11 + 0) * var7];
            int var15 = var9[var12 + 0 + (var11 + 2) * var7];
            int var16 = var9[var12 + 2 + (var11 + 2) * var7];
            int var17 = var9[var12 + 1 + (var11 + 1) * var7];
            this.initChunkSeed((long)(var12 + var1), (long)(var11 + var2));
            if (var17 != 0 || var13 == 0 && var14 == 0 && var15 == 0 && var16 == 0) {
               if (var17 > 0 && (var13 == 0 || var14 == 0 || var15 == 0 || var16 == 0)) {
                  if (this.nextInt(5) == 0) {
                     if (var17 == 4) {
                        var10[var12 + var11 * var3] = 4;
                     } else {
                        var10[var12 + var11 * var3] = 0;
                     }
                  } else {
                     var10[var12 + var11 * var3] = var17;
                  }
               } else {
                  var10[var12 + var11 * var3] = var17;
               }
            } else {
               int var18 = 1;
               int var19 = 1;
               if (var13 != 0 && this.nextInt(var18++) == 0) {
                  var19 = var13;
               }

               if (var14 != 0 && this.nextInt(var18++) == 0) {
                  var19 = var14;
               }

               if (var15 != 0 && this.nextInt(var18++) == 0) {
                  var19 = var15;
               }

               if (var16 != 0 && this.nextInt(var18++) == 0) {
                  var19 = var16;
               }

               if (this.nextInt(3) == 0) {
                  var10[var12 + var11 * var3] = var19;
               } else if (var19 == 4) {
                  var10[var12 + var11 * var3] = 4;
               } else {
                  var10[var12 + var11 * var3] = 0;
               }
            }
         }
      }

      return var10;
   }
}
