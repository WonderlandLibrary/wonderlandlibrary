package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;

public final class Identity extends BaseTransform {
   public BaseTransform.Degree getDegree() {
      return BaseTransform.Degree.IDENTITY;
   }

   public int getType() {
      return 0;
   }

   public boolean isIdentity() {
      return true;
   }

   public boolean isTranslateOrIdentity() {
      return true;
   }

   public boolean is2D() {
      return true;
   }

   public double getDeterminant() {
      return 1.0;
   }

   public Point2D transform(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = makePoint(var1, var2);
      }

      var2.setLocation(var1);
      return var2;
   }

   public Point2D inverseTransform(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = makePoint(var1, var2);
      }

      var2.setLocation(var1);
      return var2;
   }

   public Vec3d transform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         return new Vec3d(var1);
      } else {
         var2.set(var1);
         return var2;
      }
   }

   public Vec3d deltaTransform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         return new Vec3d(var1);
      } else {
         var2.set(var1);
         return var2;
      }
   }

   public Vec3d inverseTransform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         return new Vec3d(var1);
      } else {
         var2.set(var1);
         return var2;
      }
   }

   public Vec3d inverseDeltaTransform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         return new Vec3d(var1);
      } else {
         var2.set(var1);
         return var2;
      }
   }

   public void transform(float[] var1, int var2, float[] var3, int var4, int var5) {
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public void transform(double[] var1, int var2, double[] var3, int var4, int var5) {
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public void transform(float[] var1, int var2, double[] var3, int var4, int var5) {
      for(int var6 = 0; var6 < var5; ++var6) {
         var3[var4++] = (double)var1[var2++];
         var3[var4++] = (double)var1[var2++];
      }

   }

   public void transform(double[] var1, int var2, float[] var3, int var4, int var5) {
      for(int var6 = 0; var6 < var5; ++var6) {
         var3[var4++] = (float)var1[var2++];
         var3[var4++] = (float)var1[var2++];
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
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public void inverseDeltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) {
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public void inverseTransform(double[] var1, int var2, double[] var3, int var4, int var5) {
      if (var1 != var3 || var2 != var4) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
      }

   }

   public BaseBounds transform(BaseBounds var1, BaseBounds var2) {
      if (var2 != var1) {
         var2 = var2.deriveWithNewBounds(var1);
      }

      return var2;
   }

   public void transform(Rectangle var1, Rectangle var2) {
      if (var2 != var1) {
         var2.setBounds(var1);
      }

   }

   public BaseBounds inverseTransform(BaseBounds var1, BaseBounds var2) {
      if (var2 != var1) {
         var2 = var2.deriveWithNewBounds(var1);
      }

      return var2;
   }

   public void inverseTransform(Rectangle var1, Rectangle var2) {
      if (var2 != var1) {
         var2.setBounds(var1);
      }

   }

   public Shape createTransformedShape(Shape var1) {
      return new Path2D(var1);
   }

   public void setToIdentity() {
   }

   public void setTransform(BaseTransform var1) {
      if (!var1.isIdentity()) {
         degreeError(BaseTransform.Degree.IDENTITY);
      }

   }

   public void invert() {
   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (var1 != 1.0 || var3 != 0.0 || var5 != 0.0 || var7 != 1.0 || var9 != 0.0 || var11 != 0.0) {
         degreeError(BaseTransform.Degree.IDENTITY);
      }

   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var1 != 1.0 || var3 != 0.0 || var5 != 0.0 || var7 != 0.0 || var9 != 0.0 || var11 != 1.0 || var13 != 0.0 || var15 != 0.0 || var17 != 0.0 || var19 != 0.0 || var21 != 1.0 || var23 != 0.0) {
         degreeError(BaseTransform.Degree.IDENTITY);
      }

   }

   public BaseTransform deriveWithTranslation(double var1, double var3) {
      return Translate2D.getInstance(var1, var3);
   }

   public BaseTransform deriveWithPreTranslation(double var1, double var3) {
      return Translate2D.getInstance(var1, var3);
   }

   public BaseTransform deriveWithTranslation(double var1, double var3, double var5) {
      if (var5 == 0.0) {
         return (BaseTransform)(var1 == 0.0 && var3 == 0.0 ? this : new Translate2D(var1, var3));
      } else {
         Affine3D var7 = new Affine3D();
         var7.translate(var1, var3, var5);
         return var7;
      }
   }

   public BaseTransform deriveWithScale(double var1, double var3, double var5) {
      if (var5 == 1.0) {
         if (var1 == 1.0 && var3 == 1.0) {
            return this;
         } else {
            Affine2D var8 = new Affine2D();
            var8.scale(var1, var3);
            return var8;
         }
      } else {
         Affine3D var7 = new Affine3D();
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
            if (var7 > 0.0) {
               var10.rotate(var1);
            } else if (var7 < 0.0) {
               var10.rotate(-var1);
            }

            return var10;
         }
      } else {
         Affine3D var9 = new Affine3D();
         var9.rotate(var1, var3, var5, var7);
         return var9;
      }
   }

   public BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11) {
      return getInstance(var1, var3, var5, var7, var9, var11);
   }

   public BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      return getInstance(var1, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23);
   }

   public BaseTransform deriveWithConcatenation(BaseTransform var1) {
      return getInstance(var1);
   }

   public BaseTransform deriveWithPreConcatenation(BaseTransform var1) {
      return getInstance(var1);
   }

   public BaseTransform deriveWithNewTransform(BaseTransform var1) {
      return getInstance(var1);
   }

   public BaseTransform createInverse() {
      return this;
   }

   public String toString() {
      return "Identity[]";
   }

   public BaseTransform copy() {
      return this;
   }

   public boolean equals(Object var1) {
      return var1 instanceof BaseTransform && ((BaseTransform)var1).isIdentity();
   }

   public int hashCode() {
      return 0;
   }
}
