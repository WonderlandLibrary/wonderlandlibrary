package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class ArcIterator implements PathIterator {
   double x;
   double y;
   double w;
   double h;
   double angStRad;
   double increment;
   double cv;
   BaseTransform transform;
   int index;
   int arcSegs;
   int lineSegs;

   ArcIterator(Arc2D var1, BaseTransform var2) {
      this.w = (double)(var1.width / 2.0F);
      this.h = (double)(var1.height / 2.0F);
      this.x = (double)var1.x + this.w;
      this.y = (double)var1.y + this.h;
      this.angStRad = -Math.toRadians((double)var1.start);
      this.transform = var2;
      double var3 = (double)(-var1.extent);
      if (!(var3 >= 360.0) && !(var3 <= -360.0)) {
         this.arcSegs = (int)Math.ceil(Math.abs(var3) / 90.0);
         this.increment = Math.toRadians(var3 / (double)this.arcSegs);
         this.cv = btan(this.increment);
         if (this.cv == 0.0) {
            this.arcSegs = 0;
         }
      } else {
         this.arcSegs = 4;
         this.increment = 1.5707963267948966;
         this.cv = 0.5522847498307933;
         if (var3 < 0.0) {
            this.increment = -this.increment;
            this.cv = -this.cv;
         }
      }

      switch (var1.getArcType()) {
         case 0:
            this.lineSegs = 0;
            break;
         case 1:
            this.lineSegs = 1;
            break;
         case 2:
            this.lineSegs = 2;
      }

      if (this.w < 0.0 || this.h < 0.0) {
         this.arcSegs = this.lineSegs = -1;
      }

   }

   public int getWindingRule() {
      return 1;
   }

   public boolean isDone() {
      return this.index > this.arcSegs + this.lineSegs;
   }

   public void next() {
      ++this.index;
   }

   private static double btan(double var0) {
      var0 /= 2.0;
      return 1.3333333333333333 * Math.sin(var0) / (1.0 + Math.cos(var0));
   }

   public int currentSegment(float[] var1) {
      if (this.isDone()) {
         throw new NoSuchElementException("arc iterator out of bounds");
      } else {
         double var2 = this.angStRad;
         if (this.index == 0) {
            var1[0] = (float)(this.x + Math.cos(var2) * this.w);
            var1[1] = (float)(this.y + Math.sin(var2) * this.h);
            if (this.transform != null) {
               this.transform.transform((float[])var1, 0, (float[])var1, 0, 1);
            }

            return 0;
         } else if (this.index > this.arcSegs) {
            if (this.index == this.arcSegs + this.lineSegs) {
               return 4;
            } else {
               var1[0] = (float)this.x;
               var1[1] = (float)this.y;
               if (this.transform != null) {
                  this.transform.transform((float[])var1, 0, (float[])var1, 0, 1);
               }

               return 1;
            }
         } else {
            var2 += this.increment * (double)(this.index - 1);
            double var4 = Math.cos(var2);
            double var6 = Math.sin(var2);
            var1[0] = (float)(this.x + (var4 - this.cv * var6) * this.w);
            var1[1] = (float)(this.y + (var6 + this.cv * var4) * this.h);
            var2 += this.increment;
            var4 = Math.cos(var2);
            var6 = Math.sin(var2);
            var1[2] = (float)(this.x + (var4 + this.cv * var6) * this.w);
            var1[3] = (float)(this.y + (var6 - this.cv * var4) * this.h);
            var1[4] = (float)(this.x + var4 * this.w);
            var1[5] = (float)(this.y + var6 * this.h);
            if (this.transform != null) {
               this.transform.transform((float[])var1, 0, (float[])var1, 0, 3);
            }

            return 3;
         }
      }
   }
}
