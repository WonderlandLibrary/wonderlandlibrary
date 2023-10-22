package javafx.util.converter;

import javafx.util.StringConverter;

public class DefaultStringConverter extends StringConverter {
   public String toString(String var1) {
      return var1 != null ? var1 : "";
   }

   public String fromString(String var1) {
      return var1;
   }
}
