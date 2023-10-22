package com.sun.scenario.effect;

public abstract class AbstractShadow extends LinearConvolveCoreEffect {
   public AbstractShadow(Effect var1) {
      super(var1);
   }

   public abstract ShadowMode getMode();

   public abstract AbstractShadow implFor(ShadowMode var1);

   public abstract float getGaussianRadius();

   public abstract void setGaussianRadius(float var1);

   public abstract float getGaussianWidth();

   public abstract void setGaussianWidth(float var1);

   public abstract float getGaussianHeight();

   public abstract void setGaussianHeight(float var1);

   public abstract float getSpread();

   public abstract void setSpread(float var1);

   public abstract Color4f getColor();

   public abstract void setColor(Color4f var1);

   public abstract Effect getInput();

   public abstract void setInput(Effect var1);

   public static enum ShadowMode {
      ONE_PASS_BOX,
      TWO_PASS_BOX,
      THREE_PASS_BOX,
      GAUSSIAN;
   }
}
