package com.sun.webkit.graphics;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public abstract class WCImage extends Ref {
   private WCRenderQueue rq;
   private String fileExtension;

   public abstract int getWidth();

   public abstract int getHeight();

   public String getFileExtension() {
      return this.fileExtension;
   }

   public void setFileExtension(String var1) {
      this.fileExtension = var1;
   }

   public Object getPlatformImage() {
      return null;
   }

   protected abstract byte[] toData(String var1);

   protected abstract String toDataURL(String var1);

   public ByteBuffer getPixelBuffer() {
      return null;
   }

   protected void drawPixelBuffer() {
   }

   public synchronized void setRQ(WCRenderQueue var1) {
      this.rq = var1;
   }

   protected synchronized void flushRQ() {
      if (this.rq != null) {
         this.rq.decode();
      }

   }

   protected synchronized boolean isDirty() {
      return this.rq == null ? false : !this.rq.isEmpty();
   }

   public static WCImage getImage(Object var0) {
      WCImage var1 = null;
      if (var0 instanceof WCImage) {
         var1 = (WCImage)var0;
      } else if (var0 instanceof WCImageFrame) {
         var1 = ((WCImageFrame)var0).getFrame();
      }

      return var1;
   }

   public boolean isNull() {
      return this.getWidth() <= 0 || this.getHeight() <= 0 || this.getPlatformImage() == null;
   }

   public abstract float getPixelScale();

   public abstract BufferedImage toBufferedImage();
}
