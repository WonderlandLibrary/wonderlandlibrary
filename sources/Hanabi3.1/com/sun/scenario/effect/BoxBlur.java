package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxBlurState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public class BoxBlur extends LinearConvolveCoreEffect {
   private final BoxBlurState state;

   public BoxBlur() {
      this(1, 1);
   }

   public BoxBlur(int var1, int var2) {
      this(var1, var2, 1, DefaultInput);
   }

   public BoxBlur(int var1, int var2, int var3) {
      this(var1, var2, var3, DefaultInput);
   }

   public BoxBlur(int var1, int var2, int var3, Effect var4) {
      super(var4);
      this.state = new BoxBlurState();
      this.setHorizontalSize(var1);
      this.setVerticalSize(var2);
      this.setPasses(var3);
   }

   LinearConvolveKernel getState() {
      return this.state;
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public int getHorizontalSize() {
      return this.state.getHsize();
   }

   public final void setHorizontalSize(int var1) {
      this.state.setHsize(var1);
   }

   public int getVerticalSize() {
      return this.state.getVsize();
   }

   public final void setVerticalSize(int var1) {
      this.state.setVsize(var1);
   }

   public int getPasses() {
      return this.state.getBlurPasses();
   }

   public final void setPasses(int var1) {
      this.state.setBlurPasses(var1);
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      return Renderer.getRenderer(var1).getAccelType();
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      BaseBounds var3 = super.getBounds((BaseTransform)null, var2);
      int var4 = this.state.getKernelSize(0) / 2;
      int var5 = this.state.getKernelSize(1) / 2;
      RectBounds var6 = new RectBounds(var3.getMinX(), var3.getMinY(), var3.getMaxX(), var3.getMaxY());
      var6.grow((float)var4, (float)var5);
      return transformBounds(var1, var6);
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      Rectangle var4 = var3[0].getTransformedBounds((Rectangle)null);
      var4 = this.state.getResultBounds(var4, 0);
      var4 = this.state.getResultBounds(var4, 1);
      var4.intersectWith(var2);
      return var4;
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
      var4.grow(this.state.getKernelSize(0) / 2, this.state.getKernelSize(1) / 2);
      return var4;
   }
}
