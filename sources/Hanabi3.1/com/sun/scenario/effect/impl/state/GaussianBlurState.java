package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Color4f;

public class GaussianBlurState extends HVSeparableKernel {
   private float hradius;
   private float vradius;

   void checkRadius(float var1) {
      if (var1 < 0.0F || var1 > 63.0F) {
         throw new IllegalArgumentException("Radius must be in the range [1,63]");
      }
   }

   public float getRadius() {
      return (this.hradius + this.vradius) / 2.0F;
   }

   public void setRadius(float var1) {
      this.checkRadius(var1);
      this.hradius = var1;
      this.vradius = var1;
   }

   public float getHRadius() {
      return this.hradius;
   }

   public void setHRadius(float var1) {
      this.checkRadius(var1);
      this.hradius = var1;
   }

   public float getVRadius() {
      return this.vradius;
   }

   public void setVRadius(float var1) {
      this.checkRadius(var1);
      this.vradius = var1;
   }

   float getRadius(int var1) {
      return var1 == 0 ? this.hradius : this.vradius;
   }

   public boolean isNop() {
      return this.hradius == 0.0F && this.vradius == 0.0F;
   }

   public int getPad(int var1) {
      return (int)Math.ceil((double)this.getRadius(var1));
   }

   public int getKernelSize(int var1) {
      return this.getPad(var1) * 2 + 1;
   }

   public float getSpread() {
      return 0.0F;
   }

   public Color4f getShadowColor() {
      return null;
   }

   public LinearConvolveRenderState getRenderState(BaseTransform var1) {
      return new GaussianRenderState(this.hradius, this.vradius, this.getSpread(), this instanceof GaussianShadowState, this.getShadowColor(), var1);
   }
}
