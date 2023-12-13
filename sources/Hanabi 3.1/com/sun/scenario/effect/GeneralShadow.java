package com.sun.scenario.effect;

public class GeneralShadow extends DelegateEffect {
   private AbstractShadow shadow;

   public GeneralShadow() {
      this(DefaultInput);
   }

   public GeneralShadow(Effect var1) {
      super(var1);
      this.shadow = new GaussianShadow(10.0F, Color4f.BLACK, var1);
   }

   public AbstractShadow.ShadowMode getShadowMode() {
      return this.shadow.getMode();
   }

   public void setShadowMode(AbstractShadow.ShadowMode var1) {
      AbstractShadow.ShadowMode var2 = this.shadow.getMode();
      this.shadow = this.shadow.implFor(var1);
   }

   protected Effect getDelegate() {
      return this.shadow;
   }

   public final Effect getInput() {
      return this.shadow.getInput();
   }

   public void setInput(Effect var1) {
      this.shadow.setInput(var1);
   }

   public float getRadius() {
      return this.shadow.getGaussianRadius();
   }

   public void setRadius(float var1) {
      float var2 = this.shadow.getGaussianRadius();
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
      this.shadow.setGaussianWidth(var1);
   }

   public void setGaussianHeight(float var1) {
      float var2 = this.shadow.getGaussianHeight();
      this.shadow.setGaussianHeight(var1);
   }

   public float getSpread() {
      return this.shadow.getSpread();
   }

   public void setSpread(float var1) {
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
}
