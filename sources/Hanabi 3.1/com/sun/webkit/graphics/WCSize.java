package com.sun.webkit.graphics;

public final class WCSize {
   private final float width;
   private final float height;

   public WCSize(float var1, float var2) {
      this.width = var1;
      this.height = var2;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public int getIntWidth() {
      return (int)this.width;
   }

   public int getIntHeight() {
      return (int)this.height;
   }
}
