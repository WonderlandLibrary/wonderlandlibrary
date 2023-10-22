package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableFloatValue;

public abstract class FloatProperty extends ReadOnlyFloatProperty implements Property, WritableFloatValue {
   public void setValue(Number var1) {
      if (var1 == null) {
         Logging.getLogger().fine("Attempt to set float property to null, using default value instead.", new NullPointerException());
         this.set(0.0F);
      } else {
         this.set(var1.floatValue());
      }

   }

   public void bindBidirectional(Property var1) {
      Bindings.bindBidirectional(this, var1);
   }

   public void unbindBidirectional(Property var1) {
      Bindings.unbindBidirectional((Property)this, (Property)var1);
   }

   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("FloatProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append(this.get()).append("]");
      return var3.toString();
   }

   public static FloatProperty floatProperty(final Property var0) {
      if (var0 == null) {
         throw new NullPointerException("Property cannot be null");
      } else {
         return new FloatPropertyBase() {
            private final AccessControlContext acc = AccessController.getContext();

            {
               BidirectionalBinding.bindNumber((FloatProperty)this, (Property)var0);
            }

            public Object getBean() {
               return null;
            }

            public String getName() {
               return var0.getName();
            }

            protected void finalize() throws Throwable {
               try {
                  AccessController.doPrivileged(() -> {
                     BidirectionalBinding.unbindNumber(var0, this);
                     return null;
                  }, this.acc);
               } finally {
                  super.finalize();
               }

            }
         };
      }
   }

   public ObjectProperty asObject() {
      return new ObjectPropertyBase() {
         private final AccessControlContext acc = AccessController.getContext();

         {
            BidirectionalBinding.bindNumber((Property)this, (FloatProperty)FloatProperty.this);
         }

         public Object getBean() {
            return null;
         }

         public String getName() {
            return FloatProperty.this.getName();
         }

         protected void finalize() throws Throwable {
            try {
               AccessController.doPrivileged(() -> {
                  BidirectionalBinding.unbindNumber(this, FloatProperty.this);
                  return null;
               }, this.acc);
            } finally {
               super.finalize();
            }

         }
      };
   }
}
