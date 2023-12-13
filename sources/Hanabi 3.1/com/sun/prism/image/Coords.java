package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.Texture;

public class Coords {
   float x0;
   float y0;
   float x1;
   float y1;
   float u0;
   float v0;
   float u1;
   float v1;

   public Coords(float var1, float var2, ViewPort var3) {
      this.x0 = 0.0F;
      this.x1 = var1;
      this.y0 = 0.0F;
      this.y1 = var2;
      this.u0 = var3.u0;
      this.u1 = var3.u1;
      this.v0 = var3.v0;
      this.v1 = var3.v1;
   }

   public Coords() {
   }

   public void draw(Texture var1, Graphics var2, float var3, float var4) {
      var2.drawTexture(var1, var3 + this.x0, var4 + this.y0, var3 + this.x1, var4 + this.y1, this.u0, this.v0, this.u1, this.v1);
   }

   public float getX(float var1) {
      return (this.x0 * (this.u1 - var1) + this.x1 * (var1 - this.u0)) / (this.u1 - this.u0);
   }

   public float getY(float var1) {
      return (this.y0 * (this.v1 - var1) + this.y1 * (var1 - this.v0)) / (this.v1 - this.v0);
   }
}
