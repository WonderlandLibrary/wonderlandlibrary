package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class Blend extends CoreEffect {
   private Mode mode;
   private float opacity;

   public Blend(Mode var1, Effect var2, Effect var3) {
      super(var2, var3);
      this.setMode(var1);
      this.setOpacity(1.0F);
   }

   public final Effect getBottomInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setBottomInput(Effect var1) {
      this.setInput(0, var1);
   }

   public final Effect getTopInput() {
      return (Effect)this.getInputs().get(1);
   }

   public void setTopInput(Effect var1) {
      this.setInput(1, var1);
   }

   public Mode getMode() {
      return this.mode;
   }

   public void setMode(Mode var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Mode must be non-null");
      } else {
         Mode var2 = this.mode;
         this.mode = var1;
         this.updatePeerKey("Blend_" + var1.name());
      }
   }

   public float getOpacity() {
      return this.opacity;
   }

   public void setOpacity(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         float var2 = this.opacity;
         this.opacity = var1;
      } else {
         throw new IllegalArgumentException("Opacity must be in the range [0,1]");
      }
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(1, var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(1, var2).untransform(var1, var2);
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.RenderSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      Effect var1 = this.getBottomInput();
      Effect var2 = this.getTopInput();
      switch (this.getMode()) {
         case SRC_IN:
         case SRC_OUT:
            return true;
         case SRC_ATOP:
            return var1 != null && var1.reducesOpaquePixels();
         case SRC_OVER:
         case ADD:
         case MULTIPLY:
         case SCREEN:
         case OVERLAY:
         case DARKEN:
         case LIGHTEN:
         case COLOR_DODGE:
         case COLOR_BURN:
         case HARD_LIGHT:
         case SOFT_LIGHT:
         case DIFFERENCE:
         case EXCLUSION:
         case RED:
         case GREEN:
         case BLUE:
            return var2 != null && var2.reducesOpaquePixels() && var1 != null && var1.reducesOpaquePixels();
         default:
            return true;
      }
   }

   public static enum Mode {
      SRC_OVER,
      SRC_IN,
      SRC_OUT,
      SRC_ATOP,
      ADD,
      MULTIPLY,
      SCREEN,
      OVERLAY,
      DARKEN,
      LIGHTEN,
      COLOR_DODGE,
      COLOR_BURN,
      HARD_LIGHT,
      SOFT_LIGHT,
      DIFFERENCE,
      EXCLUSION,
      RED,
      GREEN,
      BLUE;
   }
}
