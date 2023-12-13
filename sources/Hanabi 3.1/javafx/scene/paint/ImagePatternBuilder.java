package javafx.scene.paint;

import javafx.scene.image.Image;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class ImagePatternBuilder implements Builder {
   private double height;
   private Image image;
   private boolean proportional;
   private double width;
   private double x;
   private double y;

   protected ImagePatternBuilder() {
   }

   public static ImagePatternBuilder create() {
      return new ImagePatternBuilder();
   }

   public ImagePatternBuilder height(double var1) {
      this.height = var1;
      return this;
   }

   public ImagePatternBuilder image(Image var1) {
      this.image = var1;
      return this;
   }

   public ImagePatternBuilder proportional(boolean var1) {
      this.proportional = var1;
      return this;
   }

   public ImagePatternBuilder width(double var1) {
      this.width = var1;
      return this;
   }

   public ImagePatternBuilder x(double var1) {
      this.x = var1;
      return this;
   }

   public ImagePatternBuilder y(double var1) {
      this.y = var1;
      return this;
   }

   public ImagePattern build() {
      ImagePattern var1 = new ImagePattern(this.image, this.x, this.y, this.width, this.height, this.proportional);
      return var1;
   }
}
