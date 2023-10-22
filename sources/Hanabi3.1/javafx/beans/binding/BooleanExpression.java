package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import javafx.beans.Observable;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class BooleanExpression implements ObservableBooleanValue {
   public Boolean getValue() {
      return this.get();
   }

   public static BooleanExpression booleanExpression(final ObservableBooleanValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (BooleanExpression)(var0 instanceof BooleanExpression ? (BooleanExpression)var0 : new BooleanBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected boolean computeValue() {
               return var0.get();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public static BooleanExpression booleanExpression(final ObservableValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (BooleanExpression)(var0 instanceof BooleanExpression ? (BooleanExpression)var0 : new BooleanBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected boolean computeValue() {
               Boolean var1 = (Boolean)var0.getValue();
               return var1 == null ? false : var1;
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public BooleanBinding and(ObservableBooleanValue var1) {
      return Bindings.and(this, var1);
   }

   public BooleanBinding or(ObservableBooleanValue var1) {
      return Bindings.or(this, var1);
   }

   public BooleanBinding not() {
      return Bindings.not(this);
   }

   public BooleanBinding isEqualTo(ObservableBooleanValue var1) {
      return Bindings.equal((ObservableBooleanValue)this, (ObservableBooleanValue)var1);
   }

   public BooleanBinding isNotEqualTo(ObservableBooleanValue var1) {
      return Bindings.notEqual((ObservableBooleanValue)this, (ObservableBooleanValue)var1);
   }

   public StringBinding asString() {
      return (StringBinding)StringFormatter.convert(this);
   }

   public ObjectExpression asObject() {
      return new ObjectBinding() {
         {
            this.bind(new Observable[]{BooleanExpression.this});
         }

         public void dispose() {
            this.unbind(new Observable[]{BooleanExpression.this});
         }

         protected Boolean computeValue() {
            return BooleanExpression.this.getValue();
         }
      };
   }
}
