package javafx.util.converter;

import javafx.util.StringConverter;

public class IntegerStringConverter extends StringConverter {
   public Integer fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : Integer.valueOf(var1);
      }
   }

   public String toString(Integer var1) {
      return var1 == null ? "" : Integer.toString(var1);
   }
}
