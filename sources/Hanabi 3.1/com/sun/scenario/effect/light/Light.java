package com.sun.scenario.effect.light;

import com.sun.scenario.effect.Color4f;

public abstract class Light {
   private final Type type;
   private Color4f color;

   Light(Type var1) {
      this(var1, Color4f.WHITE);
   }

   Light(Type var1, Color4f var2) {
      if (var1 == null) {
         throw new InternalError("Light type must be non-null");
      } else {
         this.type = var1;
         this.setColor(var2);
      }
   }

   public Type getType() {
      return this.type;
   }

   public Color4f getColor() {
      return this.color;
   }

   public void setColor(Color4f var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Color must be non-null");
      } else {
         this.color = var1;
      }
   }

   public abstract float[] getNormalizedLightPosition();

   public static enum Type {
      DISTANT,
      POINT,
      SPOT;
   }
}
