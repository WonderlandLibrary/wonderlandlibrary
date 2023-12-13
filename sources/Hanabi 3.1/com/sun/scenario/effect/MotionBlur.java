package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.scenario.effect.impl.state.MotionBlurState;

public class MotionBlur extends LinearConvolveCoreEffect {
   private MotionBlurState state;

   public MotionBlur() {
      this(10.0F, 0.0F, DefaultInput);
   }

   public MotionBlur(float var1, float var2) {
      this(var1, var2, DefaultInput);
   }

   public MotionBlur(float var1, float var2, Effect var3) {
      super(var3);
      this.state = new MotionBlurState();
      this.setRadius(var1);
      this.setAngle(var2);
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
      this.state.setRadius(var1);
   }

   public float getAngle() {
      return this.state.getAngle();
   }

   public void setAngle(float var1) {
      this.state.setAngle(var1);
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      BaseBounds var3 = super.getBounds((BaseTransform)null, var2);
      int var4 = this.state.getHPad();
      int var5 = this.state.getVPad();
      RectBounds var6 = new RectBounds(var3.getMinX(), var3.getMinY(), var3.getMaxX(), var3.getMaxY());
      ((RectBounds)var6).grow((float)var4, (float)var5);
      return transformBounds(var1, var6);
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      Rectangle var4 = super.getResultBounds(var1, var2, var3);
      int var5 = this.state.getHPad();
      int var6 = this.state.getVPad();
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
      var4.grow(this.state.getHPad(), this.state.getVPad());
      return var4;
   }
}
