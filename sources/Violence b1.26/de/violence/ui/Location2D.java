package de.violence.ui;

public class Location2D {
   double x;
   double y;

   public Location2D(double x, double y) {
      this.x = x;
      this.y = y;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double distance(Location2D Location) {
      double a = Math.abs(this.x - Location.getX());
      double b = Math.abs(this.y - Location.getY());
      double c = Math.sqrt(a * a + b * b);
      return c;
   }

   public String getPointString() {
      return "(" + this.x + " | " + this.y + ")";
   }
}
