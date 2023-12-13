package com.sun.javafx.geom;

final class Order1 extends Curve {
   private double x0;
   private double y0;
   private double x1;
   private double y1;
   private double xmin;
   private double xmax;

   public Order1(double var1, double var3, double var5, double var7, int var9) {
      super(var9);
      this.x0 = var1;
      this.y0 = var3;
      this.x1 = var5;
      this.y1 = var7;
      if (var1 < var5) {
         this.xmin = var1;
         this.xmax = var5;
      } else {
         this.xmin = var5;
         this.xmax = var1;
      }

   }

   public int getOrder() {
      return 1;
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

   public double getX1() {
      return this.direction == -1 ? this.x0 : this.x1;
   }

   public double getY1() {
      return this.direction == -1 ? this.y0 : this.y1;
   }

   public double XforY(double var1) {
      if (this.x0 != this.x1 && !(var1 <= this.y0)) {
         return var1 >= this.y1 ? this.x1 : this.x0 + (var1 - this.y0) * (this.x1 - this.x0) / (this.y1 - this.y0);
      } else {
         return this.x0;
      }
   }

   public double TforY(double var1) {
      if (var1 <= this.y0) {
         return 0.0;
      } else {
         return var1 >= this.y1 ? 1.0 : (var1 - this.y0) / (this.y1 - this.y0);
      }
   }

   public double XforT(double var1) {
      return this.x0 + var1 * (this.x1 - this.x0);
   }

   public double YforT(double var1) {
      return this.y0 + var1 * (this.y1 - this.y0);
   }

   public double dXforT(double var1, int var3) {
      switch (var3) {
         case 0:
            return this.x0 + var1 * (this.x1 - this.x0);
         case 1:
            return this.x1 - this.x0;
         default:
            return 0.0;
      }
   }

   public double dYforT(double var1, int var3) {
      switch (var3) {
         case 0:
            return this.y0 + var1 * (this.y1 - this.y0);
         case 1:
            return this.y1 - this.y0;
         default:
            return 0.0;
      }
   }

   public double nextVertical(double var1, double var3) {
      return var3;
   }

   public boolean accumulateCrossings(Crossings var1) {
      double var2 = var1.getXLo();
      double var4 = var1.getYLo();
      double var6 = var1.getXHi();
      double var8 = var1.getYHi();
      if (this.xmin >= var6) {
         return false;
      } else {
         double var10;
         double var12;
         if (this.y0 < var4) {
            if (this.y1 <= var4) {
               return false;
            }

            var12 = var4;
            var10 = this.XforY(var4);
         } else {
            if (this.y0 >= var8) {
               return false;
            }

            var12 = this.y0;
            var10 = this.x0;
         }

         double var16;
         double var14;
         if (this.y1 > var8) {
            var16 = var8;
            var14 = this.XforY(var8);
         } else {
            var16 = this.y1;
            var14 = this.x1;
         }

         if (var10 >= var6 && var14 >= var6) {
            return false;
         } else if (!(var10 > var2) && !(var14 > var2)) {
            var1.record(var12, var16, this.direction);
            return false;
         } else {
            return true;
         }
      }
   }

   public void enlarge(RectBounds var1) {
      var1.add((float)this.x0, (float)this.y0);
      var1.add((float)this.x1, (float)this.y1);
   }

   public Curve getSubCurve(double var1, double var3, int var5) {
      if (var1 == this.y0 && var3 == this.y1) {
         return this.getWithDirection(var5);
      } else if (this.x0 == this.x1) {
         return new Order1(this.x0, var1, this.x1, var3, var5);
      } else {
         double var6 = this.x0 - this.x1;
         double var8 = this.y0 - this.y1;
         double var10 = this.x0 + (var1 - this.y0) * var6 / var8;
         double var12 = this.x0 + (var3 - this.y0) * var6 / var8;
         return new Order1(var10, var1, var12, var3, var5);
      }
   }

   public Curve getReversedCurve() {
      return new Order1(this.x0, this.y0, this.x1, this.y1, -this.direction);
   }

   public int compareTo(Curve var1, double[] var2) {
      if (!(var1 instanceof Order1)) {
         return super.compareTo(var1, var2);
      } else {
         Order1 var3 = (Order1)var1;
         if (var2[1] <= var2[0]) {
            throw new InternalError("yrange already screwed up...");
         } else {
            var2[1] = Math.min(Math.min(var2[1], this.y1), var3.y1);
            if (var2[1] <= var2[0]) {
               throw new InternalError("backstepping from " + var2[0] + " to " + var2[1]);
            } else if (this.xmax <= var3.xmin) {
               return this.xmin == var3.xmax ? 0 : -1;
            } else if (this.xmin >= var3.xmax) {
               return 1;
            } else {
               double var4 = this.x1 - this.x0;
               double var6 = this.y1 - this.y0;
               double var8 = var3.x1 - var3.x0;
               double var10 = var3.y1 - var3.y0;
               double var12 = var8 * var6 - var4 * var10;
               double var14;
               if (var12 != 0.0) {
                  double var16 = (this.x0 - var3.x0) * var6 * var10 - this.y0 * var4 * var10 + var3.y0 * var8 * var6;
                  var14 = var16 / var12;
                  if (var14 <= var2[0]) {
                     var14 = Math.min(this.y1, var3.y1);
                  } else {
                     if (var14 < var2[1]) {
                        var2[1] = var14;
                     }

                     var14 = Math.max(this.y0, var3.y0);
                  }
               } else {
                  var14 = Math.max(this.y0, var3.y0);
               }

               return orderof(this.XforY(var14), var3.XforY(var14));
            }
         }
      }
   }

   public int getSegment(float[] var1) {
      if (this.direction == 1) {
         var1[0] = (float)this.x1;
         var1[1] = (float)this.y1;
      } else {
         var1[0] = (float)this.x0;
         var1[1] = (float)this.y0;
      }

      return 1;
   }
}
