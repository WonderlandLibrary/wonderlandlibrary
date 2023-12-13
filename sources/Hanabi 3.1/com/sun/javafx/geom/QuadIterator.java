package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class QuadIterator implements PathIterator {
   QuadCurve2D quad;
   BaseTransform transform;
   int index;

   QuadIterator(QuadCurve2D var1, BaseTransform var2) {
      this.quad = var1;
      this.transform = var2;
   }

   public int getWindingRule() {
      return 1;
   }

   public boolean isDone() {
      return this.index > 1;
   }

   public void next() {
      ++this.index;
   }

   public int currentSegment(float[] var1) {
      if (this.isDone()) {
         throw new NoSuchElementException("quad iterator iterator out of bounds");
      } else {
         byte var2;
         if (this.index == 0) {
            var1[0] = this.quad.x1;
            var1[1] = this.quad.y1;
            var2 = 0;
         } else {
            var1[0] = this.quad.ctrlx;
            var1[1] = this.quad.ctrly;
            var1[2] = this.quad.x2;
            var1[3] = this.quad.y2;
            var2 = 2;
         }

         if (this.transform != null) {
            this.transform.transform((float[])var1, 0, (float[])var1, 0, this.index == 0 ? 1 : 2);
         }

         return var2;
      }
   }
}
