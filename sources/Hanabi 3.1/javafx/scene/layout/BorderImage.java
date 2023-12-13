package javafx.scene.layout;

import com.sun.javafx.scene.layout.region.BorderImageSlices;
import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.scene.image.Image;

public class BorderImage {
   final Image image;
   final BorderRepeat repeatX;
   final BorderRepeat repeatY;
   final BorderWidths widths;
   final BorderWidths slices;
   final boolean filled;
   final Insets insets;
   final Insets innerEdge;
   final Insets outerEdge;
   private final int hash;

   public final Image getImage() {
      return this.image;
   }

   public final BorderRepeat getRepeatX() {
      return this.repeatX;
   }

   public final BorderRepeat getRepeatY() {
      return this.repeatY;
   }

   public final BorderWidths getWidths() {
      return this.widths;
   }

   public final BorderWidths getSlices() {
      return this.slices;
   }

   public final boolean isFilled() {
      return this.filled;
   }

   public final Insets getInsets() {
      return this.insets;
   }

   public BorderImage(@NamedArg("image") Image var1, @NamedArg("widths") BorderWidths var2, @NamedArg("insets") Insets var3, @NamedArg("slices") BorderWidths var4, @NamedArg("filled") boolean var5, @NamedArg("repeatX") BorderRepeat var6, @NamedArg("repeatY") BorderRepeat var7) {
      if (var1 == null) {
         throw new NullPointerException("Image cannot be null");
      } else {
         this.image = var1;
         this.widths = var2 == null ? BorderWidths.DEFAULT : var2;
         this.insets = var3 == null ? Insets.EMPTY : var3;
         this.slices = var4 == null ? BorderImageSlices.DEFAULT.widths : var4;
         this.filled = var5;
         this.repeatX = var6 == null ? BorderRepeat.STRETCH : var6;
         this.repeatY = var7 == null ? this.repeatX : var7;
         this.outerEdge = new Insets(Math.max(0.0, -this.insets.getTop()), Math.max(0.0, -this.insets.getRight()), Math.max(0.0, -this.insets.getBottom()), Math.max(0.0, -this.insets.getLeft()));
         this.innerEdge = new Insets(this.insets.getTop() + this.widths.getTop(), this.insets.getRight() + this.widths.getRight(), this.insets.getBottom() + this.widths.getBottom(), this.insets.getLeft() + this.widths.getLeft());
         int var8 = this.image.hashCode();
         var8 = 31 * var8 + this.widths.hashCode();
         var8 = 31 * var8 + this.slices.hashCode();
         var8 = 31 * var8 + this.repeatX.hashCode();
         var8 = 31 * var8 + this.repeatY.hashCode();
         var8 = 31 * var8 + (this.filled ? 1 : 0);
         this.hash = var8;
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BorderImage var2 = (BorderImage)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (this.filled != var2.filled) {
            return false;
         } else if (!this.image.equals(var2.image)) {
            return false;
         } else if (this.repeatX != var2.repeatX) {
            return false;
         } else if (this.repeatY != var2.repeatY) {
            return false;
         } else if (!this.slices.equals(var2.slices)) {
            return false;
         } else {
            return this.widths.equals(var2.widths);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }
}
