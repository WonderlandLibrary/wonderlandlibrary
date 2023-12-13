package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class RoundRectIterator implements PathIterator {
   double x;
   double y;
   double w;
   double h;
   double aw;
   double ah;
   BaseTransform transform;
   int index;
   private static final double angle = 0.7853981633974483;
   private static final double a = 1.0 - Math.cos(0.7853981633974483);
   private static final double b = Math.tan(0.7853981633974483);
   private static final double c;
   private static final double cv;
   private static final double acv;
   private static final double[][] ctrlpts;
   private static final int[] types;

   RoundRectIterator(RoundRectangle2D var1, BaseTransform var2) {
      this.x = (double)var1.x;
      this.y = (double)var1.y;
      this.w = (double)var1.width;
      this.h = (double)var1.height;
      this.aw = Math.min(this.w, (double)Math.abs(var1.arcWidth));
      this.ah = Math.min(this.h, (double)Math.abs(var1.arcHeight));
      this.transform = var2;
      if (this.aw < 0.0 || this.ah < 0.0) {
         this.index = ctrlpts.length;
      }

   }

   public int getWindingRule() {
      return 1;
   }

   public boolean isDone() {
      return this.index >= ctrlpts.length;
   }

   public void next() {
      ++this.index;
      if (this.index < ctrlpts.length && this.aw == 0.0 && this.ah == 0.0 && types[this.index] == 3) {
         ++this.index;
      }

   }

   public int currentSegment(float[] var1) {
      if (this.isDone()) {
         throw new NoSuchElementException("roundrect iterator out of bounds");
      } else {
         double[] var2 = ctrlpts[this.index];
         int var3 = 0;

         for(int var4 = 0; var4 < var2.length; var4 += 4) {
            var1[var3++] = (float)(this.x + var2[var4 + 0] * this.w + var2[var4 + 1] * this.aw);
            var1[var3++] = (float)(this.y + var2[var4 + 2] * this.h + var2[var4 + 3] * this.ah);
         }

         if (this.transform != null) {
            this.transform.transform((float[])var1, 0, (float[])var1, 0, var3 / 2);
         }

         return types[this.index];
      }
   }

   static {
      c = Math.sqrt(1.0 + b * b) - 1.0 + a;
      cv = 1.3333333333333333 * a * b / c;
      acv = (1.0 - cv) / 2.0;
      ctrlpts = new double[][]{{0.0, 0.0, 0.0, 0.5}, {0.0, 0.0, 1.0, -0.5}, {0.0, 0.0, 1.0, -acv, 0.0, acv, 1.0, 0.0, 0.0, 0.5, 1.0, 0.0}, {1.0, -0.5, 1.0, 0.0}, {1.0, -acv, 1.0, 0.0, 1.0, 0.0, 1.0, -acv, 1.0, 0.0, 1.0, -0.5}, {1.0, 0.0, 0.0, 0.5}, {1.0, 0.0, 0.0, acv, 1.0, -acv, 0.0, 0.0, 1.0, -0.5, 0.0, 0.0}, {0.0, 0.5, 0.0, 0.0}, {0.0, acv, 0.0, 0.0, 0.0, 0.0, 0.0, acv, 0.0, 0.0, 0.0, 0.5}, new double[0]};
      types = new int[]{0, 1, 3, 1, 3, 1, 3, 1, 3, 4};
   }
}
