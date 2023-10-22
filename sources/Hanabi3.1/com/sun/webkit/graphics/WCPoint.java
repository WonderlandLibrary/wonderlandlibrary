package com.sun.webkit.graphics;

public final class WCPoint {
   final float x;
   final float y;

   public WCPoint(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public int getIntX() {
      return (int)this.x;
   }

   public int getIntY() {
      return (int)this.y;
   }
}
