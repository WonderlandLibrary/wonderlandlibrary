package javafx.scene.layout;

import javafx.beans.NamedArg;

public final class BackgroundSize {
   public static final double AUTO = -1.0;
   public static final BackgroundSize DEFAULT = new BackgroundSize(-1.0, -1.0, true, true, false, false);
   final double width;
   final double height;
   final boolean widthAsPercentage;
   final boolean heightAsPercentage;
   final boolean contain;
   final boolean cover;
   private final int hash;

   public final double getWidth() {
      return this.width;
   }

   public final double getHeight() {
      return this.height;
   }

   public final boolean isWidthAsPercentage() {
      return this.widthAsPercentage;
   }

   public final boolean isHeightAsPercentage() {
      return this.heightAsPercentage;
   }

   public final boolean isContain() {
      return this.contain;
   }

   public final boolean isCover() {
      return this.cover;
   }

   public BackgroundSize(@NamedArg("width") double var1, @NamedArg("height") double var3, @NamedArg("widthAsPercentage") boolean var5, @NamedArg("heightAsPercentage") boolean var6, @NamedArg("contain") boolean var7, @NamedArg("cover") boolean var8) {
      if (var1 < 0.0 && var1 != -1.0) {
         throw new IllegalArgumentException("Width cannot be < 0, except when AUTO");
      } else if (var3 < 0.0 && var3 != -1.0) {
         throw new IllegalArgumentException("Height cannot be < 0, except when AUTO");
      } else {
         this.width = var1;
         this.height = var3;
         this.widthAsPercentage = var5;
         this.heightAsPercentage = var6;
         this.contain = var7;
         this.cover = var8;
         int var9 = this.widthAsPercentage ? 1 : 0;
         var9 = 31 * var9 + (this.heightAsPercentage ? 1 : 0);
         long var10 = this.width != 0.0 ? Double.doubleToLongBits(this.width) : 0L;
         var9 = 31 * var9 + (int)(var10 ^ var10 >>> 32);
         var10 = this.height != 0.0 ? Double.doubleToLongBits(this.height) : 0L;
         var9 = 31 * var9 + (int)(var10 ^ var10 >>> 32);
         var9 = 31 * var9 + (this.cover ? 1 : 0);
         var9 = 31 * var9 + (this.contain ? 1 : 0);
         this.hash = var9;
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BackgroundSize var2 = (BackgroundSize)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (this.contain != var2.contain) {
            return false;
         } else if (this.cover != var2.cover) {
            return false;
         } else if (Double.compare(var2.height, this.height) != 0) {
            return false;
         } else if (this.heightAsPercentage != var2.heightAsPercentage) {
            return false;
         } else if (this.widthAsPercentage != var2.widthAsPercentage) {
            return false;
         } else {
            return Double.compare(var2.width, this.width) == 0;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }
}
