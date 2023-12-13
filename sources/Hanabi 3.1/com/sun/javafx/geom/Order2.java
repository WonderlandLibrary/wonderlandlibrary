package com.sun.javafx.geom;

import java.util.Vector;

final class Order2 extends Curve {
   private double x0;
   private double y0;
   private double cx0;
   private double cy0;
   private double x1;
   private double y1;
   private double xmin;
   private double xmax;
   private double xcoeff0;
   private double xcoeff1;
   private double xcoeff2;
   private double ycoeff0;
   private double ycoeff1;
   private double ycoeff2;

   public static void insert(Vector var0, double[] var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      int var15 = getHorizontalParams(var4, var8, var12, var1);
      if (var15 == 0) {
         addInstance(var0, var2, var4, var6, var8, var10, var12, var14);
      } else {
         double var16 = var1[0];
         var1[0] = var2;
         var1[1] = var4;
         var1[2] = var6;
         var1[3] = var8;
         var1[4] = var10;
         var1[5] = var12;
         split(var1, 0, var16);
         int var18 = var14 == 1 ? 0 : 4;
         int var19 = 4 - var18;
         addInstance(var0, var1[var18], var1[var18 + 1], var1[var18 + 2], var1[var18 + 3], var1[var18 + 4], var1[var18 + 5], var14);
         addInstance(var0, var1[var19], var1[var19 + 1], var1[var19 + 2], var1[var19 + 3], var1[var19 + 4], var1[var19 + 5], var14);
      }
   }

   public static void addInstance(Vector var0, double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      if (var3 > var11) {
         var0.add(new Order2(var9, var11, var5, var7, var1, var3, -var13));
      } else if (var11 > var3) {
         var0.add(new Order2(var1, var3, var5, var7, var9, var11, var13));
      }

   }

   public static int getHorizontalParams(double var0, double var2, double var4, double[] var6) {
      if (var0 <= var2 && var2 <= var4) {
         return 0;
      } else {
         var0 -= var2;
         var4 -= var2;
         double var7 = var0 + var4;
         if (var7 == 0.0) {
            return 0;
         } else {
            double var9 = var0 / var7;
            if (!(var9 <= 0.0) && !(var9 >= 1.0)) {
               var6[0] = var9;
               return 1;
            } else {
               return 0;
            }
         }
      }
   }

   public static void split(double[] var0, int var1, double var2) {
      double var12;
      var0[var1 + 8] = var12 = var0[var1 + 4];
      double var14;
      var0[var1 + 9] = var14 = var0[var1 + 5];
      double var8 = var0[var1 + 2];
      double var10 = var0[var1 + 3];
      var12 = var8 + (var12 - var8) * var2;
      var14 = var10 + (var14 - var10) * var2;
      double var4 = var0[var1 + 0];
      double var6 = var0[var1 + 1];
      var4 += (var8 - var4) * var2;
      var6 += (var10 - var6) * var2;
      var8 = var4 + (var12 - var4) * var2;
      var10 = var6 + (var14 - var6) * var2;
      var0[var1 + 2] = var4;
      var0[var1 + 3] = var6;
      var0[var1 + 4] = var8;
      var0[var1 + 5] = var10;
      var0[var1 + 6] = var12;
      var0[var1 + 7] = var14;
   }

   public Order2(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      super(var13);
      if (var7 < var3) {
         var7 = var3;
      } else if (var7 > var11) {
         var7 = var11;
      }

      this.x0 = var1;
      this.y0 = var3;
      this.cx0 = var5;
      this.cy0 = var7;
      this.x1 = var9;
      this.y1 = var11;
      this.xmin = Math.min(Math.min(var1, var9), var5);
      this.xmax = Math.max(Math.max(var1, var9), var5);
      this.xcoeff0 = var1;
      this.xcoeff1 = var5 + var5 - var1 - var1;
      this.xcoeff2 = var1 - var5 - var5 + var9;
      this.ycoeff0 = var3;
      this.ycoeff1 = var7 + var7 - var3 - var3;
      this.ycoeff2 = var3 - var7 - var7 + var11;
   }

   public int getOrder() {
      return 2;
   }

   public double getXTop() {
      return this.x0;
   }

   public double getYTop() {
      return this.y0;
   }

   public double getXBot() {
      return this.x1;
   }

   public double getYBot() {
      return this.y1;
   }

   public double getXMin() {
      return this.xmin;
   }

   public double getXMax() {
      return this.xmax;
   }

   public double getX0() {
      return this.direction == 1 ? this.x0 : this.x1;
   }

   public double getY0() {
      return this.direction == 1 ? this.y0 : this.y1;
   }

   public double getCX0() {
      return this.cx0;
   }

   public double getCY0() {
      return this.cy0;
   }

   public double getX1() {
      return this.direction == -1 ? this.x0 : this.x1;
   }

   public double getY1() {
      return this.direction == -1 ? this.y0 : this.y1;
   }

   public double XforY(double var1) {
      if (var1 <= this.y0) {
         return this.x0;
      } else {
         return var1 >= this.y1 ? this.x1 : this.XforT(this.TforY(var1));
      }
   }

   public double TforY(double var1) {
      if (var1 <= this.y0) {
         return 0.0;
      } else {
         return var1 >= this.y1 ? 1.0 : TforY(var1, this.ycoeff0, this.ycoeff1, this.ycoeff2);
      }
   }

   public static double TforY(double var0, double var2, double var4, double var6) {
      var2 -= var0;
      double var8;
      double var10;
      if (var6 == 0.0) {
         var8 = -var2 / var4;
         if (var8 >= 0.0 && var8 <= 1.0) {
            return var8;
         }
      } else {
         var8 = var4 * var4 - 4.0 * var6 * var2;
         if (var8 >= 0.0) {
            var8 = Math.sqrt(var8);
            if (var4 < 0.0) {
               var8 = -var8;
            }

            var10 = (var4 + var8) / -2.0;
            double var12 = var10 / var6;
            if (var12 >= 0.0 && var12 <= 1.0) {
               return var12;
            }

            if (var10 != 0.0) {
               var12 = var2 / var10;
               if (var12 >= 0.0 && var12 <= 1.0) {
                  return var12;
               }
            }
         }
      }

      var10 = var2 + var4 + var6;
      return 0.0 < (var2 + var10) / 2.0 ? 0.0 : 1.0;
   }

   public double XforT(double var1) {
      return (this.xcoeff2 * var1 + this.xcoeff1) * var1 + this.xcoeff0;
   }

   public double YforT(double var1) {
      return (this.ycoeff2 * var1 + this.ycoeff1) * var1 + this.ycoeff0;
   }

   public double dXforT(double var1, int var3) {
      switch (var3) {
         case 0:
            return (this.xcoeff2 * var1 + this.xcoeff1) * var1 + this.xcoeff0;
         case 1:
            return 2.0 * this.xcoeff2 * var1 + this.xcoeff1;
         case 2:
            return 2.0 * this.xcoeff2;
         default:
            return 0.0;
      }
   }

   public double dYforT(double var1, int var3) {
      switch (var3) {
         case 0:
            return (this.ycoeff2 * var1 + this.ycoeff1) * var1 + this.ycoeff0;
         case 1:
            return 2.0 * this.ycoeff2 * var1 + this.ycoeff1;
         case 2:
            return 2.0 * this.ycoeff2;
         default:
            return 0.0;
      }
   }

   public double nextVertical(double var1, double var3) {
      double var5 = -this.xcoeff1 / (2.0 * this.xcoeff2);
      return var5 > var1 && var5 < var3 ? var5 : var3;
   }

   public void enlarge(RectBounds var1) {
      var1.add((float)this.x0, (float)this.y0);
      double var2 = -this.xcoeff1 / (2.0 * this.xcoeff2);
      if (var2 > 0.0 && var2 < 1.0) {
         var1.add((float)this.XforT(var2), (float)this.YforT(var2));
      }

      var1.add((float)this.x1, (float)this.y1);
   }

   public Curve getSubCurve(double var1, double var3, int var5) {
      double var6;
      if (var1 <= this.y0) {
         if (var3 >= this.y1) {
            return this.getWithDirection(var5);
         }

         var6 = 0.0;
      } else {
         var6 = TforY(var1, this.ycoeff0, this.ycoeff1, this.ycoeff2);
      }

      double var8;
      if (var3 >= this.y1) {
         var8 = 1.0;
      } else {
         var8 = TforY(var3, this.ycoeff0, this.ycoeff1, this.ycoeff2);
      }

      double[] var10 = new double[10];
      var10[0] = this.x0;
      var10[1] = this.y0;
      var10[2] = this.cx0;
      var10[3] = this.cy0;
      var10[4] = this.x1;
      var10[5] = this.y1;
      if (var8 < 1.0) {
         split(var10, 0, var8);
      }

      byte var11;
      if (var6 <= 0.0) {
         var11 = 0;
      } else {
         split(var10, 0, var6 / var8);
         var11 = 4;
      }

      return new Order2(var10[var11 + 0], var1, var10[var11 + 2], var10[var11 + 3], var10[var11 + 4], var3, var5);
   }

   public Curve getReversedCurve() {
      return new Order2(this.x0, this.y0, this.cx0, this.cy0, this.x1, this.y1, -this.direction);
   }

   public int getSegment(float[] var1) {
      var1[0] = (float)this.cx0;
      var1[1] = (float)this.cy0;
      if (this.direction == 1) {
         var1[2] = (float)this.x1;
         var1[3] = (float)this.y1;
      } else {
         var1[2] = (float)this.x0;
         var1[3] = (float)this.y0;
      }

      return 2;
   }

   public String controlPointString() {
      return "(" + round(this.cx0) + ", " + round(this.cy0) + "), ";
   }
}
