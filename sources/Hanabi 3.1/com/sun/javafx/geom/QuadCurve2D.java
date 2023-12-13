package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public class QuadCurve2D extends Shape {
   public float x1;
   public float y1;
   public float ctrlx;
   public float ctrly;
   public float x2;
   public float y2;
   private static final int BELOW = -2;
   private static final int LOWEDGE = -1;
   private static final int INSIDE = 0;
   private static final int HIGHEDGE = 1;
   private static final int ABOVE = 2;

   public QuadCurve2D() {
   }

   public QuadCurve2D(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.setCurve(var1, var2, var3, var4, var5, var6);
   }

   public void setCurve(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.x1 = var1;
      this.y1 = var2;
      this.ctrlx = var3;
      this.ctrly = var4;
      this.x2 = var5;
      this.y2 = var6;
   }

   public RectBounds getBounds() {
      float var1 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
      float var2 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
      float var3 = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
      float var4 = Math.max(Math.max(this.y1, this.y2), this.ctrly);
      return new RectBounds(var1, var2, var3, var4);
   }

   public CubicCurve2D toCubic() {
      return new CubicCurve2D(this.x1, this.y1, (this.x1 + 2.0F * this.ctrlx) / 3.0F, (this.y1 + 2.0F * this.ctrly) / 3.0F, (2.0F * this.ctrlx + this.x2) / 3.0F, (2.0F * this.ctrly + this.y2) / 3.0F, this.x2, this.y2);
   }

   public void setCurve(float[] var1, int var2) {
      this.setCurve(var1[var2 + 0], var1[var2 + 1], var1[var2 + 2], var1[var2 + 3], var1[var2 + 4], var1[var2 + 5]);
   }

   public void setCurve(Point2D var1, Point2D var2, Point2D var3) {
      this.setCurve(var1.x, var1.y, var2.x, var2.y, var3.x, var3.y);
   }

   public void setCurve(Point2D[] var1, int var2) {
      this.setCurve(var1[var2 + 0].x, var1[var2 + 0].y, var1[var2 + 1].x, var1[var2 + 1].y, var1[var2 + 2].x, var1[var2 + 2].y);
   }

   public void setCurve(QuadCurve2D var1) {
      this.setCurve(var1.x1, var1.y1, var1.ctrlx, var1.ctrly, var1.x2, var1.y2);
   }

   public static float getFlatnessSq(float var0, float var1, float var2, float var3, float var4, float var5) {
      return Line2D.ptSegDistSq(var0, var1, var4, var5, var2, var3);
   }

   public static float getFlatness(float var0, float var1, float var2, float var3, float var4, float var5) {
      return Line2D.ptSegDist(var0, var1, var4, var5, var2, var3);
   }

   public static float getFlatnessSq(float[] var0, int var1) {
      return Line2D.ptSegDistSq(var0[var1 + 0], var0[var1 + 1], var0[var1 + 4], var0[var1 + 5], var0[var1 + 2], var0[var1 + 3]);
   }

   public static float getFlatness(float[] var0, int var1) {
      return Line2D.ptSegDist(var0[var1 + 0], var0[var1 + 1], var0[var1 + 4], var0[var1 + 5], var0[var1 + 2], var0[var1 + 3]);
   }

   public float getFlatnessSq() {
      return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
   }

   public float getFlatness() {
      return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
   }

   public void subdivide(QuadCurve2D var1, QuadCurve2D var2) {
      subdivide(this, var1, var2);
   }

   public static void subdivide(QuadCurve2D var0, QuadCurve2D var1, QuadCurve2D var2) {
      float var3 = var0.x1;
      float var4 = var0.y1;
      float var5 = var0.ctrlx;
      float var6 = var0.ctrly;
      float var7 = var0.x2;
      float var8 = var0.y2;
      float var9 = (var3 + var5) / 2.0F;
      float var10 = (var4 + var6) / 2.0F;
      float var11 = (var7 + var5) / 2.0F;
      float var12 = (var8 + var6) / 2.0F;
      var5 = (var9 + var11) / 2.0F;
      var6 = (var10 + var12) / 2.0F;
      if (var1 != null) {
         var1.setCurve(var3, var4, var9, var10, var5, var6);
      }

      if (var2 != null) {
         var2.setCurve(var5, var6, var11, var12, var7, var8);
      }

   }

   public static void subdivide(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5) {
      float var6 = var0[var1 + 0];
      float var7 = var0[var1 + 1];
      float var8 = var0[var1 + 2];
      float var9 = var0[var1 + 3];
      float var10 = var0[var1 + 4];
      float var11 = var0[var1 + 5];
      if (var2 != null) {
         var2[var3 + 0] = var6;
         var2[var3 + 1] = var7;
      }

      if (var4 != null) {
         var4[var5 + 4] = var10;
         var4[var5 + 5] = var11;
      }

      var6 = (var6 + var8) / 2.0F;
      var7 = (var7 + var9) / 2.0F;
      var10 = (var10 + var8) / 2.0F;
      var11 = (var11 + var9) / 2.0F;
      var8 = (var6 + var10) / 2.0F;
      var9 = (var7 + var11) / 2.0F;
      if (var2 != null) {
         var2[var3 + 2] = var6;
         var2[var3 + 3] = var7;
         var2[var3 + 4] = var8;
         var2[var3 + 5] = var9;
      }

      if (var4 != null) {
         var4[var5 + 0] = var8;
         var4[var5 + 1] = var9;
         var4[var5 + 2] = var10;
         var4[var5 + 3] = var11;
      }

   }

   public static int solveQuadratic(float[] var0) {
      return solveQuadratic(var0, var0);
   }

   public static int solveQuadratic(float[] var0, float[] var1) {
      float var2 = var0[2];
      float var3 = var0[1];
      float var4 = var0[0];
      int var5 = 0;
      if (var2 == 0.0F) {
         if (var3 == 0.0F) {
            return -1;
         }

         var1[var5++] = -var4 / var3;
      } else {
         float var6 = var3 * var3 - 4.0F * var2 * var4;
         if (var6 < 0.0F) {
            return 0;
         }

         var6 = (float)Math.sqrt((double)var6);
         if (var3 < 0.0F) {
            var6 = -var6;
         }

         float var7 = (var3 + var6) / -2.0F;
         var1[var5++] = var7 / var2;
         if (var7 != 0.0F) {
            var1[var5++] = var4 / var7;
         }
      }

      return var5;
   }

   public boolean contains(float var1, float var2) {
      float var3 = this.x1;
      float var4 = this.y1;
      float var5 = this.ctrlx;
      float var6 = this.ctrly;
      float var7 = this.x2;
      float var8 = this.y2;
      float var9 = var3 - 2.0F * var5 + var7;
      float var10 = var4 - 2.0F * var6 + var8;
      float var11 = var1 - var3;
      float var12 = var2 - var4;
      float var13 = var7 - var3;
      float var14 = var8 - var4;
      float var15 = (var11 * var10 - var12 * var9) / (var13 * var10 - var14 * var9);
      if (!(var15 < 0.0F) && !(var15 > 1.0F) && var15 == var15) {
         float var16 = var9 * var15 * var15 + 2.0F * (var5 - var3) * var15 + var3;
         float var17 = var10 * var15 * var15 + 2.0F * (var6 - var4) * var15 + var4;
         float var18 = var13 * var15 + var3;
         float var19 = var14 * var15 + var4;
         return var1 >= var16 && var1 < var18 || var1 >= var18 && var1 < var16 || var2 >= var17 && var2 < var19 || var2 >= var19 && var2 < var17;
      } else {
         return false;
      }
   }

   public boolean contains(Point2D var1) {
      return this.contains(var1.x, var1.y);
   }

   private static void fillEqn(float[] var0, float var1, float var2, float var3, float var4) {
      var0[0] = var2 - var1;
      var0[1] = var3 + var3 - var2 - var2;
      var0[2] = var2 - var3 - var3 + var4;
   }

   private static int evalQuadratic(float[] var0, int var1, boolean var2, boolean var3, float[] var4, float var5, float var6, float var7) {
      int var8 = 0;

      for(int var9 = 0; var9 < var1; ++var9) {
         float var10 = var0[var9];
         if (var2) {
            if (!(var10 >= 0.0F)) {
               continue;
            }
         } else if (!(var10 > 0.0F)) {
            continue;
         }

         if (var3) {
            if (!(var10 <= 1.0F)) {
               continue;
            }
         } else if (!(var10 < 1.0F)) {
            continue;
         }

         if (var4 == null || var4[1] + 2.0F * var4[2] * var10 != 0.0F) {
            float var11 = 1.0F - var10;
            var0[var8++] = var5 * var11 * var11 + 2.0F * var6 * var10 * var11 + var7 * var10 * var10;
         }
      }

      return var8;
   }

   private static int getTag(float var0, float var1, float var2) {
      if (var0 <= var1) {
         return var0 < var1 ? -2 : -1;
      } else if (var0 >= var2) {
         return var0 > var2 ? 2 : 1;
      } else {
         return 0;
      }
   }

   private static boolean inwards(int var0, int var1, int var2) {
      switch (var0) {
         case -2:
         case 2:
         default:
            return false;
         case -1:
            return var1 >= 0 || var2 >= 0;
         case 0:
            return true;
         case 1:
            return var1 <= 0 || var2 <= 0;
      }
   }

   public boolean intersects(float var1, float var2, float var3, float var4) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var5 = this.x1;
         float var6 = this.y1;
         int var7 = getTag(var5, var1, var1 + var3);
         int var8 = getTag(var6, var2, var2 + var4);
         if (var7 == 0 && var8 == 0) {
            return true;
         } else {
            float var9 = this.x2;
            float var10 = this.y2;
            int var11 = getTag(var9, var1, var1 + var3);
            int var12 = getTag(var10, var2, var2 + var4);
            if (var11 == 0 && var12 == 0) {
               return true;
            } else {
               float var13 = this.ctrlx;
               float var14 = this.ctrly;
               int var15 = getTag(var13, var1, var1 + var3);
               int var16 = getTag(var14, var2, var2 + var4);
               if (var7 < 0 && var11 < 0 && var15 < 0) {
                  return false;
               } else if (var8 < 0 && var12 < 0 && var16 < 0) {
                  return false;
               } else if (var7 > 0 && var11 > 0 && var15 > 0) {
                  return false;
               } else if (var8 > 0 && var12 > 0 && var16 > 0) {
                  return false;
               } else if (inwards(var7, var11, var15) && inwards(var8, var12, var16)) {
                  return true;
               } else if (inwards(var11, var7, var15) && inwards(var12, var8, var16)) {
                  return true;
               } else {
                  boolean var17 = var7 * var11 <= 0;
                  boolean var18 = var8 * var12 <= 0;
                  if (var7 == 0 && var11 == 0 && var18) {
                     return true;
                  } else if (var8 == 0 && var12 == 0 && var17) {
                     return true;
                  } else {
                     float[] var19 = new float[3];
                     float[] var20 = new float[3];
                     if (!var18) {
                        fillEqn(var19, var8 < 0 ? var2 : var2 + var4, var6, var14, var10);
                        return solveQuadratic(var19, var20) == 2 && evalQuadratic(var20, 2, true, true, (float[])null, var5, var13, var9) == 2 && getTag(var20[0], var1, var1 + var3) * getTag(var20[1], var1, var1 + var3) <= 0;
                     } else if (!var17) {
                        fillEqn(var19, var7 < 0 ? var1 : var1 + var3, var5, var13, var9);
                        return solveQuadratic(var19, var20) == 2 && evalQuadratic(var20, 2, true, true, (float[])null, var6, var14, var10) == 2 && getTag(var20[0], var2, var2 + var4) * getTag(var20[1], var2, var2 + var4) <= 0;
                     } else {
                        float var21 = var9 - var5;
                        float var22 = var10 - var6;
                        float var23 = var10 * var5 - var9 * var6;
                        int var24;
                        if (var8 == 0) {
                           var24 = var7;
                        } else {
                           var24 = getTag((var23 + var21 * (var8 < 0 ? var2 : var2 + var4)) / var22, var1, var1 + var3);
                        }

                        int var25;
                        if (var12 == 0) {
                           var25 = var11;
                        } else {
                           var25 = getTag((var23 + var21 * (var12 < 0 ? var2 : var2 + var4)) / var22, var1, var1 + var3);
                        }

                        if (var24 * var25 <= 0) {
                           return true;
                        } else {
                           var24 = var24 * var7 <= 0 ? var8 : var12;
                           fillEqn(var19, var25 < 0 ? var1 : var1 + var3, var5, var13, var9);
                           int var26 = solveQuadratic(var19, var20);
                           evalQuadratic(var20, var26, true, true, (float[])null, var6, var14, var10);
                           var25 = getTag(var20[0], var2, var2 + var4);
                           return var24 * var25 <= 0;
                        }
                     }
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   public boolean contains(float var1, float var2, float var3, float var4) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         return this.contains(var1, var2) && this.contains(var1 + var3, var2) && this.contains(var1 + var3, var2 + var4) && this.contains(var1, var2 + var4);
      } else {
         return false;
      }
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      return new QuadIterator(this, var1);
   }

   public PathIterator getPathIterator(BaseTransform var1, float var2) {
      return new FlatteningPathIterator(this.getPathIterator(var1), var2);
   }

   public QuadCurve2D copy() {
      return new QuadCurve2D(this.x1, this.y1, this.ctrlx, this.ctrly, this.x2, this.y2);
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.x1);
      var1 += Float.floatToIntBits(this.y1) * 37;
      var1 += Float.floatToIntBits(this.x2) * 43;
      var1 += Float.floatToIntBits(this.y2) * 47;
      var1 += Float.floatToIntBits(this.ctrlx) * 53;
      var1 += Float.floatToIntBits(this.ctrly) * 59;
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof QuadCurve2D)) {
         return false;
      } else {
         QuadCurve2D var2 = (QuadCurve2D)var1;
         return this.x1 == var2.x1 && this.y1 == var2.y1 && this.x2 == var2.x2 && this.y2 == var2.y2 && this.ctrlx == var2.ctrlx && this.ctrly == var2.ctrly;
      }
   }
}
