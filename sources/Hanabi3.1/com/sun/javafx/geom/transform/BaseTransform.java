package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;

public abstract class BaseTransform implements CanTransformVec3d {
   public static final BaseTransform IDENTITY_TRANSFORM = new Identity();
   protected static final int TYPE_UNKNOWN = -1;
   public static final int TYPE_IDENTITY = 0;
   public static final int TYPE_TRANSLATION = 1;
   public static final int TYPE_UNIFORM_SCALE = 2;
   public static final int TYPE_GENERAL_SCALE = 4;
   public static final int TYPE_MASK_SCALE = 6;
   public static final int TYPE_FLIP = 64;
   public static final int TYPE_QUADRANT_ROTATION = 8;
   public static final int TYPE_GENERAL_ROTATION = 16;
   public static final int TYPE_MASK_ROTATION = 24;
   public static final int TYPE_GENERAL_TRANSFORM = 32;
   public static final int TYPE_AFFINE2D_MASK = 127;
   public static final int TYPE_AFFINE_3D = 128;
   static final double EPSILON_ABSOLUTE = 1.0E-5;

   static void degreeError(Degree var0) {
      throw new InternalError("does not support higher than " + var0 + " operations");
   }

   public static BaseTransform getInstance(BaseTransform var0) {
      if (var0.isIdentity()) {
         return IDENTITY_TRANSFORM;
      } else if (var0.isTranslateOrIdentity()) {
         return new Translate2D(var0);
      } else {
         return (BaseTransform)(var0.is2D() ? new Affine2D(var0) : new Affine3D(var0));
      }
   }

   public static BaseTransform getInstance(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22) {
      return (BaseTransform)(var4 == 0.0 && var12 == 0.0 && var16 == 0.0 && var18 == 0.0 && var20 == 1.0 && var22 == 0.0 ? getInstance(var0, var8, var2, var10, var6, var14) : new Affine3D(var0, var2, var4, var6, var8, var10, var12, var14, var16, var18, var20, var22));
   }

   public static BaseTransform getInstance(double var0, double var2, double var4, double var6, double var8, double var10) {
      return (BaseTransform)(var0 == 1.0 && var2 == 0.0 && var4 == 0.0 && var6 == 1.0 ? getTranslateInstance(var8, var10) : new Affine2D(var0, var2, var4, var6, var8, var10));
   }

   public static BaseTransform getTranslateInstance(double var0, double var2) {
      return (BaseTransform)(var0 == 0.0 && var2 == 0.0 ? IDENTITY_TRANSFORM : new Translate2D(var0, var2));
   }

   public static BaseTransform getScaleInstance(double var0, double var2) {
      return getInstance(var0, 0.0, 0.0, var2, 0.0, 0.0);
   }

   public static BaseTransform getRotateInstance(double var0, double var2, double var4) {
      Affine2D var6 = new Affine2D();
      var6.setToRotation(var0, var2, var4);
      return var6;
   }

   public abstract Degree getDegree();

   public abstract int getType();

   public abstract boolean isIdentity();

   public abstract boolean isTranslateOrIdentity();

   public abstract boolean is2D();

   public abstract double getDeterminant();

   public double getMxx() {
      return 1.0;
   }

   public double getMxy() {
      return 0.0;
   }

   public double getMxz() {
      return 0.0;
   }

   public double getMxt() {
      return 0.0;
   }

   public double getMyx() {
      return 0.0;
   }

   public double getMyy() {
      return 1.0;
   }

   public double getMyz() {
      return 0.0;
   }

   public double getMyt() {
      return 0.0;
   }

   public double getMzx() {
      return 0.0;
   }

   public double getMzy() {
      return 0.0;
   }

   public double getMzz() {
      return 1.0;
   }

   public double getMzt() {
      return 0.0;
   }

   public abstract Point2D transform(Point2D var1, Point2D var2);

   public abstract Point2D inverseTransform(Point2D var1, Point2D var2) throws NoninvertibleTransformException;

   public abstract Vec3d transform(Vec3d var1, Vec3d var2);

   public abstract Vec3d deltaTransform(Vec3d var1, Vec3d var2);

   public abstract Vec3d inverseTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException;

   public abstract Vec3d inverseDeltaTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException;

   public abstract void transform(float[] var1, int var2, float[] var3, int var4, int var5);

   public abstract void transform(double[] var1, int var2, double[] var3, int var4, int var5);

   public abstract void transform(float[] var1, int var2, double[] var3, int var4, int var5);

   public abstract void transform(double[] var1, int var2, float[] var3, int var4, int var5);

   public abstract void deltaTransform(float[] var1, int var2, float[] var3, int var4, int var5);

   public abstract void deltaTransform(double[] var1, int var2, double[] var3, int var4, int var5);

   public abstract void inverseTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException;

   public abstract void inverseDeltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException;

   public abstract void inverseTransform(double[] var1, int var2, double[] var3, int var4, int var5) throws NoninvertibleTransformException;

   public abstract BaseBounds transform(BaseBounds var1, BaseBounds var2);

   public abstract void transform(Rectangle var1, Rectangle var2);

   public abstract BaseBounds inverseTransform(BaseBounds var1, BaseBounds var2) throws NoninvertibleTransformException;

   public abstract void inverseTransform(Rectangle var1, Rectangle var2) throws NoninvertibleTransformException;

   public abstract Shape createTransformedShape(Shape var1);

   public abstract void setToIdentity();

   public abstract void setTransform(BaseTransform var1);

   public abstract void invert() throws NoninvertibleTransformException;

   public abstract void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11);

   public abstract void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23);

   public abstract BaseTransform deriveWithTranslation(double var1, double var3);

   public abstract BaseTransform deriveWithTranslation(double var1, double var3, double var5);

   public abstract BaseTransform deriveWithScale(double var1, double var3, double var5);

   public abstract BaseTransform deriveWithRotation(double var1, double var3, double var5, double var7);

   public abstract BaseTransform deriveWithPreTranslation(double var1, double var3);

   public abstract BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11);

   public abstract BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23);

   public abstract BaseTransform deriveWithPreConcatenation(BaseTransform var1);

   public abstract BaseTransform deriveWithConcatenation(BaseTransform var1);

   public abstract BaseTransform deriveWithNewTransform(BaseTransform var1);

   public abstract BaseTransform createInverse() throws NoninvertibleTransformException;

   public abstract BaseTransform copy();

   public int hashCode() {
      if (this.isIdentity()) {
         return 0;
      } else {
         long var1 = 0L;
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMzz());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMzy());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMzx());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyz());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxz());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyy());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyx());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxy());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxx());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMzt());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyt());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxt());
         return (int)var1 ^ (int)(var1 >> 32);
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BaseTransform)) {
         return false;
      } else {
         BaseTransform var2 = (BaseTransform)var1;
         return this.getMxx() == var2.getMxx() && this.getMxy() == var2.getMxy() && this.getMxz() == var2.getMxz() && this.getMxt() == var2.getMxt() && this.getMyx() == var2.getMyx() && this.getMyy() == var2.getMyy() && this.getMyz() == var2.getMyz() && this.getMyt() == var2.getMyt() && this.getMzx() == var2.getMzx() && this.getMzy() == var2.getMzy() && this.getMzz() == var2.getMzz() && this.getMzt() == var2.getMzt();
      }
   }

   static Point2D makePoint(Point2D var0, Point2D var1) {
      if (var1 == null) {
         var1 = new Point2D();
      }

      return var1;
   }

   public static boolean almostZero(double var0) {
      return var0 < 1.0E-5 && var0 > -1.0E-5;
   }

   public String toString() {
      return "Matrix: degree " + this.getDegree() + "\n" + this.getMxx() + ", " + this.getMxy() + ", " + this.getMxz() + ", " + this.getMxt() + "\n" + this.getMyx() + ", " + this.getMyy() + ", " + this.getMyz() + ", " + this.getMyt() + "\n" + this.getMzx() + ", " + this.getMzy() + ", " + this.getMzz() + ", " + this.getMzt() + "\n";
   }

   public static enum Degree {
      IDENTITY,
      TRANSLATE_2D,
      AFFINE_2D,
      TRANSLATE_3D,
      AFFINE_3D;
   }
}
