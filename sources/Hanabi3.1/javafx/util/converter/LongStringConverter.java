package javafx.util.converter;

import javafx.util.StringConverter;

public class LongStringConverter extends StringConverter {
   public Long fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : Long.valueOf(var1);
      }
   }

   public String toString(Long var1) {
      return var1 == null ? "" : Long.toString(var1);
   }
}
