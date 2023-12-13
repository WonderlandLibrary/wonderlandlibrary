package javafx.util.converter;

import javafx.util.StringConverter;

public class BooleanStringConverter extends StringConverter {
   public Boolean fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : Boolean.valueOf(var1);
      }
   }

   public String toString(Boolean var1) {
      return var1 == null ? "" : var1.toString();
   }
}
