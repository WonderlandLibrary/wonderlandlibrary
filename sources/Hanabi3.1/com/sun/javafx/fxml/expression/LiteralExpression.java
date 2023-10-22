package com.sun.javafx.fxml.expression;

import java.util.List;

public class LiteralExpression extends Expression {
   private Object value;

   public LiteralExpression(Object var1) {
      this.value = var1;
   }

   public Object evaluate(Object var1) {
      return this.value;
   }

   public void update(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public boolean isDefined(Object var1) {
      return true;
   }

   public boolean isLValue() {
      return false;
   }

   protected void getArguments(List var1) {
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
