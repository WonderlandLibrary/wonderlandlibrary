package my.NewSnake.utils;

public class StringConversions {
   public static Object castNumber(String var0, Object var1) {
      if (var0.contains(".")) {
         return var0.toLowerCase().contains("f") ? Float.parseFloat(var0) : Double.parseDouble(var0);
      } else {
         return isNumeric(var0) ? Integer.parseInt(var0) : var0;
      }
   }

   public static boolean isNumeric(String var0) {
      try {
         Integer.parseInt(var0);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public static boolean isBoolean(String var0) {
      return var0 != null && (var0.toLowerCase().equalsIgnoreCase("true") || var0.toLowerCase().equalsIgnoreCase("false"));
   }
}
