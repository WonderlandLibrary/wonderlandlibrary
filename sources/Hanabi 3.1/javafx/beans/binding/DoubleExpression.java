package javafx.beans.binding;

import javafx.beans.Observable;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class DoubleExpression extends NumberExpressionBase implements ObservableDoubleValue {
   public int intValue() {
      return (int)this.get();
   }

   public long longValue() {
      return (long)this.get();
   }

   public float floatValue() {
      return (float)this.get();
   }

   public double doubleValue() {
      return this.get();
   }

   public Double getValue() {
      return this.get();
   }

   public static DoubleExpression doubleExpression(final ObservableDoubleValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (DoubleExpression)(var0 instanceof DoubleExpression ? (DoubleExpression)var0 : new DoubleBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected double computeValue() {
               return var0.get();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public static DoubleExpression doubleExpression(final ObservableValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (DoubleExpression)(var0 instanceof DoubleExpression ? (DoubleExpression)var0 : new DoubleBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected double computeValue() {
               Number var1 = (Number)var0.getValue();
               return var1 == null ? 0.0 : var1.doubleValue();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public DoubleBinding negate() {
      return (DoubleBinding)Bindings.negate(this);
   }

   public DoubleBinding add(ObservableNumberValue var1) {
      return (DoubleBinding)Bindings.add(this, var1);
   }

   public DoubleBinding add(double var1) {
      return Bindings.add(this, var1);
   }

   public DoubleBinding add(float var1) {
      return (DoubleBinding)Bindings.add(this, var1);
   }

   public DoubleBinding add(long var1) {
      return (DoubleBinding)Bindings.add(this, var1);
   }

   public DoubleBinding add(int var1) {
      return (DoubleBinding)Bindings.add(this, var1);
   }

   public DoubleBinding subtract(ObservableNumberValue var1) {
      return (DoubleBinding)Bindings.subtract(this, var1);
   }

   public DoubleBinding subtract(double var1) {
      return Bindings.subtract(this, var1);
   }

   public DoubleBinding subtract(float var1) {
      return (DoubleBinding)Bindings.subtract(this, var1);
   }

   public DoubleBinding subtract(long var1) {
      return (DoubleBinding)Bindings.subtract(this, var1);
   }

   public DoubleBinding subtract(int var1) {
      return (DoubleBinding)Bindings.subtract(this, var1);
   }

   public DoubleBinding multiply(ObservableNumberValue var1) {
      return (DoubleBinding)Bindings.multiply(this, var1);
   }

   public DoubleBinding multiply(double var1) {
      return Bindings.multiply(this, var1);
   }

   public DoubleBinding multiply(float var1) {
      return (DoubleBinding)Bindings.multiply(this, var1);
   }

   public DoubleBinding multiply(long var1) {
      return (DoubleBinding)Bindings.multiply(this, var1);
   }

   public DoubleBinding multiply(int var1) {
      return (DoubleBinding)Bindings.multiply(this, var1);
   }

   public DoubleBinding divide(ObservableNumberValue var1) {
      return (DoubleBinding)Bindings.divide(this, var1);
   }

   public DoubleBinding divide(double var1) {
      return Bindings.divide(this, var1);
   }

   public DoubleBinding divide(float var1) {
      return (DoubleBinding)Bindings.divide(this, var1);
   }

   public DoubleBinding divide(long var1) {
      return (DoubleBinding)Bindings.divide(this, var1);
   }

   public DoubleBinding divide(int var1) {
      return (DoubleBinding)Bindings.divide(this, var1);
   }

   public ObjectExpression asObject() {
      return new ObjectBinding() {
         {
            this.bind(new Observable[]{DoubleExpression.this});
         }

         public void dispose() {
            this.unbind(new Observable[]{DoubleExpression.this});
         }

         protected Double computeValue() {
            return DoubleExpression.this.getValue();
         }
      };
   }
}
