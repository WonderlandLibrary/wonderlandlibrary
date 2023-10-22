package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;

public class InnerShadow extends DelegateEffect {
   private final InvertMask invert;
   private AbstractShadow shadow;
   private final Blend blend;

   public InnerShadow() {
      this(DefaultInput, DefaultInput);
   }

   public InnerShadow(Effect var1) {
      this(var1, var1);
   }

   public InnerShadow(Effect var1, Effect var2) {
      super(var1, var2);
      this.invert = new InvertMask(10, var1);
      this.shadow = new GaussianShadow(10.0F, Color4f.BLACK, this.invert);
      this.blend = new Blend(Blend.Mode.SRC_ATOP, var2, this.shadow);
   }

   public AbstractShadow.ShadowMode getShadowMode() {
      return this.shadow.getMode();
   }

   public void setShadowMode(AbstractShadow.ShadowMode var1) {
      AbstractShadow.ShadowMode var2 = this.shadow.getMode();
      AbstractShadow var3 = this.shadow.implFor(var1);
      if (var3 != this.shadow) {
         this.blend.setTopInput(var3);
      }

      this.shadow = var3;
   }

   protected Effect getDelegate() {
      return this.blend;
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      Effect var3 = getDefaultedInput(this.getContentInput(), var2);
      return var3.getBounds(var1, var2);
   }

   public final Effect getShadowSourceInput() {
      return this.invert.getInput();
   }

   public void setShadowSourceInput(Effect var1) {
      this.invert.setInput(var1);
   }

   public final Effect getContentInput() {
      return this.blend.getBottomInput();
   }

   public void setContentInput(Effect var1) {
      this.blend.setBottomInput(var1);
   }

   public float getRadius() {
      return this.shadow.getGaussianRadius();
   }

   public void setRadius(float var1) {
      float var2 = this.shadow.getGaussianRadius();
      this.invert.setPad((int)Math.ceil((double)var1));
      this.shadow.setGaussianRadius(var1);
   }

   public float getGaussianRadius() {
      return this.shadow.getGaussianRadius();
   }

   public float getGaussianWidth() {
      return this.shadow.getGaussianWidth();
   }

   public float getGaussianHeight() {
      return this.shadow.getGaussianHeight();
   }

   public void setGaussianRadius(float var1) {
      this.setRadius(var1);
   }

   public void setGaussianWidth(float var1) {
      float var2 = this.shadow.getGaussianWidth();
      float var3 = (Math.max(var1, this.shadow.getGaussianHeight()) - 1.0F) / 2.0F;
      this.invert.setPad((int)Math.ceil((double)var3));
      this.shadow.setGaussianWidth(var1);
   }

   public void setGaussianHeight(float var1) {
      float var2 = this.shadow.getGaussianHeight();
      float var3 = (Math.max(this.shadow.getGaussianWidth(), var1) - 1.0F) / 2.0F;
      this.invert.setPad((int)Math.ceil((double)var3));
      this.shadow.setGaussianHeight(var1);
   }

   public float getChoke() {
      return this.shadow.getSpread();
   }

   public void setChoke(float var1) {
      float var2 = this.shadow.getSpread();
      this.shadow.setSpread(var1);
   }

   public Color4f getColor() {
      return this.shadow.getColor();
   }

   public void setColor(Color4f var1) {
      Color4f var2 = this.shadow.getColor();
      this.shadow.setColor(var1);
   }

   public int getOffsetX() {
      return this.invert.getOffsetX();
   }

   public void setOffsetX(int var1) {
      int var2 = this.invert.getOffsetX();
      this.invert.setOffsetX(var1);
   }

   public int getOffsetY() {
      return this.invert.getOffsetY();
   }

   public void setOffsetY(int var1) {
      int var2 = this.invert.getOffsetY();
      this.invert.setOffsetY(var1);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(1, var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(1, var2).untransform(var1, var2);
   }
}
