package com.ibm.icu.text;

import com.ibm.icu.impl.UCharacterProperty;
import java.util.Comparator;

public final class UTF16 {
   public static final int SINGLE_CHAR_BOUNDARY = 1;
   public static final int LEAD_SURROGATE_BOUNDARY = 2;
   public static final int TRAIL_SURROGATE_BOUNDARY = 5;
   public static final int CODEPOINT_MIN_VALUE = 0;
   public static final int CODEPOINT_MAX_VALUE = 1114111;
   public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
   public static final int LEAD_SURROGATE_MIN_VALUE = 55296;
   public static final int TRAIL_SURROGATE_MIN_VALUE = 56320;
   public static final int LEAD_SURROGATE_MAX_VALUE = 56319;
   public static final int TRAIL_SURROGATE_MAX_VALUE = 57343;
   public static final int SURROGATE_MIN_VALUE = 55296;
   public static final int SURROGATE_MAX_VALUE = 57343;
   private static final int LEAD_SURROGATE_BITMASK = -1024;
   private static final int TRAIL_SURROGATE_BITMASK = -1024;
   private static final int SURROGATE_BITMASK = -2048;
   private static final int LEAD_SURROGATE_BITS = 55296;
   private static final int TRAIL_SURROGATE_BITS = 56320;
   private static final int SURROGATE_BITS = 55296;
   private static final int LEAD_SURROGATE_SHIFT_ = 10;
   private static final int TRAIL_SURROGATE_MASK_ = 1023;
   private static final int LEAD_SURROGATE_OFFSET_ = 55232;

   private UTF16() {
   }

   public static int charAt(String var0, int var1) {
      char var2 = var0.charAt(var1);
      return var2 < '\ud800' ? var2 : _charAt(var0, var1, var2);
   }

   private static int _charAt(String var0, int var1, char var2) {
      if (var2 > '\udfff') {
         return var2;
      } else {
         char var3;
         if (var2 <= '\udbff') {
            ++var1;
            if (var0.length() != var1) {
               var3 = var0.charAt(var1);
               if (var3 >= '\udc00' && var3 <= '\udfff') {
                  return UCharacterProperty.getRawSupplementary(var2, var3);
               }
            }
         } else {
            --var1;
            if (var1 >= 0) {
               var3 = var0.charAt(var1);
               if (var3 >= '\ud800' && var3 <= '\udbff') {
                  return UCharacterProperty.getRawSupplementary(var3, var2);
               }
            }
         }

         return var2;
      }
   }

   public static int charAt(CharSequence var0, int var1) {
      char var2 = var0.charAt(var1);
      return var2 < '\ud800' ? var2 : _charAt(var0, var1, var2);
   }

   private static int _charAt(CharSequence var0, int var1, char var2) {
      if (var2 > '\udfff') {
         return var2;
      } else {
         char var3;
         if (var2 <= '\udbff') {
            ++var1;
            if (var0.length() != var1) {
               var3 = var0.charAt(var1);
               if (var3 >= '\udc00' && var3 <= '\udfff') {
                  return UCharacterProperty.getRawSupplementary(var2, var3);
               }
            }
         } else {
            --var1;
            if (var1 >= 0) {
               var3 = var0.charAt(var1);
               if (var3 >= '\ud800' && var3 <= '\udbff') {
                  return UCharacterProperty.getRawSupplementary(var3, var2);
               }
            }
         }

         return var2;
      }
   }

   public static int charAt(StringBuffer param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int charAt(char[] param0, int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static int charAt(Replaceable param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int getCharCount(int var0) {
      return var0 < 65536 ? 1 : 2;
   }

   public static int bounds(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int bounds(StringBuffer param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int bounds(char[] param0, int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static char getLeadSurrogate(int var0) {
      return var0 >= 65536 ? (char)('ퟀ' + (var0 >> 10)) : '\u0000';
   }

   public static char getTrailSurrogate(int var0) {
      return var0 >= 65536 ? (char)('\udc00' + (var0 & 1023)) : (char)var0;
   }

   public static String valueOf(int var0) {
      if (var0 >= 0 && var0 <= 1114111) {
         return toString(var0);
      } else {
         throw new IllegalArgumentException("Illegal codepoint");
      }
   }

   public static String valueOf(String var0, int var1) {
      switch(bounds(var0, var1)) {
      case 2:
         return var0.substring(var1, var1 + 2);
      case 5:
         return var0.substring(var1 - 1, var1 + 1);
      default:
         return var0.substring(var1, var1 + 1);
      }
   }

   public static String valueOf(StringBuffer var0, int var1) {
      switch(bounds(var0, var1)) {
      case 2:
         return var0.substring(var1, var1 + 2);
      case 5:
         return var0.substring(var1 - 1, var1 + 1);
      default:
         return var0.substring(var1, var1 + 1);
      }
   }

   public static String valueOf(char[] var0, int var1, int var2, int var3) {
      switch(bounds(var0, var1, var2, var3)) {
      case 2:
         return new String(var0, var1 + var3, 2);
      case 5:
         return new String(var0, var1 + var3 - 1, 2);
      default:
         return new String(var0, var1 + var3, 1);
      }
   }

   public static int findOffsetFromCodePoint(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int findOffsetFromCodePoint(StringBuffer param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int findOffsetFromCodePoint(char[] param0, int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static int findCodePointOffset(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int findCodePointOffset(StringBuffer param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int findCodePointOffset(char[] param0, int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static StringBuffer append(StringBuffer var0, int var1) {
      if (var1 >= 0 && var1 <= 1114111) {
         if (var1 >= 65536) {
            var0.append(getLeadSurrogate(var1));
            var0.append(getTrailSurrogate(var1));
         } else {
            var0.append((char)var1);
         }

         return var0;
      } else {
         throw new IllegalArgumentException("Illegal codepoint: " + Integer.toHexString(var1));
      }
   }

   public static StringBuffer appendCodePoint(StringBuffer var0, int var1) {
      return append(var0, var1);
   }

   public static int append(char[] var0, int var1, int var2) {
      if (var2 >= 0 && var2 <= 1114111) {
         if (var2 >= 65536) {
            var0[var1++] = getLeadSurrogate(var2);
            var0[var1++] = getTrailSurrogate(var2);
         } else {
            var0[var1++] = (char)var2;
         }

         return var1;
      } else {
         throw new IllegalArgumentException("Illegal codepoint");
      }
   }

   public static int countCodePoint(String var0) {
      return var0 != null && var0.length() != 0 ? findCodePointOffset(var0, var0.length()) : 0;
   }

   public static int countCodePoint(StringBuffer var0) {
      return var0 != null && var0.length() != 0 ? findCodePointOffset(var0, var0.length()) : 0;
   }

   public static int countCodePoint(char[] var0, int var1, int var2) {
      return var0 != null && var0.length != 0 ? findCodePointOffset(var0, var1, var2, var2 - var1) : 0;
   }

   public static void setCharAt(StringBuffer param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static int setCharAt(char[] param0, int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static int moveCodePointOffset(String param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static int moveCodePointOffset(StringBuffer param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static int moveCodePointOffset(char[] param0, int param1, int param2, int param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   public static StringBuffer insert(StringBuffer var0, int var1, int var2) {
      String var3 = valueOf(var2);
      if (var1 != var0.length() && bounds(var0, var1) == 5) {
         ++var1;
      }

      var0.insert(var1, var3);
      return var0;
   }

   public static int insert(char[] var0, int var1, int var2, int var3) {
      String var4 = valueOf(var3);
      if (var2 != var1 && bounds(var0, 0, var1, var2) == 5) {
         ++var2;
      }

      int var5 = var4.length();
      if (var1 + var5 > var0.length) {
         throw new ArrayIndexOutOfBoundsException(var2 + var5);
      } else {
         System.arraycopy(var0, var2, var0, var2 + var5, var1 - var2);
         var0[var2] = var4.charAt(0);
         if (var5 == 2) {
            var0[var2 + 1] = var4.charAt(1);
         }

         return var1 + var5;
      }
   }

   public static StringBuffer delete(StringBuffer var0, int var1) {
      int var2 = 1;
      switch(bounds(var0, var1)) {
      case 2:
         ++var2;
         break;
      case 5:
         ++var2;
         --var1;
      }

      var0.delete(var1, var1 + var2);
      return var0;
   }

   public static int delete(char[] var0, int var1, int var2) {
      int var3 = 1;
      switch(bounds(var0, 0, var1, var2)) {
      case 2:
         ++var3;
         break;
      case 5:
         ++var3;
         --var2;
      }

      System.arraycopy(var0, var2 + var3, var0, var2, var1 - (var2 + var3));
      var0[var1 - var3] = 0;
      return var1 - var3;
   }

   public static int indexOf(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int indexOf(String param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static int indexOf(String param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static int indexOf(String param0, String param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static int lastIndexOf(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static int lastIndexOf(String param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static int lastIndexOf(String param0, int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static int lastIndexOf(String param0, String param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public static String replace(String var0, int var1, int var2) {
      if (var1 > 0 && var1 <= 1114111) {
         if (var2 > 0 && var2 <= 1114111) {
            int var3 = indexOf(var0, var1);
            if (var3 == -1) {
               return var0;
            } else {
               String var4 = toString(var2);
               byte var5 = 1;
               int var6 = var4.length();
               StringBuffer var7 = new StringBuffer(var0);
               int var8 = var3;
               if (var1 >= 65536) {
                  var5 = 2;
               }

               while(var3 != -1) {
                  int var9 = var8 + var5;
                  var7.replace(var8, var9, var4);
                  int var10 = var3 + var5;
                  var3 = indexOf(var0, var1, var10);
                  var8 += var6 + var3 - var10;
               }

               return var7.toString();
            }
         } else {
            throw new IllegalArgumentException("Argument newChar32 is not a valid codepoint");
         }
      } else {
         throw new IllegalArgumentException("Argument oldChar32 is not a valid codepoint");
      }
   }

   public static String replace(String var0, String var1, String var2) {
      int var3 = indexOf(var0, var1);
      if (var3 == -1) {
         return var0;
      } else {
         int var4 = var1.length();
         int var5 = var2.length();
         StringBuffer var6 = new StringBuffer(var0);

         int var9;
         for(int var7 = var3; var3 != -1; var7 += var5 + var3 - var9) {
            int var8 = var7 + var4;
            var6.replace(var7, var8, var2);
            var9 = var3 + var4;
            var3 = indexOf(var0, var1, var9);
         }

         return var6.toString();
      }
   }

   public static StringBuffer reverse(StringBuffer param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean hasMoreCodePointsThan(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean hasMoreCodePointsThan(char[] param0, int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static boolean hasMoreCodePointsThan(StringBuffer param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static String newString(int[] var0, int var1, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException();
      } else {
         char[] var3 = new char[var2];
         int var4 = 0;
         int var5 = var1;

         for(int var6 = var1 + var2; var5 < var6; ++var5) {
            int var7 = var0[var5];
            if (var7 < 0 || var7 > 1114111) {
               throw new IllegalArgumentException();
            }

            while(true) {
               try {
                  if (var7 < 65536) {
                     var3[var4] = (char)var7;
                     ++var4;
                  } else {
                     var3[var4] = (char)('ퟀ' + (var7 >> 10));
                     var3[var4 + 1] = (char)('\udc00' + (var7 & 1023));
                     var4 += 2;
                  }
                  break;
               } catch (IndexOutOfBoundsException var11) {
                  int var9 = (int)Math.ceil((double)var0.length * (double)(var4 + 2) / (double)(var5 - var1 + 1));
                  char[] var10 = new char[var9];
                  System.arraycopy(var3, 0, var10, 0, var4);
                  var3 = var10;
               }
            }
         }

         return new String(var3, 0, var4);
      }
   }

   private static String toString(int var0) {
      if (var0 < 65536) {
         return String.valueOf((char)var0);
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(getLeadSurrogate(var0));
         var1.append(getTrailSurrogate(var0));
         return var1.toString();
      }
   }

   public static final class StringComparator implements Comparator {
      public static final int FOLD_CASE_DEFAULT = 0;
      public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
      private int m_codePointCompare_;
      private int m_foldCase_;
      private boolean m_ignoreCase_;
      private static final int CODE_POINT_COMPARE_SURROGATE_OFFSET_ = 10240;

      public StringComparator() {
         this(false, false, 0);
      }

      public StringComparator(boolean var1, boolean var2, int var3) {
         this.setCodePointCompare(var1);
         this.m_ignoreCase_ = var2;
         if (var3 >= 0 && var3 <= 1) {
            this.m_foldCase_ = var3;
         } else {
            throw new IllegalArgumentException("Invalid fold case option");
         }
      }

      public void setCodePointCompare(boolean var1) {
         if (var1) {
            this.m_codePointCompare_ = 32768;
         } else {
            this.m_codePointCompare_ = 0;
         }

      }

      public void setIgnoreCase(boolean var1, int var2) {
         this.m_ignoreCase_ = var1;
         if (var2 >= 0 && var2 <= 1) {
            this.m_foldCase_ = var2;
         } else {
            throw new IllegalArgumentException("Invalid fold case option");
         }
      }

      public boolean getCodePointCompare() {
         return this.m_codePointCompare_ == 32768;
      }

      public boolean getIgnoreCase() {
         return this.m_ignoreCase_;
      }

      public int getIgnoreCaseOption() {
         return this.m_foldCase_;
      }

      public int compare(String var1, String var2) {
         if (var1 == var2) {
            return 0;
         } else if (var1 == null) {
            return -1;
         } else if (var2 == null) {
            return 1;
         } else {
            return this.m_ignoreCase_ ? this.compareCaseInsensitive(var1, var2) : this.compareCaseSensitive(var1, var2);
         }
      }

      private int compareCaseInsensitive(String var1, String var2) {
         return Normalizer.cmpEquivFold(var1, var2, this.m_foldCase_ | this.m_codePointCompare_ | 65536);
      }

      private int compareCaseSensitive(String var1, String var2) {
         int var3 = var1.length();
         int var4 = var2.length();
         int var5 = var3;
         byte var6 = 0;
         if (var3 < var4) {
            var6 = -1;
         } else if (var3 > var4) {
            var6 = 1;
            var5 = var4;
         }

         char var7 = 0;
         char var8 = 0;

         int var9;
         for(var9 = 0; var9 < var5; ++var9) {
            var7 = var1.charAt(var9);
            var8 = var2.charAt(var9);
            if (var7 != var8) {
               break;
            }
         }

         if (var9 == var5) {
            return var6;
         } else {
            boolean var10 = this.m_codePointCompare_ == 32768;
            if (var7 >= '\ud800' && var8 >= '\ud800' && var10) {
               if ((var7 > '\udbff' || var9 + 1 == var3 || !UTF16.isTrailSurrogate(var1.charAt(var9 + 1))) && (!UTF16.isTrailSurrogate(var7) || var9 == 0 || !UTF16.isLeadSurrogate(var1.charAt(var9 - 1)))) {
                  var7 = (char)(var7 - 10240);
               }

               if ((var8 > '\udbff' || var9 + 1 == var4 || !UTF16.isTrailSurrogate(var2.charAt(var9 + 1))) && (!UTF16.isTrailSurrogate(var8) || var9 == 0 || !UTF16.isLeadSurrogate(var2.charAt(var9 - 1)))) {
                  var8 = (char)(var8 - 10240);
               }
            }

            return var7 - var8;
         }
      }

      public int compare(Object var1, Object var2) {
         return this.compare((String)var1, (String)var2);
      }
   }
}
