package com.ibm.icu.impl.duration.impl;

import java.util.Locale;

public class Utils {
   public static final Locale localeFromString(String var0) {
      String var1 = var0;
      String var2 = "";
      String var3 = "";
      int var4 = var0.indexOf("_");
      if (var4 != -1) {
         var2 = var0.substring(var4 + 1);
         var1 = var0.substring(0, var4);
      }

      var4 = var2.indexOf("_");
      if (var4 != -1) {
         var3 = var2.substring(var4 + 1);
         var2 = var2.substring(0, var4);
      }

      return new Locale(var1, var2, var3);
   }

   public static String chineseNumber(long var0, Utils.ChineseDigits var2) {
      if (var0 < 0L) {
         var0 = -var0;
      }

      if (var0 <= 10L) {
         return var0 == 2L ? String.valueOf(var2.liang) : String.valueOf(var2.digits[(int)var0]);
      } else {
         char[] var3 = new char[40];
         char[] var4 = String.valueOf(var0).toCharArray();
         boolean var5 = true;
         boolean var6 = false;
         int var7 = var3.length;
         int var8 = var4.length;
         int var9 = -1;
         int var10 = -1;

         while(true) {
            --var8;
            int var11;
            if (var8 < 0) {
               if (var0 > 1000000L) {
                  boolean var13 = true;
                  var9 = var3.length - 3;

                  while(var3[var9] != '0') {
                     var9 -= 8;
                     var13 = !var13;
                     if (var9 <= var7) {
                        break;
                     }
                  }

                  var9 = var3.length - 7;

                  do {
                     if (var3[var9] == var2.digits[0] && !var13) {
                        var3[var9] = '*';
                     }

                     var9 -= 8;
                     var13 = !var13;
                  } while(var9 > var7);

                  if (var0 >= 100000000L) {
                     var9 = var3.length - 8;

                     do {
                        boolean var14 = true;
                        var11 = var9 - 1;

                        for(int var12 = Math.max(var7 - 1, var9 - 8); var11 > var12; --var11) {
                           if (var3[var11] != '*') {
                              var14 = false;
                              break;
                           }
                        }

                        if (var14) {
                           if (var3[var9 + 1] != '*' && var3[var9 + 1] != var2.digits[0]) {
                              var3[var9] = var2.digits[0];
                           } else {
                              var3[var9] = '*';
                           }
                        }

                        var9 -= 8;
                     } while(var9 > var7);
                  }
               }

               for(var8 = var7; var8 < var3.length; ++var8) {
                  if (var3[var8] == var2.digits[2] && (var8 >= var3.length - 1 || var3[var8 + 1] != var2.units[0]) && (var8 <= var7 || var3[var8 - 1] != var2.units[0] && var3[var8 - 1] != var2.digits[0] && var3[var8 - 1] != '*')) {
                     var3[var8] = var2.liang;
                  }
               }

               if (var3[var7] == var2.digits[1] && (var2.ko || var3[var7 + 1] == var2.units[0])) {
                  ++var7;
               }

               var8 = var7;

               for(var9 = var7; var9 < var3.length; ++var9) {
                  if (var3[var9] != '*') {
                     var3[var8++] = var3[var9];
                  }
               }

               return new String(var3, var7, var8 - var7);
            }

            if (var9 == -1) {
               if (var10 != -1) {
                  --var7;
                  var3[var7] = var2.levels[var10];
                  var5 = true;
                  var6 = false;
               }

               ++var9;
            } else {
               --var7;
               var3[var7] = var2.units[var9++];
               if (var9 == 3) {
                  var9 = -1;
                  ++var10;
               }
            }

            var11 = var4[var8] - 48;
            if (var11 == 0) {
               if (var7 < var3.length - 1 && var9 != 0) {
                  var3[var7] = '*';
               }

               if (!var5 && !var6) {
                  --var7;
                  var3[var7] = var2.digits[0];
                  var5 = true;
                  var6 = var9 == 1;
               } else {
                  --var7;
                  var3[var7] = '*';
               }
            } else {
               var5 = false;
               --var7;
               var3[var7] = var2.digits[var11];
            }
         }
      }
   }

   public static class ChineseDigits {
      final char[] digits;
      final char[] units;
      final char[] levels;
      final char liang;
      final boolean ko;
      public static final Utils.ChineseDigits DEBUG = new Utils.ChineseDigits("0123456789s", "sbq", "WYZ", 'L', false);
      public static final Utils.ChineseDigits TRADITIONAL = new Utils.ChineseDigits("零一二三四五六七八九十", "十百千", "萬億兆", '兩', false);
      public static final Utils.ChineseDigits SIMPLIFIED = new Utils.ChineseDigits("零一二三四五六七八九十", "十百千", "万亿兆", '两', false);
      public static final Utils.ChineseDigits KOREAN = new Utils.ChineseDigits("영일이삼사오육칠팔구십", "십백천", "만억?", '이', true);

      ChineseDigits(String var1, String var2, String var3, char var4, boolean var5) {
         this.digits = var1.toCharArray();
         this.units = var2.toCharArray();
         this.levels = var3.toCharArray();
         this.liang = var4;
         this.ko = var5;
      }
   }
}
