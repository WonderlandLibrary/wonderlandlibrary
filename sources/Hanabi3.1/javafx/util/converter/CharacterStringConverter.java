package javafx.util.converter;

import javafx.util.StringConverter;

public class CharacterStringConverter extends StringConverter {
   public Character fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : var1.charAt(0);
      }
   }

   public String toString(Character var1) {
      return var1 == null ? "" : var1.toString();
   }
}
