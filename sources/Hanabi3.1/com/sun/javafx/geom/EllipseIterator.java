package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class EllipseIterator implements PathIterator {
   double x;
   double y;
   double w;
   double h;
   BaseTransform transform;
   int index;
   public static final double CtrlVal = 0.5522847498307933;
   private static final double pcv = 0.7761423749153966;
   private static final double ncv = 0.22385762508460333;
   private static final double[][] ctrlpts = new double[][]{{1.0, 0.7761423749153966, 0.7761423749153966, 1.0, 0.5, 1.0}, {0.22385762508460333, 1.0, 0.0, 0.7761423749153966, 0.0, 0.5}, {0.0, 0.22385762508460333, 0.22385762508460333, 0.0, 0.5, 0.0}, {0.7761423749153966, 0.0, 1.0, 0.22385762508460333, 1.0, 0.5}};

   EllipseIterator(Ellipse2D var1, BaseTransform var2) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.w = (double)var1.width;
      this.h = (double)var1.height;
      this.transform = var2;
      if (this.w < 0.0 || this.h < 0.0) {
         this.index = 6;
      }

   }

   public int getWindingRule() {
      return 1;
   }

   public boolean isDone() {
      return this.index > 5;
   }

   public void next() {
      ++this.index;
   }

   public int currentSegment(float[] var1) {
      if (this.isDone()) {
         throw new NoSuchElementException("ellipse iterator out of bounds");
      } else if (this.index == 5) {
         return 4;
      } else {
         double[] var2;
         if (this.index == 0) {
            var2 = ctrlpts[3];
            var1[0] = (float)(this.x + var2[4] * this.w);
            var1[1] = (float)(this.y + var2[5] * this.h);
            if (this.transform != null) {
               this.transform.transform((float[])var1, 0, (float[])var1, 0, 1);
            }

            return 0;
         } else {
            var2 = ctrlpts[this.index - 1];
            var1[0] = (float)(this.x + var2[0] * this.w);
            var1[1] = (float)(this.y + var2[1] * this.h);
            var1[2] = (float)(this.x + var2[2] * this.w);
            var1[3] = (float)(this.y + var2[3] * this.h);
            var1[4] = (float)(this.x + var2[4] * this.w);
            var1[5] = (float)(this.y + var2[5] * this.h);
            if (this.transform != null) {
               this.transform.transform((float[])var1, 0, (float[])var1, 0, 3);
            }

            return 3;
         }
      }
   }
}
