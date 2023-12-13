package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;

public class Translate2D extends BaseTransform {
   private double mxt;
   private double myt;
   private static final long BASE_HASH;

   public static BaseTransform getInstance(double var0, double var2) {
      return (BaseTransform)(var0 == 0.0 && var2 == 0.0 ? IDENTITY_TRANSFORM : new Translate2D(var0, var2));
   }

   public Translate2D(double var1, double var3) {
      this.mxt = var1;
      this.myt = var3;
   }

   public Translate2D(BaseTransform var1) {
      if (!var1.isTranslateOrIdentity()) {
         degreeError(BaseTransform.Degree.TRANSLATE_2D);
      }

      this.mxt = var1.getMxt();
      this.myt = var1.getMyt();
   }

   public BaseTransform.Degree getDegree() {
      return BaseTransform.Degree.TRANSLATE_2D;
   }

   public double getDeterminant() {
      return 1.0;
   }

   public double getMxt() {
      return this.mxt;
   }

   public double getMyt() {
      return this.myt;
   }

   public int getType() {
      return this.mxt == 0.0 && this.myt == 0.0 ? 0 : 1;
   }

   public boolean isIdentity() {
      return this.mxt == 0.0 && this.myt == 0.0;
   }

   public boolean isTranslateOrIdentity() {
      return true;
   }

   public boolean is2D() {
      return true;
   }

   public Point2D transform(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = makePoint(var1, var2);
      }

      var2.setLocation((float)((double)var1.x + this.mxt), (float)((double)var1.y + this.myt));
      return var2;
   }

   public Point2D inverseTransform(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = makePoint(var1, var2);
      }

      var2.setLocation((float)((double)var1.x - this.mxt), (float)((double)var1.y - this.myt));
      return var2;
   }

   public Vec3d transform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      var2.x = var1.x + this.mxt;
      var2.y = var1.y + this.myt;
      var2.z = var1.z;
      return var2;
   }

   public Vec3d deltaTransform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      var2.set(var1);
      return var2;
   }

   public Vec3d inverseTransform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      var2.x = var1.x - this.mxt;
      var2.y = var1.y - this.myt;
      var2.z = var1.z;
      return var2;
   }

   public Vec3d inverseDeltaTransform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      var2.set(var1);
      return var2;
   }

   public void transform(float[] var1, int var2, float[] var3, int var4, int var5) {
      float var6 = (float)this.mxt;
      float var7 = (float)this.myt;
      if (var3 == var1) {
         if (var4 > var2 && var4 < var2 + var5 * 2) {
            System.arraycopy(var1, var2, var3, var4, var5 * 2);
            var2 = var4;
         }

         if (var4 == var2 && var6 == 0.0F && var7 == 0.0F) {
            return;
         }
      }

      for(int var8 = 0; var8 < var5; ++var8) {
         var3[var4++] = var1[var2++] + var6;
         var3[var4++] = var1[var2++] + var7;
      }

   }

   public void transform(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.mxt;
      double var8 = this.myt;
      if (var3 == var1) {
         if (var4 > var2 && var4 < var2 + var5 * 2) {
            System.arraycopy(var1, var2, var3, var4, var5 * 2);
            var2 = var4;
         }

         if (var4 == var2 && var6 == 0.0 && var8 == 0.0) {
            return;
         }
      }

      for(int var10 = 0; var10 < var5; ++var10) {
         var3[var4++] = var1[var2++] + var6;
         var3[var4++] = var1[var2++] + var8;
      }

   }

   public void transform(float[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.mxt;
      double var8 = this.myt;

      for(int var10 = 0; var10 < var5; ++var10) {
         var3[var4++] = (double)var1[var2++] + var6;
         var3[var4++] = (double)var1[var2++] + var8;
      }

   }

   public void transform(double[] var1, int var2, float[] var3, int var4, int var5) {
      double var6 = this.mxt;
      double var8 = this.myt;

      for(int var10 = 0; var10 < var5; ++var10) {
         var3[var4++] = (float)(var1[var2++] + var6);
         var3[var4++] = (float)(var1[var2++] + var8);
      }

   }

   public void deltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) {
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public void deltaTransform(double[] var1, int var2, double[] var3, int var4, int var5) {
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public void inverseTransform(float[] var1, int var2, float[] var3, int var4, int var5) {
      float var6 = (float)this.mxt;
      float var7 = (float)this.myt;
      if (var3 == var1) {
         if (var4 > var2 && var4 < var2 + var5 * 2) {
            System.arraycopy(var1, var2, var3, var4, var5 * 2);
            var2 = var4;
         }

         if (var4 == var2 && var6 == 0.0F && var7 == 0.0F) {
            return;
         }
      }

      for(int var8 = 0; var8 < var5; ++var8) {
         var3[var4++] = var1[var2++] - var6;
         var3[var4++] = var1[var2++] - var7;
      }

   }

   public void inverseDeltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) {
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public void inverseTransform(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.mxt;
      double var8 = this.myt;
      if (var3 == var1) {
         if (var4 > var2 && var4 < var2 + var5 * 2) {
            System.arraycopy(var1, var2, var3, var4, var5 * 2);
            var2 = var4;
         }

         if (var4 == var2 && var6 == 0.0 && var8 == 0.0) {
            return;
         }
      }

      for(int var10 = 0; var10 < var5; ++var10) {
         var3[var4++] = var1[var2++] - var6;
         var3[var4++] = var1[var2++] - var8;
      }

   }

   public BaseBounds transform(BaseBounds var1, BaseBounds var2) {
      float var3 = (float)((double)var1.getMinX() + this.mxt);
      float var4 = (float)((double)var1.getMinY() + this.myt);
      float var5 = var1.getMinZ();
      float var6 = (float)((double)var1.getMaxX() + this.mxt);
      float var7 = (float)((double)var1.getMaxY() + this.myt);
      float var8 = var1.getMaxZ();
      return var2.deriveWithNewBounds(var3, var4, var5, var6, var7, var8);
   }

   public void transform(Rectangle var1, Rectangle var2) {
      transform(var1, var2, this.mxt, this.myt);
   }

   public BaseBounds inverseTransform(BaseBounds var1, BaseBounds var2) {
      float var3 = (float)((double)var1.getMinX() - this.mxt);
      float var4 = (float)((double)var1.getMinY() - this.myt);
      float var5 = var1.getMinZ();
      float var6 = (float)((double)var1.getMaxX() - this.mxt);
      float var7 = (float)((double)var1.getMaxY() - this.myt);
      float var8 = var1.getMaxZ();
      return var2.deriveWithNewBounds(var3, var4, var5, var6, var7, var8);
   }

   public void inverseTransform(Rectangle var1, Rectangle var2) {
      transform(var1, var2, -this.mxt, -this.myt);
   }

   static void transform(Rectangle var0, Rectangle var1, double var2, double var4) {
      int var6 = (int)var2;
      int var7 = (int)var4;
      if ((double)var6 == var2 && (double)var7 == var4) {
         var1.setBounds(var0);
         var1.translate(var6, var7);
      } else {
         double var8 = (double)var0.x + var2;
         double var10 = (double)var0.y + var4;
         double var12 = Math.ceil(var8 + (double)var0.width);
         double var14 = Math.ceil(var10 + (double)var0.height);
         var8 = Math.floor(var8);
         var10 = Math.floor(var10);
         var1.setBounds((int)var8, (int)var10, (int)(var12 - var8), (int)(var14 - var10));
      }

   }

   public Shape createTransformedShape(Shape var1) {
      return new Path2D(var1, this);
   }

   public void setToIdentity() {
      this.mxt = this.myt = 0.0;
   }

   public void setTransform(BaseTransform var1) {
      if (!var1.isTranslateOrIdentity()) {
         degreeError(BaseTransform.Degree.TRANSLATE_2D);
      }

      this.mxt = var1.getMxt();
      this.myt = var1.getMyt();
   }

   public void invert() {
      this.mxt = -this.mxt;
      this.myt = -this.myt;
   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (var1 != 1.0 || var3 != 0.0 || var5 != 0.0 || var7 != 1.0) {
         degreeError(BaseTransform.Degree.TRANSLATE_2D);
      }

      this.mxt = var9;
      this.myt = var11;
   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var1 != 1.0 || var3 != 0.0 || var5 != 0.0 || var9 != 0.0 || var11 != 1.0 || var13 != 0.0 || var17 != 0.0 || var19 != 0.0 || var21 != 1.0 || var23 != 0.0) {
         degreeError(BaseTransform.Degree.TRANSLATE_2D);
      }

      this.mxt = var7;
      this.myt = var15;
   }

   public BaseTransform deriveWithTranslation(double var1, double var3) {
      this.mxt += var1;
      this.myt += var3;
      return this;
   }

   public BaseTransform deriveWithTranslation(double var1, double var3, double var5) {
      if (var5 == 0.0) {
         this.mxt += var1;
         this.myt += var3;
         return this;
      } else {
         Affine3D var7 = new Affine3D();
         var7.translate(this.mxt + var1, this.myt + var3, var5);
         return var7;
      }
   }

   public BaseTransform deriveWithScale(double var1, double var3, double var5) {
      if (var5 == 1.0) {
         if (var1 == 1.0 && var3 == 1.0) {
            return this;
         } else {
            Affine2D var8 = new Affine2D();
            var8.translate(this.mxt, this.myt);
            var8.scale(var1, var3);
            return var8;
         }
      } else {
         Affine3D var7 = new Affine3D();
         var7.translate(this.mxt, this.myt);
         var7.scale(var1, var3, var5);
         return var7;
      }
   }

   public BaseTransform deriveWithRotation(double var1, double var3, double var5, double var7) {
      if (var1 == 0.0) {
         return this;
      } else if (almostZero(var3) && almostZero(var5)) {
         if (var7 == 0.0) {
            return this;
         } else {
            Affine2D var10 = new Affine2D();
            var10.translate(this.mxt, this.myt);
            if (var7 > 0.0) {
               var10.rotate(var1);
            } else if (var7 < 0.0) {
               var10.rotate(-var1);
            }

            return var10;
         }
      } else {
         Affine3D var9 = new Affine3D();
         var9.translate(this.mxt, this.myt);
         var9.rotate(var1, var3, var5, var7);
         return var9;
      }
   }

   public BaseTransform deriveWithPreTranslation(double var1, double var3) {
      this.mxt += var1;
      this.myt += var3;
      return this;
   }

   public BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (var1 == 1.0 && var3 == 0.0 && var5 == 0.0 && var7 == 1.0) {
         this.mxt += var9;
         this.myt += var11;
         return this;
      } else {
         return new Affine2D(var1, var3, var5, var7, this.mxt + var9, this.myt + var11);
      }
   }

   public BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      return (BaseTransform)(var5 == 0.0 && var13 == 0.0 && var17 == 0.0 && var19 == 0.0 && var21 == 1.0 && var23 == 0.0 ? this.deriveWithConcatenation(var1, var9, var3, var11, var7, var15) : new Affine3D(var1, var3, var5, var7 + this.mxt, var9, var11, var13, var15 + this.myt, var17, var19, var21, var23));
   }

   public BaseTransform deriveWithConcatenation(BaseTransform var1) {
      if (var1.isTranslateOrIdentity()) {
         this.mxt += var1.getMxt();
         this.myt += var1.getMyt();
         return this;
      } else if (var1.is2D()) {
         return getInstance(var1.getMxx(), var1.getMyx(), var1.getMxy(), var1.getMyy(), this.mxt + var1.getMxt(), this.myt + var1.getMyt());
      } else {
         Affine3D var2 = new Affine3D(var1);
         var2.preTranslate(this.mxt, this.myt, 0.0);
         return var2;
      }
   }

   public BaseTransform deriveWithPreConcatenation(BaseTransform var1) {
      if (var1.isTranslateOrIdentity()) {
         this.mxt += var1.getMxt();
         this.myt += var1.getMyt();
         return this;
      } else if (var1.is2D()) {
         Affine2D var3 = new Affine2D(var1);
         var3.translate(this.mxt, this.myt);
         return var3;
      } else {
         Affine3D var2 = new Affine3D(var1);
         var2.translate(this.mxt, this.myt, 0.0);
         return var2;
      }
   }

   public BaseTransform deriveWithNewTransform(BaseTransform var1) {
      if (var1.isTranslateOrIdentity()) {
         this.mxt = var1.getMxt();
         this.myt = var1.getMyt();
         return this;
      } else {
         return getInstance(var1);
      }
   }

   public BaseTransform createInverse() {
      return (BaseTransform)(this.isIdentity() ? IDENTITY_TRANSFORM : new Translate2D(-this.mxt, -this.myt));
   }

   private static double _matround(double var0) {
      return Math.rint(var0 * 1.0E15) / 1.0E15;
   }

   public String toString() {
      return "Translate2D[" + _matround(this.mxt) + ", " + _matround(this.myt) + "]";
   }

   public BaseTransform copy() {
      return new Translate2D(this.mxt, this.myt);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BaseTransform)) {
         return false;
      } else {
         BaseTransform var2 = (BaseTransform)var1;
         return var2.isTranslateOrIdentity() && var2.getMxt() == this.mxt && var2.getMyt() == this.myt;
      }
   }

   public int hashCode() {
      if (this.isIdentity()) {
         return 0;
      } else {
         long var1 = BASE_HASH;
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyt());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxt());
         return (int)var1 ^ (int)(var1 >> 32);
      }
   }

   static {
      long var0 = 0L;
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyy());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyx());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxy());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxx());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzt());
      BASE_HASH = var0;
   }
}
