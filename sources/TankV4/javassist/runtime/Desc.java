package javassist.runtime;

public class Desc {
   public static boolean useContextClassLoader = false;

   private static Class getClassObject(String var0) throws ClassNotFoundException {
      return useContextClassLoader ? Class.forName(var0, true, Thread.currentThread().getContextClassLoader()) : Class.forName(var0);
   }

   public static Class getClazz(String var0) {
      try {
         return getClassObject(var0);
      } catch (ClassNotFoundException var2) {
         throw new RuntimeException("$class: internal error, could not find class '" + var0 + "' (Desc.useContextClassLoader: " + Boolean.toString(useContextClassLoader) + ")", var2);
      }
   }

   public static Class[] getParams(String var0) {
      if (var0.charAt(0) != '(') {
         throw new RuntimeException("$sig: internal error");
      } else {
         return getType(var0, var0.length(), 1, 0);
      }
   }

   public static Class getType(String var0) {
      Class[] var1 = getType(var0, var0.length(), 0, 0);
      if (var1 != null && var1.length == 1) {
         return var1[0];
      } else {
         throw new RuntimeException("$type: internal error");
      }
   }

   private static Class[] getType(String var0, int var1, int var2, int var3) {
      if (var2 >= var1) {
         return new Class[var3];
      } else {
         char var5 = var0.charAt(var2);
         Class var4;
         switch(var5) {
         case 'B':
            var4 = Byte.TYPE;
            break;
         case 'C':
            var4 = Character.TYPE;
            break;
         case 'D':
            var4 = Double.TYPE;
            break;
         case 'E':
         case 'G':
         case 'H':
         case 'K':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'W':
         case 'X':
         case 'Y':
         default:
            return new Class[var3];
         case 'F':
            var4 = Float.TYPE;
            break;
         case 'I':
            var4 = Integer.TYPE;
            break;
         case 'J':
            var4 = Long.TYPE;
            break;
         case 'L':
         case '[':
            return getClassType(var0, var1, var2, var3);
         case 'S':
            var4 = Short.TYPE;
            break;
         case 'V':
            var4 = Void.TYPE;
            break;
         case 'Z':
            var4 = Boolean.TYPE;
         }

         Class[] var6 = getType(var0, var1, var2 + 1, var3 + 1);
         var6[var3] = var4;
         return var6;
      }
   }

   private static Class[] getClassType(String var0, int var1, int var2, int var3) {
      int var4;
      for(var4 = var2; var0.charAt(var4) == '['; ++var4) {
      }

      if (var0.charAt(var4) == 'L') {
         var4 = var0.indexOf(59, var4);
         if (var4 < 0) {
            throw new IndexOutOfBoundsException("bad descriptor");
         }
      }

      String var5;
      if (var0.charAt(var2) == 'L') {
         var5 = var0.substring(var2 + 1, var4);
      } else {
         var5 = var0.substring(var2, var4 + 1);
      }

      Class[] var6 = getType(var0, var1, var4 + 1, var3 + 1);

      try {
         var6[var3] = getClassObject(var5.replace('/', '.'));
         return var6;
      } catch (ClassNotFoundException var8) {
         throw new RuntimeException(var8.getMessage());
      }
   }
}
