package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;

public final class DoubleConstant implements ObservableDoubleValue {
   private final double value;

   private DoubleConstant(double var1) {
      this.value = var1;
   }

   public static DoubleConstant valueOf(double var0) {
      return new DoubleConstant(var0);
   }

   public double get() {
      return this.value;
   }

   public Double getValue() {
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
      return (float)this.value;
   }

   public double doubleValue() {
      return this.value;
   }
}
