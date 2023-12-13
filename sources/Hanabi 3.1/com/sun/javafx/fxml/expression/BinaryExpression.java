package com.sun.javafx.fxml.expression;

import java.util.List;
import java.util.function.BiFunction;

public final class BinaryExpression extends Expression {
   private final BiFunction evaluator;
   private final Expression left;
   private final Expression right;

   public BinaryExpression(Expression var1, Expression var2, BiFunction var3) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var2 == null) {
         throw new NullPointerException();
      } else {
         this.left = var1;
         this.right = var2;
         this.evaluator = var3;
      }
   }

   public Object evaluate(Object var1) {
      return this.evaluator.apply(this.left.evaluate(var1), this.right.evaluate(var1));
   }

   public void update(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public boolean isDefined(Object var1) {
      return this.left.isDefined(var1) && this.right.isDefined(var1);
   }

   public boolean isLValue() {
      return false;
   }

   protected void getArguments(List var1) {
      this.left.getArguments(var1);
      this.right.getArguments(var1);
   }
}
