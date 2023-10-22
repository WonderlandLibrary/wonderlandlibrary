package com.sun.javafx.geom;

import java.util.Enumeration;
import java.util.Vector;

public abstract class Crossings {
   public static final boolean debug = false;
   int limit = 0;
   double[] yranges = new double[10];
   double xlo;
   double ylo;
   double xhi;
   double yhi;

   public Crossings(double var1, double var3, double var5, double var7) {
      this.xlo = var1;
      this.ylo = var3;
      this.xhi = var5;
      this.yhi = var7;
   }

   public final double getXLo() {
      return this.xlo;
   }

   public final double getYLo() {
      return this.ylo;
   }

   public final double getXHi() {
      return this.xhi;
   }

   public final double getYHi() {
      return this.yhi;
   }

   public abstract void record(double var1, double var3, int var5);

   public void print() {
      System.out.println("Crossings [");
      System.out.println("  bounds = [" + this.ylo + ", " + this.yhi + "]");

      for(int var1 = 0; var1 < this.limit; var1 += 2) {
         System.out.println("  [" + this.yranges[var1] + ", " + this.yranges[var1 + 1] + "]");
      }

      System.out.println("]");
   }

   public final boolean isEmpty() {
      return this.limit == 0;
   }

   public abstract boolean covers(double var1, double var3);

   public static Crossings findCrossings(Vector var0, double var1, double var3, double var5, double var7) {
      EvenOdd var9 = new EvenOdd(var1, var3, var5, var7);
      Enumeration var10 = var0.elements();

      Curve var11;
      do {
         if (!var10.hasMoreElements()) {
            return var9;
         }

         var11 = (Curve)var10.nextElement();
      } while(!var11.accumulateCrossings(var9));

      return null;
   }

   public static final class EvenOdd extends Crossings {
      public EvenOdd(double var1, double var3, double var5, double var7) {
         super(var1, var3, var5, var7);
      }

      public final boolean covers(double var1, double var3) {
         return this.limit == 2 && this.yranges[0] <= var1 && this.yranges[1] >= var3;
      }

      public void record(double var1, double var3, int var5) {
         if (!(var1 >= var3)) {
            int var6;
            for(var6 = 0; var6 < this.limit && var1 > this.yranges[var6 + 1]; var6 += 2) {
            }

            int var7 = var6;

            while(var6 < this.limit) {
               double var8 = this.yranges[var6++];
               double var10 = this.yranges[var6++];
               if (var3 < var8) {
                  this.yranges[var7++] = var1;
                  this.yranges[var7++] = var3;
                  var1 = var8;
                  var3 = var10;
               } else {
                  double var12;
                  double var14;
                  if (var1 < var8) {
                     var12 = var1;
                     var14 = var8;
                  } else {
                     var12 = var8;
                     var14 = var1;
                  }

                  double var16;
                  double var18;
                  if (var3 < var10) {
                     var16 = var3;
                     var18 = var10;
                  } else {
                     var16 = var10;
                     var18 = var3;
                  }

                  if (var14 == var16) {
                     var1 = var12;
                     var3 = var18;
                  } else {
                     if (var14 > var16) {
                        var1 = var16;
                        var16 = var14;
                        var14 = var1;
                     }

                     if (var12 != var14) {
                        this.yranges[var7++] = var12;
                        this.yranges[var7++] = var14;
                     }

                     var1 = var16;
                     var3 = var18;
                  }

                  if (var1 >= var3) {
                     break;
                  }
               }
            }

            if (var7 < var6 && var6 < this.limit) {
               System.arraycopy(this.yranges, var6, this.yranges, var7, this.limit - var6);
            }

            var7 += this.limit - var6;
            if (var1 < var3) {
               if (var7 >= this.yranges.length) {
                  double[] var20 = new double[var7 + 10];
                  System.arraycopy(this.yranges, 0, var20, 0, var7);
                  this.yranges = var20;
               }

               this.yranges[var7++] = var1;
               this.yranges[var7++] = var3;
            }

            this.limit = var7;
         }
      }
   }
}
