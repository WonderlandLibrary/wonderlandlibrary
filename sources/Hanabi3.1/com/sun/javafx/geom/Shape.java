package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public abstract class Shape {
   public static final int RECT_INTERSECTS = Integer.MIN_VALUE;
   public static final int OUT_LEFT = 1;
   public static final int OUT_TOP = 2;
   public static final int OUT_RIGHT = 4;
   public static final int OUT_BOTTOM = 8;

   public abstract RectBounds getBounds();

   public abstract boolean contains(float var1, float var2);

   public boolean contains(Point2D var1) {
      return this.contains(var1.x, var1.y);
   }

   public abstract boolean intersects(float var1, float var2, float var3, float var4);

   public boolean intersects(RectBounds var1) {
      float var2 = var1.getMinX();
      float var3 = var1.getMinY();
      float var4 = var1.getMaxX() - var2;
      float var5 = var1.getMaxY() - var3;
      return this.intersects(var2, var3, var4, var5);
   }

   public abstract boolean contains(float var1, float var2, float var3, float var4);

   public boolean contains(RectBounds var1) {
      float var2 = var1.getMinX();
      float var3 = var1.getMinY();
      float var4 = var1.getMaxX() - var2;
      float var5 = var1.getMaxY() - var3;
      return this.contains(var2, var3, var4, var5);
   }

   public abstract PathIterator getPathIterator(BaseTransform var1);

   public abstract PathIterator getPathIterator(BaseTransform var1, float var2);

   public abstract Shape copy();

   public static int pointCrossingsForPath(PathIterator var0, float var1, float var2) {
      if (var0.isDone()) {
         return 0;
      } else {
         float[] var3 = new float[6];
         if (var0.currentSegment(var3) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
         } else {
            var0.next();
            float var4 = var3[0];
            float var5 = var3[1];
            float var6 = var4;
            float var7 = var5;

            int var10;
            for(var10 = 0; !var0.isDone(); var0.next()) {
               float var8;
               float var9;
               switch (var0.currentSegment(var3)) {
                  case 0:
                     if (var7 != var5) {
                        var10 += pointCrossingsForLine(var1, var2, var6, var7, var4, var5);
                     }

                     var4 = var6 = var3[0];
                     var5 = var7 = var3[1];
                     break;
                  case 1:
                     var8 = var3[0];
                     var9 = var3[1];
                     var10 += pointCrossingsForLine(var1, var2, var6, var7, var8, var9);
                     var6 = var8;
                     var7 = var9;
                     break;
                  case 2:
                     var8 = var3[2];
                     var9 = var3[3];
                     var10 += pointCrossingsForQuad(var1, var2, var6, var7, var3[0], var3[1], var8, var9, 0);
                     var6 = var8;
                     var7 = var9;
                     break;
                  case 3:
                     var8 = var3[4];
                     var9 = var3[5];
                     var10 += pointCrossingsForCubic(var1, var2, var6, var7, var3[0], var3[1], var3[2], var3[3], var8, var9, 0);
                     var6 = var8;
                     var7 = var9;
                     break;
                  case 4:
                     if (var7 != var5) {
                        var10 += pointCrossingsForLine(var1, var2, var6, var7, var4, var5);
                     }

                     var6 = var4;
                     var7 = var5;
               }
            }

            if (var7 != var5) {
               var10 += pointCrossingsForLine(var1, var2, var6, var7, var4, var5);
            }

            return var10;
         }
      }
   }

   public static int pointCrossingsForLine(float var0, float var1, float var2, float var3, float var4, float var5) {
      if (var1 < var3 && var1 < var5) {
         return 0;
      } else if (var1 >= var3 && var1 >= var5) {
         return 0;
      } else if (var0 >= var2 && var0 >= var4) {
         return 0;
      } else if (var0 < var2 && var0 < var4) {
         return var3 < var5 ? 1 : -1;
      } else {
         float var6 = var2 + (var1 - var3) * (var4 - var2) / (var5 - var3);
         if (var0 >= var6) {
            return 0;
         } else {
            return var3 < var5 ? 1 : -1;
         }
      }
   }

   public static int pointCrossingsForQuad(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, int var8) {
      if (var1 < var3 && var1 < var5 && var1 < var7) {
         return 0;
      } else if (var1 >= var3 && var1 >= var5 && var1 >= var7) {
         return 0;
      } else if (var0 >= var2 && var0 >= var4 && var0 >= var6) {
         return 0;
      } else if (var0 < var2 && var0 < var4 && var0 < var6) {
         if (var1 >= var3) {
            if (var1 < var7) {
               return 1;
            }
         } else if (var1 >= var7) {
            return -1;
         }

         return 0;
      } else if (var8 > 52) {
         return pointCrossingsForLine(var0, var1, var2, var3, var6, var7);
      } else {
         float var9 = (var2 + var4) / 2.0F;
         float var10 = (var3 + var5) / 2.0F;
         float var11 = (var4 + var6) / 2.0F;
         float var12 = (var5 + var7) / 2.0F;
         var4 = (var9 + var11) / 2.0F;
         var5 = (var10 + var12) / 2.0F;
         return !Float.isNaN(var4) && !Float.isNaN(var5) ? pointCrossingsForQuad(var0, var1, var2, var3, var9, var10, var4, var5, var8 + 1) + pointCrossingsForQuad(var0, var1, var4, var5, var11, var12, var6, var7, var8 + 1) : 0;
      }
   }

   public static int pointCrossingsForCubic(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10) {
      if (var1 < var3 && var1 < var5 && var1 < var7 && var1 < var9) {
         return 0;
      } else if (var1 >= var3 && var1 >= var5 && var1 >= var7 && var1 >= var9) {
         return 0;
      } else if (var0 >= var2 && var0 >= var4 && var0 >= var6 && var0 >= var8) {
         return 0;
      } else if (var0 < var2 && var0 < var4 && var0 < var6 && var0 < var8) {
         if (var1 >= var3) {
            if (var1 < var9) {
               return 1;
            }
         } else if (var1 >= var9) {
            return -1;
         }

         return 0;
      } else if (var10 > 52) {
         return pointCrossingsForLine(var0, var1, var2, var3, var8, var9);
      } else {
         float var11 = (var4 + var6) / 2.0F;
         float var12 = (var5 + var7) / 2.0F;
         var4 = (var2 + var4) / 2.0F;
         var5 = (var3 + var5) / 2.0F;
         var6 = (var6 + var8) / 2.0F;
         var7 = (var7 + var9) / 2.0F;
         float var13 = (var4 + var11) / 2.0F;
         float var14 = (var5 + var12) / 2.0F;
         float var15 = (var11 + var6) / 2.0F;
         float var16 = (var12 + var7) / 2.0F;
         var11 = (var13 + var15) / 2.0F;
         var12 = (var14 + var16) / 2.0F;
         return !Float.isNaN(var11) && !Float.isNaN(var12) ? pointCrossingsForCubic(var0, var1, var2, var3, var4, var5, var13, var14, var11, var12, var10 + 1) + pointCrossingsForCubic(var0, var1, var11, var12, var15, var16, var6, var7, var8, var9, var10 + 1) : 0;
      }
   }

   public static int rectCrossingsForPath(PathIterator var0, float var1, float var2, float var3, float var4) {
      if (!(var3 <= var1) && !(var4 <= var2)) {
         if (var0.isDone()) {
            return 0;
         } else {
            float[] var5 = new float[6];
            if (var0.currentSegment(var5) != 0) {
               throw new IllegalPathStateException("missing initial moveto in path definition");
            } else {
               var0.next();
               float var8;
               float var6 = var8 = var5[0];
               float var9;
               float var7 = var9 = var5[1];

               int var12;
               for(var12 = 0; var12 != Integer.MIN_VALUE && !var0.isDone(); var0.next()) {
                  float var10;
                  float var11;
                  switch (var0.currentSegment(var5)) {
                     case 0:
                        if (var6 != var8 || var7 != var9) {
                           var12 = rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var8, var9);
                        }

                        var8 = var6 = var5[0];
                        var9 = var7 = var5[1];
                        break;
                     case 1:
                        var10 = var5[0];
                        var11 = var5[1];
                        var12 = rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var10, var11);
                        var6 = var10;
                        var7 = var11;
                        break;
                     case 2:
                        var10 = var5[2];
                        var11 = var5[3];
                        var12 = rectCrossingsForQuad(var12, var1, var2, var3, var4, var6, var7, var5[0], var5[1], var10, var11, 0);
                        var6 = var10;
                        var7 = var11;
                        break;
                     case 3:
                        var10 = var5[4];
                        var11 = var5[5];
                        var12 = rectCrossingsForCubic(var12, var1, var2, var3, var4, var6, var7, var5[0], var5[1], var5[2], var5[3], var10, var11, 0);
                        var6 = var10;
                        var7 = var11;
                        break;
                     case 4:
                        if (var6 != var8 || var7 != var9) {
                           var12 = rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var8, var9);
                        }

                        var6 = var8;
                        var7 = var9;
                  }
               }

               if (var12 != Integer.MIN_VALUE && (var6 != var8 || var7 != var9)) {
                  var12 = rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var8, var9);
               }

               return var12;
            }
         }
      } else {
         return 0;
      }
   }

   public static int rectCrossingsForLine(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var6 >= var4 && var8 >= var4) {
         return var0;
      } else if (var6 <= var2 && var8 <= var2) {
         return var0;
      } else if (var5 <= var1 && var7 <= var1) {
         return var0;
      } else if (var5 >= var3 && var7 >= var3) {
         if (var6 < var8) {
            if (var6 <= var2) {
               ++var0;
            }

            if (var8 >= var4) {
               ++var0;
            }
         } else if (var8 < var6) {
            if (var8 <= var2) {
               --var0;
            }

            if (var6 >= var4) {
               --var0;
            }
         }

         return var0;
      } else if (var5 > var1 && var5 < var3 && var6 > var2 && var6 < var4 || var7 > var1 && var7 < var3 && var8 > var2 && var8 < var4) {
         return Integer.MIN_VALUE;
      } else {
         float var9 = var5;
         if (var6 < var2) {
            var9 = var5 + (var2 - var6) * (var7 - var5) / (var8 - var6);
         } else if (var6 > var4) {
            var9 = var5 + (var4 - var6) * (var7 - var5) / (var8 - var6);
         }

         float var10 = var7;
         if (var8 < var2) {
            var10 = var7 + (var2 - var8) * (var5 - var7) / (var6 - var8);
         } else if (var8 > var4) {
            var10 = var7 + (var4 - var8) * (var5 - var7) / (var6 - var8);
         }

         if (var9 <= var1 && var10 <= var1) {
            return var0;
         } else if (var9 >= var3 && var10 >= var3) {
            if (var6 < var8) {
               if (var6 <= var2) {
                  ++var0;
               }

               if (var8 >= var4) {
                  ++var0;
               }
            } else if (var8 < var6) {
               if (var8 <= var2) {
                  --var0;
               }

               if (var6 >= var4) {
                  --var0;
               }
            }

            return var0;
         } else {
            return Integer.MIN_VALUE;
         }
      }
   }

   public static int rectCrossingsForQuad(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, int var11) {
      if (var6 >= var4 && var8 >= var4 && var10 >= var4) {
         return var0;
      } else if (var6 <= var2 && var8 <= var2 && var10 <= var2) {
         return var0;
      } else if (var5 <= var1 && var7 <= var1 && var9 <= var1) {
         return var0;
      } else if (var5 >= var3 && var7 >= var3 && var9 >= var3) {
         if (var6 < var10) {
            if (var6 <= var2 && var10 > var2) {
               ++var0;
            }

            if (var6 < var4 && var10 >= var4) {
               ++var0;
            }
         } else if (var10 < var6) {
            if (var10 <= var2 && var6 > var2) {
               --var0;
            }

            if (var10 < var4 && var6 >= var4) {
               --var0;
            }
         }

         return var0;
      } else if (var5 < var3 && var5 > var1 && var6 < var4 && var6 > var2 || var9 < var3 && var9 > var1 && var10 < var4 && var10 > var2) {
         return Integer.MIN_VALUE;
      } else if (var11 > 52) {
         return rectCrossingsForLine(var0, var1, var2, var3, var4, var5, var6, var9, var10);
      } else {
         float var12 = (var5 + var7) / 2.0F;
         float var13 = (var6 + var8) / 2.0F;
         float var14 = (var7 + var9) / 2.0F;
         float var15 = (var8 + var10) / 2.0F;
         var7 = (var12 + var14) / 2.0F;
         var8 = (var13 + var15) / 2.0F;
         if (!Float.isNaN(var7) && !Float.isNaN(var8)) {
            var0 = rectCrossingsForQuad(var0, var1, var2, var3, var4, var5, var6, var12, var13, var7, var8, var11 + 1);
            if (var0 != Integer.MIN_VALUE) {
               var0 = rectCrossingsForQuad(var0, var1, var2, var3, var4, var7, var8, var14, var15, var9, var10, var11 + 1);
            }

            return var0;
         } else {
            return 0;
         }
      }
   }

   public static int rectCrossingsForCubic(int var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, int var13) {
      if (var6 >= var4 && var8 >= var4 && var10 >= var4 && var12 >= var4) {
         return var0;
      } else if (var6 <= var2 && var8 <= var2 && var10 <= var2 && var12 <= var2) {
         return var0;
      } else if (var5 <= var1 && var7 <= var1 && var9 <= var1 && var11 <= var1) {
         return var0;
      } else if (var5 >= var3 && var7 >= var3 && var9 >= var3 && var11 >= var3) {
         if (var6 < var12) {
            if (var6 <= var2 && var12 > var2) {
               ++var0;
            }

            if (var6 < var4 && var12 >= var4) {
               ++var0;
            }
         } else if (var12 < var6) {
            if (var12 <= var2 && var6 > var2) {
               --var0;
            }

            if (var12 < var4 && var6 >= var4) {
               --var0;
            }
         }

         return var0;
      } else if (var5 > var1 && var5 < var3 && var6 > var2 && var6 < var4 || var11 > var1 && var11 < var3 && var12 > var2 && var12 < var4) {
         return Integer.MIN_VALUE;
      } else if (var13 > 52) {
         return rectCrossingsForLine(var0, var1, var2, var3, var4, var5, var6, var11, var12);
      } else {
         float var14 = (var7 + var9) / 2.0F;
         float var15 = (var8 + var10) / 2.0F;
         var7 = (var5 + var7) / 2.0F;
         var8 = (var6 + var8) / 2.0F;
         var9 = (var9 + var11) / 2.0F;
         var10 = (var10 + var12) / 2.0F;
         float var16 = (var7 + var14) / 2.0F;
         float var17 = (var8 + var15) / 2.0F;
         float var18 = (var14 + var9) / 2.0F;
         float var19 = (var15 + var10) / 2.0F;
         var14 = (var16 + var18) / 2.0F;
         var15 = (var17 + var19) / 2.0F;
         if (!Float.isNaN(var14) && !Float.isNaN(var15)) {
            var0 = rectCrossingsForCubic(var0, var1, var2, var3, var4, var5, var6, var7, var8, var16, var17, var14, var15, var13 + 1);
            if (var0 != Integer.MIN_VALUE) {
               var0 = rectCrossingsForCubic(var0, var1, var2, var3, var4, var14, var15, var18, var19, var9, var10, var11, var12, var13 + 1);
            }

            return var0;
         } else {
            return 0;
         }
      }
   }

   static boolean intersectsLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      int var9;
      if ((var9 = outcode(var0, var1, var2, var3, var6, var7)) == 0) {
         return true;
      } else {
         int var8;
         while((var8 = outcode(var0, var1, var2, var3, var4, var5)) != 0) {
            if ((var8 & var9) != 0) {
               return false;
            }

            if ((var8 & 5) != 0) {
               if ((var8 & 4) != 0) {
                  var0 += var2;
               }

               var5 += (var0 - var4) * (var7 - var5) / (var6 - var4);
               var4 = var0;
            } else {
               if ((var8 & 8) != 0) {
                  var1 += var3;
               }

               var4 += (var1 - var5) * (var6 - var4) / (var7 - var5);
               var5 = var1;
            }
         }

         return true;
      }
   }

   static int outcode(float var0, float var1, float var2, float var3, float var4, float var5) {
      int var6 = 0;
      if (var2 <= 0.0F) {
         var6 |= 5;
      } else if (var4 < var0) {
         var6 |= 1;
      } else if ((double)var4 > (double)var0 + (double)var2) {
         var6 |= 4;
      }

      if (var3 <= 0.0F) {
         var6 |= 10;
      } else if (var5 < var1) {
         var6 |= 2;
      } else if ((double)var5 > (double)var1 + (double)var3) {
         var6 |= 8;
      }

      return var6;
   }

   public static void accumulate(float[] var0, Shape var1, BaseTransform var2) {
      PathIterator var3 = var1.getPathIterator(var2);
      float[] var4 = new float[6];
      float var5 = 0.0F;
      float var6 = 0.0F;
      float var7 = 0.0F;

      for(float var8 = 0.0F; !var3.isDone(); var3.next()) {
         float var9;
         float var10;
         switch (var3.currentSegment(var4)) {
            case 0:
               var5 = var4[0];
               var6 = var4[1];
            case 1:
               var7 = var4[0];
               var8 = var4[1];
               if (var0[0] > var7) {
                  var0[0] = var7;
               }

               if (var0[1] > var8) {
                  var0[1] = var8;
               }

               if (var0[2] < var7) {
                  var0[2] = var7;
               }

               if (var0[3] < var8) {
                  var0[3] = var8;
               }
               break;
            case 2:
               var9 = var4[2];
               var10 = var4[3];
               if (var0[0] > var9) {
                  var0[0] = var9;
               }

               if (var0[1] > var10) {
                  var0[1] = var10;
               }

               if (var0[2] < var9) {
                  var0[2] = var9;
               }

               if (var0[3] < var10) {
                  var0[3] = var10;
               }

               if (var0[0] > var4[0] || var0[2] < var4[0]) {
                  accumulateQuad(var0, 0, var7, var4[0], var9);
               }

               if (var0[1] > var4[1] || var0[3] < var4[1]) {
                  accumulateQuad(var0, 1, var8, var4[1], var10);
               }

               var7 = var9;
               var8 = var10;
               break;
            case 3:
               var9 = var4[4];
               var10 = var4[5];
               if (var0[0] > var9) {
                  var0[0] = var9;
               }

               if (var0[1] > var10) {
                  var0[1] = var10;
               }

               if (var0[2] < var9) {
                  var0[2] = var9;
               }

               if (var0[3] < var10) {
                  var0[3] = var10;
               }

               if (var0[0] > var4[0] || var0[2] < var4[0] || var0[0] > var4[2] || var0[2] < var4[2]) {
                  accumulateCubic(var0, 0, var7, var4[0], var4[2], var9);
               }

               if (var0[1] > var4[1] || var0[3] < var4[1] || var0[1] > var4[3] || var0[3] < var4[3]) {
                  accumulateCubic(var0, 1, var8, var4[1], var4[3], var10);
               }

               var7 = var9;
               var8 = var10;
               break;
            case 4:
               var7 = var5;
               var8 = var6;
         }
      }

   }

   public static void accumulateQuad(float[] var0, int var1, float var2, float var3, float var4) {
      float var5 = var2 - var3;
      float var6 = var4 - var3 + var5;
      if (var6 != 0.0F) {
         float var7 = var5 / var6;
         if (var7 > 0.0F && var7 < 1.0F) {
            float var8 = 1.0F - var7;
            float var9 = var2 * var8 * var8 + 2.0F * var3 * var7 * var8 + var4 * var7 * var7;
            if (var0[var1] > var9) {
               var0[var1] = var9;
            }

            if (var0[var1 + 2] < var9) {
               var0[var1 + 2] = var9;
            }
         }
      }

   }

   public static void accumulateCubic(float[] var0, int var1, float var2, float var3, float var4, float var5) {
      float var6 = var3 - var2;
      float var7 = 2.0F * (var4 - var3 - var6);
      float var8 = var5 - var4 - var7 - var6;
      if (var8 == 0.0F) {
         if (var7 == 0.0F) {
            return;
         }

         accumulateCubic(var0, var1, -var6 / var7, var2, var3, var4, var5);
      } else {
         float var9 = var7 * var7 - 4.0F * var8 * var6;
         if (var9 < 0.0F) {
            return;
         }

         var9 = (float)Math.sqrt((double)var9);
         if (var7 < 0.0F) {
            var9 = -var9;
         }

         float var10 = (var7 + var9) / -2.0F;
         accumulateCubic(var0, var1, var10 / var8, var2, var3, var4, var5);
         if (var10 != 0.0F) {
            accumulateCubic(var0, var1, var6 / var10, var2, var3, var4, var5);
         }
      }

   }

   public static void accumulateCubic(float[] var0, int var1, float var2, float var3, float var4, float var5, float var6) {
      if (var2 > 0.0F && var2 < 1.0F) {
         float var7 = 1.0F - var2;
         float var8 = var3 * var7 * var7 * var7 + 3.0F * var4 * var2 * var7 * var7 + 3.0F * var5 * var2 * var2 * var7 + var6 * var2 * var2 * var2;
         if (var0[var1] > var8) {
            var0[var1] = var8;
         }

         if (var0[var1 + 2] < var8) {
            var0[var1 + 2] = var8;
         }
      }

   }
}
