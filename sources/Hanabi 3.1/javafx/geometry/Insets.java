package javafx.geometry;

import javafx.beans.NamedArg;

public class Insets {
   public static final Insets EMPTY = new Insets(0.0, 0.0, 0.0, 0.0);
   private double top;
   private double right;
   private double bottom;
   private double left;
   private int hash = 0;

   public final double getTop() {
      return this.top;
   }

   public final double getRight() {
      return this.right;
   }

   public final double getBottom() {
      return this.bottom;
   }

   public final double getLeft() {
      return this.left;
   }

   public Insets(@NamedArg("top") double var1, @NamedArg("right") double var3, @NamedArg("bottom") double var5, @NamedArg("left") double var7) {
      this.top = var1;
      this.right = var3;
      this.bottom = var5;
      this.left = var7;
   }

   public Insets(@NamedArg("topRightBottomLeft") double var1) {
      this.top = var1;
      this.right = var1;
      this.bottom = var1;
      this.left = var1;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Insets)) {
         return false;
      } else {
         Insets var2 = (Insets)var1;
         return this.top == var2.top && this.right == var2.right && this.bottom == var2.bottom && this.left == var2.left;
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 17L;
         var1 = 37L * var1 + Double.doubleToLongBits(this.top);
         var1 = 37L * var1 + Double.doubleToLongBits(this.right);
         var1 = 37L * var1 + Double.doubleToLongBits(this.bottom);
         var1 = 37L * var1 + Double.doubleToLongBits(this.left);
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return "Insets [top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + ", left=" + this.left + "]";
   }
}
