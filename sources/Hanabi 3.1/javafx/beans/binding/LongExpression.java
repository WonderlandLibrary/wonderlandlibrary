package javafx.beans.binding;

import javafx.beans.Observable;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class LongExpression extends NumberExpressionBase implements ObservableLongValue {
   public int intValue() {
      return (int)this.get();
   }

   public long longValue() {
      return this.get();
   }

   public float floatValue() {
      return (float)this.get();
   }

   public double doubleValue() {
      return (double)this.get();
   }

   public Long getValue() {
      return this.get();
   }

   public static LongExpression longExpression(final ObservableLongValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (LongExpression)(var0 instanceof LongExpression ? (LongExpression)var0 : new LongBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected long computeValue() {
               return var0.get();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public static LongExpression longExpression(final ObservableValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (LongExpression)(var0 instanceof LongExpression ? (LongExpression)var0 : new LongBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected long computeValue() {
               Number var1 = (Number)var0.getValue();
               return var1 == null ? 0L : var1.longValue();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public LongBinding negate() {
      return (LongBinding)Bindings.negate(this);
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

   public LongBinding add(int var1) {
      return (LongBinding)Bindings.add(this, var1);
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

   public LongBinding subtract(int var1) {
      return (LongBinding)Bindings.subtract(this, var1);
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

   public LongBinding multiply(int var1) {
      return (LongBinding)Bindings.multiply(this, var1);
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

   public LongBinding divide(int var1) {
      return (LongBinding)Bindings.divide(this, var1);
   }

   public ObjectExpression asObject() {
      return new ObjectBinding() {
         {
            this.bind(new Observable[]{LongExpression.this});
         }

         public void dispose() {
            this.unbind(new Observable[]{LongExpression.this});
         }

         protected Long computeValue() {
            return LongExpression.this.getValue();
         }
      };
   }
}
