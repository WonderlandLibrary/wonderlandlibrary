package javafx.beans.property;

import java.util.Iterator;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.MapExpression;
import javafx.collections.ObservableMap;

public abstract class ReadOnlyMapProperty extends MapExpression implements ReadOnlyProperty {
   public void bindContentBidirectional(ObservableMap var1) {
      Bindings.bindContentBidirectional((ObservableMap)this, (ObservableMap)var1);
   }

   public void unbindContentBidirectional(Object var1) {
      Bindings.unbindContentBidirectional(this, var1);
   }

   public void bindContent(ObservableMap var1) {
      Bindings.bindContent((Map)this, (ObservableMap)var1);
   }

   public void unbindContent(Object var1) {
      Bindings.unbindContent(this, var1);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Map)) {
         return false;
      } else {
         Map var2 = (Map)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            try {
               Iterator var3 = this.entrySet().iterator();

               Object var5;
               label43:
               do {
                  Object var6;
                  do {
                     if (!var3.hasNext()) {
                        return true;
                     }

                     Map.Entry var4 = (Map.Entry)var3.next();
                     var5 = var4.getKey();
                     var6 = var4.getValue();
                     if (var6 == null) {
                        continue label43;
                     }
                  } while(var6.equals(var2.get(var5)));

                  return false;
               } while(var2.get(var5) == null && var2.containsKey(var5));

               return false;
            } catch (ClassCastException var7) {
               return false;
            } catch (NullPointerException var8) {
               return false;
            }
         }
      }
   }

   public int hashCode() {
      int var1 = 0;

      Map.Entry var3;
      for(Iterator var2 = this.entrySet().iterator(); var2.hasNext(); var1 += var3.hashCode()) {
         var3 = (Map.Entry)var2.next();
      }

      return var1;
   }

   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("ReadOnlyMapProperty [");
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
