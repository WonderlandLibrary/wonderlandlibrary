package javafx.util.converter;

import javafx.util.StringConverter;

public class ByteStringConverter extends StringConverter {
   public Byte fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : Byte.valueOf(var1);
      }
   }

   public String toString(Byte var1) {
      return var1 == null ? "" : Byte.toString(var1);
   }
}
