package javafx.scene.paint;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.NamedArg;
import javafx.scene.image.Image;

public final class ImagePattern extends Paint {
   private Image image;
   private double x;
   private double y;
   private double width = 1.0;
   private double height = 1.0;
   private boolean proportional = true;
   private Object platformPaint;

   public final Image getImage() {
      return this.image;
   }

   public final double getX() {
      return this.x;
   }

   public final double getY() {
      return this.y;
   }

   public final double getWidth() {
      return this.width;
   }

   public final double getHeight() {
      return this.height;
   }

   public final boolean isProportional() {
      return this.proportional;
   }

   public final boolean isOpaque() {
      return ((com.sun.prism.paint.ImagePattern)this.acc_getPlatformPaint()).isOpaque();
   }

   public ImagePattern(@NamedArg("image") Image var1) {
      if (var1 == null) {
         throw new NullPointerException("Image must be non-null.");
      } else if (var1.getProgress() < 1.0) {
         throw new IllegalArgumentException("Image not yet loaded");
      } else {
         this.image = var1;
      }
   }

   public ImagePattern(@NamedArg("image") Image var1, @NamedArg("x") double var2, @NamedArg("y") double var4, @NamedArg("width") double var6, @NamedArg("height") double var8, @NamedArg("proportional") boolean var10) {
      if (var1 == null) {
         throw new NullPointerException("Image must be non-null.");
      } else if (var1.getProgress() < 1.0) {
         throw new IllegalArgumentException("Image not yet loaded");
      } else {
         this.image = var1;
         this.x = var2;
         this.y = var4;
         this.width = var6;
         this.height = var8;
         this.proportional = var10;
      }
   }

   boolean acc_isMutable() {
      return Toolkit.getImageAccessor().isAnimation(this.image);
   }

   void acc_addListener(AbstractNotifyListener var1) {
      Toolkit.getImageAccessor().getImageProperty(this.image).addListener(var1);
   }

   void acc_removeListener(AbstractNotifyListener var1) {
      Toolkit.getImageAccessor().getImageProperty(this.image).removeListener(var1);
   }

   Object acc_getPlatformPaint() {
      if (this.acc_isMutable() || this.platformPaint == null) {
         this.platformPaint = Toolkit.getToolkit().getPaint((Paint)this);

         assert this.platformPaint != null;
      }

      return this.platformPaint;
   }
}
