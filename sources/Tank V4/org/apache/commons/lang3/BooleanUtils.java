package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;

public class BooleanUtils {
   public static Boolean negate(Boolean var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 ? Boolean.FALSE : Boolean.TRUE;
      }
   }

   public static boolean isTrue(Boolean var0) {
      return Boolean.TRUE.equals(var0);
   }

   public static boolean isNotTrue(Boolean var0) {
      return !isTrue(var0);
   }

   public static boolean isFalse(Boolean var0) {
      return Boolean.FALSE.equals(var0);
   }

   public static boolean isNotFalse(Boolean var0) {
      return !isFalse(var0);
   }

   public static boolean toBoolean(Boolean var0) {
      return var0 != null && var0;
   }

   public static boolean toBooleanDefaultIfNull(Boolean var0, boolean var1) {
      return var0 == null ? var1 : var0;
   }

   public static boolean toBoolean(int var0) {
      return var0 != 0;
   }

   public static Boolean toBooleanObject(int var0) {
      return var0 == 0 ? Boolean.FALSE : Boolean.TRUE;
   }

   public static Boolean toBooleanObject(Integer var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 == 0 ? Boolean.FALSE : Boolean.TRUE;
      }
   }

   public static boolean toBoolean(int var0, int var1, int var2) {
      if (var0 == var1) {
         return true;
      } else if (var0 == var2) {
         return false;
      } else {
         throw new IllegalArgumentException("The Integer did not match either specified value");
      }
   }

   public static boolean toBoolean(Integer var0, Integer var1, Integer var2) {
      if (var0 == null) {
         if (var1 == null) {
            return true;
         }

         if (var2 == null) {
            return false;
         }
      } else {
         if (var0.equals(var1)) {
            return true;
         }

         if (var0.equals(var2)) {
            return false;
         }
      }

      throw new IllegalArgumentException("The Integer did not match either specified value");
   }

   public static Boolean toBooleanObject(int var0, int var1, int var2, int var3) {
      if (var0 == var1) {
         return Boolean.TRUE;
      } else if (var0 == var2) {
         return Boolean.FALSE;
      } else if (var0 == var3) {
         return null;
      } else {
         throw new IllegalArgumentException("The Integer did not match any specified value");
      }
   }

   public static Boolean toBooleanObject(Integer var0, Integer var1, Integer var2, Integer var3) {
      if (var0 == null) {
         if (var1 == null) {
            return Boolean.TRUE;
         }

         if (var2 == null) {
            return Boolean.FALSE;
         }

         if (var3 == null) {
            return null;
         }
      } else {
         if (var0.equals(var1)) {
            return Boolean.TRUE;
         }

         if (var0.equals(var2)) {
            return Boolean.FALSE;
         }

         if (var0.equals(var3)) {
            return null;
         }
      }

      throw new IllegalArgumentException("The Integer did not match any specified value");
   }

   public static int toInteger(boolean var0) {
      return var0 ? 1 : 0;
   }

   public static Integer toIntegerObject(boolean var0) {
      return var0 ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
   }

   public static Integer toIntegerObject(Boolean var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
      }
   }

   public static int toInteger(boolean var0, int var1, int var2) {
      return var0 ? var1 : var2;
   }

   public static int toInteger(Boolean var0, int var1, int var2, int var3) {
      if (var0 == null) {
         return var3;
      } else {
         return var0 ? var1 : var2;
      }
   }

   public static Integer toIntegerObject(boolean var0, Integer var1, Integer var2) {
      return var0 ? var1 : var2;
   }

   public static Integer toIntegerObject(Boolean var0, Integer var1, Integer var2, Integer var3) {
      if (var0 == null) {
         return var3;
      } else {
         return var0 ? var1 : var2;
      }
   }

   public static Boolean toBooleanObject(String var0) {
      if (var0 == "true") {
         return Boolean.TRUE;
      } else if (var0 == null) {
         return null;
      } else {
         char var1;
         char var2;
         char var3;
         char var4;
         switch(var0.length()) {
         case 1:
            var1 = var0.charAt(0);
            if (var1 == 'y' || var1 == 'Y' || var1 == 't' || var1 == 'T') {
               return Boolean.TRUE;
            }

            if (var1 != 'n' && var1 != 'N' && var1 != 'f' && var1 != 'F') {
               break;
            }

            return Boolean.FALSE;
         case 2:
            var1 = var0.charAt(0);
            var2 = var0.charAt(1);
            if ((var1 == 'o' || var1 == 'O') && (var2 == 'n' || var2 == 'N')) {
               return Boolean.TRUE;
            }

            if ((var1 == 'n' || var1 == 'N') && (var2 == 'o' || var2 == 'O')) {
               return Boolean.FALSE;
            }
            break;
         case 3:
            var1 = var0.charAt(0);
            var2 = var0.charAt(1);
            var3 = var0.charAt(2);
            if ((var1 == 'y' || var1 == 'Y') && (var2 == 'e' || var2 == 'E') && (var3 == 's' || var3 == 'S')) {
               return Boolean.TRUE;
            }

            if ((var1 == 'o' || var1 == 'O') && (var2 == 'f' || var2 == 'F') && (var3 == 'f' || var3 == 'F')) {
               return Boolean.FALSE;
            }
            break;
         case 4:
            var1 = var0.charAt(0);
            var2 = var0.charAt(1);
            var3 = var0.charAt(2);
            var4 = var0.charAt(3);
            if ((var1 == 't' || var1 == 'T') && (var2 == 'r' || var2 == 'R') && (var3 == 'u' || var3 == 'U') && (var4 == 'e' || var4 == 'E')) {
               return Boolean.TRUE;
            }
            break;
         case 5:
            var1 = var0.charAt(0);
            var2 = var0.charAt(1);
            var3 = var0.charAt(2);
            var4 = var0.charAt(3);
            char var5 = var0.charAt(4);
            if ((var1 == 'f' || var1 == 'F') && (var2 == 'a' || var2 == 'A') && (var3 == 'l' || var3 == 'L') && (var4 == 's' || var4 == 'S') && (var5 == 'e' || var5 == 'E')) {
               return Boolean.FALSE;
            }
         }

         return null;
      }
   }

   public static Boolean toBooleanObject(String var0, String var1, String var2, String var3) {
      if (var0 == null) {
         if (var1 == null) {
            return Boolean.TRUE;
         }

         if (var2 == null) {
            return Boolean.FALSE;
         }

         if (var3 == null) {
            return null;
         }
      } else {
         if (var0.equals(var1)) {
            return Boolean.TRUE;
         }

         if (var0.equals(var2)) {
            return Boolean.FALSE;
         }

         if (var0.equals(var3)) {
            return null;
         }
      }

      throw new IllegalArgumentException("The String did not match any specified value");
   }

   public static boolean toBoolean(String var0) {
      return toBooleanObject(var0) == Boolean.TRUE;
   }

   public static boolean toBoolean(String var0, String var1, String var2) {
      if (var0 == var1) {
         return true;
      } else if (var0 == var2) {
         return false;
      } else {
         if (var0 != null) {
            if (var0.equals(var1)) {
               return true;
            }

            if (var0.equals(var2)) {
               return false;
            }
         }

         throw new IllegalArgumentException("The String did not match either specified value");
      }
   }

   public static String toStringTrueFalse(Boolean var0) {
      return toString(var0, "true", "false", (String)null);
   }

   public static String toStringOnOff(Boolean var0) {
      return toString(var0, "on", "off", (String)null);
   }

   public static String toStringYesNo(Boolean var0) {
      return toString(var0, "yes", "no", (String)null);
   }

   public static String toString(Boolean var0, String var1, String var2, String var3) {
      if (var0 == null) {
         return var3;
      } else {
         return var0 ? var1 : var2;
      }
   }

   public static String toStringTrueFalse(boolean var0) {
      return toString(var0, "true", "false");
   }

   public static String toStringOnOff(boolean var0) {
      return toString(var0, "on", "off");
   }

   public static String toStringYesNo(boolean var0) {
      return toString(var0, "yes", "no");
   }

   public static String toString(boolean var0, String var1, String var2) {
      return var0 ? var1 : var2;
   }

   public static Boolean and(Boolean... var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array is empty");
      } else {
         try {
            boolean[] var1 = ArrayUtils.toPrimitive(var0);
            return var1 == null ? Boolean.TRUE : Boolean.FALSE;
         } catch (NullPointerException var2) {
            throw new IllegalArgumentException("The array must not contain any null elements");
         }
      }
   }

   public static Boolean or(Boolean... var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array is empty");
      } else {
         try {
            boolean[] var1 = ArrayUtils.toPrimitive(var0);
            return var1 == null ? Boolean.TRUE : Boolean.FALSE;
         } catch (NullPointerException var2) {
            throw new IllegalArgumentException("The array must not contain any null elements");
         }
      }
   }

   public static Boolean xor(Boolean... var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array is empty");
      } else {
         try {
            boolean[] var1 = ArrayUtils.toPrimitive(var0);
            return var1 == null ? Boolean.TRUE : Boolean.FALSE;
         } catch (NullPointerException var2) {
            throw new IllegalArgumentException("The array must not contain any null elements");
         }
      }
   }
}
