package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;
import java.util.Vector;

class AreaIterator implements PathIterator {
   private BaseTransform transform;
   private Vector curves;
   private int index;
   private Curve prevcurve;
   private Curve thiscurve;

   public AreaIterator(Vector var1, BaseTransform var2) {
      this.curves = var1;
      this.transform = var2;
      if (var1.size() >= 1) {
         this.thiscurve = (Curve)var1.get(0);
      }

   }

   public int getWindingRule() {
      return 1;
   }

   public boolean isDone() {
      return this.prevcurve == null && this.thiscurve == null;
   }

   public void next() {
      if (this.prevcurve != null) {
         this.prevcurve = null;
      } else {
         this.prevcurve = this.thiscurve;
         ++this.index;
         if (this.index < this.curves.size()) {
            this.thiscurve = (Curve)this.curves.get(this.index);
            if (this.thiscurve.getOrder() != 0 && this.prevcurve.getX1() == this.thiscurve.getX0() && this.prevcurve.getY1() == this.thiscurve.getY0()) {
               this.prevcurve = null;
            }
         } else {
            this.thiscurve = null;
         }
      }

   }

   public int currentSegment(float[] var1) {
      int var2;
      int var3;
      if (this.prevcurve != null) {
         if (this.thiscurve == null || this.thiscurve.getOrder() == 0) {
            return 4;
         }

         var1[0] = (float)this.thiscurve.getX0();
         var1[1] = (float)this.thiscurve.getY0();
         var2 = 1;
         var3 = 1;
      } else {
         if (this.thiscurve == null) {
            throw new NoSuchElementException("area iterator out of bounds");
         }

         var2 = this.thiscurve.getSegment(var1);
         var3 = this.thiscurve.getOrder();
         if (var3 == 0) {
            var3 = 1;
         }
      }

      if (this.transform != null) {
         this.transform.transform((float[])var1, 0, (float[])var1, 0, var3);
      }

      return var2;
   }
}
