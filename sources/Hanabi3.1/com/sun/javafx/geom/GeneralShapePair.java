package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public class GeneralShapePair extends ShapePair {
   private final Shape outer;
   private final Shape inner;
   private final int combinationType;

   public GeneralShapePair(Shape var1, Shape var2, int var3) {
      this.outer = var1;
      this.inner = var2;
      this.combinationType = var3;
   }

   public final int getCombinationType() {
      return this.combinationType;
   }

   public final Shape getOuterShape() {
      return this.outer;
   }

   public final Shape getInnerShape() {
      return this.inner;
   }

   public Shape copy() {
      return new GeneralShapePair(this.outer.copy(), this.inner.copy(), this.combinationType);
   }

   public boolean contains(float var1, float var2) {
      if (this.combinationType == 4) {
         return this.outer.contains(var1, var2) && this.inner.contains(var1, var2);
      } else {
         return this.outer.contains(var1, var2) && !this.inner.contains(var1, var2);
      }
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      if (this.combinationType == 4) {
         return this.outer.intersects(var1, var2, var3, var4) && this.inner.intersects(var1, var2, var3, var4);
      } else {
         return this.outer.intersects(var1, var2, var3, var4) && !this.inner.contains(var1, var2, var3, var4);
      }
   }

   public boolean contains(float var1, float var2, float var3, float var4) {
      if (this.combinationType == 4) {
         return this.outer.contains(var1, var2, var3, var4) && this.inner.contains(var1, var2, var3, var4);
      } else {
         return this.outer.contains(var1, var2, var3, var4) && !this.inner.intersects(var1, var2, var3, var4);
      }
   }

   public RectBounds getBounds() {
      RectBounds var1 = this.outer.getBounds();
      if (this.combinationType == 4) {
         var1.intersectWith((BaseBounds)this.inner.getBounds());
      }

      return var1;
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public PathIterator getPathIterator(BaseTransform var1, float var2) {
      return new FlatteningPathIterator(this.getPathIterator(var1), var2);
   }
}
