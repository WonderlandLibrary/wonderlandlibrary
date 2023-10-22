package com.sun.javafx.cursor;

public final class ImageCursorFrame extends CursorFrame {
   private final Object platformImage;
   private final double width;
   private final double height;
   private final double hotspotX;
   private final double hotspotY;

   public ImageCursorFrame(Object var1, double var2, double var4, double var6, double var8) {
      this.platformImage = var1;
      this.width = var2;
      this.height = var4;
      this.hotspotX = var6;
      this.hotspotY = var8;
   }

   public CursorType getCursorType() {
      return CursorType.IMAGE;
   }

   public Object getPlatformImage() {
      return this.platformImage;
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public double getHotspotX() {
      return this.hotspotX;
   }

   public double getHotspotY() {
      return this.hotspotY;
   }
}
