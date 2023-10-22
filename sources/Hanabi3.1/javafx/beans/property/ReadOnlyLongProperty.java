package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.LongExpression;

public abstract class ReadOnlyLongProperty extends LongExpression implements ReadOnlyProperty {
   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("ReadOnlyLongProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append(this.get()).append("]");
      return var3.toString();
   }

   public static ReadOnlyLongProperty readOnlyLongProperty(final ReadOnlyProperty var0) {
      if (var0 == null) {
         throw new NullPointerException("Property cannot be null");
      } else {
         return (ReadOnlyLongProperty)(var0 instanceof ReadOnlyLongProperty ? (ReadOnlyLongProperty)var0 : new ReadOnlyLongPropertyBase() {
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

            public long get() {
               this.valid = true;
               Number var1 = (Number)var0.getValue();
               return var1 == null ? 0L : var1.longValue();
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
            ReadOnlyLongProperty.this.addListener(new WeakInvalidationListener(this.listener));
         }

         public Object getBean() {
            return null;
         }

         public String getName() {
            return ReadOnlyLongProperty.this.getName();
         }

         public Long get() {
            this.valid = true;
            return ReadOnlyLongProperty.this.getValue();
         }
      };
   }
}
