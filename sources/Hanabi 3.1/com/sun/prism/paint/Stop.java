package com.sun.prism.paint;

public class Stop {
   private final Color color;
   private final float offset;

   public Stop(Color var1, float var2) {
      this.color = var1;
      this.offset = var2;
   }

   public Color getColor() {
      return this.color;
   }

   public float getOffset() {
      return this.offset;
   }
}
