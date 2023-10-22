package javafx.scene.layout;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public final class BackgroundFill {
   final Paint fill;
   final CornerRadii radii;
   final Insets insets;
   private final int hash;

   public final Paint getFill() {
      return this.fill;
   }

   public final CornerRadii getRadii() {
      return this.radii;
   }

   public final Insets getInsets() {
      return this.insets;
   }

   public BackgroundFill(@NamedArg("fill") Paint var1, @NamedArg("radii") CornerRadii var2, @NamedArg("insets") Insets var3) {
      this.fill = (Paint)(var1 == null ? Color.TRANSPARENT : var1);
      this.radii = var2 == null ? CornerRadii.EMPTY : var2;
      this.insets = var3 == null ? Insets.EMPTY : var3;
      int var4 = this.fill.hashCode();
      var4 = 31 * var4 + this.radii.hashCode();
      var4 = 31 * var4 + this.insets.hashCode();
      this.hash = var4;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BackgroundFill var2 = (BackgroundFill)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (!this.fill.equals(var2.fill)) {
            return false;
         } else if (!this.insets.equals(var2.insets)) {
            return false;
         } else {
            return this.radii.equals(var2.radii);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }
}
