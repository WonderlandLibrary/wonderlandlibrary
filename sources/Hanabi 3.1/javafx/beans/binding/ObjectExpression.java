package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.Locale;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class ObjectExpression implements ObservableObjectValue {
   public Object getValue() {
      return this.get();
   }

   public static ObjectExpression objectExpression(final ObservableObjectValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Value must be specified.");
      } else {
         return (ObjectExpression)(var0 instanceof ObjectExpression ? (ObjectExpression)var0 : new ObjectBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected Object computeValue() {
               return var0.get();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public BooleanBinding isEqualTo(ObservableObjectValue var1) {
      return Bindings.equal((ObservableObjectValue)this, (ObservableObjectValue)var1);
   }

   public BooleanBinding isEqualTo(Object var1) {
      return Bindings.equal((ObservableObjectValue)this, (Object)var1);
   }

   public BooleanBinding isNotEqualTo(ObservableObjectValue var1) {
      return Bindings.notEqual((ObservableObjectValue)this, (ObservableObjectValue)var1);
   }

   public BooleanBinding isNotEqualTo(Object var1) {
      return Bindings.notEqual((ObservableObjectValue)this, (Object)var1);
   }

   public BooleanBinding isNull() {
      return Bindings.isNull(this);
   }

   public BooleanBinding isNotNull() {
      return Bindings.isNotNull(this);
   }

   public StringBinding asString() {
      return (StringBinding)StringFormatter.convert(this);
   }

   public StringBinding asString(String var1) {
      return (StringBinding)Bindings.format(var1, this);
   }

   public StringBinding asString(Locale var1, String var2) {
      return (StringBinding)Bindings.format(var1, var2, this);
   }
}
