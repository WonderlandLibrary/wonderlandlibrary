package javafx.geometry;

import javafx.beans.NamedArg;

public class Point2D {
   public static final Point2D ZERO = new Point2D(0.0, 0.0);
   private double x;
   private double y;
   private int hash = 0;

   public final double getX() {
      return this.x;
   }

   public final double getY() {
      return this.y;
   }

   public Point2D(@NamedArg("x") double var1, @NamedArg("y") double var3) {
      this.x = var1;
      this.y = var3;
   }

   public double distance(double var1, double var3) {
      double var5 = this.getX() - var1;
      double var7 = this.getY() - var3;
      return Math.sqrt(var5 * var5 + var7 * var7);
   }

   public double distance(Point2D var1) {
      return this.distance(var1.getX(), var1.getY());
   }

   public Point2D add(double var1, double var3) {
      return new Point2D(this.getX() + var1, this.getY() + var3);
   }

   public Point2D add(Point2D var1) {
      return this.add(var1.getX(), var1.getY());
   }

   public Point2D subtract(double var1, double var3) {
      return new Point2D(this.getX() - var1, this.getY() - var3);
   }

   public Point2D multiply(double var1) {
      return new Point2D(this.getX() * var1, this.getY() * var1);
   }

   public Point2D subtract(Point2D var1) {
      return this.subtract(var1.getX(), var1.getY());
   }

   public Point2D normalize() {
      double var1 = this.magnitude();
      return var1 == 0.0 ? new Point2D(0.0, 0.0) : new Point2D(this.getX() / var1, this.getY() / var1);
   }

   public Point2D midpoint(double var1, double var3) {
      return new Point2D(var1 + (this.getX() - var1) / 2.0, var3 + (this.getY() - var3) / 2.0);
   }

   public Point2D midpoint(Point2D var1) {
      return this.midpoint(var1.getX(), var1.getY());
   }

   public double angle(double var1, double var3) {
      double var5 = this.getX();
      double var7 = this.getY();
      double var9 = (var5 * var1 + var7 * var3) / Math.sqrt((var5 * var5 + var7 * var7) * (var1 * var1 + var3 * var3));
      if (var9 > 1.0) {
         return 0.0;
      } else {
         return var9 < -1.0 ? 180.0 : Math.toDegrees(Math.acos(var9));
      }
   }

   public double angle(Point2D var1) {
      return this.angle(var1.getX(), var1.getY());
   }

   public double angle(Point2D var1, Point2D var2) {
      double var3 = this.getX();
      double var5 = this.getY();
      double var7 = var1.getX() - var3;
      double var9 = var1.getY() - var5;
      double var11 = var2.getX() - var3;
      double var13 = var2.getY() - var5;
      double var15 = (var7 * var11 + var9 * var13) / Math.sqrt((var7 * var7 + var9 * var9) * (var11 * var11 + var13 * var13));
      if (var15 > 1.0) {
         return 0.0;
      } else {
         return var15 < -1.0 ? 180.0 : Math.toDegrees(Math.acos(var15));
      }
   }

   public double magnitude() {
      double var1 = this.getX();
      double var3 = this.getY();
      return Math.sqrt(var1 * var1 + var3 * var3);
   }

   public double dotProduct(double var1, double var3) {
      return this.getX() * var1 + this.getY() * var3;
   }

   public double dotProduct(Point2D var1) {
      return this.dotProduct(var1.getX(), var1.getY());
   }

   public Point3D crossProduct(double var1, double var3) {
      double var5 = this.getX();
      double var7 = this.getY();
      return new Point3D(0.0, 0.0, var5 * var3 - var7 * var1);
   }

   public Point3D crossProduct(Point2D var1) {
      return this.crossProduct(var1.getX(), var1.getY());
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Point2D)) {
         return false;
      } else {
         Point2D var2 = (Point2D)var1;
         return this.getX() == var2.getX() && this.getY() == var2.getY();
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 7L;
         var1 = 31L * var1 + Double.doubleToLongBits(this.getX());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getY());
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return "Point2D [x = " + this.getX() + ", y = " + this.getY() + "]";
   }
}
