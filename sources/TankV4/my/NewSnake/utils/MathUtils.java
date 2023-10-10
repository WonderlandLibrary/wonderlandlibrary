package my.NewSnake.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtils {
   public static double round(double var0, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal var3 = new BigDecimal(var0);
         var3 = var3.setScale(var2, RoundingMode.HALF_UP);
         return var3.doubleValue();
      }
   }

   public static double roundToPlace(double var0, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal var3 = new BigDecimal(var0);
         var3 = var3.setScale(var2, RoundingMode.HALF_UP);
         return var3.doubleValue();
      }
   }
}
