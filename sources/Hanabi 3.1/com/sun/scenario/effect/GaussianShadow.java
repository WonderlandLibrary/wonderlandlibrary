package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.GaussianShadowState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public class GaussianShadow extends AbstractShadow {
   private GaussianShadowState state;

   public GaussianShadow() {
      this(10.0F);
   }

   public GaussianShadow(float var1) {
      this(var1, Color4f.BLACK);
   }

   public GaussianShadow(float var1, Color4f var2) {
      this(var1, var2, DefaultInput);
   }

   public GaussianShadow(float var1, Color4f var2, Effect var3) {
      super(var3);
      this.state = new GaussianShadowState();
      this.state.setRadius(var1);
      this.state.setShadowColor(var2);
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

   public float getHRadius() {
      return this.state.getHRadius();
   }

   public void setHRadius(float var1) {
      float var2 = this.state.getHRadius();
      this.state.setHRadius(var1);
   }

   public float getVRadius() {
      return this.state.getVRadius();
   }

   public void setVRadius(float var1) {
      float var2 = this.state.getVRadius();
      this.state.setVRadius(var1);
   }

   public float getSpread() {
      return this.state.getSpread();
   }

   public void setSpread(float var1) {
      float var2 = this.state.getSpread();
      this.state.setSpread(var1);
   }

   public Color4f getColor() {
      return this.state.getShadowColor();
   }

   public void setColor(Color4f var1) {
      Color4f var2 = this.state.getShadowColor();
      this.state.setShadowColor(var1);
   }

   public float getGaussianRadius() {
      return this.getRadius();
   }

   public float getGaussianWidth() {
      return this.getHRadius() * 2.0F + 1.0F;
   }

   public float getGaussianHeight() {
      return this.getVRadius() * 2.0F + 1.0F;
   }

   public void setGaussianRadius(float var1) {
      this.setRadius(var1);
   }

   public void setGaussianWidth(float var1) {
      this.setHRadius(var1 < 1.0F ? 0.0F : (var1 - 1.0F) / 2.0F);
   }

   public void setGaussianHeight(float var1) {
      this.setVRadius(var1 < 1.0F ? 0.0F : (var1 - 1.0F) / 2.0F);
   }

   public AbstractShadow.ShadowMode getMode() {
      return AbstractShadow.ShadowMode.GAUSSIAN;
   }

   public AbstractShadow implFor(AbstractShadow.ShadowMode var1) {
      byte var2 = 0;
      switch (var1) {
         case GAUSSIAN:
            return this;
         case ONE_PASS_BOX:
            var2 = 1;
            break;
         case TWO_PASS_BOX:
            var2 = 2;
            break;
         case THREE_PASS_BOX:
            var2 = 3;
      }

      BoxShadow var3 = new BoxShadow();
      var3.setInput(this.getInput());
      var3.setGaussianWidth(this.getGaussianWidth());
      var3.setGaussianHeight(this.getGaussianHeight());
      var3.setColor(this.getColor());
      var3.setPasses(var2);
      var3.setSpread(this.getSpread());
      return var3;
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
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      var4.grow(this.state.getPad(0), this.state.getPad(1));
      return var4;
   }
}
