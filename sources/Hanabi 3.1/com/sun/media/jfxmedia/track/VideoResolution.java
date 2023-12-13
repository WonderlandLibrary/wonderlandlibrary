package com.sun.media.jfxmedia.track;

public class VideoResolution {
   public int width;
   public int height;

   public VideoResolution(int var1, int var2) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("width <= 0");
      } else if (var2 <= 0) {
         throw new IllegalArgumentException("height <= 0");
      } else {
         this.width = var1;
         this.height = var2;
      }
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public String toString() {
      return "VideoResolution {width: " + this.width + " height: " + this.height + "}";
   }
}
