package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.FloatExpression;

public abstract class ReadOnlyFloatProperty extends FloatExpression implements ReadOnlyProperty {
   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("ReadOnlyFloatProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append(this.get()).append("]");
      return var3.toString();
   }

   public static ReadOnlyFloatProperty readOnlyFloatProperty(final ReadOnlyProperty var0) {
      if (var0 == null) {
         throw new NullPointerException("Property cannot be null");
      } else {
         return (ReadOnlyFloatProperty)(var0 instanceof ReadOnlyFloatProperty ? (ReadOnlyFloatProperty)var0 : new ReadOnlyFloatPropertyBase() {
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

            public float get() {
               this.valid = true;
               Number var1 = (Number)var0.getValue();
               return var1 == null ? 0.0F : var1.floatValue();
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
            ReadOnlyFloatProperty.this.addListener(new WeakInvalidationListener(this.listener));
         }

         public Object getBean() {
            return null;
         }

         public String getName() {
            return ReadOnlyFloatProperty.this.getName();
         }

         public Float get() {
            this.valid = true;
            return ReadOnlyFloatProperty.this.getValue();
         }
      };
   }
}
