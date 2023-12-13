package com.ibm.icu.impl;

import com.ibm.icu.text.StringPrep;
import com.ibm.icu.text.StringPrepParseException;
import com.ibm.icu.text.UCharacterIterator;

public final class IDNA2003 {
   private static char[] ACE_PREFIX = new char[]{'x', 'n', '-', '-'};
   private static final int MAX_LABEL_LENGTH = 63;
   private static final int HYPHEN = 45;
   private static final int CAPITAL_A = 65;
   private static final int CAPITAL_Z = 90;
   private static final int LOWER_CASE_DELTA = 32;
   private static final int FULL_STOP = 46;
   private static final int MAX_DOMAIN_NAME_LENGTH = 255;
   private static final StringPrep namePrep = StringPrep.getInstance(0);

   private static char toASCIILower(char var0) {
      return 'A' <= var0 && var0 <= 'Z' ? (char)(var0 + 32) : var0;
   }

   private static StringBuffer toASCIILower(CharSequence var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         var1.append(toASCIILower(var0.charAt(var2)));
      }

      return var1;
   }

   private static int compareCaseInsensitiveASCII(StringBuffer var0, StringBuffer var1) {
      for(int var5 = 0; var5 != var0.length(); ++var5) {
         char var2 = var0.charAt(var5);
         char var3 = var1.charAt(var5);
         if (var2 != var3) {
            int var4 = toASCIILower(var2) - toASCIILower(var3);
            if (var4 != 0) {
               return var4;
            }
         }
      }

      return 0;
   }

   private static int getSeparatorIndex(char[] var0, int var1, int var2) {
      while(var1 < var2) {
         if (isLabelSeparator(var0[var1])) {
            return var1;
         }

         ++var1;
      }

      return var1;
   }

   private static boolean isLabelSeparator(int var0) {
      switch(var0) {
      case 46:
      case 12290:
      case 65294:
      case 65377:
         return true;
      default:
         return false;
      }
   }

   public static StringBuffer convertToASCII(UCharacterIterator param0, int param1) throws StringPrepParseException {
      // $FF: Couldn't be decompiled
   }

   public static StringBuffer convertIDNToASCII(String var0, int var1) throws StringPrepParseException {
      char[] var2 = var0.toCharArray();
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;
      int var5 = 0;

      while(true) {
         var4 = getSeparatorIndex(var2, var4, var2.length);
         String var6 = new String(var2, var5, var4 - var5);
         if (var6.length() != 0 || var4 != var2.length) {
            UCharacterIterator var7 = UCharacterIterator.getInstance(var6);
            var3.append(convertToASCII(var7, var1));
         }

         if (var4 == var2.length) {
            if (var3.length() > 255) {
               throw new StringPrepParseException("The output exceed the max allowed length.", 11);
            }

            return var3;
         }

         ++var4;
         var5 = var4;
         var3.append('.');
      }
   }

   public static StringBuffer convertToUnicode(UCharacterIterator param0, int param1) throws StringPrepParseException {
      // $FF: Couldn't be decompiled
   }

   public static StringBuffer convertIDNToUnicode(String var0, int var1) throws StringPrepParseException {
      char[] var2 = var0.toCharArray();
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;
      int var5 = 0;

      while(true) {
         var4 = getSeparatorIndex(var2, var4, var2.length);
         String var6 = new String(var2, var5, var4 - var5);
         if (var6.length() == 0 && var4 != var2.length) {
            throw new StringPrepParseException("Found zero length lable after NamePrep.", 10);
         }

         UCharacterIterator var7 = UCharacterIterator.getInstance(var6);
         var3.append(convertToUnicode(var7, var1));
         if (var4 == var2.length) {
            if (var3.length() > 255) {
               throw new StringPrepParseException("The output exceed the max allowed length.", 11);
            }

            return var3;
         }

         var3.append(var2[var4]);
         ++var4;
         var5 = var4;
      }
   }

   public static int compare(String var0, String var1, int var2) throws StringPrepParseException {
      StringBuffer var3 = convertIDNToASCII(var0, var2);
      StringBuffer var4 = convertIDNToASCII(var1, var2);
      return compareCaseInsensitiveASCII(var3, var4);
   }
}
