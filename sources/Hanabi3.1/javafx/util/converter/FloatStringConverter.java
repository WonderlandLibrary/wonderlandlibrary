package javafx.util.converter;

import javafx.util.StringConverter;

public class FloatStringConverter extends StringConverter {
   public Float fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : Float.valueOf(var1);
      }
   }

   public String toString(Float var1) {
      return var1 == null ? "" : Float.toString(var1);
   }
}
