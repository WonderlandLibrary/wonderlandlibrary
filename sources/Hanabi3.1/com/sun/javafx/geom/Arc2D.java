package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public class Arc2D extends RectangularShape {
   public static final int OPEN = 0;
   public static final int CHORD = 1;
   public static final int PIE = 2;
   private int type;
   public float x;
   public float y;
   public float width;
   public float height;
   public float start;
   public float extent;

   public Arc2D() {
      this(0);
   }

   public Arc2D(int var1) {
      this.setArcType(var1);
   }

   public Arc2D(float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      this(var7);
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
      this.start = var5;
      this.extent = var6;
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

   public void setArc(float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      this.setArcType(var7);
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
      this.start = var5;
      this.extent = var6;
   }

   public int getArcType() {
      return this.type;
   }

   public Point2D getStartPoint() {
      double var1 = Math.toRadians((double)(-this.start));
      double var3 = (double)this.x + (Math.cos(var1) * 0.5 + 0.5) * (double)this.width;
      double var5 = (double)this.y + (Math.sin(var1) * 0.5 + 0.5) * (double)this.height;
      return new Point2D((float)var3, (float)var5);
   }

   public Point2D getEndPoint() {
      double var1 = Math.toRadians((double)(-this.start - this.extent));
      double var3 = (double)this.x + (Math.cos(var1) * 0.5 + 0.5) * (double)this.width;
      double var5 = (double)this.y + (Math.sin(var1) * 0.5 + 0.5) * (double)this.height;
      return new Point2D((float)var3, (float)var5);
   }

   public void setArc(Point2D var1, Dimension2D var2, float var3, float var4, int var5) {
      this.setArc(var1.x, var1.y, var2.width, var2.height, var3, var4, var5);
   }

   public void setArc(Arc2D var1) {
      this.setArc(var1.x, var1.y, var1.width, var1.height, var1.start, var1.extent, var1.type);
   }

   public void setArcByCenter(float var1, float var2, float var3, float var4, float var5, int var6) {
      this.setArc(var1 - var3, var2 - var3, var3 * 2.0F, var3 * 2.0F, var4, var5, var6);
   }

   public void setArcByTangent(Point2D var1, Point2D var2, Point2D var3, float var4) {
      double var5 = Math.atan2((double)(var1.y - var2.y), (double)(var1.x - var2.x));
      double var7 = Math.atan2((double)(var3.y - var2.y), (double)(var3.x - var2.x));
      double var9 = var7 - var5;
      if (var9 > Math.PI) {
         var7 -= 6.283185307179586;
      } else if (var9 < -3.141592653589793) {
         var7 += 6.283185307179586;
      }

      double var11 = (var5 + var7) / 2.0;
      double var13 = Math.abs(var7 - var11);
      double var15 = (double)var4 / Math.sin(var13);
      double var17 = (double)var2.x + var15 * Math.cos(var11);
      double var19 = (double)var2.y + var15 * Math.sin(var11);
      if (var5 < var7) {
         --var5;
         ++var7;
      } else {
         ++var5;
         --var7;
      }

      var5 = Math.toDegrees(-var5);
      var7 = Math.toDegrees(-var7);
      var9 = var7 - var5;
      if (var9 < 0.0) {
         var9 += 360.0;
      } else {
         var9 -= 360.0;
      }

      this.setArcByCenter((float)var17, (float)var19, var4, (float)var5, (float)var9, this.type);
   }

   public void setAngleStart(Point2D var1) {
      double var2 = (double)(this.height * (var1.x - this.getCenterX()));
      double var4 = (double)(this.width * (var1.y - this.getCenterY()));
      this.start = (float)(-Math.toDegrees(Math.atan2(var4, var2)));
   }

   public void setAngles(float var1, float var2, float var3, float var4) {
      double var5 = (double)this.getCenterX();
      double var7 = (double)this.getCenterY();
      double var9 = (double)this.width;
      double var11 = (double)this.height;
      double var13 = Math.atan2(var9 * (var7 - (double)var2), var11 * ((double)var1 - var5));
      double var15 = Math.atan2(var9 * (var7 - (double)var4), var11 * ((double)var3 - var5));
      var15 -= var13;
      if (var15 <= 0.0) {
         var15 += 6.283185307179586;
      }

      this.start = (float)Math.toDegrees(var13);
      this.extent = (float)Math.toDegrees(var15);
   }

   public void setAngles(Point2D var1, Point2D var2) {
      this.setAngles(var1.x, var1.y, var2.x, var2.y);
   }

   public void setArcType(int var1) {
      if (var1 >= 0 && var1 <= 2) {
         this.type = var1;
      } else {
         throw new IllegalArgumentException("invalid type for Arc: " + var1);
      }
   }

   public void setFrame(float var1, float var2, float var3, float var4) {
      this.setArc(var1, var2, var3, var4, this.start, this.extent, this.type);
   }

   public RectBounds getBounds() {
      if (this.isEmpty()) {
         return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
      } else {
         double var1;
         double var3;
         double var5;
         double var7;
         if (this.getArcType() == 2) {
            var7 = 0.0;
            var5 = 0.0;
            var3 = 0.0;
            var1 = 0.0;
         } else {
            var3 = 1.0;
            var1 = 1.0;
            var7 = -1.0;
            var5 = -1.0;
         }

         double var9 = 0.0;

         for(int var11 = 0; var11 < 6; ++var11) {
            if (var11 < 4) {
               var9 += 90.0;
               if (!this.containsAngle((float)var9)) {
                  continue;
               }
            } else if (var11 == 4) {
               var9 = (double)this.start;
            } else {
               var9 += (double)this.extent;
            }

            double var12 = Math.toRadians(-var9);
            double var14 = Math.cos(var12);
            double var16 = Math.sin(var12);
            var1 = Math.min(var1, var14);
            var3 = Math.min(var3, var16);
            var5 = Math.max(var5, var14);
            var7 = Math.max(var7, var16);
         }

         double var18 = (double)this.width;
         double var13 = (double)this.height;
         var5 = (double)this.x + (var5 * 0.5 + 0.5) * var18;
         var7 = (double)this.y + (var7 * 0.5 + 0.5) * var13;
         var1 = (double)this.x + (var1 * 0.5 + 0.5) * var18;
         var3 = (double)this.y + (var3 * 0.5 + 0.5) * var13;
         return new RectBounds((float)var1, (float)var3, (float)var5, (float)var7);
      }
   }

   static float normalizeDegrees(double var0) {
      if (var0 > 180.0) {
         if (var0 <= 540.0) {
            var0 -= 360.0;
         } else {
            var0 = Math.IEEEremainder(var0, 360.0);
            if (var0 == -180.0) {
               var0 = 180.0;
            }
         }
      } else if (var0 <= -180.0) {
         if (var0 > -540.0) {
            var0 += 360.0;
         } else {
            var0 = Math.IEEEremainder(var0, 360.0);
            if (var0 == -180.0) {
               var0 = 180.0;
            }
         }
      }

      return (float)var0;
   }

   public boolean containsAngle(float var1) {
      double var2 = (double)this.extent;
      boolean var4 = var2 < 0.0;
      if (var4) {
         var2 = -var2;
      }

      if (var2 >= 360.0) {
         return true;
      } else {
         var1 = normalizeDegrees((double)var1) - normalizeDegrees((double)this.start);
         if (var4) {
            var1 = -var1;
         }

         if ((double)var1 < 0.0) {
            var1 = (float)((double)var1 + 360.0);
         }

         return (double)var1 >= 0.0 && (double)var1 < var2;
      }
   }

   public boolean contains(float var1, float var2) {
      double var3 = (double)this.width;
      if (var3 <= 0.0) {
         return false;
      } else {
         double var5 = (double)(var1 - this.x) / var3 - 0.5;
         double var7 = (double)this.height;
         if (var7 <= 0.0) {
            return false;
         } else {
            double var9 = (double)(var2 - this.y) / var7 - 0.5;
            double var11 = var5 * var5 + var9 * var9;
            if (var11 >= 0.25) {
               return false;
            } else {
               double var13 = (double)Math.abs(this.extent);
               if (var13 >= 360.0) {
                  return true;
               } else {
                  boolean var15 = this.containsAngle((float)(-Math.toDegrees(Math.atan2(var9, var5))));
                  if (this.type == 2) {
                     return var15;
                  } else {
                     if (var15) {
                        if (var13 >= 180.0) {
                           return true;
                        }
                     } else if (var13 <= 180.0) {
                        return false;
                     }

                     double var16 = Math.toRadians((double)(-this.start));
                     double var18 = Math.cos(var16);
                     double var20 = Math.sin(var16);
                     var16 += Math.toRadians((double)(-this.extent));
                     double var22 = Math.cos(var16);
                     double var24 = Math.sin(var16);
                     boolean var26 = Line2D.relativeCCW((float)var18, (float)var20, (float)var22, (float)var24, (float)(2.0 * var5), (float)(2.0 * var9)) * Line2D.relativeCCW((float)var18, (float)var20, (float)var22, (float)var24, 0.0F, 0.0F) >= 0;
                     return var15 ? !var26 : var26;
                  }
               }
            }
         }
      }
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      float var5 = this.width;
      float var6 = this.height;
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var5 <= 0.0F) && !(var6 <= 0.0F)) {
         float var7 = this.extent;
         if (var7 == 0.0F) {
            return false;
         } else {
            float var8 = this.x;
            float var9 = this.y;
            float var10 = var8 + var5;
            float var11 = var9 + var6;
            float var12 = var1 + var3;
            float var13 = var2 + var4;
            if (!(var1 >= var10) && !(var2 >= var11) && !(var12 <= var8) && !(var13 <= var9)) {
               float var14 = this.getCenterX();
               float var15 = this.getCenterY();
               double var16 = Math.toRadians((double)(-this.start));
               float var18 = (float)((double)this.x + (Math.cos(var16) * 0.5 + 0.5) * (double)this.width);
               float var19 = (float)((double)this.y + (Math.sin(var16) * 0.5 + 0.5) * (double)this.height);
               double var20 = Math.toRadians((double)(-this.start - this.extent));
               float var22 = (float)((double)this.x + (Math.cos(var20) * 0.5 + 0.5) * (double)this.width);
               float var23 = (float)((double)this.y + (Math.sin(var20) * 0.5 + 0.5) * (double)this.height);
               if (var15 >= var2 && var15 <= var13 && (var18 < var12 && var22 < var12 && var14 < var12 && var10 > var1 && this.containsAngle(0.0F) || var18 > var1 && var22 > var1 && var14 > var1 && var8 < var12 && this.containsAngle(180.0F))) {
                  return true;
               } else if (!(var14 >= var1) || !(var14 <= var12) || (!(var19 > var2) || !(var23 > var2) || !(var15 > var2) || !(var9 < var13) || !this.containsAngle(90.0F)) && (!(var19 < var13) || !(var23 < var13) || !(var15 < var13) || !(var11 > var2) || !this.containsAngle(270.0F))) {
                  if (this.type != 2 && !(Math.abs(var7) > 180.0F)) {
                     if (Shape.intersectsLine(var1, var2, var3, var4, var18, var19, var22, var23)) {
                        return true;
                     }
                  } else if (Shape.intersectsLine(var1, var2, var3, var4, var14, var15, var18, var19) || Shape.intersectsLine(var1, var2, var3, var4, var14, var15, var22, var23)) {
                     return true;
                  }

                  if (!this.contains(var1, var2) && !this.contains(var1 + var3, var2) && !this.contains(var1, var2 + var4) && !this.contains(var1 + var3, var2 + var4)) {
                     return false;
                  } else {
                     return true;
                  }
               } else {
                  return true;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public boolean contains(float var1, float var2, float var3, float var4) {
      if (this.contains(var1, var2) && this.contains(var1 + var3, var2) && this.contains(var1, var2 + var4) && this.contains(var1 + var3, var2 + var4)) {
         if (this.type == 2 && !((double)Math.abs(this.extent) <= 180.0)) {
            float var5 = this.getWidth() / 2.0F;
            float var6 = this.getHeight() / 2.0F;
            float var7 = var1 + var5;
            float var8 = var2 + var6;
            float var9 = (float)Math.toRadians((double)(-this.start));
            float var10 = (float)((double)var7 + (double)var5 * Math.cos((double)var9));
            float var11 = (float)((double)var8 + (double)var6 * Math.sin((double)var9));
            if (Shape.intersectsLine(var1, var2, var3, var4, var7, var8, var10, var11)) {
               return false;
            } else {
               var9 += (float)Math.toRadians((double)(-this.extent));
               var10 = (float)((double)var7 + (double)var5 * Math.cos((double)var9));
               var11 = (float)((double)var8 + (double)var6 * Math.sin((double)var9));
               return !Shape.intersectsLine(var1, var2, var3, var4, var7, var8, var10, var11);
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      return new ArcIterator(this, var1);
   }

   public Arc2D copy() {
      return new Arc2D(this.x, this.y, this.width, this.height, this.start, this.extent, this.type);
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.x);
      var1 += Float.floatToIntBits(this.y) * 37;
      var1 += Float.floatToIntBits(this.width) * 43;
      var1 += Float.floatToIntBits(this.height) * 47;
      var1 += Float.floatToIntBits(this.start) * 53;
      var1 += Float.floatToIntBits(this.extent) * 59;
      var1 += this.getArcType() * 61;
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Arc2D)) {
         return false;
      } else {
         Arc2D var2 = (Arc2D)var1;
         return this.x == var2.x && this.y == var2.y && this.width == var2.width && this.height == var2.height && this.start == var2.start && this.extent == var2.extent && this.type == var2.type;
      }
   }
}
