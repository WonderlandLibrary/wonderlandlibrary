package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.GaussianBlurState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public class GaussianBlur extends LinearConvolveCoreEffect {
   private GaussianBlurState state;

   public GaussianBlur() {
      this(10.0F, DefaultInput);
   }

   public GaussianBlur(float var1) {
      this(var1, DefaultInput);
   }

   public GaussianBlur(float var1, Effect var2) {
      super(var2);
      this.state = new GaussianBlurState();
      this.state.setRadius(var1);
   }

   LinearConvolveKernel getState() {
      return this.state;
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      return Renderer.getRenderer(var1).getAccelType();
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public float getRadius() {
      return this.state.getRadius();
   }

   public void setRadius(float var1) {
      float var2 = this.state.getRadius();
      this.state.setRadius(var1);
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      BaseBounds var3 = super.getBounds((BaseTransform)null, var2);
      int var4 = this.state.getPad(0);
      int var5 = this.state.getPad(1);
      RectBounds var6 = new RectBounds(var3.getMinX(), var3.getMinY(), var3.getMaxX(), var3.getMaxY());
      var6.grow((float)var4, (float)var5);
      return transformBounds(var1, var6);
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      Rectangle var4 = super.getResultBounds(var1, var2, var3);
      int var5 = this.state.getPad(0);
      int var6 = this.state.getPad(1);
      Rectangle var7 = new Rectangle(var4);
      var7.grow(var5, var6);
      return var7;
   }

   public boolean reducesOpaquePixels() {
      if (!this.state.isNop()) {
         return true;
      } else {
         Effect var1 = this.getInput();
         return var1 != null && var1.reducesOpaquePixels();
      }
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      var4.grow(this.state.getPad(0), this.state.getPad(1));
      return var4;
   }
}
