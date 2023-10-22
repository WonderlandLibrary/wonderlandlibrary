package javafx.geometry;

import javafx.beans.NamedArg;

public class Dimension2D {
   private double width;
   private double height;
   private int hash = 0;

   public Dimension2D(@NamedArg("width") double var1, @NamedArg("height") double var3) {
      this.width = var1;
      this.height = var3;
   }

   public final double getWidth() {
      return this.width;
   }

   public final double getHeight() {
      return this.height;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Dimension2D)) {
         return false;
      } else {
         Dimension2D var2 = (Dimension2D)var1;
         return this.getWidth() == var2.getWidth() && this.getHeight() == var2.getHeight();
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 7L;
         var1 = 31L * var1 + Double.doubleToLongBits(this.getWidth());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getHeight());
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return "Dimension2D [width = " + this.getWidth() + ", height = " + this.getHeight() + "]";
   }
}
