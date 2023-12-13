package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorImproved extends NoiseGenerator {
   private static final double[] field_152383_g = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D};
   private int[] permutations;
   private static final double[] field_152384_h = new double[]{1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D};
   public double zCoord;
   public double yCoord;
   private static final double[] field_152385_i = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D};
   private static final double[] field_152381_e = new double[]{1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D};
   public double xCoord;
   private static final double[] field_152382_f = new double[]{1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D};

   public void populateNoiseArray(double[] var1, double var2, double var4, double var6, int var8, int var9, int var10, double var11, double var13, double var15, double var17) {
      int var27;
      double var31;
      double var35;
      int var37;
      double var38;
      int var40;
      int var41;
      double var42;
      int var65;
      int var69;
      if (var9 == 1) {
         boolean var19 = false;
         boolean var20 = false;
         boolean var21 = false;
         boolean var22 = false;
         double var23 = 0.0D;
         double var25 = 0.0D;
         var27 = 0;
         double var28 = 1.0D / var17;

         for(int var30 = 0; var30 < var8; ++var30) {
            var31 = var2 + (double)var30 * var11 + this.xCoord;
            int var33 = (int)var31;
            if (var31 < (double)var33) {
               --var33;
            }

            int var34 = var33 & 255;
            var31 -= (double)var33;
            var35 = var31 * var31 * var31 * (var31 * (var31 * 6.0D - 15.0D) + 10.0D);

            for(var37 = 0; var37 < var10; ++var37) {
               var38 = var6 + (double)var37 * var15 + this.zCoord;
               var40 = (int)var38;
               if (var38 < (double)var40) {
                  --var40;
               }

               var41 = var40 & 255;
               var38 -= (double)var40;
               var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);
               var65 = this.permutations[var34] + 0;
               int var66 = this.permutations[var65] + var41;
               int var68 = this.permutations[var34 + 1] + 0;
               var69 = this.permutations[var68] + var41;
               var23 = this.lerp(var35, this.func_76309_a(this.permutations[var66], var31, var38), this.grad(this.permutations[var69], var31 - 1.0D, 0.0D, var38));
               var25 = this.lerp(var35, this.grad(this.permutations[var66 + 1], var31, 0.0D, var38 - 1.0D), this.grad(this.permutations[var69 + 1], var31 - 1.0D, 0.0D, var38 - 1.0D));
               double var44 = this.lerp(var42, var23, var25);
               int var46 = var27++;
               var1[var46] += var44 * var28;
            }
         }
      } else {
         var65 = 0;
         double var67 = 1.0D / var17;
         var69 = -1;
         boolean var70 = false;
         boolean var24 = false;
         boolean var73 = false;
         boolean var26 = false;
         boolean var76 = false;
         boolean var77 = false;
         double var29 = 0.0D;
         var31 = 0.0D;
         double var79 = 0.0D;
         var35 = 0.0D;

         for(var37 = 0; var37 < var8; ++var37) {
            var38 = var2 + (double)var37 * var11 + this.xCoord;
            var40 = (int)var38;
            if (var38 < (double)var40) {
               --var40;
            }

            var41 = var40 & 255;
            var38 -= (double)var40;
            var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);

            for(int var80 = 0; var80 < var10; ++var80) {
               double var45 = var6 + (double)var80 * var15 + this.zCoord;
               int var47 = (int)var45;
               if (var45 < (double)var47) {
                  --var47;
               }

               int var48 = var47 & 255;
               var45 -= (double)var47;
               double var49 = var45 * var45 * var45 * (var45 * (var45 * 6.0D - 15.0D) + 10.0D);

               for(int var51 = 0; var51 < var9; ++var51) {
                  double var52 = var4 + (double)var51 * var13 + this.yCoord;
                  int var54 = (int)var52;
                  if (var52 < (double)var54) {
                     --var54;
                  }

                  int var55 = var54 & 255;
                  var52 -= (double)var54;
                  double var56 = var52 * var52 * var52 * (var52 * (var52 * 6.0D - 15.0D) + 10.0D);
                  if (var51 == 0 || var55 != var69) {
                     var69 = var55;
                     int var71 = this.permutations[var41] + var55;
                     int var72 = this.permutations[var71] + var48;
                     int var74 = this.permutations[var71 + 1] + var48;
                     int var75 = this.permutations[var41 + 1] + var55;
                     var27 = this.permutations[var75] + var48;
                     int var78 = this.permutations[var75 + 1] + var48;
                     var29 = this.lerp(var42, this.grad(this.permutations[var72], var38, var52, var45), this.grad(this.permutations[var27], var38 - 1.0D, var52, var45));
                     var31 = this.lerp(var42, this.grad(this.permutations[var74], var38, var52 - 1.0D, var45), this.grad(this.permutations[var78], var38 - 1.0D, var52 - 1.0D, var45));
                     var79 = this.lerp(var42, this.grad(this.permutations[var72 + 1], var38, var52, var45 - 1.0D), this.grad(this.permutations[var27 + 1], var38 - 1.0D, var52, var45 - 1.0D));
                     var35 = this.lerp(var42, this.grad(this.permutations[var74 + 1], var38, var52 - 1.0D, var45 - 1.0D), this.grad(this.permutations[var78 + 1], var38 - 1.0D, var52 - 1.0D, var45 - 1.0D));
                  }

                  double var58 = this.lerp(var56, var29, var31);
                  double var60 = this.lerp(var56, var79, var35);
                  double var62 = this.lerp(var49, var58, var60);
                  int var64 = var65++;
                  var1[var64] += var62 * var67;
               }
            }
         }
      }

   }

   public final double func_76309_a(int var1, double var2, double var4) {
      int var6 = var1 & 15;
      return field_152384_h[var6] * var2 + field_152385_i[var6] * var4;
   }

   public NoiseGeneratorImproved(Random var1) {
      this.permutations = new int[512];
      this.xCoord = var1.nextDouble() * 256.0D;
      this.yCoord = var1.nextDouble() * 256.0D;
      this.zCoord = var1.nextDouble() * 256.0D;

      int var2;
      for(var2 = 0; var2 < 256; this.permutations[var2] = var2++) {
      }

      for(var2 = 0; var2 < 256; ++var2) {
         int var3 = var1.nextInt(256 - var2) + var2;
         int var4 = this.permutations[var2];
         this.permutations[var2] = this.permutations[var3];
         this.permutations[var3] = var4;
         this.permutations[var2 + 256] = this.permutations[var2];
      }

   }

   public NoiseGeneratorImproved() {
      this(new Random());
   }

   public final double lerp(double var1, double var3, double var5) {
      return var3 + var1 * (var5 - var3);
   }

   public final double grad(int var1, double var2, double var4, double var6) {
      int var8 = var1 & 15;
      return field_152381_e[var8] * var2 + field_152382_f[var8] * var4 + field_152383_g[var8] * var6;
   }
}
