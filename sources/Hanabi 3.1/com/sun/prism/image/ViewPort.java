package com.sun.prism.image;

public class ViewPort {
   public float u0;
   public float v0;
   public float u1;
   public float v1;

   public ViewPort(float var1, float var2, float var3, float var4) {
      this.u0 = var1;
      this.u1 = var1 + var3;
      this.v0 = var2;
      this.v1 = var2 + var4;
   }

   public ViewPort getScaledVersion(float var1) {
      if (var1 == 1.0F) {
         return this;
      } else {
         float var2 = this.u0 * var1;
         float var3 = this.v0 * var1;
         float var4 = this.u1 * var1;
         float var5 = this.v1 * var1;
         return new ViewPort(var2, var3, var4 - var2, var5 - var3);
      }
   }

   public float getRelX(float var1) {
      return (var1 - this.u0) / (this.u1 - this.u0);
   }

   public float getRelY(float var1) {
      return (var1 - this.v0) / (this.v1 - this.v0);
   }

   public Coords getClippedCoords(float var1, float var2, float var3, float var4) {
      Coords var5 = new Coords(var3, var4, this);
      if (this.u1 > var1 || this.u0 < 0.0F) {
         if (this.u0 >= var1 || this.u1 <= 0.0F) {
            return null;
         }

         if (this.u1 > var1) {
            var5.x1 = var3 * this.getRelX(var1);
            var5.u1 = var1;
         }

         if (this.u0 < 0.0F) {
            var5.x0 = var3 * this.getRelX(0.0F);
            var5.u0 = 0.0F;
         }
      }

      if (this.v1 > var2 || this.v0 < 0.0F) {
         if (this.v0 >= var2 || this.v1 <= 0.0F) {
            return null;
         }

         if (this.v1 > var2) {
            var5.y1 = var4 * this.getRelY(var2);
            var5.v1 = var2;
         }

         if (this.v0 < 0.0F) {
            var5.y0 = var4 * this.getRelY(0.0F);
            var5.v0 = 0.0F;
         }
      }

      return var5;
   }
}
