package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;

public final class IntegerConstant implements ObservableIntegerValue {
   private final int value;

   private IntegerConstant(int var1) {
      this.value = var1;
   }

   public static IntegerConstant valueOf(int var0) {
      return new IntegerConstant(var0);
   }

   public int get() {
      return this.value;
   }

   public Integer getValue() {
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
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }
}
