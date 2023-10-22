package javafx.util.converter;

import javafx.util.StringConverter;

public class DoubleStringConverter extends StringConverter {
   public Double fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : Double.valueOf(var1);
      }
   }

   public String toString(Double var1) {
      return var1 == null ? "" : Double.toString(var1);
   }
}
