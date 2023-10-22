package javafx.beans.property;

import java.text.Format;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableStringValue;
import javafx.util.StringConverter;

public abstract class StringProperty extends ReadOnlyStringProperty implements Property, WritableStringValue {
   public void setValue(String var1) {
      this.set(var1);
   }

   public void bindBidirectional(Property var1) {
      Bindings.bindBidirectional(this, var1);
   }

   public void bindBidirectional(Property var1, Format var2) {
      Bindings.bindBidirectional(this, var1, (Format)var2);
   }

   public void bindBidirectional(Property var1, StringConverter var2) {
      Bindings.bindBidirectional(this, var1, (StringConverter)var2);
   }

   public void unbindBidirectional(Property var1) {
      Bindings.unbindBidirectional((Property)this, (Property)var1);
   }

   public void unbindBidirectional(Object var1) {
      Bindings.unbindBidirectional((Object)this, (Object)var1);
   }

   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("StringProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      var3.append("value: ").append((String)this.get()).append("]");
      return var3.toString();
   }
}
