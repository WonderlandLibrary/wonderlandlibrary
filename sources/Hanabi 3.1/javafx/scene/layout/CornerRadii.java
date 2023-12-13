package javafx.scene.layout;

import javafx.beans.NamedArg;

public class CornerRadii {
   public static final CornerRadii EMPTY = new CornerRadii(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false, false, false, false, false, false, false, false);
   private double topLeftHorizontalRadius;
   private double topLeftVerticalRadius;
   private double topRightVerticalRadius;
   private double topRightHorizontalRadius;
   private double bottomRightHorizontalRadius;
   private double bottomRightVerticalRadius;
   private double bottomLeftVerticalRadius;
   private double bottomLeftHorizontalRadius;
   private final boolean topLeftHorizontalRadiusAsPercentage;
   private final boolean topLeftVerticalRadiusAsPercentage;
   private final boolean topRightVerticalRadiusAsPercentage;
   private final boolean topRightHorizontalRadiusAsPercentage;
   private final boolean bottomRightHorizontalRadiusAsPercentage;
   private final boolean bottomRightVerticalRadiusAsPercentage;
   private final boolean bottomLeftVerticalRadiusAsPercentage;
   private final boolean bottomLeftHorizontalRadiusAsPercentage;
   final boolean hasPercentBasedRadii;
   final boolean uniform;
   private final int hash;

   public final double getTopLeftHorizontalRadius() {
      return this.topLeftHorizontalRadius;
   }

   public final double getTopLeftVerticalRadius() {
      return this.topLeftVerticalRadius;
   }

   public final double getTopRightVerticalRadius() {
      return this.topRightVerticalRadius;
   }

   public final double getTopRightHorizontalRadius() {
      return this.topRightHorizontalRadius;
   }

   public final double getBottomRightHorizontalRadius() {
      return this.bottomRightHorizontalRadius;
   }

   public final double getBottomRightVerticalRadius() {
      return this.bottomRightVerticalRadius;
   }

   public final double getBottomLeftVerticalRadius() {
      return this.bottomLeftVerticalRadius;
   }

   public final double getBottomLeftHorizontalRadius() {
      return this.bottomLeftHorizontalRadius;
   }

   public final boolean isTopLeftHorizontalRadiusAsPercentage() {
      return this.topLeftHorizontalRadiusAsPercentage;
   }

   public final boolean isTopLeftVerticalRadiusAsPercentage() {
      return this.topLeftVerticalRadiusAsPercentage;
   }

   public final boolean isTopRightVerticalRadiusAsPercentage() {
      return this.topRightVerticalRadiusAsPercentage;
   }

   public final boolean isTopRightHorizontalRadiusAsPercentage() {
      return this.topRightHorizontalRadiusAsPercentage;
   }

   public final boolean isBottomRightHorizontalRadiusAsPercentage() {
      return this.bottomRightHorizontalRadiusAsPercentage;
   }

   public final boolean isBottomRightVerticalRadiusAsPercentage() {
      return this.bottomRightVerticalRadiusAsPercentage;
   }

   public final boolean isBottomLeftVerticalRadiusAsPercentage() {
      return this.bottomLeftVerticalRadiusAsPercentage;
   }

   public final boolean isBottomLeftHorizontalRadiusAsPercentage() {
      return this.bottomLeftHorizontalRadiusAsPercentage;
   }

   public final boolean isUniform() {
      return this.uniform;
   }

   public CornerRadii(@NamedArg("radius") double var1) {
      if (var1 < 0.0) {
         throw new IllegalArgumentException("The radii value may not be < 0");
      } else {
         this.topLeftHorizontalRadius = this.topLeftVerticalRadius = this.topRightVerticalRadius = this.topRightHorizontalRadius = this.bottomRightHorizontalRadius = this.bottomRightVerticalRadius = this.bottomLeftVerticalRadius = this.bottomLeftHorizontalRadius = var1;
         this.topLeftHorizontalRadiusAsPercentage = this.topLeftVerticalRadiusAsPercentage = this.topRightVerticalRadiusAsPercentage = this.topRightHorizontalRadiusAsPercentage = this.bottomRightHorizontalRadiusAsPercentage = this.bottomRightVerticalRadiusAsPercentage = this.bottomLeftVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage = false;
         this.hasPercentBasedRadii = false;
         this.uniform = true;
         this.hash = this.preComputeHash();
      }
   }

   public CornerRadii(@NamedArg("radius") double var1, @NamedArg("asPercent") boolean var3) {
      if (var1 < 0.0) {
         throw new IllegalArgumentException("The radii value may not be < 0");
      } else {
         this.topLeftHorizontalRadius = this.topLeftVerticalRadius = this.topRightVerticalRadius = this.topRightHorizontalRadius = this.bottomRightHorizontalRadius = this.bottomRightVerticalRadius = this.bottomLeftVerticalRadius = this.bottomLeftHorizontalRadius = var1;
         this.topLeftHorizontalRadiusAsPercentage = this.topLeftVerticalRadiusAsPercentage = this.topRightVerticalRadiusAsPercentage = this.topRightHorizontalRadiusAsPercentage = this.bottomRightHorizontalRadiusAsPercentage = this.bottomRightVerticalRadiusAsPercentage = this.bottomLeftVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage = var3;
         this.uniform = true;
         this.hasPercentBasedRadii = var3;
         this.hash = this.preComputeHash();
      }
   }

   public CornerRadii(@NamedArg("topLeft") double var1, @NamedArg("topRight") double var3, @NamedArg("bottomRight") double var5, @NamedArg("bottomLeft") double var7, @NamedArg("asPercent") boolean var9) {
      if (!(var1 < 0.0) && !(var3 < 0.0) && !(var5 < 0.0) && !(var7 < 0.0)) {
         this.topLeftHorizontalRadius = this.topLeftVerticalRadius = var1;
         this.topRightVerticalRadius = this.topRightHorizontalRadius = var3;
         this.bottomRightHorizontalRadius = this.bottomRightVerticalRadius = var5;
         this.bottomLeftVerticalRadius = this.bottomLeftHorizontalRadius = var7;
         this.topLeftHorizontalRadiusAsPercentage = this.topLeftVerticalRadiusAsPercentage = this.topRightVerticalRadiusAsPercentage = this.topRightHorizontalRadiusAsPercentage = this.bottomRightHorizontalRadiusAsPercentage = this.bottomRightVerticalRadiusAsPercentage = this.bottomLeftVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage = var9;
         this.uniform = var1 == var3 && var1 == var7 && var1 == var5;
         this.hasPercentBasedRadii = var9;
         this.hash = this.preComputeHash();
      } else {
         throw new IllegalArgumentException("No radii value may be < 0");
      }
   }

   public CornerRadii(@NamedArg("topLeftHorizontalRadius") double var1, @NamedArg("topLeftVerticalRadius") double var3, @NamedArg("topRightVerticalRadius") double var5, @NamedArg("topRightHorizontalRadius") double var7, @NamedArg("bottomRightHorizontalRadius") double var9, @NamedArg("bottomRightVerticalRadius") double var11, @NamedArg("bottomLeftVerticalRadius") double var13, @NamedArg("bottomLeftHorizontalRadius") double var15, @NamedArg("topLeftHorizontalRadiusAsPercent") boolean var17, @NamedArg("topLeftVerticalRadiusAsPercent") boolean var18, @NamedArg("topRightVerticalRadiusAsPercent") boolean var19, @NamedArg("topRightHorizontalRadiusAsPercent") boolean var20, @NamedArg("bottomRightHorizontalRadiusAsPercent") boolean var21, @NamedArg("bottomRightVerticalRadiusAsPercent") boolean var22, @NamedArg("bottomLeftVerticalRadiusAsPercent") boolean var23, @NamedArg("bottomLeftHorizontalRadiusAsPercent") boolean var24) {
      if (!(var1 < 0.0) && !(var3 < 0.0) && !(var5 < 0.0) && !(var7 < 0.0) && !(var9 < 0.0) && !(var11 < 0.0) && !(var13 < 0.0) && !(var15 < 0.0)) {
         this.topLeftHorizontalRadius = var1;
         this.topLeftVerticalRadius = var3;
         this.topRightVerticalRadius = var5;
         this.topRightHorizontalRadius = var7;
         this.bottomRightHorizontalRadius = var9;
         this.bottomRightVerticalRadius = var11;
         this.bottomLeftVerticalRadius = var13;
         this.bottomLeftHorizontalRadius = var15;
         this.topLeftHorizontalRadiusAsPercentage = var17;
         this.topLeftVerticalRadiusAsPercentage = var18;
         this.topRightVerticalRadiusAsPercentage = var19;
         this.topRightHorizontalRadiusAsPercentage = var20;
         this.bottomRightHorizontalRadiusAsPercentage = var21;
         this.bottomRightVerticalRadiusAsPercentage = var22;
         this.bottomLeftVerticalRadiusAsPercentage = var23;
         this.bottomLeftHorizontalRadiusAsPercentage = var24;
         this.hash = this.preComputeHash();
         this.hasPercentBasedRadii = var17 || var18 || var19 || var20 || var21 || var22 || var23 || var24;
         this.uniform = var1 == var7 && var3 == var5 && var1 == var9 && var3 == var11 && var1 == var15 && var3 == var13 && var17 == var20 && var18 == var19 && var17 == var21 && var18 == var22 && var17 == var24 && var18 == var23;
      } else {
         throw new IllegalArgumentException("No radii value may be < 0");
      }
   }

   private int preComputeHash() {
      long var2 = this.topLeftHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.topLeftHorizontalRadius) : 0L;
      int var1 = (int)(var2 ^ var2 >>> 32);
      var2 = this.topLeftVerticalRadius != 0.0 ? Double.doubleToLongBits(this.topLeftVerticalRadius) : 0L;
      var1 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = this.topRightVerticalRadius != 0.0 ? Double.doubleToLongBits(this.topRightVerticalRadius) : 0L;
      var1 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = this.topRightHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.topRightHorizontalRadius) : 0L;
      var1 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = this.bottomRightHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.bottomRightHorizontalRadius) : 0L;
      var1 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = this.bottomRightVerticalRadius != 0.0 ? Double.doubleToLongBits(this.bottomRightVerticalRadius) : 0L;
      var1 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = this.bottomLeftVerticalRadius != 0.0 ? Double.doubleToLongBits(this.bottomLeftVerticalRadius) : 0L;
      var1 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var2 = this.bottomLeftHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.bottomLeftHorizontalRadius) : 0L;
      var1 = 31 * var1 + (int)(var2 ^ var2 >>> 32);
      var1 = 31 * var1 + (this.topLeftHorizontalRadiusAsPercentage ? 1 : 0);
      var1 = 31 * var1 + (this.topLeftVerticalRadiusAsPercentage ? 1 : 0);
      var1 = 31 * var1 + (this.topRightVerticalRadiusAsPercentage ? 1 : 0);
      var1 = 31 * var1 + (this.topRightHorizontalRadiusAsPercentage ? 1 : 0);
      var1 = 31 * var1 + (this.bottomRightHorizontalRadiusAsPercentage ? 1 : 0);
      var1 = 31 * var1 + (this.bottomRightVerticalRadiusAsPercentage ? 1 : 0);
      var1 = 31 * var1 + (this.bottomLeftVerticalRadiusAsPercentage ? 1 : 0);
      var1 = 31 * var1 + (this.bottomLeftHorizontalRadiusAsPercentage ? 1 : 0);
      var1 += 31 * var1;
      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         CornerRadii var2 = (CornerRadii)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (Double.compare(var2.bottomLeftHorizontalRadius, this.bottomLeftHorizontalRadius) != 0) {
            return false;
         } else if (this.bottomLeftHorizontalRadiusAsPercentage != var2.bottomLeftHorizontalRadiusAsPercentage) {
            return false;
         } else if (Double.compare(var2.bottomLeftVerticalRadius, this.bottomLeftVerticalRadius) != 0) {
            return false;
         } else if (this.bottomLeftVerticalRadiusAsPercentage != var2.bottomLeftVerticalRadiusAsPercentage) {
            return false;
         } else if (Double.compare(var2.bottomRightVerticalRadius, this.bottomRightVerticalRadius) != 0) {
            return false;
         } else if (this.bottomRightVerticalRadiusAsPercentage != var2.bottomRightVerticalRadiusAsPercentage) {
            return false;
         } else if (Double.compare(var2.bottomRightHorizontalRadius, this.bottomRightHorizontalRadius) != 0) {
            return false;
         } else if (this.bottomRightHorizontalRadiusAsPercentage != var2.bottomRightHorizontalRadiusAsPercentage) {
            return false;
         } else if (Double.compare(var2.topLeftVerticalRadius, this.topLeftVerticalRadius) != 0) {
            return false;
         } else if (this.topLeftVerticalRadiusAsPercentage != var2.topLeftVerticalRadiusAsPercentage) {
            return false;
         } else if (Double.compare(var2.topLeftHorizontalRadius, this.topLeftHorizontalRadius) != 0) {
            return false;
         } else if (this.topLeftHorizontalRadiusAsPercentage != var2.topLeftHorizontalRadiusAsPercentage) {
            return false;
         } else if (Double.compare(var2.topRightHorizontalRadius, this.topRightHorizontalRadius) != 0) {
            return false;
         } else if (this.topRightHorizontalRadiusAsPercentage != var2.topRightHorizontalRadiusAsPercentage) {
            return false;
         } else if (Double.compare(var2.topRightVerticalRadius, this.topRightVerticalRadius) != 0) {
            return false;
         } else {
            return this.topRightVerticalRadiusAsPercentage == var2.topRightVerticalRadiusAsPercentage;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   public String toString() {
      return this.isUniform() ? "CornerRadii [uniform radius = " + this.topLeftHorizontalRadius + "]" : "CornerRadii [" + (this.topLeftHorizontalRadius == this.topLeftVerticalRadius ? "topLeft=" + this.topLeftHorizontalRadius : "topLeftHorizontalRadius=" + this.topLeftHorizontalRadius + ", topLeftVerticalRadius=" + this.topLeftVerticalRadius) + (this.topRightHorizontalRadius == this.topRightVerticalRadius ? ", topRight=" + this.topRightHorizontalRadius : ", topRightVerticalRadius=" + this.topRightVerticalRadius + ", topRightHorizontalRadius=" + this.topRightHorizontalRadius) + (this.bottomRightHorizontalRadius == this.bottomRightVerticalRadius ? ", bottomRight=" + this.bottomRightHorizontalRadius : ", bottomRightHorizontalRadius=" + this.bottomRightHorizontalRadius + ", bottomRightVerticalRadius=" + this.bottomRightVerticalRadius) + (this.bottomLeftHorizontalRadius == this.bottomLeftVerticalRadius ? ", bottomLeft=" + this.bottomLeftHorizontalRadius : ", bottomLeftVerticalRadius=" + this.bottomLeftVerticalRadius + ", bottomLeftHorizontalRadius=" + this.bottomLeftHorizontalRadius) + ']';
   }
}
