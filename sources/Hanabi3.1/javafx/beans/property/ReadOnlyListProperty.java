package javafx.beans.property;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListExpression;
import javafx.collections.ObservableList;

public abstract class ReadOnlyListProperty extends ListExpression implements ReadOnlyProperty {
   public void bindContentBidirectional(ObservableList var1) {
      Bindings.bindContentBidirectional((ObservableList)this, (ObservableList)var1);
   }

   public void unbindContentBidirectional(Object var1) {
      Bindings.unbindContentBidirectional(this, var1);
   }

   public void bindContent(ObservableList var1) {
      Bindings.bindContent((List)this, (ObservableList)var1);
   }

   public void unbindContent(Object var1) {
      Bindings.unbindContent(this, var1);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof List)) {
         return false;
      } else {
         List var2 = (List)var1;
         if (this.size() != var2.size()) {
            return false;
         } else {
            ListIterator var3 = this.listIterator();
            ListIterator var4 = var2.listIterator();

            while(true) {
               if (var3.hasNext() && var4.hasNext()) {
                  Object var5 = var3.next();
                  Object var6 = var4.next();
                  if (var5 == null) {
                     if (var6 == null) {
                        continue;
                     }
                  } else if (var5.equals(var6)) {
                     continue;
                  }

                  return false;
               }

               return true;
            }
         }
      }
   }

   public int hashCode() {
      int var1 = 1;

      Object var3;
      for(Iterator var2 = this.iterator(); var2.hasNext(); var1 = 31 * var1 + (var3 == null ? 0 : var3.hashCode())) {
         var3 = var2.next();
      }

      return var1;
   }

   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("ReadOnlyListProperty [");
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
