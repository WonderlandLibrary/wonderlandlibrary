package javafx.beans.binding;

import javafx.beans.Observable;
import javafx.beans.value.ObservableFloatValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class FloatExpression extends NumberExpressionBase implements ObservableFloatValue {
   public int intValue() {
      return (int)this.get();
   }

   public long longValue() {
      return (long)this.get();
   }

   public float floatValue() {
      return this.get();
   }

   public double doubleValue() {
      return (double)this.get();
   }

   public Float getValue() {
      return this.get();
   }

   public static FloatExpression floatExpression(final ObservableFloatValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (FloatExpression)(var0 instanceof FloatExpression ? (FloatExpression)var0 : new FloatBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected float computeValue() {
               return var0.get();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public static FloatExpression floatExpression(final ObservableValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (FloatExpression)(var0 instanceof FloatExpression ? (FloatExpression)var0 : new FloatBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected float computeValue() {
               Number var1 = (Number)var0.getValue();
               return var1 == null ? 0.0F : var1.floatValue();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public FloatBinding negate() {
      return (FloatBinding)Bindings.negate(this);
   }

   public DoubleBinding add(double var1) {
      return Bindings.add(this, var1);
   }

   public FloatBinding add(float var1) {
      return (FloatBinding)Bindings.add(this, var1);
   }

   public FloatBinding add(long var1) {
      return (FloatBinding)Bindings.add(this, var1);
   }

   public FloatBinding add(int var1) {
      return (FloatBinding)Bindings.add(this, var1);
   }

   public DoubleBinding subtract(double var1) {
      return Bindings.subtract(this, var1);
   }

   public FloatBinding subtract(float var1) {
      return (FloatBinding)Bindings.subtract(this, var1);
   }

   public FloatBinding subtract(long var1) {
      return (FloatBinding)Bindings.subtract(this, var1);
   }

   public FloatBinding subtract(int var1) {
      return (FloatBinding)Bindings.subtract(this, var1);
   }

   public DoubleBinding multiply(double var1) {
      return Bindings.multiply(this, var1);
   }

   public FloatBinding multiply(float var1) {
      return (FloatBinding)Bindings.multiply(this, var1);
   }

   public FloatBinding multiply(long var1) {
      return (FloatBinding)Bindings.multiply(this, var1);
   }

   public FloatBinding multiply(int var1) {
      return (FloatBinding)Bindings.multiply(this, var1);
   }

   public DoubleBinding divide(double var1) {
      return Bindings.divide(this, var1);
   }

   public FloatBinding divide(float var1) {
      return (FloatBinding)Bindings.divide(this, var1);
   }

   public FloatBinding divide(long var1) {
      return (FloatBinding)Bindings.divide(this, var1);
   }

   public FloatBinding divide(int var1) {
      return (FloatBinding)Bindings.divide(this, var1);
   }

   public ObjectExpression asObject() {
      return new ObjectBinding() {
         {
            this.bind(new Observable[]{FloatExpression.this});
         }

         public void dispose() {
            this.unbind(new Observable[]{FloatExpression.this});
         }

         protected Float computeValue() {
            return FloatExpression.this.getValue();
         }
      };
   }
}
