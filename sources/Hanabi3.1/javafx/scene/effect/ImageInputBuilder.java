package javafx.scene.effect;

import javafx.scene.image.Image;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ImageInputBuilder implements Builder {
   private int __set;
   private Image source;
   private double x;
   private double y;

   protected ImageInputBuilder() {
   }

   public static ImageInputBuilder create() {
      return new ImageInputBuilder();
   }

   public void applyTo(ImageInput var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setSource(this.source);
      }

      if ((var2 & 2) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 4) != 0) {
         var1.setY(this.y);
      }

   }

   public ImageInputBuilder source(Image var1) {
      this.source = var1;
      this.__set |= 1;
      return this;
   }

   public ImageInputBuilder x(double var1) {
      this.x = var1;
      this.__set |= 2;
      return this;
   }

   public ImageInputBuilder y(double var1) {
      this.y = var1;
      this.__set |= 4;
      return this;
   }

   public ImageInput build() {
      ImageInput var1 = new ImageInput();
      this.applyTo(var1);
      return var1;
   }
}
