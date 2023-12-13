package javafx.util.converter;

import javafx.util.StringConverter;

public class ShortStringConverter extends StringConverter {
   public Short fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : Short.valueOf(var1);
      }
   }

   public String toString(Short var1) {
      return var1 == null ? "" : Short.toString(var1);
   }
}
