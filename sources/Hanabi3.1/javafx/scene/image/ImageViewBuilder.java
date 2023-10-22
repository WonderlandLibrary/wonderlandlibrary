package javafx.scene.image;

import javafx.geometry.Rectangle2D;
import javafx.scene.NodeBuilder;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ImageViewBuilder extends NodeBuilder implements Builder {
   private int __set;
   private double fitHeight;
   private double fitWidth;
   private Image image;
   private boolean preserveRatio;
   private boolean smooth;
   private Rectangle2D viewport;
   private double x;
   private double y;

   protected ImageViewBuilder() {
   }

   public static ImageViewBuilder create() {
      return new ImageViewBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(ImageView var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setFitHeight(this.fitHeight);
               break;
            case 1:
               var1.setFitWidth(this.fitWidth);
               break;
            case 2:
               var1.setImage(this.image);
               break;
            case 3:
               var1.setPreserveRatio(this.preserveRatio);
               break;
            case 4:
               var1.setSmooth(this.smooth);
               break;
            case 5:
               var1.setViewport(this.viewport);
               break;
            case 6:
               var1.setX(this.x);
               break;
            case 7:
               var1.setY(this.y);
         }
      }

   }

   public ImageViewBuilder fitHeight(double var1) {
      this.fitHeight = var1;
      this.__set(0);
      return this;
   }

   public ImageViewBuilder fitWidth(double var1) {
      this.fitWidth = var1;
      this.__set(1);
      return this;
   }

   public ImageViewBuilder image(Image var1) {
      this.image = var1;
      this.__set(2);
      return this;
   }

   public ImageViewBuilder preserveRatio(boolean var1) {
      this.preserveRatio = var1;
      this.__set(3);
      return this;
   }

   public ImageViewBuilder smooth(boolean var1) {
      this.smooth = var1;
      this.__set(4);
      return this;
   }

   public ImageViewBuilder viewport(Rectangle2D var1) {
      this.viewport = var1;
      this.__set(5);
      return this;
   }

   public ImageViewBuilder x(double var1) {
      this.x = var1;
      this.__set(6);
      return this;
   }

   public ImageViewBuilder y(double var1) {
      this.y = var1;
      this.__set(7);
      return this;
   }

   public ImageView build() {
      ImageView var1 = new ImageView();
      this.applyTo(var1);
      return var1;
   }
}
