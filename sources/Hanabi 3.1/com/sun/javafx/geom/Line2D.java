package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public class Line2D extends Shape {
   public float x1;
   public float y1;
   public float x2;
   public float y2;

   public Line2D() {
   }

   public Line2D(float var1, float var2, float var3, float var4) {
      this.setLine(var1, var2, var3, var4);
   }

   public Line2D(Point2D var1, Point2D var2) {
      this.setLine(var1, var2);
   }

   public void setLine(float var1, float var2, float var3, float var4) {
      this.x1 = var1;
      this.y1 = var2;
      this.x2 = var3;
      this.y2 = var4;
   }

   public void setLine(Point2D var1, Point2D var2) {
      this.setLine(var1.x, var1.y, var2.x, var2.y);
   }

   public void setLine(Line2D var1) {
      this.setLine(var1.x1, var1.y1, var1.x2, var1.y2);
   }

   public RectBounds getBounds() {
      RectBounds var1 = new RectBounds();
      var1.setBoundsAndSort(this.x1, this.y1, this.x2, this.y2);
      return var1;
   }

   public boolean contains(float var1, float var2) {
      return false;
   }

   public boolean contains(float var1, float var2, float var3, float var4) {
      return false;
   }

   public boolean contains(Point2D var1) {
      return false;
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      int var6;
      if ((var6 = outcode(var1, var2, var3, var4, this.x2, this.y2)) == 0) {
         return true;
      } else {
         float var7 = this.x1;
         float var8 = this.y1;

         int var5;
         while((var5 = outcode(var1, var2, var3, var4, var7, var8)) != 0) {
            if ((var5 & var6) != 0) {
               return false;
            }

            if ((var5 & 5) != 0) {
               var7 = var1;
               if ((var5 & 4) != 0) {
                  var7 = var1 + var3;
               }

               var8 = this.y1 + (var7 - this.x1) * (this.y2 - this.y1) / (this.x2 - this.x1);
            } else {
               var8 = var2;
               if ((var5 & 8) != 0) {
                  var8 = var2 + var4;
               }

               var7 = this.x1 + (var8 - this.y1) * (this.x2 - this.x1) / (this.y2 - this.y1);
            }
         }

         return true;
      }
   }

   public static int relativeCCW(float var0, float var1, float var2, float var3, float var4, float var5) {
      var2 -= var0;
      var3 -= var1;
      var4 -= var0;
      var5 -= var1;
      float var6 = var4 * var3 - var5 * var2;
      if (var6 == 0.0F) {
         var6 = var4 * var2 + var5 * var3;
         if (var6 > 0.0F) {
            var4 -= var2;
            var5 -= var3;
            var6 = var4 * var2 + var5 * var3;
            if (var6 < 0.0F) {
               var6 = 0.0F;
            }
         }
      }

      return var6 < 0.0F ? -1 : (var6 > 0.0F ? 1 : 0);
   }

   public int relativeCCW(float var1, float var2) {
      return relativeCCW(this.x1, this.y1, this.x2, this.y2, var1, var2);
   }

   public int relativeCCW(Point2D var1) {
      return relativeCCW(this.x1, this.y1, this.x2, this.y2, var1.x, var1.y);
   }

   public static boolean linesIntersect(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      return relativeCCW(var0, var1, var2, var3, var4, var5) * relativeCCW(var0, var1, var2, var3, var6, var7) <= 0 && relativeCCW(var4, var5, var6, var7, var0, var1) * relativeCCW(var4, var5, var6, var7, var2, var3) <= 0;
   }

   public boolean intersectsLine(float var1, float var2, float var3, float var4) {
      return linesIntersect(var1, var2, var3, var4, this.x1, this.y1, this.x2, this.y2);
   }

   public boolean intersectsLine(Line2D var1) {
      return linesIntersect(var1.x1, var1.y1, var1.x2, var1.y2, this.x1, this.y1, this.x2, this.y2);
   }

   public static float ptSegDistSq(float var0, float var1, float var2, float var3, float var4, float var5) {
      var2 -= var0;
      var3 -= var1;
      var4 -= var0;
      var5 -= var1;
      float var6 = var4 * var2 + var5 * var3;
      float var7;
      if (var6 <= 0.0F) {
         var7 = 0.0F;
      } else {
         var4 = var2 - var4;
         var5 = var3 - var5;
         var6 = var4 * var2 + var5 * var3;
         if (var6 <= 0.0F) {
            var7 = 0.0F;
         } else {
            var7 = var6 * var6 / (var2 * var2 + var3 * var3);
         }
      }

      float var8 = var4 * var4 + var5 * var5 - var7;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      }

      return var8;
   }

   public static float ptSegDist(float var0, float var1, float var2, float var3, float var4, float var5) {
      return (float)Math.sqrt((double)ptSegDistSq(var0, var1, var2, var3, var4, var5));
   }

   public float ptSegDistSq(float var1, float var2) {
      return ptSegDistSq(this.x1, this.y1, this.x2, this.y2, var1, var2);
   }

   public float ptSegDistSq(Point2D var1) {
      return ptSegDistSq(this.x1, this.y1, this.x2, this.y2, var1.x, var1.y);
   }

   public double ptSegDist(float var1, float var2) {
      return (double)ptSegDist(this.x1, this.y1, this.x2, this.y2, var1, var2);
   }

   public float ptSegDist(Point2D var1) {
      return ptSegDist(this.x1, this.y1, this.x2, this.y2, var1.x, var1.y);
   }

   public static float ptLineDistSq(float var0, float var1, float var2, float var3, float var4, float var5) {
      var2 -= var0;
      var3 -= var1;
      var4 -= var0;
      var5 -= var1;
      float var6 = var4 * var2 + var5 * var3;
      float var7 = var6 * var6 / (var2 * var2 + var3 * var3);
      float var8 = var4 * var4 + var5 * var5 - var7;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      }

      return var8;
   }

   public static float ptLineDist(float var0, float var1, float var2, float var3, float var4, float var5) {
      return (float)Math.sqrt((double)ptLineDistSq(var0, var1, var2, var3, var4, var5));
   }

   public float ptLineDistSq(float var1, float var2) {
      return ptLineDistSq(this.x1, this.y1, this.x2, this.y2, var1, var2);
   }

   public float ptLineDistSq(Point2D var1) {
      return ptLineDistSq(this.x1, this.y1, this.x2, this.y2, var1.x, var1.y);
   }

   public float ptLineDist(float var1, float var2) {
      return ptLineDist(this.x1, this.y1, this.x2, this.y2, var1, var2);
   }

   public float ptLineDist(Point2D var1) {
      return ptLineDist(this.x1, this.y1, this.x2, this.y2, var1.x, var1.y);
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      return new LineIterator(this, var1);
   }

   public PathIterator getPathIterator(BaseTransform var1, float var2) {
      return new LineIterator(this, var1);
   }

   public Line2D copy() {
      return new Line2D(this.x1, this.y1, this.x2, this.y2);
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.x1);
      var1 += Float.floatToIntBits(this.y1) * 37;
      var1 += Float.floatToIntBits(this.x2) * 43;
      var1 += Float.floatToIntBits(this.y2) * 47;
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Line2D)) {
         return false;
      } else {
         Line2D var2 = (Line2D)var1;
         return this.x1 == var2.x1 && this.y1 == var2.y1 && this.x2 == var2.x2 && this.y2 == var2.y2;
      }
   }
}
