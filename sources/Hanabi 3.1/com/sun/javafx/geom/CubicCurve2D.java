package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Arrays;

public class CubicCurve2D extends Shape {
   public float x1;
   public float y1;
   public float ctrlx1;
   public float ctrly1;
   public float ctrlx2;
   public float ctrly2;
   public float x2;
   public float y2;
   private static final int BELOW = -2;
   private static final int LOWEDGE = -1;
   private static final int INSIDE = 0;
   private static final int HIGHEDGE = 1;
   private static final int ABOVE = 2;

   public CubicCurve2D() {
   }

   public CubicCurve2D(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.setCurve(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void setCurve(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.x1 = var1;
      this.y1 = var2;
      this.ctrlx1 = var3;
      this.ctrly1 = var4;
      this.ctrlx2 = var5;
      this.ctrly2 = var6;
      this.x2 = var7;
      this.y2 = var8;
   }

   public RectBounds getBounds() {
      float var1 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
      float var2 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
      float var3 = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
      float var4 = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
      return new RectBounds(var1, var2, var3, var4);
   }

   public Point2D eval(float var1) {
      Point2D var2 = new Point2D();
      this.eval(var1, var2);
      return var2;
   }

   public void eval(float var1, Point2D var2) {
      var2.setLocation(this.calcX(var1), this.calcY(var1));
   }

   public Point2D evalDt(float var1) {
      Point2D var2 = new Point2D();
      this.evalDt(var1, var2);
      return var2;
   }

   public void evalDt(float var1, Point2D var2) {
      float var4 = 1.0F - var1;
      float var5 = 3.0F * ((this.ctrlx1 - this.x1) * var4 * var4 + 2.0F * (this.ctrlx2 - this.ctrlx1) * var4 * var1 + (this.x2 - this.ctrlx2) * var1 * var1);
      float var6 = 3.0F * ((this.ctrly1 - this.y1) * var4 * var4 + 2.0F * (this.ctrly2 - this.ctrly1) * var4 * var1 + (this.y2 - this.ctrly2) * var1 * var1);
      var2.setLocation(var5, var6);
   }

   public void setCurve(float[] var1, int var2) {
      this.setCurve(var1[var2 + 0], var1[var2 + 1], var1[var2 + 2], var1[var2 + 3], var1[var2 + 4], var1[var2 + 5], var1[var2 + 6], var1[var2 + 7]);
   }

   public void setCurve(Point2D var1, Point2D var2, Point2D var3, Point2D var4) {
      this.setCurve(var1.x, var1.y, var2.x, var2.y, var3.x, var3.y, var4.x, var4.y);
   }

   public void setCurve(Point2D[] var1, int var2) {
      this.setCurve(var1[var2 + 0].x, var1[var2 + 0].y, var1[var2 + 1].x, var1[var2 + 1].y, var1[var2 + 2].x, var1[var2 + 2].y, var1[var2 + 3].x, var1[var2 + 3].y);
   }

   public void setCurve(CubicCurve2D var1) {
      this.setCurve(var1.x1, var1.y1, var1.ctrlx1, var1.ctrly1, var1.ctrlx2, var1.ctrly2, var1.x2, var1.y2);
   }

   public static float getFlatnessSq(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      return Math.max(Line2D.ptSegDistSq(var0, var1, var6, var7, var2, var3), Line2D.ptSegDistSq(var0, var1, var6, var7, var4, var5));
   }

   public static float getFlatness(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      return (float)Math.sqrt((double)getFlatnessSq(var0, var1, var2, var3, var4, var5, var6, var7));
   }

   public static float getFlatnessSq(float[] var0, int var1) {
      return getFlatnessSq(var0[var1 + 0], var0[var1 + 1], var0[var1 + 2], var0[var1 + 3], var0[var1 + 4], var0[var1 + 5], var0[var1 + 6], var0[var1 + 7]);
   }

   public static float getFlatness(float[] var0, int var1) {
      return getFlatness(var0[var1 + 0], var0[var1 + 1], var0[var1 + 2], var0[var1 + 3], var0[var1 + 4], var0[var1 + 5], var0[var1 + 6], var0[var1 + 7]);
   }

   public float getFlatnessSq() {
      return getFlatnessSq(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
   }

   public float getFlatness() {
      return getFlatness(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
   }

   public void subdivide(float var1, CubicCurve2D var2, CubicCurve2D var3) {
      if (var2 != null || var3 != null) {
         float var4 = this.calcX(var1);
         float var5 = this.calcY(var1);
         float var6 = this.x1;
         float var7 = this.y1;
         float var8 = this.ctrlx1;
         float var9 = this.ctrly1;
         float var10 = this.ctrlx2;
         float var11 = this.ctrly2;
         float var12 = this.x2;
         float var13 = this.y2;
         float var14 = 1.0F - var1;
         float var15 = var14 * var8 + var1 * var10;
         float var16 = var14 * var9 + var1 * var11;
         float var19;
         float var20;
         float var21;
         float var22;
         if (var2 != null) {
            var19 = var14 * var6 + var1 * var8;
            var20 = var14 * var7 + var1 * var9;
            var21 = var14 * var19 + var1 * var15;
            var22 = var14 * var20 + var1 * var16;
            var2.setCurve(var6, var7, var19, var20, var21, var22, var4, var5);
         }

         if (var3 != null) {
            var19 = var14 * var10 + var1 * var12;
            var20 = var14 * var11 + var1 * var13;
            var21 = var14 * var15 + var1 * var19;
            var22 = var14 * var16 + var1 * var20;
            var3.setCurve(var4, var5, var21, var22, var19, var20, var12, var13);
         }

      }
   }

   public void subdivide(CubicCurve2D var1, CubicCurve2D var2) {
      subdivide(this, var1, var2);
   }

   public static void subdivide(CubicCurve2D var0, CubicCurve2D var1, CubicCurve2D var2) {
      float var3 = var0.x1;
      float var4 = var0.y1;
      float var5 = var0.ctrlx1;
      float var6 = var0.ctrly1;
      float var7 = var0.ctrlx2;
      float var8 = var0.ctrly2;
      float var9 = var0.x2;
      float var10 = var0.y2;
      float var11 = (var5 + var7) / 2.0F;
      float var12 = (var6 + var8) / 2.0F;
      var5 = (var3 + var5) / 2.0F;
      var6 = (var4 + var6) / 2.0F;
      var7 = (var9 + var7) / 2.0F;
      var8 = (var10 + var8) / 2.0F;
      float var13 = (var5 + var11) / 2.0F;
      float var14 = (var6 + var12) / 2.0F;
      float var15 = (var7 + var11) / 2.0F;
      float var16 = (var8 + var12) / 2.0F;
      var11 = (var13 + var15) / 2.0F;
      var12 = (var14 + var16) / 2.0F;
      if (var1 != null) {
         var1.setCurve(var3, var4, var5, var6, var13, var14, var11, var12);
      }

      if (var2 != null) {
         var2.setCurve(var11, var12, var15, var16, var7, var8, var9, var10);
      }

   }

   public static void subdivide(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5) {
      float var6 = var0[var1 + 0];
      float var7 = var0[var1 + 1];
      float var8 = var0[var1 + 2];
      float var9 = var0[var1 + 3];
      float var10 = var0[var1 + 4];
      float var11 = var0[var1 + 5];
      float var12 = var0[var1 + 6];
      float var13 = var0[var1 + 7];
      if (var2 != null) {
         var2[var3 + 0] = var6;
         var2[var3 + 1] = var7;
      }

      if (var4 != null) {
         var4[var5 + 6] = var12;
         var4[var5 + 7] = var13;
      }

      var6 = (var6 + var8) / 2.0F;
      var7 = (var7 + var9) / 2.0F;
      var12 = (var12 + var10) / 2.0F;
      var13 = (var13 + var11) / 2.0F;
      float var14 = (var8 + var10) / 2.0F;
      float var15 = (var9 + var11) / 2.0F;
      var8 = (var6 + var14) / 2.0F;
      var9 = (var7 + var15) / 2.0F;
      var10 = (var12 + var14) / 2.0F;
      var11 = (var13 + var15) / 2.0F;
      var14 = (var8 + var10) / 2.0F;
      var15 = (var9 + var11) / 2.0F;
      if (var2 != null) {
         var2[var3 + 2] = var6;
         var2[var3 + 3] = var7;
         var2[var3 + 4] = var8;
         var2[var3 + 5] = var9;
         var2[var3 + 6] = var14;
         var2[var3 + 7] = var15;
      }

      if (var4 != null) {
         var4[var5 + 0] = var14;
         var4[var5 + 1] = var15;
         var4[var5 + 2] = var10;
         var4[var5 + 3] = var11;
         var4[var5 + 4] = var12;
         var4[var5 + 5] = var13;
      }

   }

   public static int solveCubic(float[] var0) {
      return solveCubic(var0, var0);
   }

   public static int solveCubic(float[] var0, float[] var1) {
      float var2 = var0[3];
      if (var2 == 0.0F) {
         return QuadCurve2D.solveQuadratic(var0, var1);
      } else {
         float var3 = var0[2] / var2;
         float var4 = var0[1] / var2;
         float var5 = var0[0] / var2;
         int var6 = 0;
         float var7 = (var3 * var3 - 3.0F * var4) / 9.0F;
         float var8 = (2.0F * var3 * var3 * var3 - 9.0F * var3 * var4 + 27.0F * var5) / 54.0F;
         float var9 = var8 * var8;
         float var10 = var7 * var7 * var7;
         var3 /= 3.0F;
         if (var9 < var10) {
            float var11 = (float)Math.acos((double)var8 / Math.sqrt((double)var10));
            var7 = (float)(-2.0 * Math.sqrt((double)var7));
            if (var1 == var0) {
               var0 = new float[4];
               System.arraycopy(var1, 0, var0, 0, 4);
            }

            var1[var6++] = (float)((double)var7 * Math.cos((double)(var11 / 3.0F)) - (double)var3);
            var1[var6++] = (float)((double)var7 * Math.cos(((double)var11 + 6.283185307179586) / 3.0) - (double)var3);
            var1[var6++] = (float)((double)var7 * Math.cos(((double)var11 - 6.283185307179586) / 3.0) - (double)var3);
            fixRoots(var1, var0);
         } else {
            boolean var15 = var8 < 0.0F;
            float var12 = (float)Math.sqrt((double)(var9 - var10));
            if (var15) {
               var8 = -var8;
            }

            float var13 = (float)Math.pow((double)(var8 + var12), 0.3333333432674408);
            if (!var15) {
               var13 = -var13;
            }

            float var14 = var13 == 0.0F ? 0.0F : var7 / var13;
            var1[var6++] = var13 + var14 - var3;
         }

         return var6;
      }
   }

   private static void fixRoots(float[] var0, float[] var1) {
      for(int var3 = 0; var3 < 3; ++var3) {
         float var4 = var0[var3];
         if (Math.abs(var4) < 1.0E-5F) {
            var0[var3] = findZero(var4, 0.0F, var1);
         } else if (Math.abs(var4 - 1.0F) < 1.0E-5F) {
            var0[var3] = findZero(var4, 1.0F, var1);
         }
      }

   }

   private static float solveEqn(float[] var0, int var1, float var2) {
      float var3 = var0[var1];

      while(true) {
         --var1;
         if (var1 < 0) {
            return var3;
         }

         var3 = var3 * var2 + var0[var1];
      }
   }

   private static float findZero(float var0, float var1, float[] var2) {
      float[] var3 = new float[]{var2[1], 2.0F * var2[2], 3.0F * var2[3]};
      float var5 = 0.0F;
      float var6 = var0;

      while(true) {
         float var4 = solveEqn(var3, 2, var0);
         if (var4 == 0.0F) {
            return var0;
         }

         float var7 = solveEqn(var2, 3, var0);
         if (var7 == 0.0F) {
            return var0;
         }

         float var8 = -(var7 / var4);
         if (var5 == 0.0F) {
            var5 = var8;
         }

         if (var0 < var1) {
            if (var8 < 0.0F) {
               return var0;
            }
         } else {
            if (!(var0 > var1)) {
               return var8 > 0.0F ? var1 + Float.MIN_VALUE : var1 - Float.MIN_VALUE;
            }

            if (var8 > 0.0F) {
               return var0;
            }
         }

         float var9 = var0 + var8;
         if (var0 == var9) {
            return var0;
         }

         if (var8 * var5 < 0.0F) {
            int var10 = var6 < var0 ? getTag(var1, var6, var0) : getTag(var1, var0, var6);
            if (var10 != 0) {
               return (var6 + var0) / 2.0F;
            }

            var0 = var1;
         } else {
            var0 = var9;
         }
      }
   }

   public boolean contains(float var1, float var2) {
      if (var1 * 0.0F + var2 * 0.0F != 0.0F) {
         return false;
      } else {
         int var3 = Shape.pointCrossingsForLine(var1, var2, this.x1, this.y1, this.x2, this.y2) + Shape.pointCrossingsForCubic(var1, var2, this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2, 0);
         return (var3 & 1) == 1;
      }
   }

   public boolean contains(Point2D var1) {
      return this.contains(var1.x, var1.y);
   }

   private static void fillEqn(float[] var0, float var1, float var2, float var3, float var4, float var5) {
      var0[0] = var2 - var1;
      var0[1] = (var3 - var2) * 3.0F;
      var0[2] = (var4 - var3 - var3 + var2) * 3.0F;
      var0[3] = var5 + (var3 - var4) * 3.0F - var2;
   }

   private static int evalCubic(float[] var0, int var1, boolean var2, boolean var3, float[] var4, float var5, float var6, float var7, float var8) {
      int var9 = 0;

      for(int var10 = 0; var10 < var1; ++var10) {
         float var11 = var0[var10];
         if (var2) {
            if (!(var11 >= 0.0F)) {
               continue;
            }
         } else if (!(var11 > 0.0F)) {
            continue;
         }

         if (var3) {
            if (!(var11 <= 1.0F)) {
               continue;
            }
         } else if (!(var11 < 1.0F)) {
            continue;
         }

         if (var4 == null || var4[1] + (2.0F * var4[2] + 3.0F * var4[3] * var11) * var11 != 0.0F) {
            float var12 = 1.0F - var11;
            var0[var9++] = var5 * var12 * var12 * var12 + 3.0F * var6 * var11 * var12 * var12 + 3.0F * var7 * var11 * var11 * var12 + var8 * var11 * var11 * var11;
         }
      }

      return var9;
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
               float var13 = this.ctrlx1;
               float var14 = this.ctrly1;
               float var15 = this.ctrlx2;
               float var16 = this.ctrly2;
               int var17 = getTag(var13, var1, var1 + var3);
               int var18 = getTag(var14, var2, var2 + var4);
               int var19 = getTag(var15, var1, var1 + var3);
               int var20 = getTag(var16, var2, var2 + var4);
               if (var7 < 0 && var11 < 0 && var17 < 0 && var19 < 0) {
                  return false;
               } else if (var8 < 0 && var12 < 0 && var18 < 0 && var20 < 0) {
                  return false;
               } else if (var7 > 0 && var11 > 0 && var17 > 0 && var19 > 0) {
                  return false;
               } else if (var8 > 0 && var12 > 0 && var18 > 0 && var20 > 0) {
                  return false;
               } else if (inwards(var7, var11, var17) && inwards(var8, var12, var18)) {
                  return true;
               } else if (inwards(var11, var7, var19) && inwards(var12, var8, var20)) {
                  return true;
               } else {
                  boolean var21 = var7 * var11 <= 0;
                  boolean var22 = var8 * var12 <= 0;
                  if (var7 == 0 && var11 == 0 && var22) {
                     return true;
                  } else if (var8 == 0 && var12 == 0 && var21) {
                     return true;
                  } else {
                     float[] var23 = new float[4];
                     float[] var24 = new float[4];
                     int var33;
                     if (!var22) {
                        fillEqn(var23, var8 < 0 ? var2 : var2 + var4, var6, var14, var16, var10);
                        var33 = solveCubic(var23, var24);
                        var33 = evalCubic(var24, var33, true, true, (float[])null, var5, var13, var15, var9);
                        return var33 == 2 && getTag(var24[0], var1, var1 + var3) * getTag(var24[1], var1, var1 + var3) <= 0;
                     } else if (!var21) {
                        fillEqn(var23, var7 < 0 ? var1 : var1 + var3, var5, var13, var15, var9);
                        var33 = solveCubic(var23, var24);
                        var33 = evalCubic(var24, var33, true, true, (float[])null, var6, var14, var16, var10);
                        return var33 == 2 && getTag(var24[0], var2, var2 + var4) * getTag(var24[1], var2, var2 + var4) <= 0;
                     } else {
                        float var25 = var9 - var5;
                        float var26 = var10 - var6;
                        float var27 = var10 * var5 - var9 * var6;
                        int var28;
                        if (var8 == 0) {
                           var28 = var7;
                        } else {
                           var28 = getTag((var27 + var25 * (var8 < 0 ? var2 : var2 + var4)) / var26, var1, var1 + var3);
                        }

                        int var29;
                        if (var12 == 0) {
                           var29 = var11;
                        } else {
                           var29 = getTag((var27 + var25 * (var12 < 0 ? var2 : var2 + var4)) / var26, var1, var1 + var3);
                        }

                        if (var28 * var29 <= 0) {
                           return true;
                        } else {
                           var28 = var28 * var7 <= 0 ? var8 : var12;
                           fillEqn(var23, var29 < 0 ? var1 : var1 + var3, var5, var13, var15, var9);
                           int var30 = solveCubic(var23, var24);
                           var30 = evalCubic(var24, var30, true, true, (float[])null, var6, var14, var16, var10);
                           int[] var31 = new int[var30 + 1];

                           for(int var32 = 0; var32 < var30; ++var32) {
                              var31[var32] = getTag(var24[var32], var2, var2 + var4);
                           }

                           var31[var30] = var28;
                           Arrays.sort(var31);
                           return var30 >= 1 && var31[0] * var31[1] <= 0 || var30 >= 3 && var31[2] * var31[3] <= 0;
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
         if (this.contains(var1, var2) && this.contains(var1 + var3, var2) && this.contains(var1 + var3, var2 + var4) && this.contains(var1, var2 + var4)) {
            return !Shape.intersectsLine(var1, var2, var3, var4, this.x1, this.y1, this.x2, this.y2);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      return new CubicIterator(this, var1);
   }

   public PathIterator getPathIterator(BaseTransform var1, float var2) {
      return new FlatteningPathIterator(this.getPathIterator(var1), var2);
   }

   public CubicCurve2D copy() {
      return new CubicCurve2D(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.x1);
      var1 += Float.floatToIntBits(this.y1) * 37;
      var1 += Float.floatToIntBits(this.x2) * 43;
      var1 += Float.floatToIntBits(this.y2) * 47;
      var1 += Float.floatToIntBits(this.ctrlx1) * 53;
      var1 += Float.floatToIntBits(this.ctrly1) * 59;
      var1 += Float.floatToIntBits(this.ctrlx2) * 61;
      var1 += Float.floatToIntBits(this.ctrly2) * 101;
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CubicCurve2D)) {
         return false;
      } else {
         CubicCurve2D var2 = (CubicCurve2D)var1;
         return this.x1 == var2.x1 && this.y1 == var2.y1 && this.x2 == var2.x2 && this.y2 == var2.y2 && this.ctrlx1 == var2.ctrlx1 && this.ctrly1 == var2.ctrly1 && this.ctrlx2 == var2.ctrlx2 && this.ctrly2 == var2.ctrly2;
      }
   }

   private float calcX(float var1) {
      float var2 = 1.0F - var1;
      return var2 * var2 * var2 * this.x1 + 3.0F * (var1 * var2 * var2 * this.ctrlx1 + var1 * var1 * var2 * this.ctrlx2) + var1 * var1 * var1 * this.x2;
   }

   private float calcY(float var1) {
      float var2 = 1.0F - var1;
      return var2 * var2 * var2 * this.y1 + 3.0F * (var1 * var2 * var2 * this.ctrly1 + var1 * var1 * var2 * this.ctrly2) + var1 * var1 * var1 * this.y2;
   }
}
