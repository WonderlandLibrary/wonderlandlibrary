package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public class RoundRectangle2D extends RectangularShape {
   public float x;
   public float y;
   public float width;
   public float height;
   public float arcWidth;
   public float arcHeight;

   public RoundRectangle2D() {
   }

   public RoundRectangle2D(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.setRoundRect(var1, var2, var3, var4, var5, var6);
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

   public void setRoundRect(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
      this.arcWidth = var5;
      this.arcHeight = var6;
   }

   public RectBounds getBounds() {
      return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
   }

   public void setRoundRect(RoundRectangle2D var1) {
      this.setRoundRect(var1.x, var1.y, var1.width, var1.height, var1.arcWidth, var1.arcHeight);
   }

   public void setFrame(float var1, float var2, float var3, float var4) {
      this.setRoundRect(var1, var2, var3, var4, this.arcWidth, this.arcHeight);
   }

   public boolean contains(float var1, float var2) {
      if (this.isEmpty()) {
         return false;
      } else {
         float var3 = this.x;
         float var4 = this.y;
         float var5 = var3 + this.width;
         float var6 = var4 + this.height;
         if (!(var1 < var3) && !(var2 < var4) && !(var1 >= var5) && !(var2 >= var6)) {
            float var7 = Math.min(this.width, Math.abs(this.arcWidth)) / 2.0F;
            float var8 = Math.min(this.height, Math.abs(this.arcHeight)) / 2.0F;
            if (var1 >= (var3 += var7) && var1 < (var3 = var5 - var7)) {
               return true;
            } else if (var2 >= (var4 += var8) && var2 < (var4 = var6 - var8)) {
               return true;
            } else {
               var1 = (var1 - var3) / var7;
               var2 = (var2 - var4) / var8;
               return (double)(var1 * var1 + var2 * var2) <= 1.0;
            }
         } else {
            return false;
         }
      }
   }

   private int classify(float var1, float var2, float var3, float var4) {
      if (var1 < var2) {
         return 0;
      } else if (var1 < var2 + var4) {
         return 1;
      } else if (var1 < var3 - var4) {
         return 2;
      } else {
         return var1 < var3 ? 3 : 4;
      }
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      if (!this.isEmpty() && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var5 = this.x;
         float var6 = this.y;
         float var7 = var5 + this.width;
         float var8 = var6 + this.height;
         if (!(var1 + var3 <= var5) && !(var1 >= var7) && !(var2 + var4 <= var6) && !(var2 >= var8)) {
            float var9 = Math.min(this.width, Math.abs(this.arcWidth)) / 2.0F;
            float var10 = Math.min(this.height, Math.abs(this.arcHeight)) / 2.0F;
            int var11 = this.classify(var1, var5, var7, var9);
            int var12 = this.classify(var1 + var3, var5, var7, var9);
            int var13 = this.classify(var2, var6, var8, var10);
            int var14 = this.classify(var2 + var4, var6, var8, var10);
            if (var11 != 2 && var12 != 2 && var13 != 2 && var14 != 2) {
               if ((var11 >= 2 || var12 <= 2) && (var13 >= 2 || var14 <= 2)) {
                  var1 = var12 == 1 ? var1 + var3 - (var5 + var9) : var1 - (var7 - var9);
                  var2 = var14 == 1 ? var2 + var4 - (var6 + var10) : var2 - (var8 - var10);
                  var1 /= var9;
                  var2 /= var10;
                  return var1 * var1 + var2 * var2 <= 1.0F;
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean contains(float var1, float var2, float var3, float var4) {
      if (!this.isEmpty() && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         return this.contains(var1, var2) && this.contains(var1 + var3, var2) && this.contains(var1, var2 + var4) && this.contains(var1 + var3, var2 + var4);
      } else {
         return false;
      }
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      return new RoundRectIterator(this, var1);
   }

   public RoundRectangle2D copy() {
      return new RoundRectangle2D(this.x, this.y, this.width, this.height, this.arcWidth, this.arcHeight);
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.x);
      var1 += Float.floatToIntBits(this.y) * 37;
      var1 += Float.floatToIntBits(this.width) * 43;
      var1 += Float.floatToIntBits(this.height) * 47;
      var1 += Float.floatToIntBits(this.arcWidth) * 53;
      var1 += Float.floatToIntBits(this.arcHeight) * 59;
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof RoundRectangle2D)) {
         return false;
      } else {
         RoundRectangle2D var2 = (RoundRectangle2D)var1;
         return this.x == var2.x && this.y == var2.y && this.width == var2.width && this.height == var2.height && this.arcWidth == var2.arcWidth && this.arcHeight == var2.arcHeight;
      }
   }
}
