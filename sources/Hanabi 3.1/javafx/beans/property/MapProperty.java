package javafx.beans.property;

import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableMapValue;
import javafx.collections.ObservableMap;

public abstract class MapProperty extends ReadOnlyMapProperty implements Property, WritableMapValue {
   public void setValue(ObservableMap var1) {
      this.set(var1);
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
      StringBuilder var3 = new StringBuilder("MapProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append(this.get()).append("]");
      return var3.toString();
   }
}
