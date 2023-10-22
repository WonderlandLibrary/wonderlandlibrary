package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

public class MotionBlurState extends LinearConvolveKernel {
   private float radius;
   private float angle;

   public float getRadius() {
      return this.radius;
   }

   public void setRadius(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 63.0F)) {
         this.radius = var1;
      } else {
         throw new IllegalArgumentException("Radius must be in the range [0,63]");
      }
   }

   public float getAngle() {
      return this.angle;
   }

   public void setAngle(float var1) {
      this.angle = var1;
   }

   public int getHPad() {
      return (int)Math.ceil(Math.abs(Math.cos((double)this.angle)) * (double)this.radius);
   }

   public int getVPad() {
      return (int)Math.ceil(Math.abs(Math.sin((double)this.angle)) * (double)this.radius);
   }

   public LinearConvolveRenderState getRenderState(BaseTransform var1) {
      float var2 = (float)Math.cos((double)this.angle);
      float var3 = (float)Math.sin((double)this.angle);
      return new GaussianRenderState(this.radius, var2, var3, var1);
   }

   public boolean isNop() {
      return this.radius == 0.0F;
   }

   public int getKernelSize(int var1) {
      return (int)Math.ceil((double)this.radius) * 2 + 1;
   }

   public final Rectangle getResultBounds(Rectangle var1, int var2) {
      Rectangle var3 = new Rectangle(var1);
      var3.grow(this.getHPad(), this.getVPad());
      return var3;
   }
}
