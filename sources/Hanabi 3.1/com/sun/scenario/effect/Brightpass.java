package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class Brightpass extends CoreEffect {
   private float threshold;

   public Brightpass() {
      this(DefaultInput);
   }

   public Brightpass(Effect var1) {
      super(var1);
      this.setThreshold(0.3F);
      this.updatePeerKey("Brightpass");
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public float getThreshold() {
      return this.threshold;
   }

   public void setThreshold(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         float var2 = this.threshold;
         this.threshold = var1;
      } else {
         throw new IllegalArgumentException("Threshold must be in the range [0,1]");
      }
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.RenderSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      return true;
   }
}
