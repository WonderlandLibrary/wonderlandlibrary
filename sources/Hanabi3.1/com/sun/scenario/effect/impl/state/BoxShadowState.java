package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.Color4f;

public class BoxShadowState extends BoxBlurState {
   private Color4f shadowColor;
   private float spread;

   public Color4f getShadowColor() {
      return this.shadowColor;
   }

   public void setShadowColor(Color4f var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Color must be non-null");
      } else {
         this.shadowColor = var1;
      }
   }

   public float getSpread() {
      return this.spread;
   }

   public void setSpread(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         this.spread = var1;
      } else {
         throw new IllegalArgumentException("Spread must be in the range [0,1]");
      }
   }

   public boolean isNop() {
      return false;
   }

   public boolean isShadow() {
      return true;
   }
}
