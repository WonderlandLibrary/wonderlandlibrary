package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex {
   public double field_151610_d;
   private int[] field_151608_f;
   public double field_151613_c;
   public static final double field_151614_a = Math.sqrt(3.0D);
   private static final double field_151609_g;
   private static int[][] field_151611_e = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
   public double field_151612_b;
   private static final double field_151615_h;

   public double func_151605_a(double var1, double var3) {
      double var5 = 0.5D * (field_151614_a - 1.0D);
      double var7 = (var1 + var3) * var5;
      int var9 = func_151607_a(var1 + var7);
      int var10 = func_151607_a(var3 + var7);
      double var11 = (3.0D - field_151614_a) / 6.0D;
      double var13 = (double)(var9 + var10) * var11;
      double var15 = (double)var9 - var13;
      double var17 = (double)var10 - var13;
      double var19 = var1 - var15;
      double var21 = var3 - var17;
      byte var23;
      byte var24;
      if (var19 > var21) {
         var23 = 1;
         var24 = 0;
      } else {
         var23 = 0;
         var24 = 1;
      }

      double var25 = var19 - (double)var23 + var11;
      double var27 = var21 - (double)var24 + var11;
      double var29 = var19 - 1.0D + 2.0D * var11;
      double var31 = var21 - 1.0D + 2.0D * var11;
      int var33 = var9 & 255;
      int var34 = var10 & 255;
      int var35 = this.field_151608_f[var33 + this.field_151608_f[var34]] % 12;
      int var36 = this.field_151608_f[var33 + var23 + this.field_151608_f[var34 + var24]] % 12;
      int var37 = this.field_151608_f[var33 + 1 + this.field_151608_f[var34 + 1]] % 12;
      double var38 = 0.5D - var19 * var19 - var21 * var21;
      double var40;
      if (var38 < 0.0D) {
         var40 = 0.0D;
      } else {
         var38 *= var38;
         var40 = var38 * var38 * func_151604_a(field_151611_e[var35], var19, var21);
      }

      double var42 = 0.5D - var25 * var25 - var27 * var27;
      double var44;
      if (var42 < 0.0D) {
         var44 = 0.0D;
      } else {
         var42 *= var42;
         var44 = var42 * var42 * func_151604_a(field_151611_e[var36], var25, var27);
      }

      double var46 = 0.5D - var29 * var29 - var31 * var31;
      double var48;
      if (var46 < 0.0D) {
         var48 = 0.0D;
      } else {
         var46 *= var46;
         var48 = var46 * var46 * func_151604_a(field_151611_e[var37], var29, var31);
      }

      return 70.0D * (var40 + var44 + var48);
   }

   public void func_151606_a(double[] var1, double var2, double var4, int var6, int var7, double var8, double var10, double var12) {
      int var14 = 0;

      for(int var15 = 0; var15 < var7; ++var15) {
         double var16 = (var4 + (double)var15) * var10 + this.field_151613_c;

         for(int var18 = 0; var18 < var6; ++var18) {
            double var19 = (var2 + (double)var18) * var8 + this.field_151612_b;
            double var21 = (var19 + var16) * field_151609_g;
            int var23 = func_151607_a(var19 + var21);
            int var24 = func_151607_a(var16 + var21);
            double var25 = (double)(var23 + var24) * field_151615_h;
            double var27 = (double)var23 - var25;
            double var29 = (double)var24 - var25;
            double var31 = var19 - var27;
            double var33 = var16 - var29;
            byte var35;
            byte var36;
            if (var31 > var33) {
               var35 = 1;
               var36 = 0;
            } else {
               var35 = 0;
               var36 = 1;
            }

            double var37 = var31 - (double)var35 + field_151615_h;
            double var39 = var33 - (double)var36 + field_151615_h;
            double var41 = var31 - 1.0D + 2.0D * field_151615_h;
            double var43 = var33 - 1.0D + 2.0D * field_151615_h;
            int var45 = var23 & 255;
            int var46 = var24 & 255;
            int var47 = this.field_151608_f[var45 + this.field_151608_f[var46]] % 12;
            int var48 = this.field_151608_f[var45 + var35 + this.field_151608_f[var46 + var36]] % 12;
            int var49 = this.field_151608_f[var45 + 1 + this.field_151608_f[var46 + 1]] % 12;
            double var50 = 0.5D - var31 * var31 - var33 * var33;
            double var52;
            if (var50 < 0.0D) {
               var52 = 0.0D;
            } else {
               var50 *= var50;
               var52 = var50 * var50 * func_151604_a(field_151611_e[var47], var31, var33);
            }

            double var54 = 0.5D - var37 * var37 - var39 * var39;
            double var56;
            if (var54 < 0.0D) {
               var56 = 0.0D;
            } else {
               var54 *= var54;
               var56 = var54 * var54 * func_151604_a(field_151611_e[var48], var37, var39);
            }

            double var58 = 0.5D - var41 * var41 - var43 * var43;
            double var60;
            if (var58 < 0.0D) {
               var60 = 0.0D;
            } else {
               var58 *= var58;
               var60 = var58 * var58 * func_151604_a(field_151611_e[var49], var41, var43);
            }

            int var62 = var14++;
            var1[var62] += 70.0D * (var52 + var56 + var60) * var12;
         }
      }

   }

   private static double func_151604_a(int[] var0, double var1, double var3) {
      return (double)var0[0] * var1 + (double)var0[1] * var3;
   }

   static {
      field_151609_g = 0.5D * (field_151614_a - 1.0D);
      field_151615_h = (3.0D - field_151614_a) / 6.0D;
   }

   private static int func_151607_a(double var0) {
      return var0 > 0.0D ? (int)var0 : (int)var0 - 1;
   }

   public NoiseGeneratorSimplex(Random var1) {
      this.field_151608_f = new int[512];
      this.field_151612_b = var1.nextDouble() * 256.0D;
      this.field_151613_c = var1.nextDouble() * 256.0D;
      this.field_151610_d = var1.nextDouble() * 256.0D;

      int var2;
      for(var2 = 0; var2 < 256; this.field_151608_f[var2] = var2++) {
      }

      for(var2 = 0; var2 < 256; ++var2) {
         int var3 = var1.nextInt(256 - var2) + var2;
         int var4 = this.field_151608_f[var2];
         this.field_151608_f[var2] = this.field_151608_f[var3];
         this.field_151608_f[var3] = var4;
         this.field_151608_f[var2 + 256] = this.field_151608_f[var2];
      }

   }

   public NoiseGeneratorSimplex() {
      this(new Random());
   }
}
