package com.sun.webkit.graphics;

public final class WCRectangle {
   float x;
   float y;
   float w;
   float h;

   public WCRectangle(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
   }

   public WCRectangle(WCRectangle var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.w = var1.w;
      this.h = var1.h;
   }

   public WCRectangle() {
   }

   public float getX() {
      return this.x;
   }

   public int getIntX() {
      return (int)this.x;
   }

   public float getY() {
      return this.y;
   }

   public int getIntY() {
      return (int)this.y;
   }

   public float getWidth() {
      return this.w;
   }

   public int getIntWidth() {
      return (int)this.w;
   }

   public float getHeight() {
      return this.h;
   }

   public int getIntHeight() {
      return (int)this.h;
   }

   public boolean contains(WCRectangle var1) {
      return this.x <= var1.x && this.x + this.w >= var1.x + var1.w && this.y <= var1.y && this.y + this.h >= var1.y + var1.h;
   }

   public WCRectangle intersection(WCRectangle var1) {
      float var2 = this.x;
      float var3 = this.y;
      float var4 = var1.x;
      float var5 = var1.y;
      float var6 = var2 + this.w;
      float var7 = var3 + this.h;
      float var8 = var4 + var1.w;
      float var9 = var5 + var1.h;
      if (var2 < var4) {
         var2 = var4;
      }

      if (var3 < var5) {
         var3 = var5;
      }

      if (var6 > var8) {
         var6 = var8;
      }

      if (var7 > var9) {
         var7 = var9;
      }

      var6 -= var2;
      var7 -= var3;
      if (var6 < Float.MIN_VALUE) {
         var6 = Float.MIN_VALUE;
      }

      if (var7 < Float.MIN_VALUE) {
         var7 = Float.MIN_VALUE;
      }

      return new WCRectangle(var2, var3, var6, var7);
   }

   public void translate(float var1, float var2) {
      float var3 = this.x;
      float var4 = var3 + var1;
      if (var1 < 0.0F) {
         if (var4 > var3) {
            if (this.w >= 0.0F) {
               this.w += var4 - Float.MIN_VALUE;
            }

            var4 = Float.MIN_VALUE;
         }
      } else if (var4 < var3) {
         if (this.w >= 0.0F) {
            this.w += var4 - Float.MAX_VALUE;
            if (this.w < 0.0F) {
               this.w = Float.MAX_VALUE;
            }
         }

         var4 = Float.MAX_VALUE;
      }

      this.x = var4;
      var3 = this.y;
      var4 = var3 + var2;
      if (var2 < 0.0F) {
         if (var4 > var3) {
            if (this.h >= 0.0F) {
               this.h += var4 - Float.MIN_VALUE;
            }

            var4 = Float.MIN_VALUE;
         }
      } else if (var4 < var3) {
         if (this.h >= 0.0F) {
            this.h += var4 - Float.MAX_VALUE;
            if (this.h < 0.0F) {
               this.h = Float.MAX_VALUE;
            }
         }

         var4 = Float.MAX_VALUE;
      }

      this.y = var4;
   }

   public WCRectangle createUnion(WCRectangle var1) {
      WCRectangle var2 = new WCRectangle();
      union(this, var1, var2);
      return var2;
   }

   public static void union(WCRectangle var0, WCRectangle var1, WCRectangle var2) {
      float var3 = Math.min(var0.getMinX(), var1.getMinX());
      float var4 = Math.min(var0.getMinY(), var1.getMinY());
      float var5 = Math.max(var0.getMaxX(), var1.getMaxX());
      float var6 = Math.max(var0.getMaxY(), var1.getMaxY());
      var2.setFrameFromDiagonal(var3, var4, var5, var6);
   }

   public void setFrameFromDiagonal(float var1, float var2, float var3, float var4) {
      float var5;
      if (var3 < var1) {
         var5 = var1;
         var1 = var3;
         var3 = var5;
      }

      if (var4 < var2) {
         var5 = var2;
         var2 = var4;
         var4 = var5;
      }

      this.setFrame(var1, var2, var3 - var1, var4 - var2);
   }

   public void setFrame(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
   }

   public float getMinX() {
      return this.getX();
   }

   public float getMaxX() {
      return this.getX() + this.getWidth();
   }

   public float getMinY() {
      return this.getY();
   }

   public float getMaxY() {
      return this.getY() + this.getHeight();
   }

   public boolean isEmpty() {
      return this.w <= 0.0F || this.h <= 0.0F;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof WCRectangle)) {
         return super.equals(var1);
      } else {
         WCRectangle var2 = (WCRectangle)var1;
         return this.x == var2.x && this.y == var2.y && this.w == var2.w && this.h == var2.h;
      }
   }

   public String toString() {
      return "WCRectangle{x:" + this.x + " y:" + this.y + " w:" + this.w + " h:" + this.h + "}";
   }
}
