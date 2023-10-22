package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableIntegerValue;

public abstract class IntegerProperty extends ReadOnlyIntegerProperty implements Property, WritableIntegerValue {
   public void setValue(Number var1) {
      if (var1 == null) {
         Logging.getLogger().fine("Attempt to set integer property to null, using default value instead.", new NullPointerException());
         this.set(0);
      } else {
         this.set(var1.intValue());
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
      StringBuilder var3 = new StringBuilder("IntegerProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append(this.get()).append("]");
      return var3.toString();
   }

   public static IntegerProperty integerProperty(final Property var0) {
      if (var0 == null) {
         throw new NullPointerException("Property cannot be null");
      } else {
         return new IntegerPropertyBase() {
            private final AccessControlContext acc = AccessController.getContext();

            {
               BidirectionalBinding.bindNumber((IntegerProperty)this, (Property)var0);
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
            BidirectionalBinding.bindNumber((Property)this, (IntegerProperty)IntegerProperty.this);
         }

         public Object getBean() {
            return null;
         }

         public String getName() {
            return IntegerProperty.this.getName();
         }

         protected void finalize() throws Throwable {
            try {
               AccessController.doPrivileged(() -> {
                  BidirectionalBinding.unbindNumber(this, IntegerProperty.this);
                  return null;
               }, this.acc);
            } finally {
               super.finalize();
            }

         }
      };
   }
}
