package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableFloatValue;

public final class FloatConstant implements ObservableFloatValue {
   private final float value;

   private FloatConstant(float var1) {
      this.value = var1;
   }

   public static FloatConstant valueOf(float var0) {
      return new FloatConstant(var0);
   }

   public float get() {
      return this.value;
   }

   public Float getValue() {
      return this.value;
   }

   public void addListener(InvalidationListener var1) {
   }

   public void addListener(ChangeListener var1) {
   }

   public void removeListener(InvalidationListener var1) {
   }

   public void removeListener(ChangeListener var1) {
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public float floatValue() {
      return this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }
}
