package com.sun.javafx.fxml.expression;

import java.util.List;
import java.util.function.Function;

public final class UnaryExpression extends Expression {
   private final Expression operand;
   private final Function evaluator;

   public UnaryExpression(Expression var1, Function var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.operand = var1;
         this.evaluator = var2;
      }
   }

   public Object evaluate(Object var1) {
      return this.evaluator.apply(this.operand.evaluate(var1));
   }

   public void update(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public boolean isDefined(Object var1) {
      return this.operand.isDefined(var1);
   }

   public boolean isLValue() {
      return false;
   }

   protected void getArguments(List var1) {
      this.operand.getArguments(var1);
   }
}
