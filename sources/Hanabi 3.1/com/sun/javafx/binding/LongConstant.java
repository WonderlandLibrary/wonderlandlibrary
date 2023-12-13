package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableLongValue;

public final class LongConstant implements ObservableLongValue {
   private final long value;

   private LongConstant(long var1) {
      this.value = var1;
   }

   public static LongConstant valueOf(long var0) {
      return new LongConstant(var0);
   }

   public long get() {
      return this.value;
   }

   public Long getValue() {
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
      return this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }
}
