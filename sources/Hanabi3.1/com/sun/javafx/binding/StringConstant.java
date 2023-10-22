package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ChangeListener;

public class StringConstant extends StringExpression {
   private final String value;

   private StringConstant(String var1) {
      this.value = var1;
   }

   public static StringConstant valueOf(String var0) {
      return new StringConstant(var0);
   }

   public String get() {
      return this.value;
   }

   public String getValue() {
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
