package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

class LineIterator implements PathIterator {
   Line2D line;
   BaseTransform transform;
   int index;

   LineIterator(Line2D var1, BaseTransform var2) {
      this.line = var1;
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
         throw new NoSuchElementException("line iterator out of bounds");
      } else {
         byte var2;
         if (this.index == 0) {
            var1[0] = this.line.x1;
            var1[1] = this.line.y1;
            var2 = 0;
         } else {
            var1[0] = this.line.x2;
            var1[1] = this.line.y2;
            var2 = 1;
         }

         if (this.transform != null) {
            this.transform.transform((float[])var1, 0, (float[])var1, 0, 1);
         }

         return var2;
      }
   }
}
