package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

public class PickRay {
   private Vec3d origin = new Vec3d();
   private Vec3d direction = new Vec3d();
   private double nearClip = 0.0;
   private double farClip = Double.POSITIVE_INFINITY;
   static final double EPS = 9.999999747378752E-6;
   private static final double EPSILON_ABSOLUTE = 1.0E-5;

   public PickRay() {
   }

   public PickRay(Vec3d var1, Vec3d var2, double var3, double var5) {
      this.set(var1, var2, var3, var5);
   }

   public PickRay(double var1, double var3, double var5, double var7, double var9) {
      this.set(var1, var3, var5, var7, var9);
   }

   public static PickRay computePerspectivePickRay(double var0, double var2, boolean var4, double var5, double var7, double var9, boolean var11, Affine3D var12, double var13, double var15, PickRay var17) {
      if (var17 == null) {
         var17 = new PickRay();
      }

      Vec3d var18 = var17.getDirectionNoClone();
      double var19 = var5 / 2.0;
      double var21 = var7 / 2.0;
      double var23 = var11 ? var21 : var19;
      double var25 = var23 / Math.tan(var9 / 2.0);
      var18.x = var0 - var19;
      var18.y = var2 - var21;
      var18.z = var25;
      Vec3d var27 = var17.getOriginNoClone();
      if (var4) {
         var27.set(0.0, 0.0, 0.0);
      } else {
         var27.set(var19, var21, -var25);
      }

      var17.nearClip = var13 * (var18.length() / (var4 ? var25 : 1.0));
      var17.farClip = var15 * (var18.length() / (var4 ? var25 : 1.0));
      var17.transform(var12);
      return var17;
   }

   public static PickRay computeParallelPickRay(double var0, double var2, double var4, Affine3D var6, double var7, double var9, PickRay var11) {
      if (var11 == null) {
         var11 = new PickRay();
      }

      double var12 = var4 / 2.0 / Math.tan(Math.toRadians(15.0));
      var11.set(var0, var2, var12, var7 * var12, var9 * var12);
      if (var6 != null) {
         var11.transform(var6);
      }

      return var11;
   }

   public final void set(Vec3d var1, Vec3d var2, double var3, double var5) {
      this.setOrigin(var1);
      this.setDirection(var2);
      this.nearClip = var3;
      this.farClip = var5;
   }

   public final void set(double var1, double var3, double var5, double var7, double var9) {
      this.setOrigin(var1, var3, -var5);
      this.setDirection(0.0, 0.0, var5);
      this.nearClip = var7;
      this.farClip = var9;
   }

   public void setPickRay(PickRay var1) {
      this.setOrigin(var1.origin);
      this.setDirection(var1.direction);
      this.nearClip = var1.nearClip;
      this.farClip = var1.farClip;
   }

   public PickRay copy() {
      return new PickRay(this.origin, this.direction, this.nearClip, this.farClip);
   }

   public void setOrigin(Vec3d var1) {
      this.origin.set(var1);
   }

   public void setOrigin(double var1, double var3, double var5) {
      this.origin.set(var1, var3, var5);
   }

   public Vec3d getOrigin(Vec3d var1) {
      if (var1 == null) {
         var1 = new Vec3d();
      }

      var1.set(this.origin);
      return var1;
   }

   public Vec3d getOriginNoClone() {
      return this.origin;
   }

   public void setDirection(Vec3d var1) {
      this.direction.set(var1);
   }

   public void setDirection(double var1, double var3, double var5) {
      this.direction.set(var1, var3, var5);
   }

   public Vec3d getDirection(Vec3d var1) {
      if (var1 == null) {
         var1 = new Vec3d();
      }

      var1.set(this.direction);
      return var1;
   }

   public Vec3d getDirectionNoClone() {
      return this.direction;
   }

   public double getNearClip() {
      return this.nearClip;
   }

   public double getFarClip() {
      return this.farClip;
   }

   public double distance(Vec3d var1) {
      double var2 = var1.x - this.origin.x;
      double var4 = var1.y - this.origin.y;
      double var6 = var1.z - this.origin.z;
      return Math.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
   }

   public Point2D projectToZeroPlane(BaseTransform var1, boolean var2, Vec3d var3, Point2D var4) {
      if (var3 == null) {
         var3 = new Vec3d();
      }

      var1.transform(this.origin, var3);
      double var5 = var3.x;
      double var7 = var3.y;
      double var9 = var3.z;
      var3.add(this.origin, this.direction);
      var1.transform(var3, var3);
      double var11 = var3.x - var5;
      double var13 = var3.y - var7;
      double var15 = var3.z - var9;
      if (almostZero(var15)) {
         return null;
      } else {
         double var17 = -var9 / var15;
         if (var2 && var17 < 0.0) {
            return null;
         } else {
            if (var4 == null) {
               var4 = new Point2D();
            }

            var4.setLocation((float)(var5 + var11 * var17), (float)(var7 + var13 * var17));
            return var4;
         }
      }
   }

   static boolean almostZero(double var0) {
      return var0 < 1.0E-5 && var0 > -1.0E-5;
   }

   private static boolean isNonZero(double var0) {
      return var0 > 9.999999747378752E-6 || var0 < -9.999999747378752E-6;
   }

   public void transform(BaseTransform var1) {
      var1.transform(this.origin, this.origin);
      var1.deltaTransform(this.direction, this.direction);
   }

   public void inverseTransform(BaseTransform var1) throws NoninvertibleTransformException {
      var1.inverseTransform(this.origin, this.origin);
      var1.inverseDeltaTransform(this.direction, this.direction);
   }

   public PickRay project(BaseTransform var1, boolean var2, Vec3d var3, Point2D var4) {
      if (var3 == null) {
         var3 = new Vec3d();
      }

      var1.transform(this.origin, var3);
      double var5 = var3.x;
      double var7 = var3.y;
      double var9 = var3.z;
      var3.add(this.origin, this.direction);
      var1.transform(var3, var3);
      double var11 = var3.x - var5;
      double var13 = var3.y - var7;
      double var15 = var3.z - var9;
      PickRay var17 = new PickRay();
      var17.origin.x = var5;
      var17.origin.y = var7;
      var17.origin.z = var9;
      var17.direction.x = var11;
      var17.direction.y = var13;
      var17.direction.z = var15;
      return var17;
   }

   public String toString() {
      return "origin: " + this.origin + "  direction: " + this.direction;
   }
}
