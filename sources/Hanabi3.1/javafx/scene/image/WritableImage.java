package javafx.scene.image;

import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import java.nio.Buffer;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.paint.Color;

public class WritableImage extends Image {
   private ImageLoader tkImageLoader;
   private PixelWriter writer;

   public WritableImage(@NamedArg("width") int var1, @NamedArg("height") int var2) {
      super(var1, var2);
   }

   public WritableImage(@NamedArg("reader") PixelReader var1, @NamedArg("width") int var2, @NamedArg("height") int var3) {
      super(var2, var3);
      this.getPixelWriter().setPixels(0, 0, var2, var3, var1, 0, 0);
   }

   public WritableImage(@NamedArg("reader") PixelReader var1, @NamedArg("x") int var2, @NamedArg("y") int var3, @NamedArg("width") int var4, @NamedArg("height") int var5) {
      super(var4, var5);
      this.getPixelWriter().setPixels(0, 0, var4, var5, var1, var2, var3);
   }

   boolean isAnimation() {
      return true;
   }

   boolean pixelsReadable() {
      return true;
   }

   public final PixelWriter getPixelWriter() {
      if (!(this.getProgress() < 1.0) && !this.isError()) {
         if (this.writer == null) {
            this.writer = new PixelWriter() {
               ReadOnlyObjectProperty pimgprop = WritableImage.this.acc_platformImageProperty();

               public PixelFormat getPixelFormat() {
                  PlatformImage var1 = WritableImage.this.getWritablePlatformImage();
                  return var1.getPlatformPixelFormat();
               }

               public void setArgb(int var1, int var2, int var3) {
                  WritableImage.this.getWritablePlatformImage().setArgb(var1, var2, var3);
                  WritableImage.this.pixelsDirty();
               }

               public void setColor(int var1, int var2, Color var3) {
                  if (var3 == null) {
                     throw new NullPointerException("Color cannot be null");
                  } else {
                     int var4 = (int)Math.round(var3.getOpacity() * 255.0);
                     int var5 = (int)Math.round(var3.getRed() * 255.0);
                     int var6 = (int)Math.round(var3.getGreen() * 255.0);
                     int var7 = (int)Math.round(var3.getBlue() * 255.0);
                     this.setArgb(var1, var2, var4 << 24 | var5 << 16 | var6 << 8 | var7);
                  }
               }

               public void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, Buffer var6, int var7) {
                  if (var5 == null) {
                     throw new NullPointerException("PixelFormat cannot be null");
                  } else if (var6 == null) {
                     throw new NullPointerException("Buffer cannot be null");
                  } else {
                     PlatformImage var8 = WritableImage.this.getWritablePlatformImage();
                     var8.setPixels(var1, var2, var3, var4, var5, var6, var7);
                     WritableImage.this.pixelsDirty();
                  }
               }

               public void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, byte[] var6, int var7, int var8) {
                  if (var5 == null) {
                     throw new NullPointerException("PixelFormat cannot be null");
                  } else if (var6 == null) {
                     throw new NullPointerException("Buffer cannot be null");
                  } else {
                     PlatformImage var9 = WritableImage.this.getWritablePlatformImage();
                     var9.setPixels(var1, var2, var3, var4, var5, var6, var7, var8);
                     WritableImage.this.pixelsDirty();
                  }
               }

               public void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, int[] var6, int var7, int var8) {
                  if (var5 == null) {
                     throw new NullPointerException("PixelFormat cannot be null");
                  } else if (var6 == null) {
                     throw new NullPointerException("Buffer cannot be null");
                  } else {
                     PlatformImage var9 = WritableImage.this.getWritablePlatformImage();
                     var9.setPixels(var1, var2, var3, var4, var5, var6, var7, var8);
                     WritableImage.this.pixelsDirty();
                  }
               }

               public void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7) {
                  if (var5 == null) {
                     throw new NullPointerException("Reader cannot be null");
                  } else {
                     PlatformImage var8 = WritableImage.this.getWritablePlatformImage();
                     var8.setPixels(var1, var2, var3, var4, var5, var6, var7);
                     WritableImage.this.pixelsDirty();
                  }
               }
            };
         }

         return this.writer;
      } else {
         return null;
      }
   }

   private void loadTkImage(Object var1) {
      if (!(var1 instanceof ImageLoader)) {
         throw new IllegalArgumentException("Unrecognized image loader: " + var1);
      } else {
         ImageLoader var2 = (ImageLoader)var1;
         if (var2.getWidth() == (int)this.getWidth() && var2.getHeight() == (int)this.getHeight()) {
            super.setPlatformImage(var2.getFrame(0));
            this.tkImageLoader = var2;
         } else {
            throw new IllegalArgumentException("Size of loader does not match size of image");
         }
      }
   }

   private Object getTkImageLoader() {
      return this.tkImageLoader;
   }

   static {
      Toolkit.setWritableImageAccessor(new Toolkit.WritableImageAccessor() {
         public void loadTkImage(WritableImage var1, Object var2) {
            var1.loadTkImage(var2);
         }

         public Object getTkImageLoader(WritableImage var1) {
            return var1.getTkImageLoader();
         }
      });
   }
}
