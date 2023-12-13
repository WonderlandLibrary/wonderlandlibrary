package javafx.geometry;

import javafx.beans.NamedArg;

public class Point3D {
   public static final Point3D ZERO = new Point3D(0.0, 0.0, 0.0);
   private double x;
   private double y;
   private double z;
   private int hash = 0;

   public final double getX() {
      return this.x;
   }

   public final double getY() {
      return this.y;
   }

   public final double getZ() {
      return this.z;
   }

   public Point3D(@NamedArg("x") double var1, @NamedArg("y") double var3, @NamedArg("z") double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public double distance(double var1, double var3, double var5) {
      double var7 = this.getX() - var1;
      double var9 = this.getY() - var3;
      double var11 = this.getZ() - var5;
      return Math.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
   }

   public double distance(Point3D var1) {
      return this.distance(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D add(double var1, double var3, double var5) {
      return new Point3D(this.getX() + var1, this.getY() + var3, this.getZ() + var5);
   }

   public Point3D add(Point3D var1) {
      return this.add(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D subtract(double var1, double var3, double var5) {
      return new Point3D(this.getX() - var1, this.getY() - var3, this.getZ() - var5);
   }

   public Point3D subtract(Point3D var1) {
      return this.subtract(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D multiply(double var1) {
      return new Point3D(this.getX() * var1, this.getY() * var1, this.getZ() * var1);
   }

   public Point3D normalize() {
      double var1 = this.magnitude();
      return var1 == 0.0 ? new Point3D(0.0, 0.0, 0.0) : new Point3D(this.getX() / var1, this.getY() / var1, this.getZ() / var1);
   }

   public Point3D midpoint(double var1, double var3, double var5) {
      return new Point3D(var1 + (this.getX() - var1) / 2.0, var3 + (this.getY() - var3) / 2.0, var5 + (this.getZ() - var5) / 2.0);
   }

   public Point3D midpoint(Point3D var1) {
      return this.midpoint(var1.getX(), var1.getY(), var1.getZ());
   }

   public double angle(double var1, double var3, double var5) {
      double var7 = this.getX();
      double var9 = this.getY();
      double var11 = this.getZ();
      double var13 = (var7 * var1 + var9 * var3 + var11 * var5) / Math.sqrt((var7 * var7 + var9 * var9 + var11 * var11) * (var1 * var1 + var3 * var3 + var5 * var5));
      if (var13 > 1.0) {
         return 0.0;
      } else {
         return var13 < -1.0 ? 180.0 : Math.toDegrees(Math.acos(var13));
      }
   }

   public double angle(Point3D var1) {
      return this.angle(var1.getX(), var1.getY(), var1.getZ());
   }

   public double angle(Point3D var1, Point3D var2) {
      double var3 = this.getX();
      double var5 = this.getY();
      double var7 = this.getZ();
      double var9 = var1.getX() - var3;
      double var11 = var1.getY() - var5;
      double var13 = var1.getZ() - var7;
      double var15 = var2.getX() - var3;
      double var17 = var2.getY() - var5;
      double var19 = var2.getZ() - var7;
      double var21 = (var9 * var15 + var11 * var17 + var13 * var19) / Math.sqrt((var9 * var9 + var11 * var11 + var13 * var13) * (var15 * var15 + var17 * var17 + var19 * var19));
      if (var21 > 1.0) {
         return 0.0;
      } else {
         return var21 < -1.0 ? 180.0 : Math.toDegrees(Math.acos(var21));
      }
   }

   public double magnitude() {
      double var1 = this.getX();
      double var3 = this.getY();
      double var5 = this.getZ();
      return Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
   }

   public double dotProduct(double var1, double var3, double var5) {
      return this.getX() * var1 + this.getY() * var3 + this.getZ() * var5;
   }

   public double dotProduct(Point3D var1) {
      return this.dotProduct(var1.getX(), var1.getY(), var1.getZ());
   }

   public Point3D crossProduct(double var1, double var3, double var5) {
      double var7 = this.getX();
      double var9 = this.getY();
      double var11 = this.getZ();
      return new Point3D(var9 * var5 - var11 * var3, var11 * var1 - var7 * var5, var7 * var3 - var9 * var1);
   }

   public Point3D crossProduct(Point3D var1) {
      return this.crossProduct(var1.getX(), var1.getY(), var1.getZ());
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Point3D)) {
         return false;
      } else {
         Point3D var2 = (Point3D)var1;
         return this.getX() == var2.getX() && this.getY() == var2.getY() && this.getZ() == var2.getZ();
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 7L;
         var1 = 31L * var1 + Double.doubleToLongBits(this.getX());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getY());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getZ());
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return "Point3D [x = " + this.getX() + ", y = " + this.getY() + ", z = " + this.getZ() + "]";
   }
}
