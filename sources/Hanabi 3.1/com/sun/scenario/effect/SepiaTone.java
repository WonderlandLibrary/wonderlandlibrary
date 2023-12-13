package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class SepiaTone extends CoreEffect {
   private float level;

   public SepiaTone() {
      this(DefaultInput);
   }

   public SepiaTone(Effect var1) {
      super(var1);
      this.setLevel(1.0F);
      this.updatePeerKey("SepiaTone");
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public float getLevel() {
      return this.level;
   }

   public void setLevel(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         float var2 = this.level;
         this.level = var1;
      } else {
         throw new IllegalArgumentException("Level must be in the range [0,1]");
      }
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.RenderSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      Effect var1 = this.getInput();
      return var1 != null && var1.reducesOpaquePixels();
   }
}
