package javafx.beans.property;

import javafx.beans.binding.StringExpression;

public abstract class ReadOnlyStringProperty extends StringExpression implements ReadOnlyProperty {
   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("ReadOnlyStringProperty [");
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
