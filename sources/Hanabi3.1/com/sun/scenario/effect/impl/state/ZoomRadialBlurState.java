package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.ZoomRadialBlur;

public class ZoomRadialBlurState {
   private float dx = -1.0F;
   private float dy = -1.0F;
   private final ZoomRadialBlur effect;

   public ZoomRadialBlurState(ZoomRadialBlur var1) {
      this.effect = var1;
   }

   public int getRadius() {
      return this.effect.getRadius();
   }

   public void updateDeltas(float var1, float var2) {
      this.dx = var1;
      this.dy = var2;
   }

   public void invalidateDeltas() {
      this.dx = -1.0F;
      this.dy = -1.0F;
   }

   public float getDx() {
      return this.dx;
   }

   public float getDy() {
      return this.dy;
   }

   public int getNumSteps() {
      int var1 = this.getRadius();
      return var1 * 2 + 1;
   }

   public float getAlpha() {
      float var1 = (float)this.getRadius();
      return 1.0F / (2.0F * var1 + 1.0F);
   }
}
