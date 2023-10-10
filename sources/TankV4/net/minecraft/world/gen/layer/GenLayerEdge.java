package net.minecraft.world.gen.layer;

public class GenLayerEdge extends GenLayer {
   private final GenLayerEdge.Mode field_151627_c;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$world$gen$layer$GenLayerEdge$Mode;

   private int[] getIntsCoolWarm(int var1, int var2, int var3, int var4) {
      int var5 = var1 - 1;
      int var6 = var2 - 1;
      int var7 = 1 + var3 + 1;
      int var8 = 1 + var4 + 1;
      int[] var9 = this.parent.getInts(var5, var6, var7, var8);
      int[] var10 = IntCache.getIntCache(var3 * var4);

      for(int var11 = 0; var11 < var4; ++var11) {
         for(int var12 = 0; var12 < var3; ++var12) {
            this.initChunkSeed((long)(var12 + var1), (long)(var11 + var2));
            int var13 = var9[var12 + 1 + (var11 + 1) * var7];
            if (var13 == 1) {
               int var14 = var9[var12 + 1 + (var11 + 1 - 1) * var7];
               int var15 = var9[var12 + 1 + 1 + (var11 + 1) * var7];
               int var16 = var9[var12 + 1 - 1 + (var11 + 1) * var7];
               int var17 = var9[var12 + 1 + (var11 + 1 + 1) * var7];
               boolean var18 = var14 == 3 || var15 == 3 || var16 == 3 || var17 == 3;
               boolean var19 = var14 == 4 || var15 == 4 || var16 == 4 || var17 == 4;
               if (var18 || var19) {
                  var13 = 2;
               }
            }

            var10[var12 + var11 * var3] = var13;
         }
      }

      return var10;
   }

   static int[] $SWITCH_TABLE$net$minecraft$world$gen$layer$GenLayerEdge$Mode() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$world$gen$layer$GenLayerEdge$Mode;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[GenLayerEdge.Mode.values().length];

         try {
            var0[GenLayerEdge.Mode.COOL_WARM.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[GenLayerEdge.Mode.HEAT_ICE.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[GenLayerEdge.Mode.SPECIAL.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$world$gen$layer$GenLayerEdge$Mode = var0;
         return var0;
      }
   }

   public GenLayerEdge(long var1, GenLayer var3, GenLayerEdge.Mode var4) {
      super(var1);
      this.parent = var3;
      this.field_151627_c = var4;
   }

   private int[] getIntsHeatIce(int var1, int var2, int var3, int var4) {
      int var5 = var1 - 1;
      int var6 = var2 - 1;
      int var7 = 1 + var3 + 1;
      int var8 = 1 + var4 + 1;
      int[] var9 = this.parent.getInts(var5, var6, var7, var8);
      int[] var10 = IntCache.getIntCache(var3 * var4);

      for(int var11 = 0; var11 < var4; ++var11) {
         for(int var12 = 0; var12 < var3; ++var12) {
            int var13 = var9[var12 + 1 + (var11 + 1) * var7];
            if (var13 == 4) {
               int var14 = var9[var12 + 1 + (var11 + 1 - 1) * var7];
               int var15 = var9[var12 + 1 + 1 + (var11 + 1) * var7];
               int var16 = var9[var12 + 1 - 1 + (var11 + 1) * var7];
               int var17 = var9[var12 + 1 + (var11 + 1 + 1) * var7];
               boolean var18 = var14 == 2 || var15 == 2 || var16 == 2 || var17 == 2;
               boolean var19 = var14 == 1 || var15 == 1 || var16 == 1 || var17 == 1;
               if (var19 || var18) {
                  var13 = 3;
               }
            }

            var10[var12 + var11 * var3] = var13;
         }
      }

      return var10;
   }

   public int[] getInts(int var1, int var2, int var3, int var4) {
      switch($SWITCH_TABLE$net$minecraft$world$gen$layer$GenLayerEdge$Mode()[this.field_151627_c.ordinal()]) {
      case 1:
      default:
         return this.getIntsCoolWarm(var1, var2, var3, var4);
      case 2:
         return this.getIntsHeatIce(var1, var2, var3, var4);
      case 3:
         return this.getIntsSpecial(var1, var2, var3, var4);
      }
   }

   private int[] getIntsSpecial(int var1, int var2, int var3, int var4) {
      int[] var5 = this.parent.getInts(var1, var2, var3, var4);
      int[] var6 = IntCache.getIntCache(var3 * var4);

      for(int var7 = 0; var7 < var4; ++var7) {
         for(int var8 = 0; var8 < var3; ++var8) {
            this.initChunkSeed((long)(var8 + var1), (long)(var7 + var2));
            int var9 = var5[var8 + var7 * var3];
            if (var9 != 0 && this.nextInt(13) == 0) {
               var9 |= 1 + this.nextInt(15) << 8 & 3840;
            }

            var6[var8 + var7 * var3] = var9;
         }
      }

      return var6;
   }

   public static enum Mode {
      HEAT_ICE,
      COOL_WARM;

      private static final GenLayerEdge.Mode[] ENUM$VALUES = new GenLayerEdge.Mode[]{COOL_WARM, HEAT_ICE, SPECIAL};
      SPECIAL;
   }
}
