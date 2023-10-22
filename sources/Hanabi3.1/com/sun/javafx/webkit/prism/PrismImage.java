package com.sun.javafx.webkit.prism;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.webkit.graphics.WCImage;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

abstract class PrismImage extends WCImage {
   abstract Image getImage();

   abstract Graphics getGraphics();

   abstract void draw(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9);

   abstract void dispose();

   public Object getPlatformImage() {
      return this.getImage();
   }

   public void deref() {
      super.deref();
      if (!this.hasRefs()) {
         this.dispose();
      }

   }

   protected final byte[] toData(String var1) {
      BufferedImage var2 = this.toBufferedImage(var1.equals("image/jpeg"));
      if (var2 != null) {
         Iterator var3 = ImageIO.getImageWritersByMIMEType(var1);

         while(var3.hasNext()) {
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            ImageWriter var5 = (ImageWriter)var3.next();

            try {
               var5.setOutput(ImageIO.createImageOutputStream(var4));
               var5.write(var2);
               return var4.toByteArray();
            } catch (IOException var10) {
            } finally {
               var5.dispose();
            }
         }
      }

      return null;
   }

   protected final String toDataURL(String var1) {
      byte[] var2 = this.toData(var1);
      if (var2 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append("data:").append(var1).append(";base64,");
         var3.append(Base64.getMimeEncoder().encodeToString(var2));
         return var3.toString();
      } else {
         return null;
      }
   }

   private static int getBestBufferedImageType(PixelFormat var0) {
      switch (var0.getType()) {
         case BYTE_BGRA_PRE:
         case INT_ARGB_PRE:
         default:
            return 3;
         case BYTE_BGRA:
         case INT_ARGB:
            return 2;
         case BYTE_RGB:
            return 1;
         case BYTE_INDEXED:
            return var0.isPremultiplied() ? 3 : 2;
      }
   }

   private static WritablePixelFormat getAssociatedPixelFormat(BufferedImage var0) {
      switch (var0.getType()) {
         case 1:
         case 3:
            return PixelFormat.getIntArgbPreInstance();
         case 2:
            return PixelFormat.getIntArgbInstance();
         default:
            throw new InternalError("Failed to validate BufferedImage type");
      }
   }

   private static BufferedImage fromFXImage(Image var0, boolean var1) {
      int var2 = var0.getWidth();
      int var3 = var0.getHeight();
      int var4 = var1 ? 1 : getBestBufferedImageType(var0.getPlatformPixelFormat());
      BufferedImage var5 = new BufferedImage(var2, var3, var4);
      DataBufferInt var6 = (DataBufferInt)var5.getRaster().getDataBuffer();
      int[] var7 = var6.getData();
      int var8 = var5.getRaster().getDataBuffer().getOffset();
      int var9 = 0;
      SampleModel var10 = var5.getRaster().getSampleModel();
      if (var10 instanceof SinglePixelPackedSampleModel) {
         var9 = ((SinglePixelPackedSampleModel)var10).getScanlineStride();
      }

      WritablePixelFormat var11 = getAssociatedPixelFormat(var5);
      var0.getPixels(0, 0, var2, var3, var11, (int[])var7, var8, var9);
      return var5;
   }

   private BufferedImage toBufferedImage(boolean var1) {
      try {
         return fromFXImage(this.getImage(), var1);
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
         return null;
      }
   }

   public BufferedImage toBufferedImage() {
      return this.toBufferedImage(false);
   }
}
