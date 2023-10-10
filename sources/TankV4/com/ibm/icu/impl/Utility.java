package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeMatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public final class Utility {
   private static final char APOSTROPHE = '\'';
   private static final char BACKSLASH = '\\';
   private static final int MAGIC_UNSIGNED = Integer.MIN_VALUE;
   private static final char ESCAPE = 'ꖥ';
   static final byte ESCAPE_BYTE = -91;
   public static String LINE_SEPARATOR = System.getProperty("line.separator");
   static final char[] HEX_DIGIT = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private static final char[] UNESCAPE_MAP = new char[]{'a', '\u0007', 'b', '\b', 'e', '\u001b', 'f', '\f', 'n', '\n', 'r', '\r', 't', '\t', 'v', '\u000b'};
   static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

   public static final boolean arrayEquals(Object[] var0, Object var1) {
      if (var0 == null) {
         return var1 == null;
      } else if (!(var1 instanceof Object[])) {
         return false;
      } else {
         Object[] var2 = (Object[])((Object[])var1);
         Object[] var10000;
         if (var0.length == var2.length) {
            var10000 = var0;
            boolean var10001 = false;
            if (0 < var0.length) {
               boolean var10003 = true;
               return (boolean)var10000;
            }
         }

         var10000 = false;
         return (boolean)var10000;
      }
   }

   public static final boolean arrayEquals(int[] var0, Object var1) {
      if (var0 == null) {
         return var1 == null;
      } else if (!(var1 instanceof int[])) {
         return false;
      } else {
         int[] var2 = (int[])((int[])var1);
         int[] var10000;
         if (var0.length == var2.length) {
            var10000 = var0;
            boolean var10001 = false;
            if (0 < var0.length) {
               boolean var10003 = true;
               return (boolean)var10000;
            }
         }

         var10000 = (int[])false;
         return (boolean)var10000;
      }
   }

   public static final boolean arrayEquals(double[] var0, Object var1) {
      if (var0 == null) {
         return var1 == null;
      } else if (!(var1 instanceof double[])) {
         return false;
      } else {
         double[] var2 = (double[])((double[])var1);
         double[] var10000;
         if (var0.length == var2.length) {
            var10000 = var0;
            boolean var10001 = false;
            if (0 < var0.length) {
               boolean var10003 = true;
               return (boolean)var10000;
            }
         }

         var10000 = (double[])false;
         return (boolean)var10000;
      }
   }

   public static final boolean arrayEquals(byte[] var0, Object var1) {
      if (var0 == null) {
         return var1 == null;
      } else if (!(var1 instanceof byte[])) {
         return false;
      } else {
         byte[] var2 = (byte[])((byte[])var1);
         byte[] var10000;
         if (var0.length == var2.length) {
            var10000 = var0;
            boolean var10001 = false;
            if (0 < var0.length) {
               boolean var10003 = true;
               return (boolean)var10000;
            }
         }

         var10000 = (byte[])false;
         return (boolean)var10000;
      }
   }

   public static final boolean arrayRegionMatches(char[] var0, int var1, char[] var2, int var3, int var4) {
      int var5 = var1 + var4;
      int var6 = var3 - var1;

      for(int var7 = var1; var7 < var5; ++var7) {
         if (var0[var7] != var2[var7 + var6]) {
            return false;
         }
      }

      return true;
   }

   public static final boolean objectEquals(Object var0, Object var1) {
      return var0 == null ? var1 == null : (var1 == null ? false : var0.equals(var1));
   }

   public static int checkCompare(Comparable var0, Comparable var1) {
      return var0 == null ? (var1 == null ? 0 : -1) : (var1 == null ? 1 : var0.compareTo(var1));
   }

   public static int checkHash(Object var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static final String arrayToRLEString(int[] var0) {
      StringBuilder var1 = new StringBuilder();
      appendInt(var1, var0.length);
      int var2 = var0[0];
      int var3 = 1;

      for(int var4 = 1; var4 < var0.length; ++var4) {
         int var5 = var0[var4];
         if (var5 == var2 && var3 < 65535) {
            ++var3;
         } else {
            encodeRun(var1, (int)var2, var3);
            var2 = var5;
            var3 = 1;
         }
      }

      encodeRun(var1, (int)var2, var3);
      return var1.toString();
   }

   public static final String arrayToRLEString(short[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append((char)(var0.length >> 16));
      var1.append((char)var0.length);
      short var2 = var0[0];
      int var3 = 1;

      for(int var4 = 1; var4 < var0.length; ++var4) {
         short var5 = var0[var4];
         if (var5 == var2 && var3 < 65535) {
            ++var3;
         } else {
            encodeRun(var1, (short)var2, var3);
            var2 = var5;
            var3 = 1;
         }
      }

      encodeRun(var1, (short)var2, var3);
      return var1.toString();
   }

   public static final String arrayToRLEString(char[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append((char)(var0.length >> 16));
      var1.append((char)var0.length);
      char var2 = var0[0];
      int var3 = 1;

      for(int var4 = 1; var4 < var0.length; ++var4) {
         char var5 = var0[var4];
         if (var5 == var2 && var3 < 65535) {
            ++var3;
         } else {
            encodeRun(var1, (short)((short)var2), var3);
            var2 = var5;
            var3 = 1;
         }
      }

      encodeRun(var1, (short)((short)var2), var3);
      return var1.toString();
   }

   public static final String arrayToRLEString(byte[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append((char)(var0.length >> 16));
      var1.append((char)var0.length);
      byte var2 = var0[0];
      int var3 = 1;
      byte[] var4 = new byte[2];

      for(int var5 = 1; var5 < var0.length; ++var5) {
         byte var6 = var0[var5];
         if (var6 == var2 && var3 < 255) {
            ++var3;
         } else {
            encodeRun(var1, var2, var3, var4);
            var2 = var6;
            var3 = 1;
         }
      }

      encodeRun(var1, var2, var3, var4);
      if (var4[0] != 0) {
         appendEncodedByte(var1, (byte)0, var4);
      }

      return var1.toString();
   }

   private static final void encodeRun(Appendable var0, int var1, int var2) {
      if (var2 < 4) {
         for(int var3 = 0; var3 < var2; ++var3) {
            if (var1 == 42405) {
               appendInt(var0, var1);
            }

            appendInt(var0, var1);
         }
      } else {
         if (var2 == 42405) {
            if (var1 == 42405) {
               appendInt(var0, 42405);
            }

            appendInt(var0, var1);
            --var2;
         }

         appendInt(var0, 42405);
         appendInt(var0, var2);
         appendInt(var0, var1);
      }

   }

   private static final void appendInt(Appendable var0, int var1) {
      try {
         var0.append((char)(var1 >>> 16));
         var0.append((char)(var1 & '\uffff'));
      } catch (IOException var3) {
         throw new IllegalIcuArgumentException(var3);
      }
   }

   private static final void encodeRun(Appendable var0, short var1, int var2) {
      try {
         if (var2 < 4) {
            for(int var3 = 0; var3 < var2; ++var3) {
               if (var1 == 'ꖥ') {
                  var0.append('ꖥ');
               }

               var0.append((char)var1);
            }
         } else {
            if (var2 == 42405) {
               if (var1 == 'ꖥ') {
                  var0.append('ꖥ');
               }

               var0.append((char)var1);
               --var2;
            }

            var0.append('ꖥ');
            var0.append((char)var2);
            var0.append((char)var1);
         }

      } catch (IOException var4) {
         throw new IllegalIcuArgumentException(var4);
      }
   }

   private static final void encodeRun(Appendable var0, byte var1, int var2, byte[] var3) {
      if (var2 < 4) {
         for(int var4 = 0; var4 < var2; ++var4) {
            if (var1 == -91) {
               appendEncodedByte(var0, (byte)-91, var3);
            }

            appendEncodedByte(var0, var1, var3);
         }
      } else {
         if (var2 == -91) {
            if (var1 == -91) {
               appendEncodedByte(var0, (byte)-91, var3);
            }

            appendEncodedByte(var0, var1, var3);
            --var2;
         }

         appendEncodedByte(var0, (byte)-91, var3);
         appendEncodedByte(var0, (byte)var2, var3);
         appendEncodedByte(var0, var1, var3);
      }

   }

   private static final void appendEncodedByte(Appendable var0, byte var1, byte[] var2) {
      try {
         if (var2[0] != 0) {
            char var3 = (char)(var2[1] << 8 | var1 & 255);
            var0.append(var3);
            var2[0] = 0;
         } else {
            var2[0] = 1;
            var2[1] = var1;
         }

      } catch (IOException var4) {
         throw new IllegalIcuArgumentException(var4);
      }
   }

   public static final int[] RLEStringToIntArray(String var0) {
      int var1 = getInt(var0, 0);
      int[] var2 = new int[var1];
      int var3 = 0;
      int var4 = 1;
      int var5 = var0.length() / 2;

      while(var3 < var1 && var4 < var5) {
         int var6 = getInt(var0, var4++);
         if (var6 == 42405) {
            var6 = getInt(var0, var4++);
            if (var6 == 42405) {
               var2[var3++] = var6;
            } else {
               int var7 = var6;
               int var8 = getInt(var0, var4++);

               for(int var9 = 0; var9 < var7; ++var9) {
                  var2[var3++] = var8;
               }
            }
         } else {
            var2[var3++] = var6;
         }
      }

      if (var3 == var1 && var4 == var5) {
         return var2;
      } else {
         throw new IllegalStateException("Bad run-length encoded int array");
      }
   }

   static final int getInt(String var0, int var1) {
      return var0.charAt(2 * var1) << 16 | var0.charAt(2 * var1 + 1);
   }

   public static final short[] RLEStringToShortArray(String var0) {
      int var1 = var0.charAt(0) << 16 | var0.charAt(1);
      short[] var2 = new short[var1];
      int var3 = 0;

      for(int var4 = 2; var4 < var0.length(); ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 == 'ꖥ') {
            ++var4;
            var5 = var0.charAt(var4);
            if (var5 == 'ꖥ') {
               var2[var3++] = (short)var5;
            } else {
               char var6 = var5;
               ++var4;
               short var7 = (short)var0.charAt(var4);

               for(int var8 = 0; var8 < var6; ++var8) {
                  var2[var3++] = var7;
               }
            }
         } else {
            var2[var3++] = (short)var5;
         }
      }

      if (var3 != var1) {
         throw new IllegalStateException("Bad run-length encoded short array");
      } else {
         return var2;
      }
   }

   public static final char[] RLEStringToCharArray(String var0) {
      int var1 = var0.charAt(0) << 16 | var0.charAt(1);
      char[] var2 = new char[var1];
      int var3 = 0;

      for(int var4 = 2; var4 < var0.length(); ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 == 'ꖥ') {
            ++var4;
            var5 = var0.charAt(var4);
            if (var5 == 'ꖥ') {
               var2[var3++] = var5;
            } else {
               char var6 = var5;
               ++var4;
               char var7 = var0.charAt(var4);

               for(int var8 = 0; var8 < var6; ++var8) {
                  var2[var3++] = var7;
               }
            }
         } else {
            var2[var3++] = var5;
         }
      }

      if (var3 != var1) {
         throw new IllegalStateException("Bad run-length encoded short array");
      } else {
         return var2;
      }
   }

   public static final byte[] RLEStringToByteArray(String var0) {
      int var1 = var0.charAt(0) << 16 | var0.charAt(1);
      byte[] var2 = new byte[var1];
      boolean var3 = true;
      char var4 = 0;
      byte var5 = 0;
      int var6 = 0;
      int var7 = 2;
      int var8 = 0;

      while(true) {
         while(var8 < var1) {
            byte var9;
            if (var3) {
               var4 = var0.charAt(var7++);
               var9 = (byte)(var4 >> 8);
               var3 = false;
            } else {
               var9 = (byte)(var4 & 255);
               var3 = true;
            }

            switch(var5) {
            case 0:
               if (var9 == -91) {
                  var5 = 1;
               } else {
                  var2[var8++] = var9;
               }
               break;
            case 1:
               if (var9 == -91) {
                  var2[var8++] = -91;
                  var5 = 0;
               } else {
                  var6 = var9;
                  if (var9 < 0) {
                     var6 = var9 + 256;
                  }

                  var5 = 2;
               }
               break;
            case 2:
               for(int var10 = 0; var10 < var6; ++var10) {
                  var2[var8++] = var9;
               }

               var5 = 0;
            }
         }

         if (var5 != 0) {
            throw new IllegalStateException("Bad run-length encoded byte array");
         }

         if (var7 != var0.length()) {
            throw new IllegalStateException("Excess data in RLE byte array string");
         }

         return var2;
      }
   }

   public static final String formatForSource(String var0) {
      StringBuilder var1 = new StringBuilder();

      label50:
      for(int var2 = 0; var2 < var0.length(); var1.append('"')) {
         if (var2 > 0) {
            var1.append('+').append(LINE_SEPARATOR);
         }

         var1.append("        \"");
         int var3 = 11;

         while(true) {
            while(true) {
               if (var2 >= var0.length() || var3 >= 80) {
                  continue label50;
               }

               char var4 = var0.charAt(var2++);
               if (var4 >= ' ' && var4 != '"' && var4 != '\\') {
                  if (var4 <= '~') {
                     var1.append(var4);
                     ++var3;
                  } else {
                     var1.append("\\u");
                     var1.append(HEX_DIGIT[(var4 & '\uf000') >> 12]);
                     var1.append(HEX_DIGIT[(var4 & 3840) >> 8]);
                     var1.append(HEX_DIGIT[(var4 & 240) >> 4]);
                     var1.append(HEX_DIGIT[var4 & 15]);
                     var3 += 6;
                  }
               } else if (var4 == '\n') {
                  var1.append("\\n");
                  var3 += 2;
               } else if (var4 == '\t') {
                  var1.append("\\t");
                  var3 += 2;
               } else if (var4 == '\r') {
                  var1.append("\\r");
                  var3 += 2;
               } else {
                  var1.append('\\');
                  var1.append(HEX_DIGIT[(var4 & 448) >> 6]);
                  var1.append(HEX_DIGIT[(var4 & 56) >> 3]);
                  var1.append(HEX_DIGIT[var4 & 7]);
                  var3 += 4;
               }
            }
         }
      }

      return var1.toString();
   }

   public static final String format1ForSource(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("\"");
      int var2 = 0;

      while(true) {
         while(var2 < var0.length()) {
            char var3 = var0.charAt(var2++);
            if (var3 >= ' ' && var3 != '"' && var3 != '\\') {
               if (var3 <= '~') {
                  var1.append(var3);
               } else {
                  var1.append("\\u");
                  var1.append(HEX_DIGIT[(var3 & '\uf000') >> 12]);
                  var1.append(HEX_DIGIT[(var3 & 3840) >> 8]);
                  var1.append(HEX_DIGIT[(var3 & 240) >> 4]);
                  var1.append(HEX_DIGIT[var3 & 15]);
               }
            } else if (var3 == '\n') {
               var1.append("\\n");
            } else if (var3 == '\t') {
               var1.append("\\t");
            } else if (var3 == '\r') {
               var1.append("\\r");
            } else {
               var1.append('\\');
               var1.append(HEX_DIGIT[(var3 & 448) >> 6]);
               var1.append(HEX_DIGIT[(var3 & 56) >> 3]);
               var1.append(HEX_DIGIT[var3 & 7]);
            }
         }

         var1.append('"');
         return var1.toString();
      }
   }

   public static final String escape(String var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      while(true) {
         while(var2 < var0.length()) {
            int var3 = Character.codePointAt(var0, var2);
            var2 += UTF16.getCharCount(var3);
            if (var3 >= 32 && var3 <= 127) {
               if (var3 == 92) {
                  var1.append("\\\\");
               } else {
                  var1.append((char)var3);
               }
            } else {
               boolean var4 = var3 <= 65535;
               var1.append(var4 ? "\\u" : "\\U");
               var1.append(hex((long)var3, var4 ? 4 : 8));
            }
         }

         return var1.toString();
      }
   }

   public static int unescapeAt(String var0, int[] var1) {
      int var3 = 0;
      int var4 = 0;
      byte var5 = 0;
      byte var6 = 0;
      byte var7 = 4;
      boolean var10 = false;
      int var11 = var1[0];
      int var12 = var0.length();
      if (var11 >= 0 && var11 < var12) {
         int var2 = Character.codePointAt(var0, var11);
         var11 += UTF16.getCharCount(var2);
         int var8;
         switch(var2) {
         case 85:
            var6 = 8;
            var5 = 8;
            break;
         case 117:
            var6 = 4;
            var5 = 4;
            break;
         case 120:
            var5 = 1;
            if (var11 < var12 && UTF16.charAt(var0, var11) == 123) {
               ++var11;
               var10 = true;
               var6 = 8;
            } else {
               var6 = 2;
            }
            break;
         default:
            var8 = UCharacter.digit(var2, 8);
            if (var8 >= 0) {
               var5 = 1;
               var6 = 3;
               var4 = 1;
               var7 = 3;
               var3 = var8;
            }
         }

         if (var5 != 0) {
            while(var11 < var12 && var4 < var6) {
               var2 = UTF16.charAt(var0, var11);
               var8 = UCharacter.digit(var2, var7 == 3 ? 8 : 16);
               if (var8 < 0) {
                  break;
               }

               var3 = var3 << var7 | var8;
               var11 += UTF16.getCharCount(var2);
               ++var4;
            }

            if (var4 < var5) {
               return -1;
            } else {
               if (var10) {
                  if (var2 != 125) {
                     return -1;
                  }

                  ++var11;
               }

               if (var3 >= 0 && var3 < 1114112) {
                  if (var11 < var12 && UTF16.isLeadSurrogate((char)var3)) {
                     int var13 = var11 + 1;
                     var2 = var0.charAt(var11);
                     if (var2 == 92 && var13 < var12) {
                        int[] var14 = new int[]{var13};
                        var2 = unescapeAt(var0, var14);
                        var13 = var14[0];
                     }

                     if (UTF16.isTrailSurrogate((char)var2)) {
                        var11 = var13;
                        var3 = UCharacterProperty.getRawSupplementary((char)var3, (char)var2);
                     }
                  }

                  var1[0] = var11;
                  return var3;
               } else {
                  return -1;
               }
            }
         } else {
            for(int var9 = 0; var9 < UNESCAPE_MAP.length; var9 += 2) {
               if (var2 == UNESCAPE_MAP[var9]) {
                  var1[0] = var11;
                  return UNESCAPE_MAP[var9 + 1];
               }

               if (var2 < UNESCAPE_MAP[var9]) {
                  break;
               }
            }

            if (var2 == 99 && var11 < var12) {
               var2 = UTF16.charAt(var0, var11);
               var1[0] = var11 + UTF16.getCharCount(var2);
               return 31 & var2;
            } else {
               var1[0] = var11;
               return var2;
            }
         }
      } else {
         return -1;
      }
   }

   public static String unescape(String var0) {
      StringBuilder var1 = new StringBuilder();
      int[] var2 = new int[1];
      int var3 = 0;

      while(var3 < var0.length()) {
         char var4 = var0.charAt(var3++);
         if (var4 == '\\') {
            var2[0] = var3;
            int var5 = unescapeAt(var0, var2);
            if (var5 < 0) {
               throw new IllegalArgumentException("Invalid escape sequence " + var0.substring(var3 - 1, Math.min(var3 + 8, var0.length())));
            }

            var1.appendCodePoint(var5);
            var3 = var2[0];
         } else {
            var1.append(var4);
         }
      }

      return var1.toString();
   }

   public static String unescapeLeniently(String var0) {
      StringBuilder var1 = new StringBuilder();
      int[] var2 = new int[1];
      int var3 = 0;

      while(var3 < var0.length()) {
         char var4 = var0.charAt(var3++);
         if (var4 == '\\') {
            var2[0] = var3;
            int var5 = unescapeAt(var0, var2);
            if (var5 < 0) {
               var1.append(var4);
            } else {
               var1.appendCodePoint(var5);
               var3 = var2[0];
            }
         } else {
            var1.append(var4);
         }
      }

      return var1.toString();
   }

   public static String hex(long var0) {
      return hex(var0, 4);
   }

   public static String hex(long var0, int var2) {
      if (var0 == Long.MIN_VALUE) {
         return "-8000000000000000";
      } else {
         boolean var3 = var0 < 0L;
         if (var3) {
            var0 = -var0;
         }

         String var4 = Long.toString(var0, 16).toUpperCase(Locale.ENGLISH);
         if (var4.length() < var2) {
            var4 = "0000000000000000".substring(var4.length(), var2) + var4;
         }

         return var3 ? '-' + var4 : var4;
      }
   }

   public static String hex(CharSequence var0) {
      return ((StringBuilder)hex(var0, 4, ",", true, new StringBuilder())).toString();
   }

   public static Appendable hex(CharSequence var0, int var1, CharSequence var2, boolean var3, Appendable var4) {
      try {
         int var5;
         if (var3) {
            for(int var6 = 0; var6 < var0.length(); var6 += UTF16.getCharCount(var5)) {
               var5 = Character.codePointAt(var0, var6);
               if (var6 != 0) {
                  var4.append(var2);
               }

               var4.append(hex((long)var5, var1));
            }
         } else {
            for(var5 = 0; var5 < var0.length(); ++var5) {
               if (var5 != 0) {
                  var4.append(var2);
               }

               var4.append(hex((long)var0.charAt(var5), var1));
            }
         }

         return var4;
      } catch (IOException var7) {
         throw new IllegalIcuArgumentException(var7);
      }
   }

   public static String hex(byte[] var0, int var1, int var2, String var3) {
      StringBuilder var4 = new StringBuilder();

      for(int var5 = var1; var5 < var2; ++var5) {
         if (var5 != 0) {
            var4.append(var3);
         }

         var4.append(hex((long)var0[var5]));
      }

      return var4.toString();
   }

   public static String hex(CharSequence var0, int var1, CharSequence var2) {
      return ((StringBuilder)hex(var0, var1, var2, true, new StringBuilder())).toString();
   }

   public static void split(String var0, char var1, String[] var2) {
      int var3 = 0;
      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < var0.length(); ++var5) {
         if (var0.charAt(var5) == var1) {
            var2[var4++] = var0.substring(var3, var5);
            var3 = var5 + 1;
         }
      }

      for(var2[var4++] = var0.substring(var3, var5); var4 < var2.length; var2[var4++] = "") {
      }

   }

   public static String[] split(String var0, char var1) {
      int var2 = 0;
      ArrayList var4 = new ArrayList();

      int var3;
      for(var3 = 0; var3 < var0.length(); ++var3) {
         if (var0.charAt(var3) == var1) {
            var4.add(var0.substring(var2, var3));
            var2 = var3 + 1;
         }
      }

      var4.add(var0.substring(var2, var3));
      return (String[])var4.toArray(new String[var4.size()]);
   }

   public static int lookup(String var0, String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var0.equals(var1[var2])) {
            return var2;
         }
      }

      return -1;
   }

   public static boolean parseChar(String var0, int[] var1, char var2) {
      int var3 = var1[0];
      var1[0] = PatternProps.skipWhiteSpace(var0, var1[0]);
      if (var1[0] != var0.length() && var0.charAt(var1[0]) == var2) {
         int var10002 = var1[0]++;
         return true;
      } else {
         var1[0] = var3;
         return false;
      }
   }

   public static int parsePattern(String var0, int var1, int var2, String var3, int[] var4) {
      int[] var5 = new int[1];
      int var6 = 0;

      for(int var7 = 0; var7 < var3.length(); ++var7) {
         char var8 = var3.charAt(var7);
         char var9;
         switch(var8) {
         case ' ':
            if (var1 >= var2) {
               return -1;
            }

            var9 = var0.charAt(var1++);
            if (!PatternProps.isWhiteSpace(var9)) {
               return -1;
            }
         case '~':
            var1 = PatternProps.skipWhiteSpace(var0, var1);
            break;
         case '#':
            var5[0] = var1;
            var4[var6++] = parseInteger(var0, var5, var2);
            if (var5[0] == var1) {
               return -1;
            }

            var1 = var5[0];
            break;
         default:
            if (var1 >= var2) {
               return -1;
            }

            var9 = (char)UCharacter.toLowerCase(var0.charAt(var1++));
            if (var9 != var8) {
               return -1;
            }
         }
      }

      return var1;
   }

   public static int parsePattern(String var0, Replaceable var1, int var2, int var3) {
      int var4 = 0;
      if (var4 == var0.length()) {
         return var2;
      } else {
         int var5 = Character.codePointAt(var0, var4);

         while(true) {
            while(var2 < var3) {
               int var6 = var1.char32At(var2);
               if (var5 == 126) {
                  if (PatternProps.isWhiteSpace(var6)) {
                     var2 += UTF16.getCharCount(var6);
                     continue;
                  }

                  ++var4;
                  if (var4 == var0.length()) {
                     return var2;
                  }
               } else {
                  if (var6 != var5) {
                     return -1;
                  }

                  int var7 = UTF16.getCharCount(var6);
                  var2 += var7;
                  var4 += var7;
                  if (var4 == var0.length()) {
                     return var2;
                  }
               }

               var5 = UTF16.charAt(var0, var4);
            }

            return -1;
         }
      }
   }

   public static int parseInteger(String var0, int[] var1, int var2) {
      int var3 = 0;
      int var4 = 0;
      int var5 = var1[0];
      byte var6 = 10;
      if (var0.regionMatches(true, var5, "0x", 0, 2)) {
         var5 += 2;
         var6 = 16;
      } else if (var5 < var2 && var0.charAt(var5) == '0') {
         ++var5;
         var3 = 1;
         var6 = 8;
      }

      while(var5 < var2) {
         int var7 = UCharacter.digit(var0.charAt(var5++), var6);
         if (var7 < 0) {
            --var5;
            break;
         }

         ++var3;
         int var8 = var4 * var6 + var7;
         if (var8 <= var4) {
            return 0;
         }

         var4 = var8;
      }

      if (var3 > 0) {
         var1[0] = var5;
      }

      return var4;
   }

   public static String parseUnicodeIdentifier(String var0, int[] var1) {
      StringBuilder var2 = new StringBuilder();

      int var3;
      int var4;
      for(var3 = var1[0]; var3 < var0.length(); var3 += UTF16.getCharCount(var4)) {
         var4 = Character.codePointAt(var0, var3);
         if (var2.length() == 0) {
            if (!UCharacter.isUnicodeIdentifierStart(var4)) {
               return null;
            }

            var2.appendCodePoint(var4);
         } else {
            if (!UCharacter.isUnicodeIdentifierPart(var4)) {
               break;
            }

            var2.appendCodePoint(var4);
         }
      }

      var1[0] = var3;
      return var2.toString();
   }

   private static void recursiveAppendNumber(Appendable var0, int var1, int var2, int var3) {
      try {
         int var4 = var1 % var2;
         if (var1 >= var2 || var3 > 1) {
            recursiveAppendNumber(var0, var1 / var2, var2, var3 - 1);
         }

         var0.append(DIGITS[var4]);
      } catch (IOException var5) {
         throw new IllegalIcuArgumentException(var5);
      }
   }

   public static Appendable appendNumber(Appendable var0, int var1, int var2, int var3) {
      try {
         if (var2 >= 2 && var2 <= 36) {
            int var4 = var1;
            if (var1 < 0) {
               var4 = -var1;
               var0.append("-");
            }

            recursiveAppendNumber(var0, var4, var2, var3);
            return var0;
         } else {
            throw new IllegalArgumentException("Illegal radix " + var2);
         }
      } catch (IOException var5) {
         throw new IllegalIcuArgumentException(var5);
      }
   }

   public static int parseNumber(String var0, int[] var1, int var2) {
      int var3 = 0;

      int var4;
      for(var4 = var1[0]; var4 < var0.length(); ++var4) {
         int var5 = Character.codePointAt(var0, var4);
         int var6 = UCharacter.digit(var5, var2);
         if (var6 < 0) {
            break;
         }

         var3 = var2 * var3 + var6;
         if (var3 < 0) {
            return -1;
         }
      }

      if (var4 == var1[0]) {
         return -1;
      } else {
         var1[0] = var4;
         return var3;
      }
   }

   public static int quotedIndexOf(String var0, int var1, int var2, String var3) {
      for(int var4 = var1; var4 < var2; ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 == '\\') {
            ++var4;
         } else if (var5 == '\'') {
            do {
               ++var4;
            } while(var4 < var2 && var0.charAt(var4) != '\'');
         } else if (var3.indexOf(var5) >= 0) {
            return var4;
         }
      }

      return -1;
   }

   public static void appendToRule(StringBuffer param0, int param1, boolean param2, boolean param3, StringBuffer param4) {
      // $FF: Couldn't be decompiled
   }

   public static void appendToRule(StringBuffer var0, String var1, boolean var2, boolean var3, StringBuffer var4) {
      for(int var5 = 0; var5 < var1.length(); ++var5) {
         appendToRule(var0, var1.charAt(var5), var2, var3, var4);
      }

   }

   public static void appendToRule(StringBuffer var0, UnicodeMatcher var1, boolean var2, StringBuffer var3) {
      if (var1 != null) {
         appendToRule(var0, var1.toPattern(var2), true, var2, var3);
      }

   }

   public static final int compareUnsigned(int var0, int var1) {
      var0 -= Integer.MIN_VALUE;
      var1 -= Integer.MIN_VALUE;
      if (var0 < var1) {
         return -1;
      } else {
         return var0 > var1 ? 1 : 0;
      }
   }

   public static final byte highBit(int var0) {
      if (var0 <= 0) {
         return -1;
      } else {
         byte var1 = 0;
         if (var0 >= 65536) {
            var0 >>= 16;
            var1 = (byte)(var1 + 16);
         }

         if (var0 >= 256) {
            var0 >>= 8;
            var1 = (byte)(var1 + 8);
         }

         if (var0 >= 16) {
            var0 >>= 4;
            var1 = (byte)(var1 + 4);
         }

         if (var0 >= 4) {
            var0 >>= 2;
            var1 = (byte)(var1 + 2);
         }

         if (var0 >= 2) {
            var0 >>= 1;
            ++var1;
         }

         return var1;
      }
   }

   public static String valueOf(int[] var0) {
      StringBuilder var1 = new StringBuilder(var0.length);

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.appendCodePoint(var0[var2]);
      }

      return var1.toString();
   }

   public static String repeat(String var0, int var1) {
      if (var1 <= 0) {
         return "";
      } else if (var1 == 1) {
         return var0;
      } else {
         StringBuilder var2 = new StringBuilder();

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append(var0);
         }

         return var2.toString();
      }
   }

   public static String[] splitString(String var0, String var1) {
      return var0.split("\\Q" + var1 + "\\E");
   }

   public static String[] splitWhitespace(String var0) {
      return var0.split("\\s+");
   }

   public static String fromHex(String var0, int var1, String var2) {
      return fromHex(var0, var1, Pattern.compile(var2 != null ? var2 : "\\s+"));
   }

   public static String fromHex(String var0, int var1, Pattern var2) {
      StringBuilder var3 = new StringBuilder();
      String[] var4 = var2.split(var0);
      String[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         if (var8.length() < var1) {
            throw new IllegalArgumentException("code point too short: " + var8);
         }

         int var9 = Integer.parseInt(var8, 16);
         var3.appendCodePoint(var9);
      }

      return var3.toString();
   }

   public static ClassLoader getFallbackClassLoader() {
      ClassLoader var0 = Thread.currentThread().getContextClassLoader();
      if (var0 == null) {
         var0 = ClassLoader.getSystemClassLoader();
         if (var0 == null) {
            throw new RuntimeException("No accessible class loader is available.");
         }
      }

      return var0;
   }
}
