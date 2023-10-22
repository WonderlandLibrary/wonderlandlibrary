package javafx.beans.binding;

import javafx.beans.Observable;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class IntegerExpression extends NumberExpressionBase implements ObservableIntegerValue {
   public int intValue() {
      return this.get();
   }

   public long longValue() {
      return (long)this.get();
   }

   public float floatValue() {
      return (float)this.get();
   }

   public double doubleValue() {
      return (double)this.get();
   }

   public Integer getValue() {
      return this.get();
   }

   public static IntegerExpression integerExpression(final ObservableIntegerValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (IntegerExpression)(var0 instanceof IntegerExpression ? (IntegerExpression)var0 : new IntegerBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected int computeValue() {
               return var0.get();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public static IntegerExpression integerExpression(final ObservableValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (IntegerExpression)(var0 instanceof IntegerExpression ? (IntegerExpression)var0 : new IntegerBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected int computeValue() {
               Number var1 = (Number)var0.getValue();
               return var1 == null ? 0 : var1.intValue();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public IntegerBinding negate() {
      return (IntegerBinding)Bindings.negate(this);
   }

   public DoubleBinding add(double var1) {
      return Bindings.add(this, var1);
   }

   public FloatBinding add(float var1) {
      return (FloatBinding)Bindings.add(this, var1);
   }

   public LongBinding add(long var1) {
      return (LongBinding)Bindings.add(this, var1);
   }

   public IntegerBinding add(int var1) {
      return (IntegerBinding)Bindings.add(this, var1);
   }

   public DoubleBinding subtract(double var1) {
      return Bindings.subtract(this, var1);
   }

   public FloatBinding subtract(float var1) {
      return (FloatBinding)Bindings.subtract(this, var1);
   }

   public LongBinding subtract(long var1) {
      return (LongBinding)Bindings.subtract(this, var1);
   }

   public IntegerBinding subtract(int var1) {
      return (IntegerBinding)Bindings.subtract(this, var1);
   }

   public DoubleBinding multiply(double var1) {
      return Bindings.multiply(this, var1);
   }

   public FloatBinding multiply(float var1) {
      return (FloatBinding)Bindings.multiply(this, var1);
   }

   public LongBinding multiply(long var1) {
      return (LongBinding)Bindings.multiply(this, var1);
   }

   public IntegerBinding multiply(int var1) {
      return (IntegerBinding)Bindings.multiply(this, var1);
   }

   public DoubleBinding divide(double var1) {
      return Bindings.divide(this, var1);
   }

   public FloatBinding divide(float var1) {
      return (FloatBinding)Bindings.divide(this, var1);
   }

   public LongBinding divide(long var1) {
      return (LongBinding)Bindings.divide(this, var1);
   }

   public IntegerBinding divide(int var1) {
      return (IntegerBinding)Bindings.divide(this, var1);
   }

   public ObjectExpression asObject() {
      return new ObjectBinding() {
         {
            this.bind(new Observable[]{IntegerExpression.this});
         }

         public void dispose() {
            this.unbind(new Observable[]{IntegerExpression.this});
         }

         protected Integer computeValue() {
            return IntegerExpression.this.getValue();
         }
      };
   }
}
