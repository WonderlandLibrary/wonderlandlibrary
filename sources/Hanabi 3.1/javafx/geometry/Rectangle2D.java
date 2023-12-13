package javafx.geometry;

import javafx.beans.NamedArg;

public class Rectangle2D {
   public static final Rectangle2D EMPTY = new Rectangle2D(0.0, 0.0, 0.0, 0.0);
   private double minX;
   private double minY;
   private double width;
   private double height;
   private double maxX;
   private double maxY;
   private int hash = 0;

   public double getMinX() {
      return this.minX;
   }

   public double getMinY() {
      return this.minY;
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public double getMaxX() {
      return this.maxX;
   }

   public double getMaxY() {
      return this.maxY;
   }

   public Rectangle2D(@NamedArg("minX") double var1, @NamedArg("minY") double var3, @NamedArg("width") double var5, @NamedArg("height") double var7) {
      if (!(var5 < 0.0) && !(var7 < 0.0)) {
         this.minX = var1;
         this.minY = var3;
         this.width = var5;
         this.height = var7;
         this.maxX = var1 + var5;
         this.maxY = var3 + var7;
      } else {
         throw new IllegalArgumentException("Both width and height must be >= 0");
      }
   }

   public boolean contains(Point2D var1) {
      return var1 == null ? false : this.contains(var1.getX(), var1.getY());
   }

   public boolean contains(double var1, double var3) {
      return var1 >= this.minX && var1 <= this.maxX && var3 >= this.minY && var3 <= this.maxY;
   }

   public boolean contains(Rectangle2D var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1.minX >= this.minX && var1.minY >= this.minY && var1.maxX <= this.maxX && var1.maxY <= this.maxY;
      }
   }

   public boolean contains(double var1, double var3, double var5, double var7) {
      return var1 >= this.minX && var3 >= this.minY && var5 <= this.maxX - var1 && var7 <= this.maxY - var3;
   }

   public boolean intersects(Rectangle2D var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1.maxX > this.minX && var1.maxY > this.minY && var1.minX < this.maxX && var1.minY < this.maxY;
      }
   }

   public boolean intersects(double var1, double var3, double var5, double var7) {
      return var1 < this.maxX && var3 < this.maxY && var1 + var5 > this.minX && var3 + var7 > this.minY;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Rectangle2D)) {
         return false;
      } else {
         Rectangle2D var2 = (Rectangle2D)var1;
         return this.minX == var2.minX && this.minY == var2.minY && this.width == var2.width && this.height == var2.height;
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 7L;
         var1 = 31L * var1 + Double.doubleToLongBits(this.minX);
         var1 = 31L * var1 + Double.doubleToLongBits(this.minY);
         var1 = 31L * var1 + Double.doubleToLongBits(this.width);
         var1 = 31L * var1 + Double.doubleToLongBits(this.height);
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return "Rectangle2D [minX = " + this.minX + ", minY=" + this.minY + ", maxX=" + this.maxX + ", maxY=" + this.maxY + ", width=" + this.width + ", height=" + this.height + "]";
   }
}
