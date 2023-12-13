package com.sun.scenario.animation;

import javafx.animation.Interpolator;

public class SplineInterpolator extends Interpolator {
   private final double x1;
   private final double y1;
   private final double x2;
   private final double y2;
   private final boolean isCurveLinear;
   private static final int SAMPLE_SIZE = 16;
   private static final double SAMPLE_INCREMENT = 0.0625;
   private final double[] xSamples = new double[17];

   public SplineInterpolator(double var1, double var3, double var5, double var7) {
      if (!(var1 < 0.0) && !(var1 > 1.0) && !(var3 < 0.0) && !(var3 > 1.0) && !(var5 < 0.0) && !(var5 > 1.0) && !(var7 < 0.0) && !(var7 > 1.0)) {
         this.x1 = var1;
         this.y1 = var3;
         this.x2 = var5;
         this.y2 = var7;
         this.isCurveLinear = this.x1 == this.y1 && this.x2 == this.y2;
         if (!this.isCurveLinear) {
            for(int var9 = 0; var9 < 17; ++var9) {
               this.xSamples[var9] = this.eval((double)var9 * 0.0625, this.x1, this.x2);
            }
         }

      } else {
         throw new IllegalArgumentException("Control point coordinates must all be in range [0,1]");
      }
   }

   public double getX1() {
      return this.x1;
   }

   public double getY1() {
      return this.y1;
   }

   public double getX2() {
      return this.x2;
   }

   public double getY2() {
      return this.y2;
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 19 * var1 + (int)(Double.doubleToLongBits(this.x1) ^ Double.doubleToLongBits(this.x1) >>> 32);
      var1 = 19 * var1 + (int)(Double.doubleToLongBits(this.y1) ^ Double.doubleToLongBits(this.y1) >>> 32);
      var1 = 19 * var1 + (int)(Double.doubleToLongBits(this.x2) ^ Double.doubleToLongBits(this.x2) >>> 32);
      var1 = 19 * var1 + (int)(Double.doubleToLongBits(this.y2) ^ Double.doubleToLongBits(this.y2) >>> 32);
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         SplineInterpolator var2 = (SplineInterpolator)var1;
         if (Double.doubleToLongBits(this.x1) != Double.doubleToLongBits(var2.x1)) {
            return false;
         } else if (Double.doubleToLongBits(this.y1) != Double.doubleToLongBits(var2.y1)) {
            return false;
         } else if (Double.doubleToLongBits(this.x2) != Double.doubleToLongBits(var2.x2)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.y2) == Double.doubleToLongBits(var2.y2);
         }
      }
   }

   public double curve(double var1) {
      if (!(var1 < 0.0) && !(var1 > 1.0)) {
         return !this.isCurveLinear && var1 != 0.0 && var1 != 1.0 ? this.eval(this.findTForX(var1), this.y1, this.y2) : var1;
      } else {
         throw new IllegalArgumentException("x must be in range [0,1]");
      }
   }

   private double eval(double var1, double var3, double var5) {
      double var7 = 1.0 - var1;
      return var1 * (3.0 * var7 * (var7 * var3 + var1 * var5) + var1 * var1);
   }

   private double evalDerivative(double var1, double var3, double var5) {
      double var7 = 1.0 - var1;
      return 3.0 * (var7 * (var7 * var3 + 2.0 * var1 * (var5 - var3)) + var1 * var1 * (1.0 - var5));
   }

   private double getInitialGuessForT(double var1) {
      for(int var3 = 1; var3 < 17; ++var3) {
         if (this.xSamples[var3] >= var1) {
            double var4 = this.xSamples[var3] - this.xSamples[var3 - 1];
            if (var4 == 0.0) {
               return (double)(var3 - 1) * 0.0625;
            }

            return ((double)(var3 - 1) + (var1 - this.xSamples[var3 - 1]) / var4) * 0.0625;
         }
      }

      return 1.0;
   }

   private double findTForX(double var1) {
      double var3 = this.getInitialGuessForT(var1);

      for(int var6 = 0; var6 < 4; ++var6) {
         double var7 = this.eval(var3, this.x1, this.x2) - var1;
         if (var7 == 0.0) {
            break;
         }

         double var9 = this.evalDerivative(var3, this.x1, this.x2);
         if (var9 == 0.0) {
            break;
         }

         var3 -= var7 / var9;
      }

      return var3;
   }

   public String toString() {
      return "SplineInterpolator [x1=" + this.x1 + ", y1=" + this.y1 + ", x2=" + this.x2 + ", y2=" + this.y2 + "]";
   }
}
