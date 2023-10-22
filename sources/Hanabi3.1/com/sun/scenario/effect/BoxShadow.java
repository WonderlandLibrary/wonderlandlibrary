package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxShadowState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public class BoxShadow extends AbstractShadow {
   private final BoxShadowState state;

   public BoxShadow() {
      this(1, 1);
   }

   public BoxShadow(int var1, int var2) {
      this(var1, var2, 1, DefaultInput);
   }

   public BoxShadow(int var1, int var2, int var3) {
      this(var1, var2, var3, DefaultInput);
   }

   public BoxShadow(int var1, int var2, int var3, Effect var4) {
      super(var4);
      this.state = new BoxShadowState();
      this.setHorizontalSize(var1);
      this.setVerticalSize(var2);
      this.setPasses(var3);
      this.setColor(Color4f.BLACK);
      this.setSpread(0.0F);
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

   public Color4f getColor() {
      return this.state.getShadowColor();
   }

   public final void setColor(Color4f var1) {
      this.state.setShadowColor(var1);
   }

   public float getSpread() {
      return this.state.getSpread();
   }

   public final void setSpread(float var1) {
      this.state.setSpread(var1);
   }

   public float getGaussianRadius() {
      float var1 = (float)(this.getHorizontalSize() + this.getVerticalSize()) / 2.0F;
      var1 *= 3.0F;
      return var1 < 1.0F ? 0.0F : (var1 - 1.0F) / 2.0F;
   }

   public float getGaussianWidth() {
      return (float)this.getHorizontalSize() * 3.0F;
   }

   public float getGaussianHeight() {
      return (float)this.getVerticalSize() * 3.0F;
   }

   public void setGaussianRadius(float var1) {
      float var2 = var1 * 2.0F + 1.0F;
      this.setGaussianWidth(var2);
      this.setGaussianHeight(var2);
   }

   public void setGaussianWidth(float var1) {
      var1 /= 3.0F;
      this.setHorizontalSize(Math.round(var1));
   }

   public void setGaussianHeight(float var1) {
      var1 /= 3.0F;
      this.setVerticalSize(Math.round(var1));
   }

   public AbstractShadow.ShadowMode getMode() {
      switch (this.getPasses()) {
         case 1:
            return AbstractShadow.ShadowMode.ONE_PASS_BOX;
         case 2:
            return AbstractShadow.ShadowMode.TWO_PASS_BOX;
         default:
            return AbstractShadow.ShadowMode.THREE_PASS_BOX;
      }
   }

   public AbstractShadow implFor(AbstractShadow.ShadowMode var1) {
      switch (var1) {
         case GAUSSIAN:
            GaussianShadow var2 = new GaussianShadow();
            var2.setInput(this.getInput());
            var2.setGaussianWidth(this.getGaussianWidth());
            var2.setGaussianHeight(this.getGaussianHeight());
            var2.setColor(this.getColor());
            var2.setSpread(this.getSpread());
            return var2;
         case ONE_PASS_BOX:
            this.setPasses(1);
            break;
         case TWO_PASS_BOX:
            this.setPasses(2);
            break;
         case THREE_PASS_BOX:
            this.setPasses(3);
      }

      return this;
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
      Rectangle var4 = var3[0].getUntransformedBounds();
      var4 = this.state.getResultBounds(var4, 0);
      var4 = this.state.getResultBounds(var4, 1);
      var4.intersectWith(var2);
      return var4;
   }

   public boolean reducesOpaquePixels() {
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      var4.grow(this.state.getKernelSize(0) / 2, this.state.getKernelSize(1) / 2);
      return var4;
   }
}
