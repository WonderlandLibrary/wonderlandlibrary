package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public abstract class RectangularShape extends Shape {
   protected RectangularShape() {
   }

   public abstract float getX();

   public abstract float getY();

   public abstract float getWidth();

   public abstract float getHeight();

   public float getMinX() {
      return this.getX();
   }

   public float getMinY() {
      return this.getY();
   }

   public float getMaxX() {
      return this.getX() + this.getWidth();
   }

   public float getMaxY() {
      return this.getY() + this.getHeight();
   }

   public float getCenterX() {
      return this.getX() + this.getWidth() / 2.0F;
   }

   public float getCenterY() {
      return this.getY() + this.getHeight() / 2.0F;
   }

   public abstract boolean isEmpty();

   public abstract void setFrame(float var1, float var2, float var3, float var4);

   public void setFrame(Point2D var1, Dimension2D var2) {
      this.setFrame(var1.x, var1.y, var2.width, var2.height);
   }

   public void setFrameFromDiagonal(float var1, float var2, float var3, float var4) {
      float var5;
      if (var3 < var1) {
         var5 = var1;
         var1 = var3;
         var3 = var5;
      }

      if (var4 < var2) {
         var5 = var2;
         var2 = var4;
         var4 = var5;
      }

      this.setFrame(var1, var2, var3 - var1, var4 - var2);
   }

   public void setFrameFromDiagonal(Point2D var1, Point2D var2) {
      this.setFrameFromDiagonal(var1.x, var1.y, var2.x, var2.y);
   }

   public void setFrameFromCenter(float var1, float var2, float var3, float var4) {
      float var5 = Math.abs(var3 - var1);
      float var6 = Math.abs(var4 - var2);
      this.setFrame(var1 - var5, var2 - var6, var5 * 2.0F, var6 * 2.0F);
   }

   public void setFrameFromCenter(Point2D var1, Point2D var2) {
      this.setFrameFromCenter(var1.x, var1.y, var2.x, var2.y);
   }

   public boolean contains(Point2D var1) {
      return this.contains(var1.x, var1.y);
   }

   public RectBounds getBounds() {
      float var1 = this.getWidth();
      float var2 = this.getHeight();
      if (!(var1 < 0.0F) && !(var2 < 0.0F)) {
         float var3 = this.getX();
         float var4 = this.getY();
         float var5 = (float)Math.floor((double)var3);
         float var6 = (float)Math.floor((double)var4);
         float var7 = (float)Math.ceil((double)(var3 + var1));
         float var8 = (float)Math.ceil((double)(var4 + var2));
         return new RectBounds(var5, var6, var7, var8);
      } else {
         return new RectBounds();
      }
   }

   public PathIterator getPathIterator(BaseTransform var1, float var2) {
      return new FlatteningPathIterator(this.getPathIterator(var1), var2);
   }

   public String toString() {
      return this.getClass().getName() + "[x=" + this.getX() + ",y=" + this.getY() + ",w=" + this.getWidth() + ",h=" + this.getHeight() + "]";
   }
}
