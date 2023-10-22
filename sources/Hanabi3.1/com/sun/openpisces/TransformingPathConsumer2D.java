package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.transform.BaseTransform;

public abstract class TransformingPathConsumer2D implements PathConsumer2D {
   protected PathConsumer2D out;

   public TransformingPathConsumer2D(PathConsumer2D var1) {
      this.out = var1;
   }

   public void setConsumer(PathConsumer2D var1) {
      this.out = var1;
   }

   static final class DeltaTransformFilter extends TransformingPathConsumer2D {
      private float Mxx;
      private float Mxy;
      private float Myx;
      private float Myy;

      DeltaTransformFilter(PathConsumer2D var1, float var2, float var3, float var4, float var5) {
         super(var1);
         this.set(var2, var3, var4, var5);
      }

      public void set(float var1, float var2, float var3, float var4) {
         this.Mxx = var1;
         this.Mxy = var2;
         this.Myx = var3;
         this.Myy = var4;
      }

      public void moveTo(float var1, float var2) {
         this.out.moveTo(var1 * this.Mxx + var2 * this.Mxy, var1 * this.Myx + var2 * this.Myy);
      }

      public void lineTo(float var1, float var2) {
         this.out.lineTo(var1 * this.Mxx + var2 * this.Mxy, var1 * this.Myx + var2 * this.Myy);
      }

      public void quadTo(float var1, float var2, float var3, float var4) {
         this.out.quadTo(var1 * this.Mxx + var2 * this.Mxy, var1 * this.Myx + var2 * this.Myy, var3 * this.Mxx + var4 * this.Mxy, var3 * this.Myx + var4 * this.Myy);
      }

      public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.out.curveTo(var1 * this.Mxx + var2 * this.Mxy, var1 * this.Myx + var2 * this.Myy, var3 * this.Mxx + var4 * this.Mxy, var3 * this.Myx + var4 * this.Myy, var5 * this.Mxx + var6 * this.Mxy, var5 * this.Myx + var6 * this.Myy);
      }

      public void closePath() {
         this.out.closePath();
      }

      public void pathDone() {
         this.out.pathDone();
      }

      public long getNativeConsumer() {
         return 0L;
      }
   }

   static final class DeltaScaleFilter extends TransformingPathConsumer2D {
      private float sx;
      private float sy;

      public DeltaScaleFilter(PathConsumer2D var1, float var2, float var3) {
         super(var1);
         this.set(var2, var3);
      }

      public void set(float var1, float var2) {
         this.sx = var1;
         this.sy = var2;
      }

      public void moveTo(float var1, float var2) {
         this.out.moveTo(var1 * this.sx, var2 * this.sy);
      }

      public void lineTo(float var1, float var2) {
         this.out.lineTo(var1 * this.sx, var2 * this.sy);
      }

      public void quadTo(float var1, float var2, float var3, float var4) {
         this.out.quadTo(var1 * this.sx, var2 * this.sy, var3 * this.sx, var4 * this.sy);
      }

      public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.out.curveTo(var1 * this.sx, var2 * this.sy, var3 * this.sx, var4 * this.sy, var5 * this.sx, var6 * this.sy);
      }

      public void closePath() {
         this.out.closePath();
      }

      public void pathDone() {
         this.out.pathDone();
      }

      public long getNativeConsumer() {
         return 0L;
      }
   }

   static final class TransformFilter extends TransformingPathConsumer2D {
      private float Mxx;
      private float Mxy;
      private float Mxt;
      private float Myx;
      private float Myy;
      private float Myt;

      TransformFilter(PathConsumer2D var1, float var2, float var3, float var4, float var5, float var6, float var7) {
         super(var1);
         this.set(var2, var3, var4, var5, var6, var7);
      }

      public void set(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.Mxx = var1;
         this.Mxy = var2;
         this.Mxt = var3;
         this.Myx = var4;
         this.Myy = var5;
         this.Myt = var6;
      }

      public void moveTo(float var1, float var2) {
         this.out.moveTo(var1 * this.Mxx + var2 * this.Mxy + this.Mxt, var1 * this.Myx + var2 * this.Myy + this.Myt);
      }

      public void lineTo(float var1, float var2) {
         this.out.lineTo(var1 * this.Mxx + var2 * this.Mxy + this.Mxt, var1 * this.Myx + var2 * this.Myy + this.Myt);
      }

      public void quadTo(float var1, float var2, float var3, float var4) {
         this.out.quadTo(var1 * this.Mxx + var2 * this.Mxy + this.Mxt, var1 * this.Myx + var2 * this.Myy + this.Myt, var3 * this.Mxx + var4 * this.Mxy + this.Mxt, var3 * this.Myx + var4 * this.Myy + this.Myt);
      }

      public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.out.curveTo(var1 * this.Mxx + var2 * this.Mxy + this.Mxt, var1 * this.Myx + var2 * this.Myy + this.Myt, var3 * this.Mxx + var4 * this.Mxy + this.Mxt, var3 * this.Myx + var4 * this.Myy + this.Myt, var5 * this.Mxx + var6 * this.Mxy + this.Mxt, var5 * this.Myx + var6 * this.Myy + this.Myt);
      }

      public void closePath() {
         this.out.closePath();
      }

      public void pathDone() {
         this.out.pathDone();
      }

      public long getNativeConsumer() {
         return 0L;
      }
   }

   static final class ScaleTranslateFilter extends TransformingPathConsumer2D {
      private float sx;
      private float sy;
      private float tx;
      private float ty;

      ScaleTranslateFilter(PathConsumer2D var1, float var2, float var3, float var4, float var5) {
         super(var1);
         this.set(var2, var3, var4, var5);
      }

      public void set(float var1, float var2, float var3, float var4) {
         this.sx = var1;
         this.sy = var2;
         this.tx = var3;
         this.ty = var4;
      }

      public void moveTo(float var1, float var2) {
         this.out.moveTo(var1 * this.sx + this.tx, var2 * this.sy + this.ty);
      }

      public void lineTo(float var1, float var2) {
         this.out.lineTo(var1 * this.sx + this.tx, var2 * this.sy + this.ty);
      }

      public void quadTo(float var1, float var2, float var3, float var4) {
         this.out.quadTo(var1 * this.sx + this.tx, var2 * this.sy + this.ty, var3 * this.sx + this.tx, var4 * this.sy + this.ty);
      }

      public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.out.curveTo(var1 * this.sx + this.tx, var2 * this.sy + this.ty, var3 * this.sx + this.tx, var4 * this.sy + this.ty, var5 * this.sx + this.tx, var6 * this.sy + this.ty);
      }

      public void closePath() {
         this.out.closePath();
      }

      public void pathDone() {
         this.out.pathDone();
      }

      public long getNativeConsumer() {
         return 0L;
      }
   }

   static final class TranslateFilter extends TransformingPathConsumer2D {
      private float tx;
      private float ty;

      TranslateFilter(PathConsumer2D var1, float var2, float var3) {
         super(var1);
         this.set(var2, var3);
      }

      public void set(float var1, float var2) {
         this.tx = var1;
         this.ty = var2;
      }

      public void moveTo(float var1, float var2) {
         this.out.moveTo(var1 + this.tx, var2 + this.ty);
      }

      public void lineTo(float var1, float var2) {
         this.out.lineTo(var1 + this.tx, var2 + this.ty);
      }

      public void quadTo(float var1, float var2, float var3, float var4) {
         this.out.quadTo(var1 + this.tx, var2 + this.ty, var3 + this.tx, var4 + this.ty);
      }

      public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.out.curveTo(var1 + this.tx, var2 + this.ty, var3 + this.tx, var4 + this.ty, var5 + this.tx, var6 + this.ty);
      }

      public void closePath() {
         this.out.closePath();
      }

      public void pathDone() {
         this.out.pathDone();
      }

      public long getNativeConsumer() {
         return 0L;
      }
   }

   public static final class FilterSet {
      private TranslateFilter translater;
      private DeltaScaleFilter deltascaler;
      private ScaleTranslateFilter scaletranslater;
      private DeltaTransformFilter deltatransformer;
      private TransformFilter transformer;

      public PathConsumer2D getConsumer(PathConsumer2D var1, BaseTransform var2) {
         if (var2 == null) {
            return var1;
         } else {
            float var3 = (float)var2.getMxx();
            float var4 = (float)var2.getMxy();
            float var5 = (float)var2.getMxt();
            float var6 = (float)var2.getMyx();
            float var7 = (float)var2.getMyy();
            float var8 = (float)var2.getMyt();
            if (var4 == 0.0F && var6 == 0.0F) {
               if (var3 == 1.0F && var7 == 1.0F) {
                  if (var5 == 0.0F && var8 == 0.0F) {
                     return var1;
                  } else {
                     if (this.translater == null) {
                        this.translater = new TranslateFilter(var1, var5, var8);
                     } else {
                        this.translater.set(var5, var8);
                     }

                     return this.translater;
                  }
               } else if (var5 == 0.0F && var8 == 0.0F) {
                  if (this.deltascaler == null) {
                     this.deltascaler = new DeltaScaleFilter(var1, var3, var7);
                  } else {
                     this.deltascaler.set(var3, var7);
                  }

                  return this.deltascaler;
               } else {
                  if (this.scaletranslater == null) {
                     this.scaletranslater = new ScaleTranslateFilter(var1, var3, var7, var5, var8);
                  } else {
                     this.scaletranslater.set(var3, var7, var5, var8);
                  }

                  return this.scaletranslater;
               }
            } else if (var5 == 0.0F && var8 == 0.0F) {
               if (this.deltatransformer == null) {
                  this.deltatransformer = new DeltaTransformFilter(var1, var3, var4, var6, var7);
               } else {
                  this.deltatransformer.set(var3, var4, var6, var7);
               }

               return this.deltatransformer;
            } else {
               if (this.transformer == null) {
                  this.transformer = new TransformFilter(var1, var3, var4, var5, var6, var7, var8);
               } else {
                  this.transformer.set(var3, var4, var5, var6, var7, var8);
               }

               return this.transformer;
            }
         }
      }
   }
}
