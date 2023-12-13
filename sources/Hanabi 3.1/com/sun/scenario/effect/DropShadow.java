package com.sun.scenario.effect;

public class DropShadow extends DelegateEffect {
   private AbstractShadow shadow;
   private final Offset offset;
   private final Merge merge;

   public DropShadow() {
      this(DefaultInput, DefaultInput);
   }

   public DropShadow(Effect var1) {
      this(var1, var1);
   }

   public DropShadow(Effect var1, Effect var2) {
      super(var1, var2);
      this.shadow = new GaussianShadow(10.0F, Color4f.BLACK, var1);
      this.offset = new Offset(0, 0, this.shadow);
      this.merge = new Merge(this.offset, var2);
   }

   public AbstractShadow.ShadowMode getShadowMode() {
      return this.shadow.getMode();
   }

   public void setShadowMode(AbstractShadow.ShadowMode var1) {
      AbstractShadow.ShadowMode var2 = this.shadow.getMode();
      AbstractShadow var3 = this.shadow.implFor(var1);
      if (var3 != this.shadow) {
         this.offset.setInput(var3);
      }

      this.shadow = var3;
   }

   protected Effect getDelegate() {
      return this.merge;
   }

   public final Effect getShadowSourceInput() {
      return this.shadow.getInput();
   }

   public void setShadowSourceInput(Effect var1) {
      this.shadow.setInput(var1);
   }

   public final Effect getContentInput() {
      return this.merge.getTopInput();
   }

   public void setContentInput(Effect var1) {
      this.merge.setTopInput(var1);
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

   public int getOffsetX() {
      return this.offset.getX();
   }

   public void setOffsetX(int var1) {
      int var2 = this.offset.getX();
      this.offset.setX(var1);
   }

   public int getOffsetY() {
      return this.offset.getY();
   }

   public void setOffsetY(int var1) {
      int var2 = this.offset.getY();
      this.offset.setY(var1);
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      return this.shadow.getAccelType(var1);
   }
}
