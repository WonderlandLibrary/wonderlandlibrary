package javafx.util.converter;

import java.math.BigInteger;
import javafx.util.StringConverter;

public class BigIntegerStringConverter extends StringConverter {
   public BigInteger fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() < 1 ? null : new BigInteger(var1);
      }
   }

   public String toString(BigInteger var1) {
      return var1 == null ? "" : var1.toString();
   }
}
