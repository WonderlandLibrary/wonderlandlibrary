package javafx.util.converter;

import java.math.BigDecimal;
import javafx.util.StringConverter;

public class BigDecimalStringConverter extends StringConverter {
   public BigDecimal fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : new BigDecimal(var1);
      }
   }

   public String toString(BigDecimal var1) {
      return var1 == null ? "" : var1.toString();
   }
}
