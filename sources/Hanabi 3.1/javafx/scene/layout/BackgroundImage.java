package javafx.scene.layout;

import javafx.beans.NamedArg;
import javafx.scene.image.Image;

public final class BackgroundImage {
   final Image image;
   final BackgroundRepeat repeatX;
   final BackgroundRepeat repeatY;
   final BackgroundPosition position;
   final BackgroundSize size;
   Boolean opaque = null;
   private final int hash;

   public final Image getImage() {
      return this.image;
   }

   public final BackgroundRepeat getRepeatX() {
      return this.repeatX;
   }

   public final BackgroundRepeat getRepeatY() {
      return this.repeatY;
   }

   public final BackgroundPosition getPosition() {
      return this.position;
   }

   public final BackgroundSize getSize() {
      return this.size;
   }

   public BackgroundImage(@NamedArg("image") Image var1, @NamedArg("repeatX") BackgroundRepeat var2, @NamedArg("repeatY") BackgroundRepeat var3, @NamedArg("position") BackgroundPosition var4, @NamedArg("size") BackgroundSize var5) {
      if (var1 == null) {
         throw new NullPointerException("Image cannot be null");
      } else {
         this.image = var1;
         this.repeatX = var2 == null ? BackgroundRepeat.REPEAT : var2;
         this.repeatY = var3 == null ? BackgroundRepeat.REPEAT : var3;
         this.position = var4 == null ? BackgroundPosition.DEFAULT : var4;
         this.size = var5 == null ? BackgroundSize.DEFAULT : var5;
         int var6 = this.image.hashCode();
         var6 = 31 * var6 + this.repeatX.hashCode();
         var6 = 31 * var6 + this.repeatY.hashCode();
         var6 = 31 * var6 + this.position.hashCode();
         var6 = 31 * var6 + this.size.hashCode();
         this.hash = var6;
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BackgroundImage var2 = (BackgroundImage)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (!this.image.equals(var2.image)) {
            return false;
         } else if (!this.position.equals(var2.position)) {
            return false;
         } else if (this.repeatX != var2.repeatX) {
            return false;
         } else if (this.repeatY != var2.repeatY) {
            return false;
         } else {
            return this.size.equals(var2.size);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }
}
