package com.sun.prism;

import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.GeneralShapePair;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.Dasher;
import com.sun.openpisces.Stroker;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import java.util.Arrays;

public final class BasicStroke {
   public static final int CAP_BUTT = 0;
   public static final int CAP_ROUND = 1;
   public static final int CAP_SQUARE = 2;
   public static final int JOIN_BEVEL = 2;
   public static final int JOIN_MITER = 0;
   public static final int JOIN_ROUND = 1;
   public static final int TYPE_CENTERED = 0;
   public static final int TYPE_INNER = 1;
   public static final int TYPE_OUTER = 2;
   float width;
   int type;
   int cap;
   int join;
   float miterLimit;
   float[] dash;
   float dashPhase;
   private static final int SAFE_ACCUMULATE_MASK = 91;
   private float[] tmpMiter = new float[2];
   static final float SQRT_2 = (float)Math.sqrt(2.0);

   public BasicStroke() {
      this.set(0, 1.0F, 2, 0, 10.0F);
   }

   public BasicStroke(float var1, int var2, int var3, float var4) {
      this.set(0, var1, var2, var3, var4);
   }

   public BasicStroke(int var1, float var2, int var3, int var4, float var5) {
      this.set(var1, var2, var3, var4, var5);
   }

   public BasicStroke(float var1, int var2, int var3, float var4, float[] var5, float var6) {
      this.set(0, var1, var2, var3, var4);
      this.set(var5, var6);
   }

   public BasicStroke(float var1, int var2, int var3, float var4, double[] var5, float var6) {
      this.set(0, var1, var2, var3, var4);
      this.set(var5, var6);
   }

   public BasicStroke(int var1, float var2, int var3, int var4, float var5, float[] var6, float var7) {
      this.set(var1, var2, var3, var4, var5);
      this.set(var6, var7);
   }

   public BasicStroke(int var1, float var2, int var3, int var4, float var5, double[] var6, float var7) {
      this.set(var1, var2, var3, var4, var5);
      this.set(var6, var7);
   }

   public void set(int var1, float var2, int var3, int var4, float var5) {
      if (var1 != 0 && var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("illegal type");
      } else if (var2 < 0.0F) {
         throw new IllegalArgumentException("negative width");
      } else if (var3 != 0 && var3 != 1 && var3 != 2) {
         throw new IllegalArgumentException("illegal end cap value");
      } else {
         if (var4 == 0) {
            if (var5 < 1.0F) {
               throw new IllegalArgumentException("miter limit < 1");
            }
         } else if (var4 != 1 && var4 != 2) {
            throw new IllegalArgumentException("illegal line join value");
         }

         this.type = var1;
         this.width = var2;
         this.cap = var3;
         this.join = var4;
         this.miterLimit = var5;
      }
   }

   public void set(float[] var1, float var2) {
      if (var1 != null) {
         boolean var3 = true;

         for(int var4 = 0; var4 < var1.length; ++var4) {
            float var5 = var1[var4];
            if ((double)var5 > 0.0) {
               var3 = false;
            } else if ((double)var5 < 0.0) {
               throw new IllegalArgumentException("negative dash length");
            }
         }

         if (var3) {
            throw new IllegalArgumentException("dash lengths all zero");
         }
      }

      this.dash = var1;
      this.dashPhase = var2;
   }

   public void set(double[] var1, float var2) {
      if (var1 != null) {
         float[] var3 = new float[var1.length];
         boolean var4 = true;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            float var6 = (float)var1[var5];
            if ((double)var6 > 0.0) {
               var4 = false;
            } else if ((double)var6 < 0.0) {
               throw new IllegalArgumentException("negative dash length");
            }

            var3[var5] = var6;
         }

         if (var4) {
            throw new IllegalArgumentException("dash lengths all zero");
         }

         this.dash = var3;
      } else {
         this.dash = null;
      }

      this.dashPhase = var2;
   }

   public int getType() {
      return this.type;
   }

   public float getLineWidth() {
      return this.width;
   }

   public int getEndCap() {
      return this.cap;
   }

   public int getLineJoin() {
      return this.join;
   }

   public float getMiterLimit() {
      return this.miterLimit;
   }

   public boolean isDashed() {
      return this.dash != null;
   }

   public float[] getDashArray() {
      return this.dash;
   }

   public float getDashPhase() {
      return this.dashPhase;
   }

   public Shape createStrokedShape(Shape var1) {
      Shape var2;
      if (var1 instanceof RoundRectangle2D) {
         var2 = this.strokeRoundRectangle((RoundRectangle2D)var1);
      } else {
         var2 = null;
      }

      if (var2 != null) {
         return var2;
      } else {
         var2 = this.createCenteredStrokedShape(var1);
         if (this.type == 1) {
            var2 = this.makeIntersectedShape(var2, var1);
         } else if (this.type == 2) {
            var2 = this.makeSubtractedShape(var2, var1);
         }

         return var2;
      }
   }

   private boolean isCW(float var1, float var2, float var3, float var4) {
      return var1 * var4 <= var2 * var3;
   }

   private void computeOffset(float var1, float var2, float var3, float[] var4, int var5) {
      float var6 = (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
      if (var6 == 0.0F) {
         var4[var5 + 0] = var4[var5 + 1] = 0.0F;
      } else {
         var4[var5 + 0] = var2 * var3 / var6;
         var4[var5 + 1] = -(var1 * var3) / var6;
      }

   }

   private void computeMiter(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float[] var9, int var10) {
      float var11 = var3 - var1;
      float var12 = var4 - var2;
      float var13 = var7 - var5;
      float var14 = var8 - var6;
      float var15 = var11 * var14 - var13 * var12;
      float var16 = var13 * (var2 - var6) - var14 * (var1 - var5);
      var16 /= var15;
      var9[var10++] = var1 + var16 * var11;
      var9[var10] = var2 + var16 * var12;
   }

   private void accumulateQuad(float[] var1, int var2, float var3, float var4, float var5, float var6) {
      float var7 = var3 - var4;
      float var8 = var5 - var4 + var7;
      if (var8 != 0.0F) {
         float var9 = var7 / var8;
         if (var9 > 0.0F && var9 < 1.0F) {
            float var10 = 1.0F - var9;
            float var11 = var3 * var10 * var10 + 2.0F * var4 * var9 * var10 + var5 * var9 * var9;
            if (var1[var2] > var11 - var6) {
               var1[var2] = var11 - var6;
            }

            if (var1[var2 + 2] < var11 + var6) {
               var1[var2 + 2] = var11 + var6;
            }
         }
      }

   }

   private void accumulateCubic(float[] var1, int var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var3 > 0.0F && var3 < 1.0F) {
         float var9 = 1.0F - var3;
         float var10 = var4 * var9 * var9 * var9 + 3.0F * var5 * var3 * var9 * var9 + 3.0F * var6 * var3 * var3 * var9 + var7 * var3 * var3 * var3;
         if (var1[var2] > var10 - var8) {
            var1[var2] = var10 - var8;
         }

         if (var1[var2 + 2] < var10 + var8) {
            var1[var2 + 2] = var10 + var8;
         }
      }

   }

   private void accumulateCubic(float[] var1, int var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = var4 - var3;
      float var9 = 2.0F * (var5 - var4 - var8);
      float var10 = var6 - var5 - var9 - var8;
      if (var10 == 0.0F) {
         if (var9 == 0.0F) {
            return;
         }

         this.accumulateCubic(var1, var2, -var8 / var9, var3, var4, var5, var6, var7);
      } else {
         float var11 = var9 * var9 - 4.0F * var10 * var8;
         if (var11 < 0.0F) {
            return;
         }

         var11 = (float)Math.sqrt((double)var11);
         if (var9 < 0.0F) {
            var11 = -var11;
         }

         float var12 = (var9 + var11) / -2.0F;
         this.accumulateCubic(var1, var2, var12 / var10, var3, var4, var5, var6, var7);
         if (var12 != 0.0F) {
            this.accumulateCubic(var1, var2, var8 / var12, var3, var4, var5, var6, var7);
         }
      }

   }

   public void accumulateShapeBounds(float[] var1, Shape var2, BaseTransform var3) {
      if (this.type == 1) {
         Shape.accumulate(var1, var2, var3);
      } else if ((var3.getType() & -92) != 0) {
         Shape.accumulate(var1, this.createStrokedShape(var2), var3);
      } else {
         PathIterator var4 = var2.getPathIterator(var3);
         boolean var5 = true;
         float[] var6 = new float[6];
         float var7 = this.type == 0 ? this.getLineWidth() / 2.0F : this.getLineWidth();
         var7 = (float)((double)var7 * Math.hypot(var3.getMxx(), var3.getMyx()));
         float var8 = 0.0F;
         float var9 = 0.0F;
         float var10 = 0.0F;
         float var11 = 0.0F;
         float var14 = 0.0F;
         float var15 = 0.0F;
         float var18 = 0.0F;
         float var19 = 0.0F;
         float[] var20 = new float[4];
         float var21 = 0.0F;
         float var22 = 0.0F;
         float var23 = 0.0F;
         float var24 = 0.0F;

         while(!var4.isDone()) {
            int var25 = var4.currentSegment(var6);
            float var12;
            float var13;
            float var16;
            float var17;
            switch (var25) {
               case 0:
                  if (!var5) {
                     this.accumulateCap(var18, var19, var10, var11, var21, var22, var1, var7);
                     this.accumulateCap(-var14, -var15, var8, var9, -var23, -var24, var1, var7);
                  }

                  var10 = var8 = var6[0];
                  var11 = var9 = var6[1];
                  break;
               case 1:
                  var12 = var6[0];
                  var13 = var6[1];
                  var16 = var12 - var10;
                  var17 = var13 - var11;
                  if (var16 == 0.0F && var17 == 0.0F) {
                     var16 = 1.0F;
                  }

                  this.computeOffset(var16, var17, var7, var20, 0);
                  if (!var5) {
                     this.accumulateJoin(var18, var19, var16, var17, var10, var11, var21, var22, var20[0], var20[1], var1, var7);
                  }

                  var10 = var12;
                  var11 = var13;
                  var18 = var16;
                  var19 = var17;
                  var21 = var20[0];
                  var22 = var20[1];
                  if (var5) {
                     var14 = var16;
                     var15 = var17;
                     var23 = var21;
                     var24 = var22;
                  }
                  break;
               case 2:
                  var12 = var6[2];
                  var13 = var6[3];
                  var16 = var6[0] - var10;
                  var17 = var6[1] - var11;
                  this.computeOffset(var16, var17, var7, var20, 0);
                  if (!var5) {
                     this.accumulateJoin(var18, var19, var16, var17, var10, var11, var21, var22, var20[0], var20[1], var1, var7);
                  }

                  if (var1[0] > var6[0] - var7 || var1[2] < var6[0] + var7) {
                     this.accumulateQuad(var1, 0, var10, var6[0], var12, var7);
                  }

                  if (var1[1] > var6[1] - var7 || var1[3] < var6[1] + var7) {
                     this.accumulateQuad(var1, 1, var11, var6[1], var13, var7);
                  }

                  var10 = var12;
                  var11 = var13;
                  if (var5) {
                     var14 = var16;
                     var15 = var17;
                     var23 = var20[0];
                     var24 = var20[1];
                  }

                  var18 = var12 - var6[0];
                  var19 = var13 - var6[1];
                  this.computeOffset(var18, var19, var7, var20, 0);
                  var21 = var20[0];
                  var22 = var20[1];
                  break;
               case 3:
                  var12 = var6[4];
                  var13 = var6[5];
                  var16 = var6[0] - var10;
                  var17 = var6[1] - var11;
                  this.computeOffset(var16, var17, var7, var20, 0);
                  if (!var5) {
                     this.accumulateJoin(var18, var19, var16, var17, var10, var11, var21, var22, var20[0], var20[1], var1, var7);
                  }

                  if (var1[0] > var6[0] - var7 || var1[2] < var6[0] + var7 || var1[0] > var6[2] - var7 || var1[2] < var6[2] + var7) {
                     this.accumulateCubic(var1, 0, var10, var6[0], var6[2], var12, var7);
                  }

                  if (var1[1] > var6[1] - var7 || var1[3] < var6[1] + var7 || var1[1] > var6[3] - var7 || var1[3] < var6[3] + var7) {
                     this.accumulateCubic(var1, 1, var11, var6[1], var6[3], var13, var7);
                  }

                  var10 = var12;
                  var11 = var13;
                  if (var5) {
                     var14 = var16;
                     var15 = var17;
                     var23 = var20[0];
                     var24 = var20[1];
                  }

                  var18 = var12 - var6[2];
                  var19 = var13 - var6[3];
                  this.computeOffset(var18, var19, var7, var20, 0);
                  var21 = var20[0];
                  var22 = var20[1];
                  break;
               case 4:
                  var16 = var8 - var10;
                  var17 = var9 - var11;
                  if (!var5) {
                     this.computeOffset(var14, var15, var7, var20, 2);
                     if (var16 == 0.0F && var17 == 0.0F) {
                        this.accumulateJoin(var18, var19, var14, var15, var8, var9, var21, var22, var20[2], var20[3], var1, var7);
                     } else {
                        this.computeOffset(var16, var17, var7, var20, 0);
                        this.accumulateJoin(var18, var19, var16, var17, var10, var11, var21, var22, var20[0], var20[1], var1, var7);
                        this.accumulateJoin(var16, var17, var14, var15, var8, var9, var20[0], var20[1], var20[2], var20[3], var1, var7);
                     }
                  }

                  var10 = var8;
                  var11 = var9;
            }

            var5 = var25 == 0 || var25 == 4;
            var4.next();
         }

         if (!var5) {
            this.accumulateCap(var18, var19, var10, var11, var21, var22, var1, var7);
            this.accumulateCap(-var14, -var15, var8, var9, -var23, -var24, var1, var7);
         }

      }
   }

   private void accumulate(float var1, float var2, float var3, float var4, float[] var5) {
      if (var1 <= var3) {
         if (var1 < var5[0]) {
            var5[0] = var1;
         }

         if (var3 > var5[2]) {
            var5[2] = var3;
         }
      } else {
         if (var3 < var5[0]) {
            var5[0] = var3;
         }

         if (var1 > var5[2]) {
            var5[2] = var1;
         }
      }

      if (var2 <= var4) {
         if (var2 < var5[1]) {
            var5[1] = var2;
         }

         if (var4 > var5[3]) {
            var5[3] = var4;
         }
      } else {
         if (var4 < var5[1]) {
            var5[1] = var4;
         }

         if (var2 > var5[3]) {
            var5[3] = var2;
         }
      }

   }

   private void accumulateOrdered(float var1, float var2, float var3, float var4, float[] var5) {
      if (var1 < var5[0]) {
         var5[0] = var1;
      }

      if (var3 > var5[2]) {
         var5[2] = var3;
      }

      if (var2 < var5[1]) {
         var5[1] = var2;
      }

      if (var4 > var5[3]) {
         var5[3] = var4;
      }

   }

   private void accumulateJoin(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float[] var11, float var12) {
      if (this.join == 2) {
         this.accumulateBevel(var5, var6, var7, var8, var9, var10, var11);
      } else if (this.join == 0) {
         this.accumulateMiter(var1, var2, var3, var4, var7, var8, var9, var10, var5, var6, var11, var12);
      } else {
         this.accumulateOrdered(var5 - var12, var6 - var12, var5 + var12, var6 + var12, var11);
      }

   }

   private void accumulateCap(float var1, float var2, float var3, float var4, float var5, float var6, float[] var7, float var8) {
      if (this.cap == 2) {
         this.accumulate(var3 + var5 - var6, var4 + var6 + var5, var3 - var5 - var6, var4 - var6 + var5, var7);
      } else if (this.cap == 0) {
         this.accumulate(var3 + var5, var4 + var6, var3 - var5, var4 - var6, var7);
      } else {
         this.accumulateOrdered(var3 - var8, var4 - var8, var3 + var8, var4 + var8, var7);
      }

   }

   private void accumulateMiter(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float[] var11, float var12) {
      this.accumulateBevel(var9, var10, var5, var6, var7, var8, var11);
      boolean var13 = this.isCW(var1, var2, var3, var4);
      if (var13) {
         var5 = -var5;
         var6 = -var6;
         var7 = -var7;
         var8 = -var8;
      }

      this.computeMiter(var9 - var1 + var5, var10 - var2 + var6, var9 + var5, var10 + var6, var9 + var3 + var7, var10 + var4 + var8, var9 + var7, var10 + var8, this.tmpMiter, 0);
      float var14 = (this.tmpMiter[0] - var9) * (this.tmpMiter[0] - var9) + (this.tmpMiter[1] - var10) * (this.tmpMiter[1] - var10);
      float var15 = this.miterLimit * var12;
      if (var14 < var15 * var15) {
         this.accumulateOrdered(this.tmpMiter[0], this.tmpMiter[1], this.tmpMiter[0], this.tmpMiter[1], var11);
      }

   }

   private void accumulateBevel(float var1, float var2, float var3, float var4, float var5, float var6, float[] var7) {
      this.accumulate(var1 + var3, var2 + var4, var1 - var3, var2 - var4, var7);
      this.accumulate(var1 + var5, var2 + var6, var1 - var5, var2 - var6, var7);
   }

   public Shape createCenteredStrokedShape(Shape var1) {
      Path2D var2 = new Path2D(1);
      float var3 = this.type == 0 ? this.width : this.width * 2.0F;
      Object var4 = new Stroker(var2, var3, this.cap, this.join, this.miterLimit);
      if (this.dash != null) {
         var4 = new Dasher((PathConsumer2D)var4, this.dash, this.dashPhase);
      }

      OpenPiscesPrismUtils.feedConsumer(var1.getPathIterator((BaseTransform)null), (PathConsumer2D)var4);
      return var2;
   }

   Shape strokeRoundRectangle(RoundRectangle2D var1) {
      if (!(var1.width < 0.0F) && !(var1.height < 0.0F)) {
         if (this.isDashed()) {
            return null;
         } else {
            float var3 = var1.arcWidth;
            float var4 = var1.arcHeight;
            int var2;
            if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
               if (var3 < var4 * 0.9F || var4 < var3 * 0.9F) {
                  return null;
               }

               var2 = 1;
            } else {
               var4 = 0.0F;
               var3 = 0.0F;
               if (this.type == 1) {
                  var2 = 0;
               } else {
                  var2 = this.join;
                  if (var2 == 0 && this.miterLimit < SQRT_2) {
                     var2 = 2;
                  }
               }
            }

            float var5;
            float var6;
            if (this.type == 1) {
               var6 = 0.0F;
               var5 = this.width;
            } else if (this.type == 2) {
               var6 = this.width;
               var5 = 0.0F;
            } else {
               var6 = var5 = this.width / 2.0F;
            }

            Object var7;
            switch (var2) {
               case 0:
                  var7 = new RoundRectangle2D(var1.x - var6, var1.y - var6, var1.width + var6 * 2.0F, var1.height + var6 * 2.0F, 0.0F, 0.0F);
                  break;
               case 1:
                  var7 = new RoundRectangle2D(var1.x - var6, var1.y - var6, var1.width + var6 * 2.0F, var1.height + var6 * 2.0F, var3 + var6 * 2.0F, var4 + var6 * 2.0F);
                  break;
               case 2:
                  var7 = makeBeveledRect(var1.x, var1.y, var1.width, var1.height, var6);
                  break;
               default:
                  throw new InternalError("Unrecognized line join style");
            }

            if (!(var1.width <= var5 * 2.0F) && !(var1.height <= var5 * 2.0F)) {
               var3 -= var5 * 2.0F;
               var4 -= var5 * 2.0F;
               if (var3 <= 0.0F || var4 <= 0.0F) {
                  var4 = 0.0F;
                  var3 = 0.0F;
               }

               RoundRectangle2D var8 = new RoundRectangle2D(var1.x + var5, var1.y + var5, var1.width - var5 * 2.0F, var1.height - var5 * 2.0F, var3, var4);
               Path2D var9 = var7 instanceof Path2D ? (Path2D)var7 : new Path2D((Shape)var7);
               var9.setWindingRule(0);
               var9.append((Shape)var8, false);
               return var9;
            } else {
               return (Shape)var7;
            }
         }
      } else {
         return new Path2D();
      }
   }

   static Shape makeBeveledRect(float var0, float var1, float var2, float var3, float var4) {
      float var7 = var0 + var2;
      float var8 = var1 + var3;
      Path2D var9 = new Path2D();
      var9.moveTo(var0, var1 - var4);
      var9.lineTo(var7, var1 - var4);
      var9.lineTo(var7 + var4, var1);
      var9.lineTo(var7 + var4, var8);
      var9.lineTo(var7, var8 + var4);
      var9.lineTo(var0, var8 + var4);
      var9.lineTo(var0 - var4, var8);
      var9.lineTo(var0 - var4, var1);
      var9.closePath();
      return var9;
   }

   protected Shape makeIntersectedShape(Shape var1, Shape var2) {
      return new CAGShapePair(var1, var2, 4);
   }

   protected Shape makeSubtractedShape(Shape var1, Shape var2) {
      return new CAGShapePair(var1, var2, 1);
   }

   public int hashCode() {
      int var1 = Float.floatToIntBits(this.width);
      var1 = var1 * 31 + this.join;
      var1 = var1 * 31 + this.cap;
      var1 = var1 * 31 + Float.floatToIntBits(this.miterLimit);
      if (this.dash != null) {
         var1 = var1 * 31 + Float.floatToIntBits(this.dashPhase);

         for(int var2 = 0; var2 < this.dash.length; ++var2) {
            var1 = var1 * 31 + Float.floatToIntBits(this.dash[var2]);
         }
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BasicStroke)) {
         return false;
      } else {
         BasicStroke var2 = (BasicStroke)var1;
         if (this.width != var2.width) {
            return false;
         } else if (this.join != var2.join) {
            return false;
         } else if (this.cap != var2.cap) {
            return false;
         } else if (this.miterLimit != var2.miterLimit) {
            return false;
         } else {
            if (this.dash != null) {
               if (this.dashPhase != var2.dashPhase) {
                  return false;
               }

               if (!Arrays.equals(this.dash, var2.dash)) {
                  return false;
               }
            } else if (var2.dash != null) {
               return false;
            }

            return true;
         }
      }
   }

   public BasicStroke copy() {
      return new BasicStroke(this.type, this.width, this.cap, this.join, this.miterLimit, this.dash, this.dashPhase);
   }

   static class CAGShapePair extends GeneralShapePair {
      private Shape cagshape;

      public CAGShapePair(Shape var1, Shape var2, int var3) {
         super(var1, var2, var3);
      }

      public PathIterator getPathIterator(BaseTransform var1) {
         if (this.cagshape == null) {
            Area var2 = new Area(this.getOuterShape());
            Area var3 = new Area(this.getInnerShape());
            if (this.getCombinationType() == 4) {
               var2.intersect(var3);
            } else {
               var2.subtract(var3);
            }

            this.cagshape = var2;
         }

         return this.cagshape.getPathIterator(var1);
      }
   }
}
