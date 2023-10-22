package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.BooleanExpression;

public abstract class ReadOnlyBooleanProperty extends BooleanExpression implements ReadOnlyProperty {
   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("ReadOnlyBooleanProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append(this.get()).append("]");
      return var3.toString();
   }

   public static ReadOnlyBooleanProperty readOnlyBooleanProperty(final ReadOnlyProperty var0) {
      if (var0 == null) {
         throw new NullPointerException("Property cannot be null");
      } else {
         return (ReadOnlyBooleanProperty)(var0 instanceof ReadOnlyBooleanProperty ? (ReadOnlyBooleanProperty)var0 : new ReadOnlyBooleanPropertyBase() {
            private boolean valid = true;
            private final InvalidationListener listener = (var1x) -> {
               if (this.valid) {
                  this.valid = false;
                  this.fireValueChangedEvent();
               }

            };

            {
               var0.addListener(new WeakInvalidationListener(this.listener));
            }

            public boolean get() {
               this.valid = true;
               Boolean var1 = (Boolean)var0.getValue();
               return var1 == null ? false : var1;
            }

            public Object getBean() {
               return null;
            }

            public String getName() {
               return var0.getName();
            }
         });
      }
   }

   public ReadOnlyObjectProperty asObject() {
      return new ReadOnlyObjectPropertyBase() {
         private boolean valid = true;
         private final InvalidationListener listener = (var1x) -> {
            if (this.valid) {
               this.valid = false;
               this.fireValueChangedEvent();
            }

         };

         {
            ReadOnlyBooleanProperty.this.addListener(new WeakInvalidationListener(this.listener));
         }

         public Object getBean() {
            return null;
         }

         public String getName() {
            return ReadOnlyBooleanProperty.this.getName();
         }

         public Boolean get() {
            this.valid = true;
            return ReadOnlyBooleanProperty.this.getValue();
         }
      };
   }
}
