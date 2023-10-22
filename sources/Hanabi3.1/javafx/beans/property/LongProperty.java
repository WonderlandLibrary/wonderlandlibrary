package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableLongValue;

public abstract class LongProperty extends ReadOnlyLongProperty implements Property, WritableLongValue {
   public void setValue(Number var1) {
      if (var1 == null) {
         Logging.getLogger().fine("Attempt to set long property to null, using default value instead.", new NullPointerException());
         this.set(0L);
      } else {
         this.set(var1.longValue());
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
      StringBuilder var3 = new StringBuilder("LongProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append(this.get()).append("]");
      return var3.toString();
   }

   public static LongProperty longProperty(final Property var0) {
      if (var0 == null) {
         throw new NullPointerException("Property cannot be null");
      } else {
         return new LongPropertyBase() {
            private final AccessControlContext acc = AccessController.getContext();

            {
               BidirectionalBinding.bindNumber((LongProperty)this, (Property)var0);
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
            BidirectionalBinding.bindNumber((Property)this, (LongProperty)LongProperty.this);
         }

         public Object getBean() {
            return null;
         }

         public String getName() {
            return LongProperty.this.getName();
         }

         protected void finalize() throws Throwable {
            try {
               AccessController.doPrivileged(() -> {
                  BidirectionalBinding.unbindNumber(this, LongProperty.this);
                  return null;
               }, this.acc);
            } finally {
               super.finalize();
            }

         }
      };
   }
}
