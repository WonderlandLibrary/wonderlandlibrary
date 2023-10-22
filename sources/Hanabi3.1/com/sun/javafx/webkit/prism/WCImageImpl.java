package com.sun.javafx.webkit.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.CompoundTexture;
import com.sun.prism.image.Coords;
import com.sun.prism.image.ViewPort;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.PixelFormat;

final class WCImageImpl extends PrismImage {
   private static final Logger log = Logger.getLogger(WCImageImpl.class.getName());
   private final Image img;
   private Texture texture;
   private CompoundTexture compoundTexture;

   WCImageImpl(int var1, int var2) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "Creating empty image({0},{1})", new Object[]{var1, var2});
      }

      this.img = Image.fromIntArgbPreData(new int[var1 * var2], var1, var2);
   }

   WCImageImpl(int[] var1, int var2, int var3) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "Creating image({0},{1}) from buffer", new Object[]{var2, var3});
      }

      this.img = Image.fromIntArgbPreData(var1, var2, var3);
   }

   WCImageImpl(ImageFrame var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "Creating image {0}x{1} of type {2} from buffer", new Object[]{var1.getWidth(), var1.getHeight(), var1.getImageType()});
      }

      this.img = Image.convertImageFrame(var1);
   }

   Image getImage() {
      return this.img;
   }

   Graphics getGraphics() {
      return null;
   }

   void draw(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      if (var1 instanceof PrinterGraphics) {
         Texture var14 = var1.getResourceFactory().createTexture(this.img, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED);
         var1.drawTexture(var14, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6, (float)var7, (float)var8, (float)var9);
         var14.dispose();
      } else {
         if (this.texture != null) {
            this.texture.lock();
            if (this.texture.isSurfaceLost()) {
               this.texture = null;
            }
         }

         if (this.texture == null && this.compoundTexture == null) {
            ResourceFactory var10 = var1.getResourceFactory();
            int var11 = var10.getMaximumTextureSize();
            if (this.img.getWidth() <= var11 && this.img.getHeight() <= var11) {
               this.texture = var10.createTexture(this.img, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);

               assert this.texture != null;
            } else {
               this.compoundTexture = new CompoundTexture(this.img, var11);
            }
         }

         if (this.texture != null) {
            assert this.compoundTexture == null;

            var1.drawTexture(this.texture, (float)var2, (float)var3, (float)var4, (float)var5, (float)var6, (float)var7, (float)var8, (float)var9);
            this.texture.unlock();
         } else {
            assert this.compoundTexture != null;

            ViewPort var13 = new ViewPort((float)var6, (float)var7, (float)(var8 - var6), (float)(var9 - var7));
            Coords var15 = new Coords((float)(var4 - var2), (float)(var5 - var3), var13);
            CompoundCoords var12 = new CompoundCoords(this.compoundTexture, var15);
            var12.draw(var1, this.compoundTexture, (float)var2, (float)var3);
         }

      }
   }

   void dispose() {
      if (this.texture != null) {
         this.texture.dispose();
         this.texture = null;
      }

      if (this.compoundTexture != null) {
         this.compoundTexture.dispose();
         this.compoundTexture = null;
      }

   }

   public int getWidth() {
      return this.img.getWidth();
   }

   public int getHeight() {
      return this.img.getHeight();
   }

   public ByteBuffer getPixelBuffer() {
      int var1 = this.img.getWidth();
      int var2 = this.img.getHeight();
      int var3 = var1 * 4;
      ByteBuffer var4 = ByteBuffer.allocate(var3 * var2);
      this.img.getPixels(0, 0, var1, var2, PixelFormat.getByteBgraInstance(), var4, var3);
      return var4;
   }

   public float getPixelScale() {
      return this.img.getPixelScale();
   }
}
