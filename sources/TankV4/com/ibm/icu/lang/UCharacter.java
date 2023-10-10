package com.ibm.icu.lang;

import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Trie2;
import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.impl.UCharacterName;
import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.impl.UPropertyAliases;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.util.RangeValueIterator;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.ValueIterator;
import com.ibm.icu.util.VersionInfo;
import java.lang.Character.Subset;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public final class UCharacter implements UCharacterEnums.ECharacterCategory, UCharacterEnums.ECharacterDirection {
   public static final int MIN_VALUE = 0;
   public static final int MAX_VALUE = 1114111;
   public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
   public static final int REPLACEMENT_CHAR = 65533;
   public static final double NO_NUMERIC_VALUE = -1.23456789E8D;
   public static final int MIN_RADIX = 2;
   public static final int MAX_RADIX = 36;
   public static final int TITLECASE_NO_LOWERCASE = 256;
   public static final int TITLECASE_NO_BREAK_ADJUSTMENT = 512;
   public static final int FOLD_CASE_DEFAULT = 0;
   public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
   public static final char MIN_HIGH_SURROGATE = '\ud800';
   public static final char MAX_HIGH_SURROGATE = '\udbff';
   public static final char MIN_LOW_SURROGATE = '\udc00';
   public static final char MAX_LOW_SURROGATE = '\udfff';
   public static final char MIN_SURROGATE = '\ud800';
   public static final char MAX_SURROGATE = '\udfff';
   public static final int MIN_SUPPLEMENTARY_CODE_POINT = 65536;
   public static final int MAX_CODE_POINT = 1114111;
   public static final int MIN_CODE_POINT = 0;
   private static final int LAST_CHAR_MASK_ = 65535;
   private static final int NO_BREAK_SPACE_ = 160;
   private static final int FIGURE_SPACE_ = 8199;
   private static final int NARROW_NO_BREAK_SPACE_ = 8239;
   private static final int IDEOGRAPHIC_NUMBER_ZERO_ = 12295;
   private static final int CJK_IDEOGRAPH_FIRST_ = 19968;
   private static final int CJK_IDEOGRAPH_SECOND_ = 20108;
   private static final int CJK_IDEOGRAPH_THIRD_ = 19977;
   private static final int CJK_IDEOGRAPH_FOURTH_ = 22235;
   private static final int CJK_IDEOGRAPH_FIFTH_ = 20116;
   private static final int CJK_IDEOGRAPH_SIXTH_ = 20845;
   private static final int CJK_IDEOGRAPH_SEVENTH_ = 19971;
   private static final int CJK_IDEOGRAPH_EIGHTH_ = 20843;
   private static final int CJK_IDEOGRAPH_NINETH_ = 20061;
   private static final int APPLICATION_PROGRAM_COMMAND_ = 159;
   private static final int UNIT_SEPARATOR_ = 31;
   private static final int DELETE_ = 127;
   private static final int CJK_IDEOGRAPH_COMPLEX_ZERO_ = 38646;
   private static final int CJK_IDEOGRAPH_COMPLEX_ONE_ = 22777;
   private static final int CJK_IDEOGRAPH_COMPLEX_TWO_ = 36019;
   private static final int CJK_IDEOGRAPH_COMPLEX_THREE_ = 21443;
   private static final int CJK_IDEOGRAPH_COMPLEX_FOUR_ = 32902;
   private static final int CJK_IDEOGRAPH_COMPLEX_FIVE_ = 20237;
   private static final int CJK_IDEOGRAPH_COMPLEX_SIX_ = 38520;
   private static final int CJK_IDEOGRAPH_COMPLEX_SEVEN_ = 26578;
   private static final int CJK_IDEOGRAPH_COMPLEX_EIGHT_ = 25420;
   private static final int CJK_IDEOGRAPH_COMPLEX_NINE_ = 29590;
   private static final int CJK_IDEOGRAPH_TEN_ = 21313;
   private static final int CJK_IDEOGRAPH_COMPLEX_TEN_ = 25342;
   private static final int CJK_IDEOGRAPH_HUNDRED_ = 30334;
   private static final int CJK_IDEOGRAPH_COMPLEX_HUNDRED_ = 20336;
   private static final int CJK_IDEOGRAPH_THOUSAND_ = 21315;
   private static final int CJK_IDEOGRAPH_COMPLEX_THOUSAND_ = 20191;
   private static final int CJK_IDEOGRAPH_TEN_THOUSAND_ = 33356;
   private static final int CJK_IDEOGRAPH_HUNDRED_MILLION_ = 20740;

   public static int digit(int var0, int var1) {
      if (2 <= var1 && var1 <= 36) {
         int var2 = digit(var0);
         if (var2 < 0) {
            var2 = UCharacterProperty.getEuropeanDigit(var0);
         }

         return var2 < var1 ? var2 : -1;
      } else {
         return -1;
      }
   }

   public static int digit(int var0) {
      return UCharacterProperty.INSTANCE.digit(var0);
   }

   public static int getNumericValue(int var0) {
      return UCharacterProperty.INSTANCE.getNumericValue(var0);
   }

   public static double getUnicodeNumericValue(int var0) {
      return UCharacterProperty.INSTANCE.getUnicodeNumericValue(var0);
   }

   /** @deprecated */
   public static boolean isSpace(int var0) {
      return var0 <= 32 && (var0 == 32 || var0 == 9 || var0 == 10 || var0 == 12 || var0 == 13);
   }

   public static int getType(int var0) {
      return UCharacterProperty.INSTANCE.getType(var0);
   }

   public static boolean isDefined(int var0) {
      return getType(var0) != 0;
   }

   public static boolean isDigit(int var0) {
      return getType(var0) == 9;
   }

   public static boolean isLetter(int var0) {
      return (1 << getType(var0) & 62) != 0;
   }

   public static boolean isLetterOrDigit(int var0) {
      return (1 << getType(var0) & 574) != 0;
   }

   /** @deprecated */
   public static boolean isJavaLetter(int var0) {
      return isJavaIdentifierStart(var0);
   }

   /** @deprecated */
   public static boolean isJavaLetterOrDigit(int var0) {
      return isJavaIdentifierPart(var0);
   }

   public static boolean isJavaIdentifierStart(int var0) {
      return Character.isJavaIdentifierStart((char)var0);
   }

   public static boolean isJavaIdentifierPart(int var0) {
      return Character.isJavaIdentifierPart((char)var0);
   }

   public static boolean isLowerCase(int var0) {
      return getType(var0) == 2;
   }

   public static boolean isWhitespace(int var0) {
      return (1 << getType(var0) & 28672) != 0 && var0 != 160 && var0 != 8199 && var0 != 8239 || var0 >= 9 && var0 <= 13 || var0 >= 28 && var0 <= 31;
   }

   public static boolean isSpaceChar(int var0) {
      return (1 << getType(var0) & 28672) != 0;
   }

   public static boolean isTitleCase(int var0) {
      return getType(var0) == 3;
   }

   public static boolean isUnicodeIdentifierPart(int param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean isUnicodeIdentifierStart(int var0) {
      return (1 << getType(var0) & 1086) != 0;
   }

   public static boolean isUpperCase(int var0) {
      return getType(var0) == 1;
   }

   public static int toLowerCase(int var0) {
      return UCaseProps.INSTANCE.tolower(var0);
   }

   public static String toString(int var0) {
      if (var0 >= 0 && var0 <= 1114111) {
         if (var0 < 65536) {
            return String.valueOf((char)var0);
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append(UTF16.getLeadSurrogate(var0));
            var1.append(UTF16.getTrailSurrogate(var0));
            return var1.toString();
         }
      } else {
         return null;
      }
   }

   public static int toTitleCase(int var0) {
      return UCaseProps.INSTANCE.totitle(var0);
   }

   public static int toUpperCase(int var0) {
      return UCaseProps.INSTANCE.toupper(var0);
   }

   public static boolean isBMP(int var0) {
      return var0 >= 0 && var0 <= 65535;
   }

   public static boolean isPrintable(int var0) {
      int var1 = getType(var0);
      return var1 != 0 && var1 != 15 && var1 != 16 && var1 != 17 && var1 != 18 && var1 != 0;
   }

   public static boolean isBaseForm(int var0) {
      int var1 = getType(var0);
      return var1 == 9 || var1 == 11 || var1 == 10 || var1 == 1 || var1 == 2 || var1 == 3 || var1 == 4 || var1 == 5 || var1 == 6 || var1 == 7 || var1 == 8;
   }

   public static int getDirection(int var0) {
      return UBiDiProps.INSTANCE.getClass(var0);
   }

   public static boolean isMirrored(int var0) {
      return UBiDiProps.INSTANCE.isMirrored(var0);
   }

   public static int getMirror(int var0) {
      return UBiDiProps.INSTANCE.getMirror(var0);
   }

   public static int getCombiningClass(int var0) {
      return Norm2AllModes.getNFCInstance().decomp.getCombiningClass(var0);
   }

   public static boolean isLegal(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static VersionInfo getUnicodeVersion() {
      return UCharacterProperty.INSTANCE.m_unicodeVersion_;
   }

   public static String getName(int var0) {
      return UCharacterName.INSTANCE.getName(var0, 0);
   }

   public static String getName(String var0, String var1) {
      if (var0.length() == 1) {
         return getName(var0.charAt(0));
      } else {
         StringBuilder var3 = new StringBuilder();

         int var2;
         for(int var4 = 0; var4 < var0.length(); var4 += UTF16.getCharCount(var2)) {
            var2 = UTF16.charAt(var0, var4);
            if (var4 != 0) {
               var3.append(var1);
            }

            var3.append(getName(var2));
         }

         return var3.toString();
      }
   }

   /** @deprecated */
   public static String getName1_0(int var0) {
      return null;
   }

   public static String getExtendedName(int var0) {
      return UCharacterName.INSTANCE.getName(var0, 2);
   }

   public static String getNameAlias(int var0) {
      return UCharacterName.INSTANCE.getName(var0, 3);
   }

   /** @deprecated */
   public static String getISOComment(int var0) {
      return null;
   }

   public static int getCharFromName(String var0) {
      return UCharacterName.INSTANCE.getCharFromName(0, var0);
   }

   /** @deprecated */
   public static int getCharFromName1_0(String var0) {
      return -1;
   }

   public static int getCharFromExtendedName(String var0) {
      return UCharacterName.INSTANCE.getCharFromName(2, var0);
   }

   public static int getCharFromNameAlias(String var0) {
      return UCharacterName.INSTANCE.getCharFromName(3, var0);
   }

   public static String getPropertyName(int var0, int var1) {
      return UPropertyAliases.INSTANCE.getPropertyName(var0, var1);
   }

   public static int getPropertyEnum(CharSequence var0) {
      int var1 = UPropertyAliases.INSTANCE.getPropertyEnum(var0);
      if (var1 == -1) {
         throw new IllegalIcuArgumentException("Invalid name: " + var0);
      } else {
         return var1;
      }
   }

   public static String getPropertyValueName(int var0, int var1, int var2) {
      if ((var0 == 4098 || var0 == 4112 || var0 == 4113) && var1 >= getIntPropertyMinValue(4098) && var1 <= getIntPropertyMaxValue(4098) && var2 >= 0 && var2 < 2) {
         try {
            return UPropertyAliases.INSTANCE.getPropertyValueName(var0, var1, var2);
         } catch (IllegalArgumentException var4) {
            return null;
         }
      } else {
         return UPropertyAliases.INSTANCE.getPropertyValueName(var0, var1, var2);
      }
   }

   public static int getPropertyValueEnum(int var0, CharSequence var1) {
      int var2 = UPropertyAliases.INSTANCE.getPropertyValueEnum(var0, var1);
      if (var2 == -1) {
         throw new IllegalIcuArgumentException("Invalid name: " + var1);
      } else {
         return var2;
      }
   }

   public static int getCodePoint(char var0, char var1) {
      if (UTF16.isLeadSurrogate(var0) && UTF16.isTrailSurrogate(var1)) {
         return UCharacterProperty.getRawSupplementary(var0, var1);
      } else {
         throw new IllegalArgumentException("Illegal surrogate characters");
      }
   }

   public static int getCodePoint(char var0) {
      if (var0 < 0) {
         return var0;
      } else {
         throw new IllegalArgumentException("Illegal codepoint");
      }
   }

   public static String toUpperCase(String var0) {
      return toUpperCase(ULocale.getDefault(), var0);
   }

   public static String toLowerCase(String var0) {
      return toLowerCase(ULocale.getDefault(), var0);
   }

   public static String toTitleCase(String var0, BreakIterator var1) {
      return toTitleCase(ULocale.getDefault(), var0, var1);
   }

   public static String toUpperCase(Locale var0, String var1) {
      return toUpperCase(ULocale.forLocale(var0), var1);
   }

   public static String toUpperCase(ULocale var0, String var1) {
      UCharacter.StringContextIterator var2 = new UCharacter.StringContextIterator(var1);
      StringBuilder var3 = new StringBuilder(var1.length());
      int[] var4 = new int[1];
      if (var0 == null) {
         var0 = ULocale.getDefault();
      }

      var4[0] = 0;

      while(true) {
         int var5;
         do {
            if ((var5 = var2.nextCaseMapCP()) < 0) {
               return var3.toString();
            }

            var5 = UCaseProps.INSTANCE.toFullUpper(var5, var2, var3, var0, var4);
            if (var5 < 0) {
               var5 = ~var5;
               break;
            }
         } while(var5 <= 31);

         var3.appendCodePoint(var5);
      }
   }

   public static String toLowerCase(Locale var0, String var1) {
      return toLowerCase(ULocale.forLocale(var0), var1);
   }

   public static String toLowerCase(ULocale var0, String var1) {
      UCharacter.StringContextIterator var2 = new UCharacter.StringContextIterator(var1);
      StringBuilder var3 = new StringBuilder(var1.length());
      int[] var4 = new int[1];
      if (var0 == null) {
         var0 = ULocale.getDefault();
      }

      var4[0] = 0;

      while(true) {
         int var5;
         do {
            if ((var5 = var2.nextCaseMapCP()) < 0) {
               return var3.toString();
            }

            var5 = UCaseProps.INSTANCE.toFullLower(var5, var2, var3, var0, var4);
            if (var5 < 0) {
               var5 = ~var5;
               break;
            }
         } while(var5 <= 31);

         var3.appendCodePoint(var5);
      }
   }

   public static String toTitleCase(Locale var0, String var1, BreakIterator var2) {
      return toTitleCase(ULocale.forLocale(var0), var1, var2);
   }

   public static String toTitleCase(ULocale var0, String var1, BreakIterator var2) {
      return toTitleCase(var0, var1, var2, 0);
   }

   public static String toTitleCase(ULocale var0, String var1, BreakIterator var2, int var3) {
      UCharacter.StringContextIterator var4 = new UCharacter.StringContextIterator(var1);
      StringBuilder var5 = new StringBuilder(var1.length());
      int[] var6 = new int[1];
      int var9 = var1.length();
      if (var0 == null) {
         var0 = ULocale.getDefault();
      }

      var6[0] = 0;
      if (var2 == null) {
         var2 = BreakIterator.getWordInstance(var0);
      }

      var2.setText(var1);
      boolean var14 = var0.getLanguage().equals("nl");
      boolean var15 = true;
      int var10 = 0;

      int var12;
      label107:
      for(boolean var13 = true; var10 < var9; var10 = var12) {
         if (var13) {
            var13 = false;
            var12 = var2.first();
         } else {
            var12 = var2.next();
         }

         if (var12 == -1 || var12 > var9) {
            var12 = var9;
         }

         if (var10 < var12) {
            var4.setLimit(var12);
            int var7 = var4.nextCaseMapCP();
            int var11;
            if ((var3 & 512) == 0 && 0 == UCaseProps.INSTANCE.getType(var7)) {
               while((var7 = var4.nextCaseMapCP()) >= 0 && 0 == UCaseProps.INSTANCE.getType(var7)) {
               }

               var11 = var4.getCPStart();
               if (var10 < var11) {
                  var5.append(var1, var10, var11);
               }
            } else {
               var11 = var10;
            }

            if (var11 < var12) {
               var15 = true;
               var7 = UCaseProps.INSTANCE.toFullTitle(var7, var4, var5, var0, var6);

               while(true) {
                  while(true) {
                     if (var7 < 0) {
                        var7 = ~var7;
                        var5.appendCodePoint(var7);
                     } else if (var7 > 31) {
                        var5.appendCodePoint(var7);
                     }

                     if ((var3 & 256) != 0) {
                        int var16 = var4.getCPLimit();
                        if (var16 < var12) {
                           String var17 = var1.substring(var16, var12);
                           if (var14 && var7 == 73 && var17.startsWith("j")) {
                              var17 = "J" + var17.substring(1);
                           }

                           var5.append(var17);
                        }

                        var4.moveToLimit();
                        continue label107;
                     }

                     int var8;
                     if ((var8 = var4.nextCaseMapCP()) < 0) {
                        continue label107;
                     }

                     if (var14 && (var8 == 74 || var8 == 106) && var7 == 73 && var15) {
                        var7 = 74;
                        var15 = false;
                     } else {
                        var7 = UCaseProps.INSTANCE.toFullLower(var8, var4, var5, var0, var6);
                     }
                  }
               }
            }
         }
      }

      return var5.toString();
   }

   public static int foldCase(int var0, boolean var1) {
      return foldCase(var0, var1 ? 0 : 1);
   }

   public static String foldCase(String var0, boolean var1) {
      return foldCase(var0, var1 ? 0 : 1);
   }

   public static int foldCase(int var0, int var1) {
      return UCaseProps.INSTANCE.fold(var0, var1);
   }

   public static final String foldCase(String var0, int var1) {
      StringBuilder var2 = new StringBuilder(var0.length());
      int var5 = var0.length();
      int var4 = 0;

      while(true) {
         int var3;
         do {
            if (var4 >= var5) {
               return var2.toString();
            }

            var3 = UTF16.charAt(var0, var4);
            var4 += UTF16.getCharCount(var3);
            var3 = UCaseProps.INSTANCE.toFullFolding(var3, var2, var1);
            if (var3 < 0) {
               var3 = ~var3;
               break;
            }
         } while(var3 <= 31);

         var2.appendCodePoint(var3);
      }
   }

   public static int getHanNumericValue(int var0) {
      switch(var0) {
      case 12295:
      case 38646:
         return 0;
      case 19968:
      case 22777:
         return 1;
      case 19971:
      case 26578:
         return 7;
      case 19977:
      case 21443:
         return 3;
      case 20061:
      case 29590:
         return 9;
      case 20108:
      case 36019:
         return 2;
      case 20116:
      case 20237:
         return 5;
      case 20191:
      case 21315:
         return 1000;
      case 20336:
      case 30334:
         return 100;
      case 20740:
         return 100000000;
      case 20843:
      case 25420:
         return 8;
      case 20845:
      case 38520:
         return 6;
      case 21313:
      case 25342:
         return 10;
      case 22235:
      case 32902:
         return 4;
      case 33356:
         return 10000;
      default:
         return -1;
      }
   }

   public static RangeValueIterator getTypeIterator() {
      return new UCharacter.UCharacterTypeIterator();
   }

   public static ValueIterator getNameIterator() {
      return new UCharacterNameIterator(UCharacterName.INSTANCE, 0);
   }

   /** @deprecated */
   public static ValueIterator getName1_0Iterator() {
      return new UCharacter.DummyValueIterator();
   }

   public static ValueIterator getExtendedNameIterator() {
      return new UCharacterNameIterator(UCharacterName.INSTANCE, 2);
   }

   public static VersionInfo getAge(int var0) {
      if (var0 >= 0 && var0 <= 1114111) {
         return UCharacterProperty.INSTANCE.getAge(var0);
      } else {
         throw new IllegalArgumentException("Codepoint out of bounds");
      }
   }

   public static boolean hasBinaryProperty(int var0, int var1) {
      return UCharacterProperty.INSTANCE.hasBinaryProperty(var0, var1);
   }

   public static boolean isUAlphabetic(int var0) {
      return hasBinaryProperty(var0, 0);
   }

   public static boolean isULowercase(int var0) {
      return hasBinaryProperty(var0, 22);
   }

   public static boolean isUUppercase(int var0) {
      return hasBinaryProperty(var0, 30);
   }

   public static boolean isUWhiteSpace(int var0) {
      return hasBinaryProperty(var0, 31);
   }

   public static int getIntPropertyValue(int var0, int var1) {
      return UCharacterProperty.INSTANCE.getIntPropertyValue(var0, var1);
   }

   /** @deprecated */
   public static String getStringPropertyValue(int var0, int var1, int var2) {
      if ((var0 < 0 || var0 >= 57) && (var0 < 4096 || var0 >= 4117)) {
         if (var0 == 12288) {
            return String.valueOf(getUnicodeNumericValue(var1));
         } else {
            switch(var0) {
            case 16384:
               return getAge(var1).toString();
            case 16385:
               return UTF16.valueOf(getMirror(var1));
            case 16386:
               return foldCase(UTF16.valueOf(var1), true);
            case 16387:
               return getISOComment(var1);
            case 16388:
               return toLowerCase(UTF16.valueOf(var1));
            case 16389:
               return getName(var1);
            case 16390:
               return UTF16.valueOf(foldCase(var1, true));
            case 16391:
               return UTF16.valueOf(toLowerCase(var1));
            case 16392:
               return UTF16.valueOf(toTitleCase(var1));
            case 16393:
               return UTF16.valueOf(toUpperCase(var1));
            case 16394:
               return toTitleCase(UTF16.valueOf(var1), (BreakIterator)null);
            case 16395:
               return getName1_0(var1);
            case 16396:
               return toUpperCase(UTF16.valueOf(var1));
            default:
               throw new IllegalArgumentException("Illegal Property Enum");
            }
         }
      } else {
         return getPropertyValueName(var0, getIntPropertyValue(var1, var0), var2);
      }
   }

   public static int getIntPropertyMinValue(int var0) {
      return 0;
   }

   public static int getIntPropertyMaxValue(int var0) {
      return UCharacterProperty.INSTANCE.getIntPropertyMaxValue(var0);
   }

   public static char forDigit(int var0, int var1) {
      return Character.forDigit(var0, var1);
   }

   public static final boolean isValidCodePoint(int var0) {
      return var0 >= 0 && var0 <= 1114111;
   }

   public static final boolean isSupplementaryCodePoint(int var0) {
      return var0 >= 65536 && var0 <= 1114111;
   }

   public static final boolean isSurrogatePair(char param0, char param1) {
      // $FF: Couldn't be decompiled
   }

   public static int charCount(int var0) {
      return UTF16.getCharCount(var0);
   }

   public static final int toCodePoint(char var0, char var1) {
      return UCharacterProperty.getRawSupplementary(var0, var1);
   }

   public static final int codePointAt(CharSequence param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static final int codePointAt(char[] param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static final int codePointAt(char[] param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static final int codePointBefore(CharSequence param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static final int codePointBefore(char[] param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static final int codePointBefore(char[] param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static final int toChars(int var0, char[] var1, int var2) {
      if (var0 >= 0) {
         if (var0 < 65536) {
            var1[var2] = (char)var0;
            return 1;
         }

         if (var0 <= 1114111) {
            var1[var2] = UTF16.getLeadSurrogate(var0);
            var1[var2 + 1] = UTF16.getTrailSurrogate(var0);
            return 2;
         }
      }

      throw new IllegalArgumentException();
   }

   public static final char[] toChars(int var0) {
      if (var0 >= 0) {
         if (var0 < 65536) {
            return new char[]{(char)var0};
         }

         if (var0 <= 1114111) {
            return new char[]{UTF16.getLeadSurrogate(var0), UTF16.getTrailSurrogate(var0)};
         }
      }

      throw new IllegalArgumentException();
   }

   public static byte getDirectionality(int var0) {
      return (byte)getDirection(var0);
   }

   public static int codePointCount(CharSequence var0, int var1, int var2) {
      if (var1 >= 0 && var2 >= var1 && var2 <= var0.length()) {
         int var3 = var2 - var1;

         while(true) {
            while(var2 > var1) {
               --var2;
               char var4 = var0.charAt(var2);

               while(var4 >= '\udc00' && var4 <= '\udfff' && var2 > var1) {
                  --var2;
                  var4 = var0.charAt(var2);
                  if (var4 >= '\ud800' && var4 <= '\udbff') {
                     --var3;
                     break;
                  }
               }
            }

            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException("start (" + var1 + ") or limit (" + var2 + ") invalid or out of range 0, " + var0.length());
      }
   }

   public static int codePointCount(char[] var0, int var1, int var2) {
      if (var1 >= 0 && var2 >= var1 && var2 <= var0.length) {
         int var3 = var2 - var1;

         while(true) {
            while(var2 > var1) {
               --var2;
               char var4 = var0[var2];

               while(var4 >= '\udc00' && var4 <= '\udfff' && var2 > var1) {
                  --var2;
                  var4 = var0[var2];
                  if (var4 >= '\ud800' && var4 <= '\udbff') {
                     --var3;
                     break;
                  }
               }
            }

            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException("start (" + var1 + ") or limit (" + var2 + ") invalid or out of range 0, " + var0.length);
      }
   }

   public static int offsetByCodePoints(CharSequence var0, int var1, int var2) {
      if (var1 >= 0 && var1 <= var0.length()) {
         if (var2 >= 0) {
            int var5 = var0.length();

            label50:
            while(true) {
               --var2;
               if (var2 < 0) {
                  break;
               }

               char var4 = var0.charAt(var1++);

               do {
                  do {
                     if (var4 < '\ud800' || var4 > '\udbff' || var1 >= var5) {
                        continue label50;
                     }

                     var4 = var0.charAt(var1++);
                  } while(var4 >= '\udc00' && var4 <= '\udfff');

                  --var2;
               } while(var2 >= 0);

               return var1 - 1;
            }
         } else {
            label71:
            while(true) {
               ++var2;
               if (var2 > 0) {
                  break;
               }

               --var1;
               char var3 = var0.charAt(var1);

               do {
                  do {
                     if (var3 < '\udc00' || var3 > '\udfff' || var1 <= 0) {
                        continue label71;
                     }

                     --var1;
                     var3 = var0.charAt(var1);
                  } while(var3 >= '\ud800' && var3 <= '\udbff');

                  ++var2;
               } while(var2 <= 0);

               return var1 + 1;
            }
         }

         return var1;
      } else {
         throw new IndexOutOfBoundsException("index ( " + var1 + ") out of range 0, " + var0.length());
      }
   }

   public static int offsetByCodePoints(char[] var0, int var1, int var2, int var3, int var4) {
      int var5 = var1 + var2;
      if (var1 >= 0 && var5 >= var1 && var5 <= var0.length && var3 >= var1 && var3 <= var5) {
         char var6;
         if (var4 < 0) {
            label58:
            while(true) {
               ++var4;
               if (var4 > 0) {
                  break;
               }

               --var3;
               var6 = var0[var3];
               if (var3 < var1) {
                  throw new IndexOutOfBoundsException("index ( " + var3 + ") < start (" + var1 + ")");
               }

               do {
                  do {
                     if (var6 < '\udc00' || var6 > '\udfff' || var3 <= var1) {
                        continue label58;
                     }

                     --var3;
                     var6 = var0[var3];
                  } while(var6 >= '\ud800' && var6 <= '\udbff');

                  ++var4;
               } while(var4 <= 0);

               return var3 + 1;
            }
         } else {
            label75:
            while(true) {
               --var4;
               if (var4 < 0) {
                  break;
               }

               var6 = var0[var3++];
               if (var3 > var5) {
                  throw new IndexOutOfBoundsException("index ( " + var3 + ") > limit (" + var5 + ")");
               }

               do {
                  do {
                     if (var6 < '\ud800' || var6 > '\udbff' || var3 >= var5) {
                        continue label75;
                     }

                     var6 = var0[var3++];
                  } while(var6 >= '\udc00' && var6 <= '\udfff');

                  --var4;
               } while(var4 >= 0);

               return var3 - 1;
            }
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("index ( " + var3 + ") out of range " + var1 + ", " + var5 + " in array 0, " + var0.length);
      }
   }

   private UCharacter() {
   }

   private static final class DummyValueIterator implements ValueIterator {
      private DummyValueIterator() {
      }

      public boolean next(ValueIterator.Element var1) {
         return false;
      }

      public void reset() {
      }

      public void setRange(int var1, int var2) {
      }

      DummyValueIterator(Object var1) {
         this();
      }
   }

   private static final class UCharacterTypeIterator implements RangeValueIterator {
      private Iterator trieIterator;
      private Trie2.Range range;
      private static final UCharacter.UCharacterTypeIterator.MaskType MASK_TYPE = new UCharacter.UCharacterTypeIterator.MaskType();

      UCharacterTypeIterator() {
         this.reset();
      }

      public boolean next(RangeValueIterator.Element var1) {
         if (this.trieIterator.hasNext() && !(this.range = (Trie2.Range)this.trieIterator.next()).leadSurrogate) {
            var1.start = this.range.startCodePoint;
            var1.limit = this.range.endCodePoint + 1;
            var1.value = this.range.value;
            return true;
         } else {
            return false;
         }
      }

      public void reset() {
         this.trieIterator = UCharacterProperty.INSTANCE.m_trie_.iterator(MASK_TYPE);
      }

      private static final class MaskType implements Trie2.ValueMapper {
         private MaskType() {
         }

         public int map(int var1) {
            return var1 & 31;
         }

         MaskType(Object var1) {
            this();
         }
      }
   }

   private static class StringContextIterator implements UCaseProps.ContextIterator {
      protected String s;
      protected int index;
      protected int limit;
      protected int cpStart;
      protected int cpLimit;
      protected int dir;

      StringContextIterator(String var1) {
         this.s = var1;
         this.limit = var1.length();
         this.cpStart = this.cpLimit = this.index = 0;
         this.dir = 0;
      }

      public void setLimit(int var1) {
         if (0 <= var1 && var1 <= this.s.length()) {
            this.limit = var1;
         } else {
            this.limit = this.s.length();
         }

      }

      public void moveToLimit() {
         this.cpStart = this.cpLimit = this.limit;
      }

      public int nextCaseMapCP() {
         this.cpStart = this.cpLimit;
         if (this.cpLimit >= this.limit) {
            return -1;
         } else {
            int var1 = this.s.charAt(this.cpLimit++);
            char var2;
            if ((55296 <= var1 || var1 <= 57343) && var1 <= 56319 && this.cpLimit < this.limit && '\udc00' <= (var2 = this.s.charAt(this.cpLimit)) && var2 <= '\udfff') {
               ++this.cpLimit;
               var1 = UCharacterProperty.getRawSupplementary((char)var1, var2);
            }

            return var1;
         }
      }

      public int getCPStart() {
         return this.cpStart;
      }

      public int getCPLimit() {
         return this.cpLimit;
      }

      public void reset(int var1) {
         if (var1 > 0) {
            this.dir = 1;
            this.index = this.cpLimit;
         } else if (var1 < 0) {
            this.dir = -1;
            this.index = this.cpStart;
         } else {
            this.dir = 0;
            this.index = 0;
         }

      }

      public int next() {
         int var1;
         if (this.dir > 0 && this.index < this.s.length()) {
            var1 = UTF16.charAt(this.s, this.index);
            this.index += UTF16.getCharCount(var1);
            return var1;
         } else if (this.dir < 0 && this.index > 0) {
            var1 = UTF16.charAt(this.s, this.index - 1);
            this.index -= UTF16.getCharCount(var1);
            return var1;
         } else {
            return -1;
         }
      }
   }

   public interface HangulSyllableType {
      int NOT_APPLICABLE = 0;
      int LEADING_JAMO = 1;
      int VOWEL_JAMO = 2;
      int TRAILING_JAMO = 3;
      int LV_SYLLABLE = 4;
      int LVT_SYLLABLE = 5;
      int COUNT = 6;
   }

   public interface NumericType {
      int NONE = 0;
      int DECIMAL = 1;
      int DIGIT = 2;
      int NUMERIC = 3;
      int COUNT = 4;
   }

   public interface LineBreak {
      int UNKNOWN = 0;
      int AMBIGUOUS = 1;
      int ALPHABETIC = 2;
      int BREAK_BOTH = 3;
      int BREAK_AFTER = 4;
      int BREAK_BEFORE = 5;
      int MANDATORY_BREAK = 6;
      int CONTINGENT_BREAK = 7;
      int CLOSE_PUNCTUATION = 8;
      int COMBINING_MARK = 9;
      int CARRIAGE_RETURN = 10;
      int EXCLAMATION = 11;
      int GLUE = 12;
      int HYPHEN = 13;
      int IDEOGRAPHIC = 14;
      int INSEPERABLE = 15;
      int INSEPARABLE = 15;
      int INFIX_NUMERIC = 16;
      int LINE_FEED = 17;
      int NONSTARTER = 18;
      int NUMERIC = 19;
      int OPEN_PUNCTUATION = 20;
      int POSTFIX_NUMERIC = 21;
      int PREFIX_NUMERIC = 22;
      int QUOTATION = 23;
      int COMPLEX_CONTEXT = 24;
      int SURROGATE = 25;
      int SPACE = 26;
      int BREAK_SYMBOLS = 27;
      int ZWSPACE = 28;
      int NEXT_LINE = 29;
      int WORD_JOINER = 30;
      int H2 = 31;
      int H3 = 32;
      int JL = 33;
      int JT = 34;
      int JV = 35;
      int CLOSE_PARENTHESIS = 36;
      int CONDITIONAL_JAPANESE_STARTER = 37;
      int HEBREW_LETTER = 38;
      int REGIONAL_INDICATOR = 39;
      int COUNT = 40;
   }

   public interface SentenceBreak {
      int OTHER = 0;
      int ATERM = 1;
      int CLOSE = 2;
      int FORMAT = 3;
      int LOWER = 4;
      int NUMERIC = 5;
      int OLETTER = 6;
      int SEP = 7;
      int SP = 8;
      int STERM = 9;
      int UPPER = 10;
      int CR = 11;
      int EXTEND = 12;
      int LF = 13;
      int SCONTINUE = 14;
      int COUNT = 15;
   }

   public interface WordBreak {
      int OTHER = 0;
      int ALETTER = 1;
      int FORMAT = 2;
      int KATAKANA = 3;
      int MIDLETTER = 4;
      int MIDNUM = 5;
      int NUMERIC = 6;
      int EXTENDNUMLET = 7;
      int CR = 8;
      int EXTEND = 9;
      int LF = 10;
      int MIDNUMLET = 11;
      int NEWLINE = 12;
      int REGIONAL_INDICATOR = 13;
      int COUNT = 14;
   }

   public interface GraphemeClusterBreak {
      int OTHER = 0;
      int CONTROL = 1;
      int CR = 2;
      int EXTEND = 3;
      int L = 4;
      int LF = 5;
      int LV = 6;
      int LVT = 7;
      int T = 8;
      int V = 9;
      int SPACING_MARK = 10;
      int PREPEND = 11;
      int REGIONAL_INDICATOR = 12;
      int COUNT = 13;
   }

   public interface JoiningGroup {
      int NO_JOINING_GROUP = 0;
      int AIN = 1;
      int ALAPH = 2;
      int ALEF = 3;
      int BEH = 4;
      int BETH = 5;
      int DAL = 6;
      int DALATH_RISH = 7;
      int E = 8;
      int FEH = 9;
      int FINAL_SEMKATH = 10;
      int GAF = 11;
      int GAMAL = 12;
      int HAH = 13;
      int TEH_MARBUTA_GOAL = 14;
      int HAMZA_ON_HEH_GOAL = 14;
      int HE = 15;
      int HEH = 16;
      int HEH_GOAL = 17;
      int HETH = 18;
      int KAF = 19;
      int KAPH = 20;
      int KNOTTED_HEH = 21;
      int LAM = 22;
      int LAMADH = 23;
      int MEEM = 24;
      int MIM = 25;
      int NOON = 26;
      int NUN = 27;
      int PE = 28;
      int QAF = 29;
      int QAPH = 30;
      int REH = 31;
      int REVERSED_PE = 32;
      int SAD = 33;
      int SADHE = 34;
      int SEEN = 35;
      int SEMKATH = 36;
      int SHIN = 37;
      int SWASH_KAF = 38;
      int SYRIAC_WAW = 39;
      int TAH = 40;
      int TAW = 41;
      int TEH_MARBUTA = 42;
      int TETH = 43;
      int WAW = 44;
      int YEH = 45;
      int YEH_BARREE = 46;
      int YEH_WITH_TAIL = 47;
      int YUDH = 48;
      int YUDH_HE = 49;
      int ZAIN = 50;
      int FE = 51;
      int KHAPH = 52;
      int ZHAIN = 53;
      int BURUSHASKI_YEH_BARREE = 54;
      int FARSI_YEH = 55;
      int NYA = 56;
      int ROHINGYA_YEH = 57;
      int COUNT = 58;
   }

   public interface JoiningType {
      int NON_JOINING = 0;
      int JOIN_CAUSING = 1;
      int DUAL_JOINING = 2;
      int LEFT_JOINING = 3;
      int RIGHT_JOINING = 4;
      int TRANSPARENT = 5;
      int COUNT = 6;
   }

   public interface DecompositionType {
      int NONE = 0;
      int CANONICAL = 1;
      int COMPAT = 2;
      int CIRCLE = 3;
      int FINAL = 4;
      int FONT = 5;
      int FRACTION = 6;
      int INITIAL = 7;
      int ISOLATED = 8;
      int MEDIAL = 9;
      int NARROW = 10;
      int NOBREAK = 11;
      int SMALL = 12;
      int SQUARE = 13;
      int SUB = 14;
      int SUPER = 15;
      int VERTICAL = 16;
      int WIDE = 17;
      int COUNT = 18;
   }

   public interface EastAsianWidth {
      int NEUTRAL = 0;
      int AMBIGUOUS = 1;
      int HALFWIDTH = 2;
      int FULLWIDTH = 3;
      int NARROW = 4;
      int WIDE = 5;
      int COUNT = 6;
   }

   public static final class UnicodeBlock extends Subset {
      public static final int INVALID_CODE_ID = -1;
      public static final int BASIC_LATIN_ID = 1;
      public static final int LATIN_1_SUPPLEMENT_ID = 2;
      public static final int LATIN_EXTENDED_A_ID = 3;
      public static final int LATIN_EXTENDED_B_ID = 4;
      public static final int IPA_EXTENSIONS_ID = 5;
      public static final int SPACING_MODIFIER_LETTERS_ID = 6;
      public static final int COMBINING_DIACRITICAL_MARKS_ID = 7;
      public static final int GREEK_ID = 8;
      public static final int CYRILLIC_ID = 9;
      public static final int ARMENIAN_ID = 10;
      public static final int HEBREW_ID = 11;
      public static final int ARABIC_ID = 12;
      public static final int SYRIAC_ID = 13;
      public static final int THAANA_ID = 14;
      public static final int DEVANAGARI_ID = 15;
      public static final int BENGALI_ID = 16;
      public static final int GURMUKHI_ID = 17;
      public static final int GUJARATI_ID = 18;
      public static final int ORIYA_ID = 19;
      public static final int TAMIL_ID = 20;
      public static final int TELUGU_ID = 21;
      public static final int KANNADA_ID = 22;
      public static final int MALAYALAM_ID = 23;
      public static final int SINHALA_ID = 24;
      public static final int THAI_ID = 25;
      public static final int LAO_ID = 26;
      public static final int TIBETAN_ID = 27;
      public static final int MYANMAR_ID = 28;
      public static final int GEORGIAN_ID = 29;
      public static final int HANGUL_JAMO_ID = 30;
      public static final int ETHIOPIC_ID = 31;
      public static final int CHEROKEE_ID = 32;
      public static final int UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_ID = 33;
      public static final int OGHAM_ID = 34;
      public static final int RUNIC_ID = 35;
      public static final int KHMER_ID = 36;
      public static final int MONGOLIAN_ID = 37;
      public static final int LATIN_EXTENDED_ADDITIONAL_ID = 38;
      public static final int GREEK_EXTENDED_ID = 39;
      public static final int GENERAL_PUNCTUATION_ID = 40;
      public static final int SUPERSCRIPTS_AND_SUBSCRIPTS_ID = 41;
      public static final int CURRENCY_SYMBOLS_ID = 42;
      public static final int COMBINING_MARKS_FOR_SYMBOLS_ID = 43;
      public static final int LETTERLIKE_SYMBOLS_ID = 44;
      public static final int NUMBER_FORMS_ID = 45;
      public static final int ARROWS_ID = 46;
      public static final int MATHEMATICAL_OPERATORS_ID = 47;
      public static final int MISCELLANEOUS_TECHNICAL_ID = 48;
      public static final int CONTROL_PICTURES_ID = 49;
      public static final int OPTICAL_CHARACTER_RECOGNITION_ID = 50;
      public static final int ENCLOSED_ALPHANUMERICS_ID = 51;
      public static final int BOX_DRAWING_ID = 52;
      public static final int BLOCK_ELEMENTS_ID = 53;
      public static final int GEOMETRIC_SHAPES_ID = 54;
      public static final int MISCELLANEOUS_SYMBOLS_ID = 55;
      public static final int DINGBATS_ID = 56;
      public static final int BRAILLE_PATTERNS_ID = 57;
      public static final int CJK_RADICALS_SUPPLEMENT_ID = 58;
      public static final int KANGXI_RADICALS_ID = 59;
      public static final int IDEOGRAPHIC_DESCRIPTION_CHARACTERS_ID = 60;
      public static final int CJK_SYMBOLS_AND_PUNCTUATION_ID = 61;
      public static final int HIRAGANA_ID = 62;
      public static final int KATAKANA_ID = 63;
      public static final int BOPOMOFO_ID = 64;
      public static final int HANGUL_COMPATIBILITY_JAMO_ID = 65;
      public static final int KANBUN_ID = 66;
      public static final int BOPOMOFO_EXTENDED_ID = 67;
      public static final int ENCLOSED_CJK_LETTERS_AND_MONTHS_ID = 68;
      public static final int CJK_COMPATIBILITY_ID = 69;
      public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A_ID = 70;
      public static final int CJK_UNIFIED_IDEOGRAPHS_ID = 71;
      public static final int YI_SYLLABLES_ID = 72;
      public static final int YI_RADICALS_ID = 73;
      public static final int HANGUL_SYLLABLES_ID = 74;
      public static final int HIGH_SURROGATES_ID = 75;
      public static final int HIGH_PRIVATE_USE_SURROGATES_ID = 76;
      public static final int LOW_SURROGATES_ID = 77;
      public static final int PRIVATE_USE_AREA_ID = 78;
      public static final int PRIVATE_USE_ID = 78;
      public static final int CJK_COMPATIBILITY_IDEOGRAPHS_ID = 79;
      public static final int ALPHABETIC_PRESENTATION_FORMS_ID = 80;
      public static final int ARABIC_PRESENTATION_FORMS_A_ID = 81;
      public static final int COMBINING_HALF_MARKS_ID = 82;
      public static final int CJK_COMPATIBILITY_FORMS_ID = 83;
      public static final int SMALL_FORM_VARIANTS_ID = 84;
      public static final int ARABIC_PRESENTATION_FORMS_B_ID = 85;
      public static final int SPECIALS_ID = 86;
      public static final int HALFWIDTH_AND_FULLWIDTH_FORMS_ID = 87;
      public static final int OLD_ITALIC_ID = 88;
      public static final int GOTHIC_ID = 89;
      public static final int DESERET_ID = 90;
      public static final int BYZANTINE_MUSICAL_SYMBOLS_ID = 91;
      public static final int MUSICAL_SYMBOLS_ID = 92;
      public static final int MATHEMATICAL_ALPHANUMERIC_SYMBOLS_ID = 93;
      public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B_ID = 94;
      public static final int CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT_ID = 95;
      public static final int TAGS_ID = 96;
      public static final int CYRILLIC_SUPPLEMENTARY_ID = 97;
      public static final int CYRILLIC_SUPPLEMENT_ID = 97;
      public static final int TAGALOG_ID = 98;
      public static final int HANUNOO_ID = 99;
      public static final int BUHID_ID = 100;
      public static final int TAGBANWA_ID = 101;
      public static final int MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A_ID = 102;
      public static final int SUPPLEMENTAL_ARROWS_A_ID = 103;
      public static final int SUPPLEMENTAL_ARROWS_B_ID = 104;
      public static final int MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B_ID = 105;
      public static final int SUPPLEMENTAL_MATHEMATICAL_OPERATORS_ID = 106;
      public static final int KATAKANA_PHONETIC_EXTENSIONS_ID = 107;
      public static final int VARIATION_SELECTORS_ID = 108;
      public static final int SUPPLEMENTARY_PRIVATE_USE_AREA_A_ID = 109;
      public static final int SUPPLEMENTARY_PRIVATE_USE_AREA_B_ID = 110;
      public static final int LIMBU_ID = 111;
      public static final int TAI_LE_ID = 112;
      public static final int KHMER_SYMBOLS_ID = 113;
      public static final int PHONETIC_EXTENSIONS_ID = 114;
      public static final int MISCELLANEOUS_SYMBOLS_AND_ARROWS_ID = 115;
      public static final int YIJING_HEXAGRAM_SYMBOLS_ID = 116;
      public static final int LINEAR_B_SYLLABARY_ID = 117;
      public static final int LINEAR_B_IDEOGRAMS_ID = 118;
      public static final int AEGEAN_NUMBERS_ID = 119;
      public static final int UGARITIC_ID = 120;
      public static final int SHAVIAN_ID = 121;
      public static final int OSMANYA_ID = 122;
      public static final int CYPRIOT_SYLLABARY_ID = 123;
      public static final int TAI_XUAN_JING_SYMBOLS_ID = 124;
      public static final int VARIATION_SELECTORS_SUPPLEMENT_ID = 125;
      public static final int ANCIENT_GREEK_MUSICAL_NOTATION_ID = 126;
      public static final int ANCIENT_GREEK_NUMBERS_ID = 127;
      public static final int ARABIC_SUPPLEMENT_ID = 128;
      public static final int BUGINESE_ID = 129;
      public static final int CJK_STROKES_ID = 130;
      public static final int COMBINING_DIACRITICAL_MARKS_SUPPLEMENT_ID = 131;
      public static final int COPTIC_ID = 132;
      public static final int ETHIOPIC_EXTENDED_ID = 133;
      public static final int ETHIOPIC_SUPPLEMENT_ID = 134;
      public static final int GEORGIAN_SUPPLEMENT_ID = 135;
      public static final int GLAGOLITIC_ID = 136;
      public static final int KHAROSHTHI_ID = 137;
      public static final int MODIFIER_TONE_LETTERS_ID = 138;
      public static final int NEW_TAI_LUE_ID = 139;
      public static final int OLD_PERSIAN_ID = 140;
      public static final int PHONETIC_EXTENSIONS_SUPPLEMENT_ID = 141;
      public static final int SUPPLEMENTAL_PUNCTUATION_ID = 142;
      public static final int SYLOTI_NAGRI_ID = 143;
      public static final int TIFINAGH_ID = 144;
      public static final int VERTICAL_FORMS_ID = 145;
      public static final int NKO_ID = 146;
      public static final int BALINESE_ID = 147;
      public static final int LATIN_EXTENDED_C_ID = 148;
      public static final int LATIN_EXTENDED_D_ID = 149;
      public static final int PHAGS_PA_ID = 150;
      public static final int PHOENICIAN_ID = 151;
      public static final int CUNEIFORM_ID = 152;
      public static final int CUNEIFORM_NUMBERS_AND_PUNCTUATION_ID = 153;
      public static final int COUNTING_ROD_NUMERALS_ID = 154;
      public static final int SUNDANESE_ID = 155;
      public static final int LEPCHA_ID = 156;
      public static final int OL_CHIKI_ID = 157;
      public static final int CYRILLIC_EXTENDED_A_ID = 158;
      public static final int VAI_ID = 159;
      public static final int CYRILLIC_EXTENDED_B_ID = 160;
      public static final int SAURASHTRA_ID = 161;
      public static final int KAYAH_LI_ID = 162;
      public static final int REJANG_ID = 163;
      public static final int CHAM_ID = 164;
      public static final int ANCIENT_SYMBOLS_ID = 165;
      public static final int PHAISTOS_DISC_ID = 166;
      public static final int LYCIAN_ID = 167;
      public static final int CARIAN_ID = 168;
      public static final int LYDIAN_ID = 169;
      public static final int MAHJONG_TILES_ID = 170;
      public static final int DOMINO_TILES_ID = 171;
      public static final int SAMARITAN_ID = 172;
      public static final int UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED_ID = 173;
      public static final int TAI_THAM_ID = 174;
      public static final int VEDIC_EXTENSIONS_ID = 175;
      public static final int LISU_ID = 176;
      public static final int BAMUM_ID = 177;
      public static final int COMMON_INDIC_NUMBER_FORMS_ID = 178;
      public static final int DEVANAGARI_EXTENDED_ID = 179;
      public static final int HANGUL_JAMO_EXTENDED_A_ID = 180;
      public static final int JAVANESE_ID = 181;
      public static final int MYANMAR_EXTENDED_A_ID = 182;
      public static final int TAI_VIET_ID = 183;
      public static final int MEETEI_MAYEK_ID = 184;
      public static final int HANGUL_JAMO_EXTENDED_B_ID = 185;
      public static final int IMPERIAL_ARAMAIC_ID = 186;
      public static final int OLD_SOUTH_ARABIAN_ID = 187;
      public static final int AVESTAN_ID = 188;
      public static final int INSCRIPTIONAL_PARTHIAN_ID = 189;
      public static final int INSCRIPTIONAL_PAHLAVI_ID = 190;
      public static final int OLD_TURKIC_ID = 191;
      public static final int RUMI_NUMERAL_SYMBOLS_ID = 192;
      public static final int KAITHI_ID = 193;
      public static final int EGYPTIAN_HIEROGLYPHS_ID = 194;
      public static final int ENCLOSED_ALPHANUMERIC_SUPPLEMENT_ID = 195;
      public static final int ENCLOSED_IDEOGRAPHIC_SUPPLEMENT_ID = 196;
      public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C_ID = 197;
      public static final int MANDAIC_ID = 198;
      public static final int BATAK_ID = 199;
      public static final int ETHIOPIC_EXTENDED_A_ID = 200;
      public static final int BRAHMI_ID = 201;
      public static final int BAMUM_SUPPLEMENT_ID = 202;
      public static final int KANA_SUPPLEMENT_ID = 203;
      public static final int PLAYING_CARDS_ID = 204;
      public static final int MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS_ID = 205;
      public static final int EMOTICONS_ID = 206;
      public static final int TRANSPORT_AND_MAP_SYMBOLS_ID = 207;
      public static final int ALCHEMICAL_SYMBOLS_ID = 208;
      public static final int CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D_ID = 209;
      public static final int ARABIC_EXTENDED_A_ID = 210;
      public static final int ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS_ID = 211;
      public static final int CHAKMA_ID = 212;
      public static final int MEETEI_MAYEK_EXTENSIONS_ID = 213;
      public static final int MEROITIC_CURSIVE_ID = 214;
      public static final int MEROITIC_HIEROGLYPHS_ID = 215;
      public static final int MIAO_ID = 216;
      public static final int SHARADA_ID = 217;
      public static final int SORA_SOMPENG_ID = 218;
      public static final int SUNDANESE_SUPPLEMENT_ID = 219;
      public static final int TAKRI_ID = 220;
      public static final int COUNT = 221;
      private static final UCharacter.UnicodeBlock[] BLOCKS_ = new UCharacter.UnicodeBlock[221];
      public static final UCharacter.UnicodeBlock NO_BLOCK = new UCharacter.UnicodeBlock("NO_BLOCK", 0);
      public static final UCharacter.UnicodeBlock BASIC_LATIN = new UCharacter.UnicodeBlock("BASIC_LATIN", 1);
      public static final UCharacter.UnicodeBlock LATIN_1_SUPPLEMENT = new UCharacter.UnicodeBlock("LATIN_1_SUPPLEMENT", 2);
      public static final UCharacter.UnicodeBlock LATIN_EXTENDED_A = new UCharacter.UnicodeBlock("LATIN_EXTENDED_A", 3);
      public static final UCharacter.UnicodeBlock LATIN_EXTENDED_B = new UCharacter.UnicodeBlock("LATIN_EXTENDED_B", 4);
      public static final UCharacter.UnicodeBlock IPA_EXTENSIONS = new UCharacter.UnicodeBlock("IPA_EXTENSIONS", 5);
      public static final UCharacter.UnicodeBlock SPACING_MODIFIER_LETTERS = new UCharacter.UnicodeBlock("SPACING_MODIFIER_LETTERS", 6);
      public static final UCharacter.UnicodeBlock COMBINING_DIACRITICAL_MARKS = new UCharacter.UnicodeBlock("COMBINING_DIACRITICAL_MARKS", 7);
      public static final UCharacter.UnicodeBlock GREEK = new UCharacter.UnicodeBlock("GREEK", 8);
      public static final UCharacter.UnicodeBlock CYRILLIC = new UCharacter.UnicodeBlock("CYRILLIC", 9);
      public static final UCharacter.UnicodeBlock ARMENIAN = new UCharacter.UnicodeBlock("ARMENIAN", 10);
      public static final UCharacter.UnicodeBlock HEBREW = new UCharacter.UnicodeBlock("HEBREW", 11);
      public static final UCharacter.UnicodeBlock ARABIC = new UCharacter.UnicodeBlock("ARABIC", 12);
      public static final UCharacter.UnicodeBlock SYRIAC = new UCharacter.UnicodeBlock("SYRIAC", 13);
      public static final UCharacter.UnicodeBlock THAANA = new UCharacter.UnicodeBlock("THAANA", 14);
      public static final UCharacter.UnicodeBlock DEVANAGARI = new UCharacter.UnicodeBlock("DEVANAGARI", 15);
      public static final UCharacter.UnicodeBlock BENGALI = new UCharacter.UnicodeBlock("BENGALI", 16);
      public static final UCharacter.UnicodeBlock GURMUKHI = new UCharacter.UnicodeBlock("GURMUKHI", 17);
      public static final UCharacter.UnicodeBlock GUJARATI = new UCharacter.UnicodeBlock("GUJARATI", 18);
      public static final UCharacter.UnicodeBlock ORIYA = new UCharacter.UnicodeBlock("ORIYA", 19);
      public static final UCharacter.UnicodeBlock TAMIL = new UCharacter.UnicodeBlock("TAMIL", 20);
      public static final UCharacter.UnicodeBlock TELUGU = new UCharacter.UnicodeBlock("TELUGU", 21);
      public static final UCharacter.UnicodeBlock KANNADA = new UCharacter.UnicodeBlock("KANNADA", 22);
      public static final UCharacter.UnicodeBlock MALAYALAM = new UCharacter.UnicodeBlock("MALAYALAM", 23);
      public static final UCharacter.UnicodeBlock SINHALA = new UCharacter.UnicodeBlock("SINHALA", 24);
      public static final UCharacter.UnicodeBlock THAI = new UCharacter.UnicodeBlock("THAI", 25);
      public static final UCharacter.UnicodeBlock LAO = new UCharacter.UnicodeBlock("LAO", 26);
      public static final UCharacter.UnicodeBlock TIBETAN = new UCharacter.UnicodeBlock("TIBETAN", 27);
      public static final UCharacter.UnicodeBlock MYANMAR = new UCharacter.UnicodeBlock("MYANMAR", 28);
      public static final UCharacter.UnicodeBlock GEORGIAN = new UCharacter.UnicodeBlock("GEORGIAN", 29);
      public static final UCharacter.UnicodeBlock HANGUL_JAMO = new UCharacter.UnicodeBlock("HANGUL_JAMO", 30);
      public static final UCharacter.UnicodeBlock ETHIOPIC = new UCharacter.UnicodeBlock("ETHIOPIC", 31);
      public static final UCharacter.UnicodeBlock CHEROKEE = new UCharacter.UnicodeBlock("CHEROKEE", 32);
      public static final UCharacter.UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS = new UCharacter.UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS", 33);
      public static final UCharacter.UnicodeBlock OGHAM = new UCharacter.UnicodeBlock("OGHAM", 34);
      public static final UCharacter.UnicodeBlock RUNIC = new UCharacter.UnicodeBlock("RUNIC", 35);
      public static final UCharacter.UnicodeBlock KHMER = new UCharacter.UnicodeBlock("KHMER", 36);
      public static final UCharacter.UnicodeBlock MONGOLIAN = new UCharacter.UnicodeBlock("MONGOLIAN", 37);
      public static final UCharacter.UnicodeBlock LATIN_EXTENDED_ADDITIONAL = new UCharacter.UnicodeBlock("LATIN_EXTENDED_ADDITIONAL", 38);
      public static final UCharacter.UnicodeBlock GREEK_EXTENDED = new UCharacter.UnicodeBlock("GREEK_EXTENDED", 39);
      public static final UCharacter.UnicodeBlock GENERAL_PUNCTUATION = new UCharacter.UnicodeBlock("GENERAL_PUNCTUATION", 40);
      public static final UCharacter.UnicodeBlock SUPERSCRIPTS_AND_SUBSCRIPTS = new UCharacter.UnicodeBlock("SUPERSCRIPTS_AND_SUBSCRIPTS", 41);
      public static final UCharacter.UnicodeBlock CURRENCY_SYMBOLS = new UCharacter.UnicodeBlock("CURRENCY_SYMBOLS", 42);
      public static final UCharacter.UnicodeBlock COMBINING_MARKS_FOR_SYMBOLS = new UCharacter.UnicodeBlock("COMBINING_MARKS_FOR_SYMBOLS", 43);
      public static final UCharacter.UnicodeBlock LETTERLIKE_SYMBOLS = new UCharacter.UnicodeBlock("LETTERLIKE_SYMBOLS", 44);
      public static final UCharacter.UnicodeBlock NUMBER_FORMS = new UCharacter.UnicodeBlock("NUMBER_FORMS", 45);
      public static final UCharacter.UnicodeBlock ARROWS = new UCharacter.UnicodeBlock("ARROWS", 46);
      public static final UCharacter.UnicodeBlock MATHEMATICAL_OPERATORS = new UCharacter.UnicodeBlock("MATHEMATICAL_OPERATORS", 47);
      public static final UCharacter.UnicodeBlock MISCELLANEOUS_TECHNICAL = new UCharacter.UnicodeBlock("MISCELLANEOUS_TECHNICAL", 48);
      public static final UCharacter.UnicodeBlock CONTROL_PICTURES = new UCharacter.UnicodeBlock("CONTROL_PICTURES", 49);
      public static final UCharacter.UnicodeBlock OPTICAL_CHARACTER_RECOGNITION = new UCharacter.UnicodeBlock("OPTICAL_CHARACTER_RECOGNITION", 50);
      public static final UCharacter.UnicodeBlock ENCLOSED_ALPHANUMERICS = new UCharacter.UnicodeBlock("ENCLOSED_ALPHANUMERICS", 51);
      public static final UCharacter.UnicodeBlock BOX_DRAWING = new UCharacter.UnicodeBlock("BOX_DRAWING", 52);
      public static final UCharacter.UnicodeBlock BLOCK_ELEMENTS = new UCharacter.UnicodeBlock("BLOCK_ELEMENTS", 53);
      public static final UCharacter.UnicodeBlock GEOMETRIC_SHAPES = new UCharacter.UnicodeBlock("GEOMETRIC_SHAPES", 54);
      public static final UCharacter.UnicodeBlock MISCELLANEOUS_SYMBOLS = new UCharacter.UnicodeBlock("MISCELLANEOUS_SYMBOLS", 55);
      public static final UCharacter.UnicodeBlock DINGBATS = new UCharacter.UnicodeBlock("DINGBATS", 56);
      public static final UCharacter.UnicodeBlock BRAILLE_PATTERNS = new UCharacter.UnicodeBlock("BRAILLE_PATTERNS", 57);
      public static final UCharacter.UnicodeBlock CJK_RADICALS_SUPPLEMENT = new UCharacter.UnicodeBlock("CJK_RADICALS_SUPPLEMENT", 58);
      public static final UCharacter.UnicodeBlock KANGXI_RADICALS = new UCharacter.UnicodeBlock("KANGXI_RADICALS", 59);
      public static final UCharacter.UnicodeBlock IDEOGRAPHIC_DESCRIPTION_CHARACTERS = new UCharacter.UnicodeBlock("IDEOGRAPHIC_DESCRIPTION_CHARACTERS", 60);
      public static final UCharacter.UnicodeBlock CJK_SYMBOLS_AND_PUNCTUATION = new UCharacter.UnicodeBlock("CJK_SYMBOLS_AND_PUNCTUATION", 61);
      public static final UCharacter.UnicodeBlock HIRAGANA = new UCharacter.UnicodeBlock("HIRAGANA", 62);
      public static final UCharacter.UnicodeBlock KATAKANA = new UCharacter.UnicodeBlock("KATAKANA", 63);
      public static final UCharacter.UnicodeBlock BOPOMOFO = new UCharacter.UnicodeBlock("BOPOMOFO", 64);
      public static final UCharacter.UnicodeBlock HANGUL_COMPATIBILITY_JAMO = new UCharacter.UnicodeBlock("HANGUL_COMPATIBILITY_JAMO", 65);
      public static final UCharacter.UnicodeBlock KANBUN = new UCharacter.UnicodeBlock("KANBUN", 66);
      public static final UCharacter.UnicodeBlock BOPOMOFO_EXTENDED = new UCharacter.UnicodeBlock("BOPOMOFO_EXTENDED", 67);
      public static final UCharacter.UnicodeBlock ENCLOSED_CJK_LETTERS_AND_MONTHS = new UCharacter.UnicodeBlock("ENCLOSED_CJK_LETTERS_AND_MONTHS", 68);
      public static final UCharacter.UnicodeBlock CJK_COMPATIBILITY = new UCharacter.UnicodeBlock("CJK_COMPATIBILITY", 69);
      public static final UCharacter.UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A = new UCharacter.UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A", 70);
      public static final UCharacter.UnicodeBlock CJK_UNIFIED_IDEOGRAPHS = new UCharacter.UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS", 71);
      public static final UCharacter.UnicodeBlock YI_SYLLABLES = new UCharacter.UnicodeBlock("YI_SYLLABLES", 72);
      public static final UCharacter.UnicodeBlock YI_RADICALS = new UCharacter.UnicodeBlock("YI_RADICALS", 73);
      public static final UCharacter.UnicodeBlock HANGUL_SYLLABLES = new UCharacter.UnicodeBlock("HANGUL_SYLLABLES", 74);
      public static final UCharacter.UnicodeBlock HIGH_SURROGATES = new UCharacter.UnicodeBlock("HIGH_SURROGATES", 75);
      public static final UCharacter.UnicodeBlock HIGH_PRIVATE_USE_SURROGATES = new UCharacter.UnicodeBlock("HIGH_PRIVATE_USE_SURROGATES", 76);
      public static final UCharacter.UnicodeBlock LOW_SURROGATES = new UCharacter.UnicodeBlock("LOW_SURROGATES", 77);
      public static final UCharacter.UnicodeBlock PRIVATE_USE_AREA = new UCharacter.UnicodeBlock("PRIVATE_USE_AREA", 78);
      public static final UCharacter.UnicodeBlock PRIVATE_USE;
      public static final UCharacter.UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS;
      public static final UCharacter.UnicodeBlock ALPHABETIC_PRESENTATION_FORMS;
      public static final UCharacter.UnicodeBlock ARABIC_PRESENTATION_FORMS_A;
      public static final UCharacter.UnicodeBlock COMBINING_HALF_MARKS;
      public static final UCharacter.UnicodeBlock CJK_COMPATIBILITY_FORMS;
      public static final UCharacter.UnicodeBlock SMALL_FORM_VARIANTS;
      public static final UCharacter.UnicodeBlock ARABIC_PRESENTATION_FORMS_B;
      public static final UCharacter.UnicodeBlock SPECIALS;
      public static final UCharacter.UnicodeBlock HALFWIDTH_AND_FULLWIDTH_FORMS;
      public static final UCharacter.UnicodeBlock OLD_ITALIC;
      public static final UCharacter.UnicodeBlock GOTHIC;
      public static final UCharacter.UnicodeBlock DESERET;
      public static final UCharacter.UnicodeBlock BYZANTINE_MUSICAL_SYMBOLS;
      public static final UCharacter.UnicodeBlock MUSICAL_SYMBOLS;
      public static final UCharacter.UnicodeBlock MATHEMATICAL_ALPHANUMERIC_SYMBOLS;
      public static final UCharacter.UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B;
      public static final UCharacter.UnicodeBlock CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock TAGS;
      public static final UCharacter.UnicodeBlock CYRILLIC_SUPPLEMENTARY;
      public static final UCharacter.UnicodeBlock CYRILLIC_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock TAGALOG;
      public static final UCharacter.UnicodeBlock HANUNOO;
      public static final UCharacter.UnicodeBlock BUHID;
      public static final UCharacter.UnicodeBlock TAGBANWA;
      public static final UCharacter.UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A;
      public static final UCharacter.UnicodeBlock SUPPLEMENTAL_ARROWS_A;
      public static final UCharacter.UnicodeBlock SUPPLEMENTAL_ARROWS_B;
      public static final UCharacter.UnicodeBlock MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B;
      public static final UCharacter.UnicodeBlock SUPPLEMENTAL_MATHEMATICAL_OPERATORS;
      public static final UCharacter.UnicodeBlock KATAKANA_PHONETIC_EXTENSIONS;
      public static final UCharacter.UnicodeBlock VARIATION_SELECTORS;
      public static final UCharacter.UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_A;
      public static final UCharacter.UnicodeBlock SUPPLEMENTARY_PRIVATE_USE_AREA_B;
      public static final UCharacter.UnicodeBlock LIMBU;
      public static final UCharacter.UnicodeBlock TAI_LE;
      public static final UCharacter.UnicodeBlock KHMER_SYMBOLS;
      public static final UCharacter.UnicodeBlock PHONETIC_EXTENSIONS;
      public static final UCharacter.UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_ARROWS;
      public static final UCharacter.UnicodeBlock YIJING_HEXAGRAM_SYMBOLS;
      public static final UCharacter.UnicodeBlock LINEAR_B_SYLLABARY;
      public static final UCharacter.UnicodeBlock LINEAR_B_IDEOGRAMS;
      public static final UCharacter.UnicodeBlock AEGEAN_NUMBERS;
      public static final UCharacter.UnicodeBlock UGARITIC;
      public static final UCharacter.UnicodeBlock SHAVIAN;
      public static final UCharacter.UnicodeBlock OSMANYA;
      public static final UCharacter.UnicodeBlock CYPRIOT_SYLLABARY;
      public static final UCharacter.UnicodeBlock TAI_XUAN_JING_SYMBOLS;
      public static final UCharacter.UnicodeBlock VARIATION_SELECTORS_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock ANCIENT_GREEK_MUSICAL_NOTATION;
      public static final UCharacter.UnicodeBlock ANCIENT_GREEK_NUMBERS;
      public static final UCharacter.UnicodeBlock ARABIC_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock BUGINESE;
      public static final UCharacter.UnicodeBlock CJK_STROKES;
      public static final UCharacter.UnicodeBlock COMBINING_DIACRITICAL_MARKS_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock COPTIC;
      public static final UCharacter.UnicodeBlock ETHIOPIC_EXTENDED;
      public static final UCharacter.UnicodeBlock ETHIOPIC_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock GEORGIAN_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock GLAGOLITIC;
      public static final UCharacter.UnicodeBlock KHAROSHTHI;
      public static final UCharacter.UnicodeBlock MODIFIER_TONE_LETTERS;
      public static final UCharacter.UnicodeBlock NEW_TAI_LUE;
      public static final UCharacter.UnicodeBlock OLD_PERSIAN;
      public static final UCharacter.UnicodeBlock PHONETIC_EXTENSIONS_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock SUPPLEMENTAL_PUNCTUATION;
      public static final UCharacter.UnicodeBlock SYLOTI_NAGRI;
      public static final UCharacter.UnicodeBlock TIFINAGH;
      public static final UCharacter.UnicodeBlock VERTICAL_FORMS;
      public static final UCharacter.UnicodeBlock NKO;
      public static final UCharacter.UnicodeBlock BALINESE;
      public static final UCharacter.UnicodeBlock LATIN_EXTENDED_C;
      public static final UCharacter.UnicodeBlock LATIN_EXTENDED_D;
      public static final UCharacter.UnicodeBlock PHAGS_PA;
      public static final UCharacter.UnicodeBlock PHOENICIAN;
      public static final UCharacter.UnicodeBlock CUNEIFORM;
      public static final UCharacter.UnicodeBlock CUNEIFORM_NUMBERS_AND_PUNCTUATION;
      public static final UCharacter.UnicodeBlock COUNTING_ROD_NUMERALS;
      public static final UCharacter.UnicodeBlock SUNDANESE;
      public static final UCharacter.UnicodeBlock LEPCHA;
      public static final UCharacter.UnicodeBlock OL_CHIKI;
      public static final UCharacter.UnicodeBlock CYRILLIC_EXTENDED_A;
      public static final UCharacter.UnicodeBlock VAI;
      public static final UCharacter.UnicodeBlock CYRILLIC_EXTENDED_B;
      public static final UCharacter.UnicodeBlock SAURASHTRA;
      public static final UCharacter.UnicodeBlock KAYAH_LI;
      public static final UCharacter.UnicodeBlock REJANG;
      public static final UCharacter.UnicodeBlock CHAM;
      public static final UCharacter.UnicodeBlock ANCIENT_SYMBOLS;
      public static final UCharacter.UnicodeBlock PHAISTOS_DISC;
      public static final UCharacter.UnicodeBlock LYCIAN;
      public static final UCharacter.UnicodeBlock CARIAN;
      public static final UCharacter.UnicodeBlock LYDIAN;
      public static final UCharacter.UnicodeBlock MAHJONG_TILES;
      public static final UCharacter.UnicodeBlock DOMINO_TILES;
      public static final UCharacter.UnicodeBlock SAMARITAN;
      public static final UCharacter.UnicodeBlock UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED;
      public static final UCharacter.UnicodeBlock TAI_THAM;
      public static final UCharacter.UnicodeBlock VEDIC_EXTENSIONS;
      public static final UCharacter.UnicodeBlock LISU;
      public static final UCharacter.UnicodeBlock BAMUM;
      public static final UCharacter.UnicodeBlock COMMON_INDIC_NUMBER_FORMS;
      public static final UCharacter.UnicodeBlock DEVANAGARI_EXTENDED;
      public static final UCharacter.UnicodeBlock HANGUL_JAMO_EXTENDED_A;
      public static final UCharacter.UnicodeBlock JAVANESE;
      public static final UCharacter.UnicodeBlock MYANMAR_EXTENDED_A;
      public static final UCharacter.UnicodeBlock TAI_VIET;
      public static final UCharacter.UnicodeBlock MEETEI_MAYEK;
      public static final UCharacter.UnicodeBlock HANGUL_JAMO_EXTENDED_B;
      public static final UCharacter.UnicodeBlock IMPERIAL_ARAMAIC;
      public static final UCharacter.UnicodeBlock OLD_SOUTH_ARABIAN;
      public static final UCharacter.UnicodeBlock AVESTAN;
      public static final UCharacter.UnicodeBlock INSCRIPTIONAL_PARTHIAN;
      public static final UCharacter.UnicodeBlock INSCRIPTIONAL_PAHLAVI;
      public static final UCharacter.UnicodeBlock OLD_TURKIC;
      public static final UCharacter.UnicodeBlock RUMI_NUMERAL_SYMBOLS;
      public static final UCharacter.UnicodeBlock KAITHI;
      public static final UCharacter.UnicodeBlock EGYPTIAN_HIEROGLYPHS;
      public static final UCharacter.UnicodeBlock ENCLOSED_ALPHANUMERIC_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock ENCLOSED_IDEOGRAPHIC_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C;
      public static final UCharacter.UnicodeBlock MANDAIC;
      public static final UCharacter.UnicodeBlock BATAK;
      public static final UCharacter.UnicodeBlock ETHIOPIC_EXTENDED_A;
      public static final UCharacter.UnicodeBlock BRAHMI;
      public static final UCharacter.UnicodeBlock BAMUM_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock KANA_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock PLAYING_CARDS;
      public static final UCharacter.UnicodeBlock MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS;
      public static final UCharacter.UnicodeBlock EMOTICONS;
      public static final UCharacter.UnicodeBlock TRANSPORT_AND_MAP_SYMBOLS;
      public static final UCharacter.UnicodeBlock ALCHEMICAL_SYMBOLS;
      public static final UCharacter.UnicodeBlock CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D;
      public static final UCharacter.UnicodeBlock ARABIC_EXTENDED_A;
      public static final UCharacter.UnicodeBlock ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS;
      public static final UCharacter.UnicodeBlock CHAKMA;
      public static final UCharacter.UnicodeBlock MEETEI_MAYEK_EXTENSIONS;
      public static final UCharacter.UnicodeBlock MEROITIC_CURSIVE;
      public static final UCharacter.UnicodeBlock MEROITIC_HIEROGLYPHS;
      public static final UCharacter.UnicodeBlock MIAO;
      public static final UCharacter.UnicodeBlock SHARADA;
      public static final UCharacter.UnicodeBlock SORA_SOMPENG;
      public static final UCharacter.UnicodeBlock SUNDANESE_SUPPLEMENT;
      public static final UCharacter.UnicodeBlock TAKRI;
      public static final UCharacter.UnicodeBlock INVALID_CODE;
      private static SoftReference mref;
      private int m_id_;

      public static UCharacter.UnicodeBlock getInstance(int var0) {
         return var0 >= 0 && var0 < BLOCKS_.length ? BLOCKS_[var0] : INVALID_CODE;
      }

      public static UCharacter.UnicodeBlock of(int var0) {
         return var0 > 1114111 ? INVALID_CODE : getInstance(UCharacterProperty.INSTANCE.getIntPropertyValue(var0, 4097));
      }

      public static final UCharacter.UnicodeBlock forName(String var0) {
         Object var1 = null;
         if (mref != null) {
            var1 = (Map)mref.get();
         }

         if (var1 == null) {
            var1 = new HashMap(BLOCKS_.length);

            for(int var2 = 0; var2 < BLOCKS_.length; ++var2) {
               UCharacter.UnicodeBlock var3 = BLOCKS_[var2];
               String var4 = trimBlockName(UCharacter.getPropertyValueName(4097, var3.getID(), 1));
               ((Map)var1).put(var4, var3);
            }

            mref = new SoftReference(var1);
         }

         UCharacter.UnicodeBlock var5 = (UCharacter.UnicodeBlock)((Map)var1).get(trimBlockName(var0));
         if (var5 == null) {
            throw new IllegalArgumentException();
         } else {
            return var5;
         }
      }

      private static String trimBlockName(String var0) {
         String var1 = var0.toUpperCase(Locale.ENGLISH);
         StringBuilder var2 = new StringBuilder(var1.length());

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            char var4 = var1.charAt(var3);
            if (var4 != ' ' && var4 != '_' && var4 != '-') {
               var2.append(var4);
            }
         }

         return var2.toString();
      }

      public int getID() {
         return this.m_id_;
      }

      private UnicodeBlock(String var1, int var2) {
         super(var1);
         this.m_id_ = var2;
         if (var2 >= 0) {
            BLOCKS_[var2] = this;
         }

      }

      static {
         PRIVATE_USE = PRIVATE_USE_AREA;
         CJK_COMPATIBILITY_IDEOGRAPHS = new UCharacter.UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS", 79);
         ALPHABETIC_PRESENTATION_FORMS = new UCharacter.UnicodeBlock("ALPHABETIC_PRESENTATION_FORMS", 80);
         ARABIC_PRESENTATION_FORMS_A = new UCharacter.UnicodeBlock("ARABIC_PRESENTATION_FORMS_A", 81);
         COMBINING_HALF_MARKS = new UCharacter.UnicodeBlock("COMBINING_HALF_MARKS", 82);
         CJK_COMPATIBILITY_FORMS = new UCharacter.UnicodeBlock("CJK_COMPATIBILITY_FORMS", 83);
         SMALL_FORM_VARIANTS = new UCharacter.UnicodeBlock("SMALL_FORM_VARIANTS", 84);
         ARABIC_PRESENTATION_FORMS_B = new UCharacter.UnicodeBlock("ARABIC_PRESENTATION_FORMS_B", 85);
         SPECIALS = new UCharacter.UnicodeBlock("SPECIALS", 86);
         HALFWIDTH_AND_FULLWIDTH_FORMS = new UCharacter.UnicodeBlock("HALFWIDTH_AND_FULLWIDTH_FORMS", 87);
         OLD_ITALIC = new UCharacter.UnicodeBlock("OLD_ITALIC", 88);
         GOTHIC = new UCharacter.UnicodeBlock("GOTHIC", 89);
         DESERET = new UCharacter.UnicodeBlock("DESERET", 90);
         BYZANTINE_MUSICAL_SYMBOLS = new UCharacter.UnicodeBlock("BYZANTINE_MUSICAL_SYMBOLS", 91);
         MUSICAL_SYMBOLS = new UCharacter.UnicodeBlock("MUSICAL_SYMBOLS", 92);
         MATHEMATICAL_ALPHANUMERIC_SYMBOLS = new UCharacter.UnicodeBlock("MATHEMATICAL_ALPHANUMERIC_SYMBOLS", 93);
         CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B = new UCharacter.UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B", 94);
         CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT = new UCharacter.UnicodeBlock("CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT", 95);
         TAGS = new UCharacter.UnicodeBlock("TAGS", 96);
         CYRILLIC_SUPPLEMENTARY = new UCharacter.UnicodeBlock("CYRILLIC_SUPPLEMENTARY", 97);
         CYRILLIC_SUPPLEMENT = new UCharacter.UnicodeBlock("CYRILLIC_SUPPLEMENT", 97);
         TAGALOG = new UCharacter.UnicodeBlock("TAGALOG", 98);
         HANUNOO = new UCharacter.UnicodeBlock("HANUNOO", 99);
         BUHID = new UCharacter.UnicodeBlock("BUHID", 100);
         TAGBANWA = new UCharacter.UnicodeBlock("TAGBANWA", 101);
         MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A = new UCharacter.UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A", 102);
         SUPPLEMENTAL_ARROWS_A = new UCharacter.UnicodeBlock("SUPPLEMENTAL_ARROWS_A", 103);
         SUPPLEMENTAL_ARROWS_B = new UCharacter.UnicodeBlock("SUPPLEMENTAL_ARROWS_B", 104);
         MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B = new UCharacter.UnicodeBlock("MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B", 105);
         SUPPLEMENTAL_MATHEMATICAL_OPERATORS = new UCharacter.UnicodeBlock("SUPPLEMENTAL_MATHEMATICAL_OPERATORS", 106);
         KATAKANA_PHONETIC_EXTENSIONS = new UCharacter.UnicodeBlock("KATAKANA_PHONETIC_EXTENSIONS", 107);
         VARIATION_SELECTORS = new UCharacter.UnicodeBlock("VARIATION_SELECTORS", 108);
         SUPPLEMENTARY_PRIVATE_USE_AREA_A = new UCharacter.UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_A", 109);
         SUPPLEMENTARY_PRIVATE_USE_AREA_B = new UCharacter.UnicodeBlock("SUPPLEMENTARY_PRIVATE_USE_AREA_B", 110);
         LIMBU = new UCharacter.UnicodeBlock("LIMBU", 111);
         TAI_LE = new UCharacter.UnicodeBlock("TAI_LE", 112);
         KHMER_SYMBOLS = new UCharacter.UnicodeBlock("KHMER_SYMBOLS", 113);
         PHONETIC_EXTENSIONS = new UCharacter.UnicodeBlock("PHONETIC_EXTENSIONS", 114);
         MISCELLANEOUS_SYMBOLS_AND_ARROWS = new UCharacter.UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_ARROWS", 115);
         YIJING_HEXAGRAM_SYMBOLS = new UCharacter.UnicodeBlock("YIJING_HEXAGRAM_SYMBOLS", 116);
         LINEAR_B_SYLLABARY = new UCharacter.UnicodeBlock("LINEAR_B_SYLLABARY", 117);
         LINEAR_B_IDEOGRAMS = new UCharacter.UnicodeBlock("LINEAR_B_IDEOGRAMS", 118);
         AEGEAN_NUMBERS = new UCharacter.UnicodeBlock("AEGEAN_NUMBERS", 119);
         UGARITIC = new UCharacter.UnicodeBlock("UGARITIC", 120);
         SHAVIAN = new UCharacter.UnicodeBlock("SHAVIAN", 121);
         OSMANYA = new UCharacter.UnicodeBlock("OSMANYA", 122);
         CYPRIOT_SYLLABARY = new UCharacter.UnicodeBlock("CYPRIOT_SYLLABARY", 123);
         TAI_XUAN_JING_SYMBOLS = new UCharacter.UnicodeBlock("TAI_XUAN_JING_SYMBOLS", 124);
         VARIATION_SELECTORS_SUPPLEMENT = new UCharacter.UnicodeBlock("VARIATION_SELECTORS_SUPPLEMENT", 125);
         ANCIENT_GREEK_MUSICAL_NOTATION = new UCharacter.UnicodeBlock("ANCIENT_GREEK_MUSICAL_NOTATION", 126);
         ANCIENT_GREEK_NUMBERS = new UCharacter.UnicodeBlock("ANCIENT_GREEK_NUMBERS", 127);
         ARABIC_SUPPLEMENT = new UCharacter.UnicodeBlock("ARABIC_SUPPLEMENT", 128);
         BUGINESE = new UCharacter.UnicodeBlock("BUGINESE", 129);
         CJK_STROKES = new UCharacter.UnicodeBlock("CJK_STROKES", 130);
         COMBINING_DIACRITICAL_MARKS_SUPPLEMENT = new UCharacter.UnicodeBlock("COMBINING_DIACRITICAL_MARKS_SUPPLEMENT", 131);
         COPTIC = new UCharacter.UnicodeBlock("COPTIC", 132);
         ETHIOPIC_EXTENDED = new UCharacter.UnicodeBlock("ETHIOPIC_EXTENDED", 133);
         ETHIOPIC_SUPPLEMENT = new UCharacter.UnicodeBlock("ETHIOPIC_SUPPLEMENT", 134);
         GEORGIAN_SUPPLEMENT = new UCharacter.UnicodeBlock("GEORGIAN_SUPPLEMENT", 135);
         GLAGOLITIC = new UCharacter.UnicodeBlock("GLAGOLITIC", 136);
         KHAROSHTHI = new UCharacter.UnicodeBlock("KHAROSHTHI", 137);
         MODIFIER_TONE_LETTERS = new UCharacter.UnicodeBlock("MODIFIER_TONE_LETTERS", 138);
         NEW_TAI_LUE = new UCharacter.UnicodeBlock("NEW_TAI_LUE", 139);
         OLD_PERSIAN = new UCharacter.UnicodeBlock("OLD_PERSIAN", 140);
         PHONETIC_EXTENSIONS_SUPPLEMENT = new UCharacter.UnicodeBlock("PHONETIC_EXTENSIONS_SUPPLEMENT", 141);
         SUPPLEMENTAL_PUNCTUATION = new UCharacter.UnicodeBlock("SUPPLEMENTAL_PUNCTUATION", 142);
         SYLOTI_NAGRI = new UCharacter.UnicodeBlock("SYLOTI_NAGRI", 143);
         TIFINAGH = new UCharacter.UnicodeBlock("TIFINAGH", 144);
         VERTICAL_FORMS = new UCharacter.UnicodeBlock("VERTICAL_FORMS", 145);
         NKO = new UCharacter.UnicodeBlock("NKO", 146);
         BALINESE = new UCharacter.UnicodeBlock("BALINESE", 147);
         LATIN_EXTENDED_C = new UCharacter.UnicodeBlock("LATIN_EXTENDED_C", 148);
         LATIN_EXTENDED_D = new UCharacter.UnicodeBlock("LATIN_EXTENDED_D", 149);
         PHAGS_PA = new UCharacter.UnicodeBlock("PHAGS_PA", 150);
         PHOENICIAN = new UCharacter.UnicodeBlock("PHOENICIAN", 151);
         CUNEIFORM = new UCharacter.UnicodeBlock("CUNEIFORM", 152);
         CUNEIFORM_NUMBERS_AND_PUNCTUATION = new UCharacter.UnicodeBlock("CUNEIFORM_NUMBERS_AND_PUNCTUATION", 153);
         COUNTING_ROD_NUMERALS = new UCharacter.UnicodeBlock("COUNTING_ROD_NUMERALS", 154);
         SUNDANESE = new UCharacter.UnicodeBlock("SUNDANESE", 155);
         LEPCHA = new UCharacter.UnicodeBlock("LEPCHA", 156);
         OL_CHIKI = new UCharacter.UnicodeBlock("OL_CHIKI", 157);
         CYRILLIC_EXTENDED_A = new UCharacter.UnicodeBlock("CYRILLIC_EXTENDED_A", 158);
         VAI = new UCharacter.UnicodeBlock("VAI", 159);
         CYRILLIC_EXTENDED_B = new UCharacter.UnicodeBlock("CYRILLIC_EXTENDED_B", 160);
         SAURASHTRA = new UCharacter.UnicodeBlock("SAURASHTRA", 161);
         KAYAH_LI = new UCharacter.UnicodeBlock("KAYAH_LI", 162);
         REJANG = new UCharacter.UnicodeBlock("REJANG", 163);
         CHAM = new UCharacter.UnicodeBlock("CHAM", 164);
         ANCIENT_SYMBOLS = new UCharacter.UnicodeBlock("ANCIENT_SYMBOLS", 165);
         PHAISTOS_DISC = new UCharacter.UnicodeBlock("PHAISTOS_DISC", 166);
         LYCIAN = new UCharacter.UnicodeBlock("LYCIAN", 167);
         CARIAN = new UCharacter.UnicodeBlock("CARIAN", 168);
         LYDIAN = new UCharacter.UnicodeBlock("LYDIAN", 169);
         MAHJONG_TILES = new UCharacter.UnicodeBlock("MAHJONG_TILES", 170);
         DOMINO_TILES = new UCharacter.UnicodeBlock("DOMINO_TILES", 171);
         SAMARITAN = new UCharacter.UnicodeBlock("SAMARITAN", 172);
         UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED = new UCharacter.UnicodeBlock("UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS_EXTENDED", 173);
         TAI_THAM = new UCharacter.UnicodeBlock("TAI_THAM", 174);
         VEDIC_EXTENSIONS = new UCharacter.UnicodeBlock("VEDIC_EXTENSIONS", 175);
         LISU = new UCharacter.UnicodeBlock("LISU", 176);
         BAMUM = new UCharacter.UnicodeBlock("BAMUM", 177);
         COMMON_INDIC_NUMBER_FORMS = new UCharacter.UnicodeBlock("COMMON_INDIC_NUMBER_FORMS", 178);
         DEVANAGARI_EXTENDED = new UCharacter.UnicodeBlock("DEVANAGARI_EXTENDED", 179);
         HANGUL_JAMO_EXTENDED_A = new UCharacter.UnicodeBlock("HANGUL_JAMO_EXTENDED_A", 180);
         JAVANESE = new UCharacter.UnicodeBlock("JAVANESE", 181);
         MYANMAR_EXTENDED_A = new UCharacter.UnicodeBlock("MYANMAR_EXTENDED_A", 182);
         TAI_VIET = new UCharacter.UnicodeBlock("TAI_VIET", 183);
         MEETEI_MAYEK = new UCharacter.UnicodeBlock("MEETEI_MAYEK", 184);
         HANGUL_JAMO_EXTENDED_B = new UCharacter.UnicodeBlock("HANGUL_JAMO_EXTENDED_B", 185);
         IMPERIAL_ARAMAIC = new UCharacter.UnicodeBlock("IMPERIAL_ARAMAIC", 186);
         OLD_SOUTH_ARABIAN = new UCharacter.UnicodeBlock("OLD_SOUTH_ARABIAN", 187);
         AVESTAN = new UCharacter.UnicodeBlock("AVESTAN", 188);
         INSCRIPTIONAL_PARTHIAN = new UCharacter.UnicodeBlock("INSCRIPTIONAL_PARTHIAN", 189);
         INSCRIPTIONAL_PAHLAVI = new UCharacter.UnicodeBlock("INSCRIPTIONAL_PAHLAVI", 190);
         OLD_TURKIC = new UCharacter.UnicodeBlock("OLD_TURKIC", 191);
         RUMI_NUMERAL_SYMBOLS = new UCharacter.UnicodeBlock("RUMI_NUMERAL_SYMBOLS", 192);
         KAITHI = new UCharacter.UnicodeBlock("KAITHI", 193);
         EGYPTIAN_HIEROGLYPHS = new UCharacter.UnicodeBlock("EGYPTIAN_HIEROGLYPHS", 194);
         ENCLOSED_ALPHANUMERIC_SUPPLEMENT = new UCharacter.UnicodeBlock("ENCLOSED_ALPHANUMERIC_SUPPLEMENT", 195);
         ENCLOSED_IDEOGRAPHIC_SUPPLEMENT = new UCharacter.UnicodeBlock("ENCLOSED_IDEOGRAPHIC_SUPPLEMENT", 196);
         CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C = new UCharacter.UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C", 197);
         MANDAIC = new UCharacter.UnicodeBlock("MANDAIC", 198);
         BATAK = new UCharacter.UnicodeBlock("BATAK", 199);
         ETHIOPIC_EXTENDED_A = new UCharacter.UnicodeBlock("ETHIOPIC_EXTENDED_A", 200);
         BRAHMI = new UCharacter.UnicodeBlock("BRAHMI", 201);
         BAMUM_SUPPLEMENT = new UCharacter.UnicodeBlock("BAMUM_SUPPLEMENT", 202);
         KANA_SUPPLEMENT = new UCharacter.UnicodeBlock("KANA_SUPPLEMENT", 203);
         PLAYING_CARDS = new UCharacter.UnicodeBlock("PLAYING_CARDS", 204);
         MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS = new UCharacter.UnicodeBlock("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS", 205);
         EMOTICONS = new UCharacter.UnicodeBlock("EMOTICONS", 206);
         TRANSPORT_AND_MAP_SYMBOLS = new UCharacter.UnicodeBlock("TRANSPORT_AND_MAP_SYMBOLS", 207);
         ALCHEMICAL_SYMBOLS = new UCharacter.UnicodeBlock("ALCHEMICAL_SYMBOLS", 208);
         CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D = new UCharacter.UnicodeBlock("CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D", 209);
         ARABIC_EXTENDED_A = new UCharacter.UnicodeBlock("ARABIC_EXTENDED_A", 210);
         ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS = new UCharacter.UnicodeBlock("ARABIC_MATHEMATICAL_ALPHABETIC_SYMBOLS", 211);
         CHAKMA = new UCharacter.UnicodeBlock("CHAKMA", 212);
         MEETEI_MAYEK_EXTENSIONS = new UCharacter.UnicodeBlock("MEETEI_MAYEK_EXTENSIONS", 213);
         MEROITIC_CURSIVE = new UCharacter.UnicodeBlock("MEROITIC_CURSIVE", 214);
         MEROITIC_HIEROGLYPHS = new UCharacter.UnicodeBlock("MEROITIC_HIEROGLYPHS", 215);
         MIAO = new UCharacter.UnicodeBlock("MIAO", 216);
         SHARADA = new UCharacter.UnicodeBlock("SHARADA", 217);
         SORA_SOMPENG = new UCharacter.UnicodeBlock("SORA_SOMPENG", 218);
         SUNDANESE_SUPPLEMENT = new UCharacter.UnicodeBlock("SUNDANESE_SUPPLEMENT", 219);
         TAKRI = new UCharacter.UnicodeBlock("TAKRI", 220);
         INVALID_CODE = new UCharacter.UnicodeBlock("INVALID_CODE", -1);

         for(int var0 = 0; var0 < 221; ++var0) {
            if (BLOCKS_[var0] == null) {
               throw new IllegalStateException("UnicodeBlock.BLOCKS_[" + var0 + "] not initialized");
            }
         }

      }
   }
}
