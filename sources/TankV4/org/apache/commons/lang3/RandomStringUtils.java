package org.apache.commons.lang3;

import java.util.Random;

public class RandomStringUtils {
   private static final Random RANDOM = new Random();

   public static String random(int var0) {
      return random(var0, false, false);
   }

   public static String randomAscii(int var0) {
      return random(var0, 32, 127, false, false);
   }

   public static String randomAlphabetic(int var0) {
      return random(var0, true, false);
   }

   public static String randomAlphanumeric(int var0) {
      return random(var0, true, true);
   }

   public static String randomNumeric(int var0) {
      return random(var0, false, true);
   }

   public static String random(int var0, boolean var1, boolean var2) {
      return random(var0, 0, 0, var1, var2);
   }

   public static String random(int var0, int var1, int var2, boolean var3, boolean var4) {
      return random(var0, var1, var2, var3, var4, (char[])null, RANDOM);
   }

   public static String random(int var0, int var1, int var2, boolean var3, boolean var4, char... var5) {
      return random(var0, var1, var2, var3, var4, var5, RANDOM);
   }

   public static String random(int var0, int var1, int var2, boolean var3, boolean var4, char[] var5, Random var6) {
      if (var0 == 0) {
         return "";
      } else if (var0 < 0) {
         throw new IllegalArgumentException("Requested random string length " + var0 + " is less than 0.");
      } else if (var5 != null && var5.length == 0) {
         throw new IllegalArgumentException("The chars array must not be empty");
      } else {
         if (var1 == 0 && var2 == 0) {
            if (var5 != null) {
               var2 = var5.length;
            } else if (!var3 && !var4) {
               var2 = Integer.MAX_VALUE;
            } else {
               var2 = 123;
               var1 = 32;
            }
         } else if (var2 <= var1) {
            throw new IllegalArgumentException("Parameter end (" + var2 + ") must be greater than start (" + var1 + ")");
         }

         char[] var7 = new char[var0];
         int var8 = var2 - var1;

         while(true) {
            while(true) {
               while(var0-- != 0) {
                  char var9;
                  if (var5 == null) {
                     var9 = (char)(var6.nextInt(var8) + var1);
                  } else {
                     var9 = var5[var6.nextInt(var8) + var1];
                  }

                  if (var3 && Character.isLetter(var9) || var4 && Character.isDigit(var9) || !var3 && !var4) {
                     if (var9 >= '\udc00' && var9 <= '\udfff') {
                        if (var0 == 0) {
                           ++var0;
                        } else {
                           var7[var0] = var9;
                           --var0;
                           var7[var0] = (char)('\ud800' + var6.nextInt(128));
                        }
                     } else if (var9 >= '\ud800' && var9 <= '\udb7f') {
                        if (var0 == 0) {
                           ++var0;
                        } else {
                           var7[var0] = (char)('\udc00' + var6.nextInt(128));
                           --var0;
                           var7[var0] = var9;
                        }
                     } else if (var9 >= '\udb80' && var9 <= '\udbff') {
                        ++var0;
                     } else {
                        var7[var0] = var9;
                     }
                  } else {
                     ++var0;
                  }
               }

               return new String(var7);
            }
         }
      }
   }

   public static String random(int var0, String var1) {
      return var1 == null ? random(var0, 0, 0, false, false, (char[])null, RANDOM) : random(var0, var1.toCharArray());
   }

   public static String random(int var0, char... var1) {
      return var1 == null ? random(var0, 0, 0, false, false, (char[])null, RANDOM) : random(var0, 0, var1.length, false, false, var1, RANDOM);
   }
}
