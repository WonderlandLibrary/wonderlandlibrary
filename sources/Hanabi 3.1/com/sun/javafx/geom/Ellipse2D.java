package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public class Ellipse2D extends RectangularShape {
   public float x;
   public float y;
   public float width;
   public float height;

   public Ellipse2D() {
   }

   public Ellipse2D(float var1, float var2, float var3, float var4) {
      this.setFrame(var1, var2, var3, var4);
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public boolean isEmpty() {
      return this.width <= 0.0F || this.height <= 0.0F;
   }

   public void setFrame(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
   }

   public RectBounds getBounds() {
      return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
   }

   public boolean contains(float var1, float var2) {
      float var3 = this.width;
      if (var3 <= 0.0F) {
         return false;
      } else {
         float var4 = (var1 - this.x) / var3 - 0.5F;
         float var5 = this.height;
         if (var5 <= 0.0F) {
            return false;
         } else {
            float var6 = (var2 - this.y) / var5 - 0.5F;
            return var4 * var4 + var6 * var6 < 0.25F;
         }
      }
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var5 = this.width;
         if (var5 <= 0.0F) {
            return false;
         } else {
            float var6 = (var1 - this.x) / var5 - 0.5F;
            float var7 = var6 + var3 / var5;
            float var8 = this.height;
            if (var8 <= 0.0F) {
               return false;
            } else {
               float var9 = (var2 - this.y) / var8 - 0.5F;
               float var10 = var9 + var4 / var8;
               float var11;
               if (var6 > 0.0F) {
                  var11 = var6;
               } else if (var7 < 0.0F) {
                  var11 = var7;
               } else {
                  var11 = 0.0F;
               }

               float var12;
               if (var9 > 0.0F) {
                  var12 = var9;
               } else if (var10 < 0.0F) {
                  var12 = var10;
               } else {
                  var12 = 0.0F;
               }

               return var11 * var11 + var12 * var12 < 0.25F;
            }
         }
      } else {
         return false;
      }
   }

   public boolean contains(float var1, float var2, float var3, float var4) {
      return this.contains(var1, var2) && this.contains(var1 + var3, var2) && this.contains(var1, var2 + var4) && this.contains(var1 + var3, var2 + var4);
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      return new EllipseIterator(this, var1);
   }

   public Ellipse2D copy() {
      return new Ellipse2D(this.x, this.y, this.width, this.height);
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.x);
      var1 += Float.floatToIntBits(this.y) * 37;
      var1 += Float.floatToIntBits(this.width) * 43;
      var1 += Float.floatToIntBits(this.height) * 47;
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Ellipse2D)) {
         return false;
      } else {
         Ellipse2D var2 = (Ellipse2D)var1;
         return this.x == var2.x && this.y == var2.y && this.width == var2.width && this.height == var2.height;
      }
   }
}
