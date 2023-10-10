package org.apache.commons.lang3.text;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class WordUtils {
   public static String wrap(String var0, int var1) {
      return wrap(var0, var1, (String)null, false);
   }

   public static String wrap(String var0, int var1, String var2, boolean var3) {
      if (var0 == null) {
         return null;
      } else {
         if (var2 == null) {
            var2 = SystemUtils.LINE_SEPARATOR;
         }

         if (var1 < 1) {
            var1 = 1;
         }

         int var4 = var0.length();
         int var5 = 0;
         StringBuilder var6 = new StringBuilder(var4 + 32);

         while(var4 - var5 > var1) {
            if (var0.charAt(var5) == ' ') {
               ++var5;
            } else {
               int var7 = var0.lastIndexOf(32, var1 + var5);
               if (var7 >= var5) {
                  var6.append(var0.substring(var5, var7));
                  var6.append(var2);
                  var5 = var7 + 1;
               } else if (var3) {
                  var6.append(var0.substring(var5, var1 + var5));
                  var6.append(var2);
                  var5 += var1;
               } else {
                  var7 = var0.indexOf(32, var1 + var5);
                  if (var7 >= 0) {
                     var6.append(var0.substring(var5, var7));
                     var6.append(var2);
                     var5 = var7 + 1;
                  } else {
                     var6.append(var0.substring(var5));
                     var5 = var4;
                  }
               }
            }
         }

         var6.append(var0.substring(var5));
         return var6.toString();
      }
   }

   public static String capitalize(String var0) {
      return capitalize(var0, (char[])null);
   }

   public static String capitalize(String var0, char... var1) {
      int var2 = var1 == null ? -1 : var1.length;
      if (!StringUtils.isEmpty(var0) && var2 != 0) {
         char[] var3 = var0.toCharArray();
         boolean var4 = true;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            char var6 = var3[var5];
            if (var1 == null) {
               var4 = true;
            } else if (var4) {
               var3[var5] = Character.toTitleCase(var6);
               var4 = false;
            }
         }

         return new String(var3);
      } else {
         return var0;
      }
   }

   public static String capitalizeFully(String var0) {
      return capitalizeFully(var0, (char[])null);
   }

   public static String capitalizeFully(String var0, char... var1) {
      int var2 = var1 == null ? -1 : var1.length;
      if (!StringUtils.isEmpty(var0) && var2 != 0) {
         var0 = var0.toLowerCase();
         return capitalize(var0, var1);
      } else {
         return var0;
      }
   }

   public static String uncapitalize(String var0) {
      return uncapitalize(var0, (char[])null);
   }

   public static String uncapitalize(String var0, char... var1) {
      int var2 = var1 == null ? -1 : var1.length;
      if (!StringUtils.isEmpty(var0) && var2 != 0) {
         char[] var3 = var0.toCharArray();
         boolean var4 = true;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            char var6 = var3[var5];
            if (var1 == null) {
               var4 = true;
            } else if (var4) {
               var3[var5] = Character.toLowerCase(var6);
               var4 = false;
            }
         }

         return new String(var3);
      } else {
         return var0;
      }
   }

   public static String swapCase(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         char[] var1 = var0.toCharArray();
         boolean var2 = true;

         for(int var3 = 0; var3 < var1.length; ++var3) {
            char var4 = var1[var3];
            if (Character.isUpperCase(var4)) {
               var1[var3] = Character.toLowerCase(var4);
               var2 = false;
            } else if (Character.isTitleCase(var4)) {
               var1[var3] = Character.toLowerCase(var4);
               var2 = false;
            } else if (Character.isLowerCase(var4)) {
               if (var2) {
                  var1[var3] = Character.toTitleCase(var4);
                  var2 = false;
               } else {
                  var1[var3] = Character.toUpperCase(var4);
               }
            } else {
               var2 = Character.isWhitespace(var4);
            }
         }

         return new String(var1);
      }
   }

   public static String initials(String var0) {
      return initials(var0, (char[])null);
   }

   public static String initials(String var0, char... var1) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else if (var1 != null && var1.length == 0) {
         return "";
      } else {
         int var2 = var0.length();
         char[] var3 = new char[var2 / 2 + 1];
         int var4 = 0;
         boolean var5 = true;

         for(int var6 = 0; var6 < var2; ++var6) {
            char var7 = var0.charAt(var6);
            if (var1 == null) {
               var5 = true;
            } else if (var5) {
               var3[var4++] = var7;
               var5 = false;
            }
         }

         return new String(var3, 0, var4);
      }
   }
}
