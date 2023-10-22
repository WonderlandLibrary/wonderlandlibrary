package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;

public class Glow extends DelegateEffect {
   private final GaussianBlur blur;
   private final Blend blend;

   public Glow() {
      this(DefaultInput);
   }

   public Glow(Effect var1) {
      super(var1);
      this.blur = new GaussianBlur(10.0F, var1);
      Crop var2 = new Crop(this.blur, var1);
      this.blend = new Blend(Blend.Mode.ADD, var1, var2);
      this.blend.setOpacity(0.3F);
   }

   protected Effect getDelegate() {
      return this.blend;
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
      this.blur.setInput(var1);
      this.blend.setBottomInput(var1);
   }

   public float getLevel() {
      return this.blend.getOpacity();
   }

   public void setLevel(float var1) {
      float var2 = this.blend.getOpacity();
      this.blend.setOpacity(var1);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).untransform(var1, var2);
   }
}
