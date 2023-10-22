package com.sun.javafx.geom;

import java.util.Vector;

final class Order3 extends Curve {
   private double x0;
   private double y0;
   private double cx0;
   private double cy0;
   private double cx1;
   private double cy1;
   private double x1;
   private double y1;
   private double xmin;
   private double xmax;
   private double xcoeff0;
   private double xcoeff1;
   private double xcoeff2;
   private double xcoeff3;
   private double ycoeff0;
   private double ycoeff1;
   private double ycoeff2;
   private double ycoeff3;
   private double TforY1;
   private double YforT1;
   private double TforY2;
   private double YforT2;
   private double TforY3;
   private double YforT3;

   public static void insert(Vector var0, double[] var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, int var18) {
      int var19 = getHorizontalParams(var4, var8, var12, var16, var1);
      if (var19 == 0) {
         addInstance(var0, var2, var4, var6, var8, var10, var12, var14, var16, var18);
      } else {
         var1[3] = var2;
         var1[4] = var4;
         var1[5] = var6;
         var1[6] = var8;
         var1[7] = var10;
         var1[8] = var12;
         var1[9] = var14;
         var1[10] = var16;
         double var20 = var1[0];
         if (var19 > 1 && var20 > var1[1]) {
            var1[0] = var1[1];
            var1[1] = var20;
            var20 = var1[0];
         }

         split(var1, 3, var20);
         if (var19 > 1) {
            var20 = (var1[1] - var20) / (1.0 - var20);
            split(var1, 9, var20);
         }

         int var22 = 3;
         if (var18 == -1) {
            var22 += var19 * 6;
         }

         while(var19 >= 0) {
            addInstance(var0, var1[var22 + 0], var1[var22 + 1], var1[var22 + 2], var1[var22 + 3], var1[var22 + 4], var1[var22 + 5], var1[var22 + 6], var1[var22 + 7], var18);
            --var19;
            if (var18 == 1) {
               var22 += 6;
            } else {
               var22 -= 6;
            }
         }

      }
   }

   public static void addInstance(Vector var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      if (var3 > var15) {
         var0.add(new Order3(var13, var15, var9, var11, var5, var7, var1, var3, -var17));
      } else if (var15 > var3) {
         var0.add(new Order3(var1, var3, var5, var7, var9, var11, var13, var15, var17));
      }

   }

   public static int solveQuadratic(double[] var0, double[] var1) {
      double var2 = var0[2];
      double var4 = var0[1];
      double var6 = var0[0];
      int var8 = 0;
      if (var2 == 0.0) {
         if (var4 == 0.0) {
            return -1;
         }

         var1[var8++] = -var6 / var4;
      } else {
         double var9 = var4 * var4 - 4.0 * var2 * var6;
         if (var9 < 0.0) {
            return 0;
         }

         var9 = Math.sqrt(var9);
         if (var4 < 0.0) {
            var9 = -var9;
         }

         double var11 = (var4 + var9) / -2.0;
         var1[var8++] = var11 / var2;
         if (var11 != 0.0) {
            var1[var8++] = var6 / var11;
         }
      }

      return var8;
   }

   public static int getHorizontalParams(double var0, double var2, double var4, double var6, double[] var8) {
      if (var0 <= var2 && var2 <= var4 && var4 <= var6) {
         return 0;
      } else {
         var6 -= var4;
         var4 -= var2;
         var2 -= var0;
         var8[0] = var2;
         var8[1] = (var4 - var2) * 2.0;
         var8[2] = var6 - var4 - var4 + var2;
         int var9 = solveQuadratic(var8, var8);
         int var10 = 0;

         for(int var11 = 0; var11 < var9; ++var11) {
            double var12 = var8[var11];
            if (var12 > 0.0 && var12 < 1.0) {
               if (var10 < var11) {
                  var8[var10] = var12;
               }

               ++var10;
            }
         }

         return var10;
      }
   }

   public static void split(double[] var0, int var1, double var2) {
      double var16;
      var0[var1 + 12] = var16 = var0[var1 + 6];
      double var18;
      var0[var1 + 13] = var18 = var0[var1 + 7];
      double var12 = var0[var1 + 4];
      double var14 = var0[var1 + 5];
      var16 = var12 + (var16 - var12) * var2;
      var18 = var14 + (var18 - var14) * var2;
      double var4 = var0[var1 + 0];
      double var6 = var0[var1 + 1];
      double var8 = var0[var1 + 2];
      double var10 = var0[var1 + 3];
      var4 += (var8 - var4) * var2;
      var6 += (var10 - var6) * var2;
      var8 += (var12 - var8) * var2;
      var10 += (var14 - var10) * var2;
      var12 = var8 + (var16 - var8) * var2;
      var14 = var10 + (var18 - var10) * var2;
      var8 = var4 + (var8 - var4) * var2;
      var10 = var6 + (var10 - var6) * var2;
      var0[var1 + 2] = var4;
      var0[var1 + 3] = var6;
      var0[var1 + 4] = var8;
      var0[var1 + 5] = var10;
      var0[var1 + 6] = var8 + (var12 - var8) * var2;
      var0[var1 + 7] = var10 + (var14 - var10) * var2;
      var0[var1 + 8] = var12;
      var0[var1 + 9] = var14;
      var0[var1 + 10] = var16;
      var0[var1 + 11] = var18;
   }

   public Order3(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      super(var17);
      if (var7 < var3) {
         var7 = var3;
      }

      if (var11 > var15) {
         var11 = var15;
      }

      this.x0 = var1;
      this.y0 = var3;
      this.cx0 = var5;
      this.cy0 = var7;
      this.cx1 = var9;
      this.cy1 = var11;
      this.x1 = var13;
      this.y1 = var15;
      this.xmin = Math.min(Math.min(var1, var13), Math.min(var5, var9));
      this.xmax = Math.max(Math.max(var1, var13), Math.max(var5, var9));
      this.xcoeff0 = var1;
      this.xcoeff1 = (var5 - var1) * 3.0;
      this.xcoeff2 = (var9 - var5 - var5 + var1) * 3.0;
      this.xcoeff3 = var13 - (var9 - var5) * 3.0 - var1;
      this.ycoeff0 = var3;
      this.ycoeff1 = (var7 - var3) * 3.0;
      this.ycoeff2 = (var11 - var7 - var7 + var3) * 3.0;
      this.ycoeff3 = var15 - (var11 - var7) * 3.0 - var3;
      this.YforT1 = this.YforT2 = this.YforT3 = var3;
   }

   public int getOrder() {
      return 3;
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
      return this.direction == 1 ? this.cx0 : this.cx1;
   }

   public double getCY0() {
      return this.direction == 1 ? this.cy0 : this.cy1;
   }

   public double getCX1() {
      return this.direction == -1 ? this.cx0 : this.cx1;
   }

   public double getCY1() {
      return this.direction == -1 ? this.cy0 : this.cy1;
   }

   public double getX1() {
      return this.direction == -1 ? this.x0 : this.x1;
   }

   public double getY1() {
      return this.direction == -1 ? this.y0 : this.y1;
   }

   public double TforY(double var1) {
      if (var1 <= this.y0) {
         return 0.0;
      } else if (var1 >= this.y1) {
         return 1.0;
      } else if (var1 == this.YforT1) {
         return this.TforY1;
      } else if (var1 == this.YforT2) {
         return this.TforY2;
      } else if (var1 == this.YforT3) {
         return this.TforY3;
      } else if (this.ycoeff3 == 0.0) {
         return Order2.TforY(var1, this.ycoeff0, this.ycoeff1, this.ycoeff2);
      } else {
         double var3 = this.ycoeff2 / this.ycoeff3;
         double var5 = this.ycoeff1 / this.ycoeff3;
         double var7 = (this.ycoeff0 - var1) / this.ycoeff3;
         boolean var9 = false;
         double var10 = (var3 * var3 - 3.0 * var5) / 9.0;
         double var12 = (2.0 * var3 * var3 * var3 - 9.0 * var3 * var5 + 27.0 * var7) / 54.0;
         double var14 = var12 * var12;
         double var16 = var10 * var10 * var10;
         double var18 = var3 / 3.0;
         double var20;
         double var22;
         if (var14 < var16) {
            var22 = Math.acos(var12 / Math.sqrt(var16));
            var10 = -2.0 * Math.sqrt(var10);
            var20 = this.refine(var3, var5, var7, var1, var10 * Math.cos(var22 / 3.0) - var18);
            if (var20 < 0.0) {
               var20 = this.refine(var3, var5, var7, var1, var10 * Math.cos((var22 + 6.283185307179586) / 3.0) - var18);
            }

            if (var20 < 0.0) {
               var20 = this.refine(var3, var5, var7, var1, var10 * Math.cos((var22 - 6.283185307179586) / 3.0) - var18);
            }
         } else {
            boolean var29 = var12 < 0.0;
            double var23 = Math.sqrt(var14 - var16);
            if (var29) {
               var12 = -var12;
            }

            double var25 = Math.pow(var12 + var23, 0.3333333333333333);
            if (!var29) {
               var25 = -var25;
            }

            double var27 = var25 == 0.0 ? 0.0 : var10 / var25;
            var20 = this.refine(var3, var5, var7, var1, var25 + var27 - var18);
         }

         if (var20 < 0.0) {
            var22 = 0.0;
            double var24 = 1.0;

            while(true) {
               var20 = (var22 + var24) / 2.0;
               if (var20 == var22 || var20 == var24) {
                  break;
               }

               double var26 = this.YforT(var20);
               if (var26 < var1) {
                  var22 = var20;
               } else {
                  if (!(var26 > var1)) {
                     break;
                  }

                  var24 = var20;
               }
            }
         }

         if (var20 >= 0.0) {
            this.TforY3 = this.TforY2;
            this.YforT3 = this.YforT2;
            this.TforY2 = this.TforY1;
            this.YforT2 = this.YforT1;
            this.TforY1 = var20;
            this.YforT1 = var1;
         }

         return var20;
      }
   }

   public double refine(double var1, double var3, double var5, double var7, double var9) {
      if (!(var9 < -0.1) && !(var9 > 1.1)) {
         double var11 = this.YforT(var9);
         double var13;
         double var15;
         if (var11 < var7) {
            var13 = var9;
            var15 = 1.0;
         } else {
            var13 = 0.0;
            var15 = var9;
         }

         boolean var21 = true;

         while(var11 != var7) {
            double var22;
            if (!var21) {
               var22 = (var13 + var15) / 2.0;
               if (var22 == var13 || var22 == var15) {
                  break;
               }

               var9 = var22;
            } else {
               var22 = this.dYforT(var9, 1);
               if (var22 == 0.0) {
                  var21 = false;
                  continue;
               }

               double var24 = var9 + (var7 - var11) / var22;
               if (var24 == var9 || var24 <= var13 || var24 >= var15) {
                  var21 = false;
                  continue;
               }

               var9 = var24;
            }

            var11 = this.YforT(var9);
            if (var11 < var7) {
               var13 = var9;
            } else {
               if (!(var11 > var7)) {
                  break;
               }

               var15 = var9;
            }
         }

         boolean var26 = false;
         return var9 > 1.0 ? -1.0 : var9;
      } else {
         return -1.0;
      }
   }

   public double XforY(double var1) {
      if (var1 <= this.y0) {
         return this.x0;
      } else {
         return var1 >= this.y1 ? this.x1 : this.XforT(this.TforY(var1));
      }
   }

   public double XforT(double var1) {
      return ((this.xcoeff3 * var1 + this.xcoeff2) * var1 + this.xcoeff1) * var1 + this.xcoeff0;
   }

   public double YforT(double var1) {
      return ((this.ycoeff3 * var1 + this.ycoeff2) * var1 + this.ycoeff1) * var1 + this.ycoeff0;
   }

   public double dXforT(double var1, int var3) {
      switch (var3) {
         case 0:
            return ((this.xcoeff3 * var1 + this.xcoeff2) * var1 + this.xcoeff1) * var1 + this.xcoeff0;
         case 1:
            return (3.0 * this.xcoeff3 * var1 + 2.0 * this.xcoeff2) * var1 + this.xcoeff1;
         case 2:
            return 6.0 * this.xcoeff3 * var1 + 2.0 * this.xcoeff2;
         case 3:
            return 6.0 * this.xcoeff3;
         default:
            return 0.0;
      }
   }

   public double dYforT(double var1, int var3) {
      switch (var3) {
         case 0:
            return ((this.ycoeff3 * var1 + this.ycoeff2) * var1 + this.ycoeff1) * var1 + this.ycoeff0;
         case 1:
            return (3.0 * this.ycoeff3 * var1 + 2.0 * this.ycoeff2) * var1 + this.ycoeff1;
         case 2:
            return 6.0 * this.ycoeff3 * var1 + 2.0 * this.ycoeff2;
         case 3:
            return 6.0 * this.ycoeff3;
         default:
            return 0.0;
      }
   }

   public double nextVertical(double var1, double var3) {
      double[] var5 = new double[]{this.xcoeff1, 2.0 * this.xcoeff2, 3.0 * this.xcoeff3};
      int var6 = solveQuadratic(var5, var5);

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var5[var7] > var1 && var5[var7] < var3) {
            var3 = var5[var7];
         }
      }

      return var3;
   }

   public void enlarge(RectBounds var1) {
      var1.add((float)this.x0, (float)this.y0);
      double[] var2 = new double[]{this.xcoeff1, 2.0 * this.xcoeff2, 3.0 * this.xcoeff3};
      int var3 = solveQuadratic(var2, var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         double var5 = var2[var4];
         if (var5 > 0.0 && var5 < 1.0) {
            var1.add((float)this.XforT(var5), (float)this.YforT(var5));
         }
      }

      var1.add((float)this.x1, (float)this.y1);
   }

   public Curve getSubCurve(double var1, double var3, int var5) {
      if (var1 <= this.y0 && var3 >= this.y1) {
         return this.getWithDirection(var5);
      } else {
         double[] var6 = new double[14];
         double var7 = this.TforY(var1);
         double var9 = this.TforY(var3);
         var6[0] = this.x0;
         var6[1] = this.y0;
         var6[2] = this.cx0;
         var6[3] = this.cy0;
         var6[4] = this.cx1;
         var6[5] = this.cy1;
         var6[6] = this.x1;
         var6[7] = this.y1;
         if (var7 > var9) {
            double var11 = var7;
            var7 = var9;
            var9 = var11;
         }

         if (var9 < 1.0) {
            split(var6, 0, var9);
         }

         byte var13;
         if (var7 <= 0.0) {
            var13 = 0;
         } else {
            split(var6, 0, var7 / var9);
            var13 = 6;
         }

         return new Order3(var6[var13 + 0], var1, var6[var13 + 2], var6[var13 + 3], var6[var13 + 4], var6[var13 + 5], var6[var13 + 6], var3, var5);
      }
   }

   public Curve getReversedCurve() {
      return new Order3(this.x0, this.y0, this.cx0, this.cy0, this.cx1, this.cy1, this.x1, this.y1, -this.direction);
   }

   public int getSegment(float[] var1) {
      if (this.direction == 1) {
         var1[0] = (float)this.cx0;
         var1[1] = (float)this.cy0;
         var1[2] = (float)this.cx1;
         var1[3] = (float)this.cy1;
         var1[4] = (float)this.x1;
         var1[5] = (float)this.y1;
      } else {
         var1[0] = (float)this.cx1;
         var1[1] = (float)this.cy1;
         var1[2] = (float)this.cx0;
         var1[3] = (float)this.cy0;
         var1[4] = (float)this.x0;
         var1[5] = (float)this.y0;
      }

      return 3;
   }

   public String controlPointString() {
      return "(" + round(this.getCX0()) + ", " + round(this.getCY0()) + "), " + "(" + round(this.getCX1()) + ", " + round(this.getCY1()) + "), ";
   }
}
