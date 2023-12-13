package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;

public class ObjectConstant implements ObservableObjectValue {
   private final Object value;

   private ObjectConstant(Object var1) {
      this.value = var1;
   }

   public static ObjectConstant valueOf(Object var0) {
      return new ObjectConstant(var0);
   }

   public Object get() {
      return this.value;
   }

   public Object getValue() {
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
}
