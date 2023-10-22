package com.sun.javafx.effect;

public enum EffectDirtyBits {
   EFFECT_DIRTY,
   BOUNDS_CHANGED;

   private int mask = 1 << this.ordinal();

   public final int getMask() {
      return this.mask;
   }

   public static boolean isSet(int var0, EffectDirtyBits var1) {
      return (var0 & var1.getMask()) != 0;
   }
}
