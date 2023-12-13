package javafx.scene.layout;

import javafx.beans.NamedArg;
import javafx.geometry.Side;

public class BackgroundPosition {
   public static final BackgroundPosition DEFAULT;
   public static final BackgroundPosition CENTER;
   final Side horizontalSide;
   final Side verticalSide;
   final double horizontalPosition;
   final double verticalPosition;
   final boolean horizontalAsPercentage;
   final boolean verticalAsPercentage;
   private final int hash;

   public final Side getHorizontalSide() {
      return this.horizontalSide;
   }

   public final Side getVerticalSide() {
      return this.verticalSide;
   }

   public final double getHorizontalPosition() {
      return this.horizontalPosition;
   }

   public final double getVerticalPosition() {
      return this.verticalPosition;
   }

   public final boolean isHorizontalAsPercentage() {
      return this.horizontalAsPercentage;
   }

   public final boolean isVerticalAsPercentage() {
      return this.verticalAsPercentage;
   }

   public BackgroundPosition(@NamedArg("horizontalSide") Side var1, @NamedArg("horizontalPosition") double var2, @NamedArg("horizontalAsPercentage") boolean var4, @NamedArg("verticalSide") Side var5, @NamedArg("verticalPosition") double var6, @NamedArg("verticalAsPercentage") boolean var8) {
      if (var1 != Side.TOP && var1 != Side.BOTTOM) {
         if (var5 != Side.LEFT && var5 != Side.RIGHT) {
            this.horizontalSide = var1 == null ? Side.LEFT : var1;
            this.verticalSide = var5 == null ? Side.TOP : var5;
            this.horizontalPosition = var2;
            this.verticalPosition = var6;
            this.horizontalAsPercentage = var4;
            this.verticalAsPercentage = var8;
            int var9 = this.horizontalSide.hashCode();
            var9 = 31 * var9 + this.verticalSide.hashCode();
            long var10 = this.horizontalPosition != 0.0 ? Double.doubleToLongBits(this.horizontalPosition) : 0L;
            var9 = 31 * var9 + (int)(var10 ^ var10 >>> 32);
            var10 = this.verticalPosition != 0.0 ? Double.doubleToLongBits(this.verticalPosition) : 0L;
            var9 = 31 * var9 + (int)(var10 ^ var10 >>> 32);
            var9 = 31 * var9 + (this.horizontalAsPercentage ? 1 : 0);
            var9 = 31 * var9 + (this.verticalAsPercentage ? 1 : 0);
            this.hash = var9;
         } else {
            throw new IllegalArgumentException("The verticalSide must be TOP or BOTTOM");
         }
      } else {
         throw new IllegalArgumentException("The horizontalSide must be LEFT or RIGHT");
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BackgroundPosition var2 = (BackgroundPosition)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (this.horizontalAsPercentage != var2.horizontalAsPercentage) {
            return false;
         } else if (Double.compare(var2.horizontalPosition, this.horizontalPosition) != 0) {
            return false;
         } else if (this.verticalAsPercentage != var2.verticalAsPercentage) {
            return false;
         } else if (Double.compare(var2.verticalPosition, this.verticalPosition) != 0) {
            return false;
         } else if (this.horizontalSide != var2.horizontalSide) {
            return false;
         } else {
            return this.verticalSide == var2.verticalSide;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   static {
      DEFAULT = new BackgroundPosition(Side.LEFT, 0.0, true, Side.TOP, 0.0, true);
      CENTER = new BackgroundPosition(Side.LEFT, 0.5, true, Side.TOP, 0.5, true);
   }
}
