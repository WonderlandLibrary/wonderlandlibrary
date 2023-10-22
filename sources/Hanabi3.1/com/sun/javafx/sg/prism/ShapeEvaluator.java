package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Vector;

class ShapeEvaluator {
   private Shape savedv0;
   private Shape savedv1;
   private Geometry geom0;
   private Geometry geom1;

   public Shape evaluate(Shape var1, Shape var2, float var3) {
      if (this.savedv0 != var1 || this.savedv1 != var2) {
         if (this.savedv0 == var2 && this.savedv1 == var1) {
            Geometry var4 = this.geom0;
            this.geom0 = this.geom1;
            this.geom1 = var4;
         } else {
            this.recalculate(var1, var2);
         }

         this.savedv0 = var1;
         this.savedv1 = var2;
      }

      return this.getShape(var3);
   }

   private void recalculate(Shape var1, Shape var2) {
      this.geom0 = new Geometry(var1);
      this.geom1 = new Geometry(var2);
      float[] var3 = this.geom0.getTvals();
      float[] var4 = this.geom1.getTvals();
      float[] var5 = mergeTvals(var3, var4);
      this.geom0.setTvals(var5);
      this.geom1.setTvals(var5);
   }

   private Shape getShape(float var1) {
      return new MorphedShape(this.geom0, this.geom1, var1);
   }

   private static float[] mergeTvals(float[] var0, float[] var1) {
      int var2 = sortTvals(var0, var1, (float[])null);
      float[] var3 = new float[var2];
      sortTvals(var0, var1, var3);
      return var3;
   }

   private static int sortTvals(float[] var0, float[] var1, float[] var2) {
      int var3 = 0;
      int var4 = 0;

      int var5;
      for(var5 = 0; var3 < var0.length && var4 < var1.length; ++var5) {
         float var6 = var0[var3];
         float var7 = var1[var4];
         if (var6 <= var7) {
            if (var2 != null) {
               var2[var5] = var6;
            }

            ++var3;
         }

         if (var7 <= var6) {
            if (var2 != null) {
               var2[var5] = var7;
            }

            ++var4;
         }
      }

      return var5;
   }

   private static float interp(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private static class Iterator implements PathIterator {
      BaseTransform at;
      Geometry g0;
      Geometry g1;
      float t;
      int cindex;

      public Iterator(BaseTransform var1, Geometry var2, Geometry var3, float var4) {
         this.at = var1;
         this.g0 = var2;
         this.g1 = var3;
         this.t = var4;
      }

      public int getWindingRule() {
         return (double)this.t < 0.5 ? this.g0.getWindingRule() : this.g1.getWindingRule();
      }

      public boolean isDone() {
         return this.cindex > this.g0.getNumCoords();
      }

      public void next() {
         if (this.cindex == 0) {
            this.cindex = 2;
         } else {
            this.cindex += 6;
         }

      }

      public int currentSegment(float[] var1) {
         byte var2;
         byte var3;
         if (this.cindex == 0) {
            var2 = 0;
            var3 = 2;
         } else if (this.cindex >= this.g0.getNumCoords()) {
            var2 = 4;
            var3 = 0;
         } else {
            var2 = 3;
            var3 = 6;
         }

         if (var3 > 0) {
            for(int var4 = 0; var4 < var3; ++var4) {
               var1[var4] = ShapeEvaluator.interp(this.g0.getCoord(this.cindex + var4), this.g1.getCoord(this.cindex + var4), this.t);
            }

            if (this.at != null) {
               this.at.transform((float[])var1, 0, (float[])var1, 0, var3 / 2);
            }
         }

         return var2;
      }
   }

   private static class MorphedShape extends Shape {
      Geometry geom0;
      Geometry geom1;
      float t;

      MorphedShape(Geometry var1, Geometry var2, float var3) {
         this.geom0 = var1;
         this.geom1 = var2;
         this.t = var3;
      }

      public Rectangle getRectangle() {
         return new Rectangle(this.getBounds());
      }

      public RectBounds getBounds() {
         int var1 = this.geom0.getNumCoords();
         float var4;
         float var2 = var4 = ShapeEvaluator.interp(this.geom0.getCoord(0), this.geom1.getCoord(0), this.t);
         float var5;
         float var3 = var5 = ShapeEvaluator.interp(this.geom0.getCoord(1), this.geom1.getCoord(1), this.t);

         for(int var6 = 2; var6 < var1; var6 += 2) {
            float var7 = ShapeEvaluator.interp(this.geom0.getCoord(var6), this.geom1.getCoord(var6), this.t);
            float var8 = ShapeEvaluator.interp(this.geom0.getCoord(var6 + 1), this.geom1.getCoord(var6 + 1), this.t);
            if (var2 > var7) {
               var2 = var7;
            }

            if (var3 > var8) {
               var3 = var8;
            }

            if (var4 < var7) {
               var4 = var7;
            }

            if (var5 < var8) {
               var5 = var8;
            }
         }

         return new RectBounds(var2, var3, var4, var5);
      }

      public boolean contains(float var1, float var2) {
         return Path2D.contains(this.getPathIterator((BaseTransform)null), var1, var2);
      }

      public boolean intersects(float var1, float var2, float var3, float var4) {
         return Path2D.intersects(this.getPathIterator((BaseTransform)null), var1, var2, var3, var4);
      }

      public boolean contains(float var1, float var2, float var3, float var4) {
         return Path2D.contains(this.getPathIterator((BaseTransform)null), var1, var2, var3, var4);
      }

      public PathIterator getPathIterator(BaseTransform var1) {
         return new Iterator(var1, this.geom0, this.geom1, this.t);
      }

      public PathIterator getPathIterator(BaseTransform var1, float var2) {
         return new FlatteningPathIterator(this.getPathIterator(var1), var2);
      }

      public Shape copy() {
         return new Path2D(this);
      }
   }

   private static class Geometry {
      static final float THIRD = 0.33333334F;
      static final float MIN_LEN = 0.001F;
      float[] bezierCoords = new float[20];
      int numCoords;
      int windingrule;
      float[] myTvals;

      public Geometry(Shape var1) {
         PathIterator var2 = var1.getPathIterator((BaseTransform)null);
         this.windingrule = var2.getWindingRule();
         if (var2.isDone()) {
            this.numCoords = 8;
         }

         float[] var3 = new float[6];
         int var4 = var2.currentSegment(var3);
         var2.next();
         if (var4 != 0) {
            throw new IllegalPathStateException("missing initial moveto");
         } else {
            float var5;
            float var7;
            this.bezierCoords[0] = var5 = var7 = var3[0];
            float var6;
            float var8;
            this.bezierCoords[1] = var6 = var8 = var3[1];
            Vector var11 = new Vector();

            float var9;
            float var10;
            float var13;
            for(this.numCoords = 2; !var2.isDone(); var2.next()) {
               switch (var2.currentSegment(var3)) {
                  case 0:
                     if (var5 != var7 || var6 != var8) {
                        this.appendLineTo(var5, var6, var7, var8);
                        var5 = var7;
                        var6 = var8;
                     }

                     var9 = var3[0];
                     var10 = var3[1];
                     if (var5 != var9 || var6 != var10) {
                        var11.add(new Point2D(var7, var8));
                        this.appendLineTo(var5, var6, var9, var10);
                        var7 = var9;
                        var5 = var9;
                        var8 = var10;
                        var6 = var10;
                     }
                     break;
                  case 1:
                     var9 = var3[0];
                     var10 = var3[1];
                     this.appendLineTo(var5, var6, var9, var10);
                     var5 = var9;
                     var6 = var10;
                     break;
                  case 2:
                     float var12 = var3[0];
                     var13 = var3[1];
                     var9 = var3[2];
                     var10 = var3[3];
                     this.appendQuadTo(var5, var6, var12, var13, var9, var10);
                     var5 = var9;
                     var6 = var10;
                     break;
                  case 3:
                     this.appendCubicTo(var3[0], var3[1], var3[2], var3[3], var5 = var3[4], var6 = var3[5]);
                     break;
                  case 4:
                     if (var5 != var7 || var6 != var8) {
                        this.appendLineTo(var5, var6, var7, var8);
                        var5 = var7;
                        var6 = var8;
                     }
               }
            }

            if (this.numCoords < 8 || var5 != var7 || var6 != var8) {
               this.appendLineTo(var5, var6, var7, var8);
               var5 = var7;
               var6 = var8;
            }

            int var18;
            for(var18 = var11.size() - 1; var18 >= 0; --var18) {
               Point2D var19 = (Point2D)var11.get(var18);
               var9 = var19.x;
               var10 = var19.y;
               if (var5 != var9 || var6 != var10) {
                  this.appendLineTo(var5, var6, var9, var10);
                  var5 = var9;
                  var6 = var10;
               }
            }

            var18 = 0;
            var13 = this.bezierCoords[0];
            float var14 = this.bezierCoords[1];

            for(int var15 = 6; var15 < this.numCoords; var15 += 6) {
               float var16 = this.bezierCoords[var15];
               float var17 = this.bezierCoords[var15 + 1];
               if (var17 < var14 || var17 == var14 && var16 < var13) {
                  var18 = var15;
                  var13 = var16;
                  var14 = var17;
               }
            }

            if (var18 > 0) {
               float[] var20 = new float[this.numCoords];
               System.arraycopy(this.bezierCoords, var18, var20, 0, this.numCoords - var18);
               System.arraycopy(this.bezierCoords, 2, var20, this.numCoords - var18, var18);
               this.bezierCoords = var20;
            }

            float var21 = 0.0F;
            var5 = this.bezierCoords[0];
            var6 = this.bezierCoords[1];

            int var22;
            for(var22 = 2; var22 < this.numCoords; var22 += 2) {
               var9 = this.bezierCoords[var22];
               var10 = this.bezierCoords[var22 + 1];
               var21 += var5 * var10 - var9 * var6;
               var5 = var9;
               var6 = var10;
            }

            if (var21 < 0.0F) {
               var22 = 2;

               for(int var23 = this.numCoords - 4; var22 < var23; var23 -= 2) {
                  var5 = this.bezierCoords[var22];
                  var6 = this.bezierCoords[var22 + 1];
                  this.bezierCoords[var22] = this.bezierCoords[var23];
                  this.bezierCoords[var22 + 1] = this.bezierCoords[var23 + 1];
                  this.bezierCoords[var23] = var5;
                  this.bezierCoords[var23 + 1] = var6;
                  var22 += 2;
               }
            }

         }
      }

      private void appendLineTo(float var1, float var2, float var3, float var4) {
         this.appendCubicTo(ShapeEvaluator.interp(var1, var3, 0.33333334F), ShapeEvaluator.interp(var2, var4, 0.33333334F), ShapeEvaluator.interp(var3, var1, 0.33333334F), ShapeEvaluator.interp(var4, var2, 0.33333334F), var3, var4);
      }

      private void appendQuadTo(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.appendCubicTo(ShapeEvaluator.interp(var3, var1, 0.33333334F), ShapeEvaluator.interp(var4, var2, 0.33333334F), ShapeEvaluator.interp(var3, var5, 0.33333334F), ShapeEvaluator.interp(var4, var6, 0.33333334F), var5, var6);
      }

      private void appendCubicTo(float var1, float var2, float var3, float var4, float var5, float var6) {
         if (this.numCoords + 6 > this.bezierCoords.length) {
            int var7 = (this.numCoords - 2) * 2 + 2;
            float[] var8 = new float[var7];
            System.arraycopy(this.bezierCoords, 0, var8, 0, this.numCoords);
            this.bezierCoords = var8;
         }

         this.bezierCoords[this.numCoords++] = var1;
         this.bezierCoords[this.numCoords++] = var2;
         this.bezierCoords[this.numCoords++] = var3;
         this.bezierCoords[this.numCoords++] = var4;
         this.bezierCoords[this.numCoords++] = var5;
         this.bezierCoords[this.numCoords++] = var6;
      }

      public int getWindingRule() {
         return this.windingrule;
      }

      public int getNumCoords() {
         return this.numCoords;
      }

      public float getCoord(int var1) {
         return this.bezierCoords[var1];
      }

      public float[] getTvals() {
         if (this.myTvals != null) {
            return this.myTvals;
         } else {
            float[] var1 = new float[(this.numCoords - 2) / 6 + 1];
            float var2 = this.bezierCoords[0];
            float var3 = this.bezierCoords[1];
            float var4 = 0.0F;
            int var5 = 2;

            int var6;
            float var7;
            float var8;
            float var10;
            for(var6 = 0; var5 < this.numCoords; var3 = var10) {
               float var9 = this.bezierCoords[var5++];
               var10 = this.bezierCoords[var5++];
               var7 = var2 - var9;
               var8 = var3 - var10;
               float var11 = (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
               var7 = var9;
               var8 = var10;
               var9 = this.bezierCoords[var5++];
               var10 = this.bezierCoords[var5++];
               var7 -= var9;
               var8 -= var10;
               var11 += (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
               var7 = var9;
               var8 = var10;
               var9 = this.bezierCoords[var5++];
               var10 = this.bezierCoords[var5++];
               var7 -= var9;
               var8 -= var10;
               var11 += (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
               var2 -= var9;
               var3 -= var10;
               var11 += (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
               var11 /= 2.0F;
               if (var11 < 0.001F) {
                  var11 = 0.001F;
               }

               var4 += var11;
               var1[var6++] = var4;
               var2 = var9;
            }

            var7 = var1[0];
            var1[0] = 0.0F;

            for(var6 = 1; var6 < var1.length - 1; ++var6) {
               var8 = var1[var6];
               var1[var6] = var7 / var4;
               var7 = var8;
            }

            var1[var6] = 1.0F;
            return this.myTvals = var1;
         }
      }

      public void setTvals(float[] var1) {
         float[] var2 = this.bezierCoords;
         float[] var3 = new float[2 + (var1.length - 1) * 6];
         float[] var4 = this.getTvals();
         int var5 = 0;
         float var7;
         float var8;
         float var9;
         float var6 = var7 = var8 = var9 = var2[var5++];
         float var11;
         float var12;
         float var13;
         float var10 = var11 = var12 = var13 = var2[var5++];
         int var14 = 0;
         var3[var14++] = var6;
         var3[var14++] = var10;
         float var15 = 0.0F;
         float var16 = 0.0F;
         int var17 = 1;

         float var19;
         for(int var18 = 1; var18 < var1.length; var15 = var19) {
            if (var15 >= var16) {
               var6 = var9;
               var10 = var13;
               var7 = var2[var5++];
               var11 = var2[var5++];
               var8 = var2[var5++];
               var12 = var2[var5++];
               var9 = var2[var5++];
               var13 = var2[var5++];
               var16 = var4[var17++];
            }

            var19 = var1[var18++];
            if (var19 < var16) {
               float var20 = (var19 - var15) / (var16 - var15);
               var3[var14++] = var6 = ShapeEvaluator.interp(var6, var7, var20);
               var3[var14++] = var10 = ShapeEvaluator.interp(var10, var11, var20);
               var7 = ShapeEvaluator.interp(var7, var8, var20);
               var11 = ShapeEvaluator.interp(var11, var12, var20);
               var8 = ShapeEvaluator.interp(var8, var9, var20);
               var12 = ShapeEvaluator.interp(var12, var13, var20);
               var3[var14++] = var6 = ShapeEvaluator.interp(var6, var7, var20);
               var3[var14++] = var10 = ShapeEvaluator.interp(var10, var11, var20);
               var7 = ShapeEvaluator.interp(var7, var8, var20);
               var11 = ShapeEvaluator.interp(var11, var12, var20);
               var3[var14++] = var6 = ShapeEvaluator.interp(var6, var7, var20);
               var3[var14++] = var10 = ShapeEvaluator.interp(var10, var11, var20);
            } else {
               var3[var14++] = var7;
               var3[var14++] = var11;
               var3[var14++] = var8;
               var3[var14++] = var12;
               var3[var14++] = var9;
               var3[var14++] = var13;
            }
         }

         this.bezierCoords = var3;
         this.numCoords = var3.length;
         this.myTvals = var1;
      }
   }
}
