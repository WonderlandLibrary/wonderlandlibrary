package javafx.scene;

import javafx.scene.image.Image;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ImageCursorBuilder implements Builder {
   private double hotspotX;
   private double hotspotY;
   private Image image;

   protected ImageCursorBuilder() {
   }

   public static ImageCursorBuilder create() {
      return new ImageCursorBuilder();
   }

   public ImageCursorBuilder hotspotX(double var1) {
      this.hotspotX = var1;
      return this;
   }

   public ImageCursorBuilder hotspotY(double var1) {
      this.hotspotY = var1;
      return this;
   }

   public ImageCursorBuilder image(Image var1) {
      this.image = var1;
      return this;
   }

   public ImageCursor build() {
      ImageCursor var1 = new ImageCursor(this.image, this.hotspotX, this.hotspotY);
      return var1;
   }
}
