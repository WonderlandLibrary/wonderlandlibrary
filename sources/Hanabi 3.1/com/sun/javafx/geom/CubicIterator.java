package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class CubicIterator implements PathIterator {
   CubicCurve2D cubic;
   BaseTransform transform;
   int index;

   CubicIterator(CubicCurve2D var1, BaseTransform var2) {
      this.cubic = var1;
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
         throw new NoSuchElementException("cubic iterator iterator out of bounds");
      } else {
         byte var2;
         if (this.index == 0) {
            var1[0] = this.cubic.x1;
            var1[1] = this.cubic.y1;
            var2 = 0;
         } else {
            var1[0] = this.cubic.ctrlx1;
            var1[1] = this.cubic.ctrly1;
            var1[2] = this.cubic.ctrlx2;
            var1[3] = this.cubic.ctrly2;
            var1[4] = this.cubic.x2;
            var1[5] = this.cubic.y2;
            var2 = 3;
         }

         if (this.transform != null) {
            this.transform.transform((float[])var1, 0, (float[])var1, 0, this.index == 0 ? 1 : 3);
         }

         return var2;
      }
   }
}
