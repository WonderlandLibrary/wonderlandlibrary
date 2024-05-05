package net.minecraft.world.gen;

import java.util.Random;

public class NoiseGeneratorImproved
  extends NoiseGenerator
{
  private int[] permutations;
  public double xCoord;
  public double yCoord;
  public double zCoord;
  private static final double[] field_152381_e = { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
  private static final double[] field_152382_f = { 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D };
  private static final double[] field_152383_g = { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
  private static final double[] field_152384_h = { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
  private static final double[] field_152385_i = { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
  private static final String __OBFID = "CL_00000534";
  
  public NoiseGeneratorImproved()
  {
    this(new Random());
  }
  
  public NoiseGeneratorImproved(Random p_i45469_1_)
  {
    this.permutations = new int['Ȁ'];
    this.xCoord = (p_i45469_1_.nextDouble() * 256.0D);
    this.yCoord = (p_i45469_1_.nextDouble() * 256.0D);
    this.zCoord = (p_i45469_1_.nextDouble() * 256.0D);
    for (int var2 = 0; var2 < 256; this.permutations[var2] = (var2++)) {}
    for (var2 = 0; var2 < 256; var2++)
    {
      int var3 = p_i45469_1_.nextInt(256 - var2) + var2;
      int var4 = this.permutations[var2];
      this.permutations[var2] = this.permutations[var3];
      this.permutations[var3] = var4;
      this.permutations[(var2 + 256)] = this.permutations[var2];
    }
  }
  
  public final double lerp(double p_76311_1_, double p_76311_3_, double p_76311_5_)
  {
    return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
  }
  
  public final double func_76309_a(int p_76309_1_, double p_76309_2_, double p_76309_4_)
  {
    int var6 = p_76309_1_ & 0xF;
    return field_152384_h[var6] * p_76309_2_ + field_152385_i[var6] * p_76309_4_;
  }
  
  public final double grad(int p_76310_1_, double p_76310_2_, double p_76310_4_, double p_76310_6_)
  {
    int var8 = p_76310_1_ & 0xF;
    return field_152381_e[var8] * p_76310_2_ + field_152382_f[var8] * p_76310_4_ + field_152383_g[var8] * p_76310_6_;
  }
  
  public void populateNoiseArray(double[] p_76308_1_, double p_76308_2_, double p_76308_4_, double p_76308_6_, int p_76308_8_, int p_76308_9_, int p_76308_10_, double p_76308_11_, double p_76308_13_, double p_76308_15_, double p_76308_17_)
  {
    if (p_76308_9_ == 1)
    {
      boolean var64 = false;
      boolean var65 = false;
      boolean var21 = false;
      boolean var68 = false;
      double var70 = 0.0D;
      double var73 = 0.0D;
      int var75 = 0;
      double var77 = 1.0D / p_76308_17_;
      for (int var30 = 0; var30 < p_76308_8_; var30++)
      {
        double var31 = p_76308_2_ + var30 * p_76308_11_ + this.xCoord;
        int var78 = (int)var31;
        if (var31 < var78) {
          var78--;
        }
        int var34 = var78 & 0xFF;
        var31 -= var78;
        double var35 = var31 * var31 * var31 * (var31 * (var31 * 6.0D - 15.0D) + 10.0D);
        for (int var37 = 0; var37 < p_76308_10_; var37++)
        {
          double var38 = p_76308_6_ + var37 * p_76308_15_ + this.zCoord;
          int var40 = (int)var38;
          if (var38 < var40) {
            var40--;
          }
          int var41 = var40 & 0xFF;
          var38 -= var40;
          double var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);
          int var19 = this.permutations[var34] + 0;
          int var66 = this.permutations[var19] + var41;
          int var67 = this.permutations[(var34 + 1)] + 0;
          int var22 = this.permutations[var67] + var41;
          var70 = lerp(var35, func_76309_a(this.permutations[var66], var31, var38), grad(this.permutations[var22], var31 - 1.0D, 0.0D, var38));
          var73 = lerp(var35, grad(this.permutations[(var66 + 1)], var31, 0.0D, var38 - 1.0D), grad(this.permutations[(var22 + 1)], var31 - 1.0D, 0.0D, var38 - 1.0D));
          double var79 = lerp(var42, var70, var73);
          int var10001 = var75++;
          p_76308_1_[var10001] += var79 * var77;
        }
      }
    }
    else
    {
      int var19 = 0;
      double var20 = 1.0D / p_76308_17_;
      int var22 = -1;
      boolean var23 = false;
      boolean var24 = false;
      boolean var25 = false;
      boolean var26 = false;
      boolean var27 = false;
      boolean var28 = false;
      double var29 = 0.0D;
      double var31 = 0.0D;
      double var33 = 0.0D;
      double var35 = 0.0D;
      for (int var37 = 0; var37 < p_76308_8_; var37++)
      {
        double var38 = p_76308_2_ + var37 * p_76308_11_ + this.xCoord;
        int var40 = (int)var38;
        if (var38 < var40) {
          var40--;
        }
        int var41 = var40 & 0xFF;
        var38 -= var40;
        double var42 = var38 * var38 * var38 * (var38 * (var38 * 6.0D - 15.0D) + 10.0D);
        for (int var44 = 0; var44 < p_76308_10_; var44++)
        {
          double var45 = p_76308_6_ + var44 * p_76308_15_ + this.zCoord;
          int var47 = (int)var45;
          if (var45 < var47) {
            var47--;
          }
          int var48 = var47 & 0xFF;
          var45 -= var47;
          double var49 = var45 * var45 * var45 * (var45 * (var45 * 6.0D - 15.0D) + 10.0D);
          for (int var51 = 0; var51 < p_76308_9_; var51++)
          {
            double var52 = p_76308_4_ + var51 * p_76308_13_ + this.yCoord;
            int var54 = (int)var52;
            if (var52 < var54) {
              var54--;
            }
            int var55 = var54 & 0xFF;
            var52 -= var54;
            double var56 = var52 * var52 * var52 * (var52 * (var52 * 6.0D - 15.0D) + 10.0D);
            if ((var51 == 0) || (var55 != var22))
            {
              var22 = var55;
              int var69 = this.permutations[var41] + var55;
              int var71 = this.permutations[var69] + var48;
              int var72 = this.permutations[(var69 + 1)] + var48;
              int var74 = this.permutations[(var41 + 1)] + var55;
              int var75 = this.permutations[var74] + var48;
              int var76 = this.permutations[(var74 + 1)] + var48;
              var29 = lerp(var42, grad(this.permutations[var71], var38, var52, var45), grad(this.permutations[var75], var38 - 1.0D, var52, var45));
              var31 = lerp(var42, grad(this.permutations[var72], var38, var52 - 1.0D, var45), grad(this.permutations[var76], var38 - 1.0D, var52 - 1.0D, var45));
              var33 = lerp(var42, grad(this.permutations[(var71 + 1)], var38, var52, var45 - 1.0D), grad(this.permutations[(var75 + 1)], var38 - 1.0D, var52, var45 - 1.0D));
              var35 = lerp(var42, grad(this.permutations[(var72 + 1)], var38, var52 - 1.0D, var45 - 1.0D), grad(this.permutations[(var76 + 1)], var38 - 1.0D, var52 - 1.0D, var45 - 1.0D));
            }
            double var58 = lerp(var56, var29, var31);
            double var60 = lerp(var56, var33, var35);
            double var62 = lerp(var49, var58, var60);
            int var10001 = var19++;
            p_76308_1_[var10001] += var62 * var20;
          }
        }
      }
    }
  }
}
