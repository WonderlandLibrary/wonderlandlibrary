package javafx.scene.layout;

import javafx.beans.NamedArg;

public final class BorderWidths {
   public static final double AUTO = -1.0;
   public static final BorderWidths DEFAULT = new BorderWidths(1.0, 1.0, 1.0, 1.0, false, false, false, false);
   public static final BorderWidths EMPTY = new BorderWidths(0.0, 0.0, 0.0, 0.0, false, false, false, false);
   public static final BorderWidths FULL = new BorderWidths(1.0, 1.0, 1.0, 1.0, true, true, true, true);
   final double top;
   final double right;
   final double bottom;
   final double left;
   final boolean topAsPercentage;
   final boolean rightAsPercentage;
   final boolean bottomAsPercentage;
   final boolean leftAsPercentage;
   private final int hash;

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

   public final boolean isTopAsPercentage() {
      return this.topAsPercentage;
   }

   public final boolean isRightAsPercentage() {
      return this.rightAsPercentage;
   }

   public final boolean isBottomAsPercentage() {
      return this.bottomAsPercentage;
   }

   public final boolean isLeftAsPercentage() {
      return this.leftAsPercentage;
   }

   public BorderWidths(@NamedArg("width") double var1) {
      this(var1, var1, var1, var1, false, false, false, false);
   }

   public BorderWidths(@NamedArg("top") double var1, @NamedArg("right") double var3, @NamedArg("bottom") double var5, @NamedArg("left") double var7) {
      this(var1, var3, var5, var7, false, false, false, false);
   }

   public BorderWidths(@NamedArg("top") double var1, @NamedArg("right") double var3, @NamedArg("bottom") double var5, @NamedArg("left") double var7, @NamedArg("topAsPercentage") boolean var9, @NamedArg("rightAsPercentage") boolean var10, @NamedArg("bottomAsPercentage") boolean var11, @NamedArg("leftAsPercentage") boolean var12) {
      if (var1 != -1.0 && var1 < 0.0 || var3 != -1.0 && var3 < 0.0 || var5 != -1.0 && var5 < 0.0 || var7 != -1.0 && var7 < 0.0) {
         throw new IllegalArgumentException("None of the widths can be < 0");
      } else {
         this.top = var1;
         this.right = var3;
         this.bottom = var5;
         this.left = var7;
         this.topAsPercentage = var9;
         this.rightAsPercentage = var10;
         this.bottomAsPercentage = var11;
         this.leftAsPercentage = var12;
         long var14 = this.top != 0.0 ? Double.doubleToLongBits(this.top) : 0L;
         int var13 = (int)(var14 ^ var14 >>> 32);
         var14 = this.right != 0.0 ? Double.doubleToLongBits(this.right) : 0L;
         var13 = 31 * var13 + (int)(var14 ^ var14 >>> 32);
         var14 = this.bottom != 0.0 ? Double.doubleToLongBits(this.bottom) : 0L;
         var13 = 31 * var13 + (int)(var14 ^ var14 >>> 32);
         var14 = this.left != 0.0 ? Double.doubleToLongBits(this.left) : 0L;
         var13 = 31 * var13 + (int)(var14 ^ var14 >>> 32);
         var13 = 31 * var13 + (this.topAsPercentage ? 1 : 0);
         var13 = 31 * var13 + (this.rightAsPercentage ? 1 : 0);
         var13 = 31 * var13 + (this.bottomAsPercentage ? 1 : 0);
         var13 = 31 * var13 + (this.leftAsPercentage ? 1 : 0);
         this.hash = var13;
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BorderWidths var2 = (BorderWidths)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (Double.compare(var2.bottom, this.bottom) != 0) {
            return false;
         } else if (this.bottomAsPercentage != var2.bottomAsPercentage) {
            return false;
         } else if (Double.compare(var2.left, this.left) != 0) {
            return false;
         } else if (this.leftAsPercentage != var2.leftAsPercentage) {
            return false;
         } else if (Double.compare(var2.right, this.right) != 0) {
            return false;
         } else if (this.rightAsPercentage != var2.rightAsPercentage) {
            return false;
         } else if (Double.compare(var2.top, this.top) != 0) {
            return false;
         } else {
            return this.topAsPercentage == var2.topAsPercentage;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }
}
