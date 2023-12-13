package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Arrays;
import java.util.BitSet;
import javax.annotation.CheckReturnValue;

@Beta
@GwtCompatible(
   emulated = true
)
public abstract class CharMatcher implements Predicate {
   public static final CharMatcher BREAKING_WHITESPACE = new CharMatcher() {
      public boolean matches(char var1) {
         switch(var1) {
         case '\t':
         case '\n':
         case '\u000b':
         case '\f':
         case '\r':
         case ' ':
         case '\u0085':
         case ' ':
         case '\u2028':
         case '\u2029':
         case ' ':
         case '　':
            return true;
         case ' ':
            return false;
         default:
            return var1 >= 8192 && var1 <= 8202;
         }
      }

      public String toString() {
         return "CharMatcher.BREAKING_WHITESPACE";
      }

      public boolean apply(Object var1) {
         return super.apply((Character)var1);
      }
   };
   public static final CharMatcher ASCII = inRange('\u0000', '\u007f', "CharMatcher.ASCII");
   private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";
   private static final String NINES;
   public static final CharMatcher DIGIT;
   public static final CharMatcher JAVA_DIGIT;
   public static final CharMatcher JAVA_LETTER;
   public static final CharMatcher JAVA_LETTER_OR_DIGIT;
   public static final CharMatcher JAVA_UPPER_CASE;
   public static final CharMatcher JAVA_LOWER_CASE;
   public static final CharMatcher JAVA_ISO_CONTROL;
   public static final CharMatcher INVISIBLE;
   public static final CharMatcher SINGLE_WIDTH;
   public static final CharMatcher ANY;
   public static final CharMatcher NONE;
   final String description;
   private static final int DISTINCT_CHARS = 65536;
   static final String WHITESPACE_TABLE = " 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　";
   static final int WHITESPACE_MULTIPLIER = 1682554634;
   static final int WHITESPACE_SHIFT;
   public static final CharMatcher WHITESPACE;

   private static String showCharacter(char var0) {
      String var1 = "0123456789ABCDEF";
      char[] var2 = new char[]{'\\', 'u', '\u0000', '\u0000', '\u0000', '\u0000'};

      for(int var3 = 0; var3 < 4; ++var3) {
         var2[5 - var3] = var1.charAt(var0 & 15);
         var0 = (char)(var0 >> 4);
      }

      return String.copyValueOf(var2);
   }

   public static CharMatcher is(char var0) {
      String var1 = "CharMatcher.is('" + showCharacter(var0) + "')";
      return new CharMatcher.FastMatcher(var1, var0) {
         final char val$match;

         {
            this.val$match = var2;
         }

         public boolean matches(char var1) {
            return var1 == this.val$match;
         }

         public String replaceFrom(CharSequence var1, char var2) {
            return var1.toString().replace(this.val$match, var2);
         }

         public CharMatcher and(CharMatcher var1) {
            return (CharMatcher)(var1.matches(this.val$match) ? this : NONE);
         }

         public CharMatcher or(CharMatcher var1) {
            return var1.matches(this.val$match) ? var1 : super.or(var1);
         }

         public CharMatcher negate() {
            return isNot(this.val$match);
         }

         @GwtIncompatible("java.util.BitSet")
         void setBits(BitSet var1) {
            var1.set(this.val$match);
         }
      };
   }

   public static CharMatcher isNot(char var0) {
      String var1 = "CharMatcher.isNot('" + showCharacter(var0) + "')";
      return new CharMatcher.FastMatcher(var1, var0) {
         final char val$match;

         {
            this.val$match = var2;
         }

         public boolean matches(char var1) {
            return var1 != this.val$match;
         }

         public CharMatcher and(CharMatcher var1) {
            return var1.matches(this.val$match) ? super.and(var1) : var1;
         }

         public CharMatcher or(CharMatcher var1) {
            return (CharMatcher)(var1.matches(this.val$match) ? ANY : this);
         }

         @GwtIncompatible("java.util.BitSet")
         void setBits(BitSet var1) {
            var1.set(0, this.val$match);
            var1.set(this.val$match + 1, 65536);
         }

         public CharMatcher negate() {
            return is(this.val$match);
         }
      };
   }

   public static CharMatcher anyOf(CharSequence var0) {
      switch(var0.length()) {
      case 0:
         return NONE;
      case 1:
         return is(var0.charAt(0));
      case 2:
         return isEither(var0.charAt(0), var0.charAt(1));
      default:
         char[] var1 = var0.toString().toCharArray();
         Arrays.sort(var1);
         StringBuilder var2 = new StringBuilder("CharMatcher.anyOf(\"");
         char[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            var2.append(showCharacter(var6));
         }

         var2.append("\")");
         return new CharMatcher(var2.toString(), var1) {
            final char[] val$chars;

            {
               this.val$chars = var2;
            }

            public boolean matches(char var1) {
               return Arrays.binarySearch(this.val$chars, var1) >= 0;
            }

            @GwtIncompatible("java.util.BitSet")
            void setBits(BitSet var1) {
               char[] var2 = this.val$chars;
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  char var5 = var2[var4];
                  var1.set(var5);
               }

            }

            public boolean apply(Object var1) {
               return super.apply((Character)var1);
            }
         };
      }
   }

   private static CharMatcher isEither(char var0, char var1) {
      String var2 = "CharMatcher.anyOf(\"" + showCharacter(var0) + showCharacter(var1) + "\")";
      return new CharMatcher.FastMatcher(var2, var0, var1) {
         final char val$match1;
         final char val$match2;

         {
            this.val$match1 = var2;
            this.val$match2 = var3;
         }

         public boolean matches(char var1) {
            return var1 == this.val$match1 || var1 == this.val$match2;
         }

         @GwtIncompatible("java.util.BitSet")
         void setBits(BitSet var1) {
            var1.set(this.val$match1);
            var1.set(this.val$match2);
         }
      };
   }

   public static CharMatcher noneOf(CharSequence var0) {
      return anyOf(var0).negate();
   }

   public static CharMatcher inRange(char var0, char var1) {
      Preconditions.checkArgument(var1 >= var0);
      String var2 = "CharMatcher.inRange('" + showCharacter(var0) + "', '" + showCharacter(var1) + "')";
      return inRange(var0, var1, var2);
   }

   static CharMatcher inRange(char var0, char var1, String var2) {
      return new CharMatcher.FastMatcher(var2, var0, var1) {
         final char val$startInclusive;
         final char val$endInclusive;

         {
            this.val$startInclusive = var2;
            this.val$endInclusive = var3;
         }

         public boolean matches(char var1) {
            return this.val$startInclusive <= var1 && var1 <= this.val$endInclusive;
         }

         @GwtIncompatible("java.util.BitSet")
         void setBits(BitSet var1) {
            var1.set(this.val$startInclusive, this.val$endInclusive + 1);
         }
      };
   }

   public static CharMatcher forPredicate(Predicate var0) {
      Preconditions.checkNotNull(var0);
      if (var0 instanceof CharMatcher) {
         return (CharMatcher)var0;
      } else {
         String var1 = "CharMatcher.forPredicate(" + var0 + ")";
         return new CharMatcher(var1, var0) {
            final Predicate val$predicate;

            {
               this.val$predicate = var2;
            }

            public boolean matches(char var1) {
               return this.val$predicate.apply(var1);
            }

            public boolean apply(Character var1) {
               return this.val$predicate.apply(Preconditions.checkNotNull(var1));
            }

            public boolean apply(Object var1) {
               return this.apply((Character)var1);
            }
         };
      }
   }

   CharMatcher(String var1) {
      this.description = var1;
   }

   protected CharMatcher() {
      this.description = super.toString();
   }

   public abstract boolean matches(char var1);

   public CharMatcher negate() {
      return new CharMatcher.NegatedMatcher(this);
   }

   public CharMatcher and(CharMatcher var1) {
      return new CharMatcher.And(this, (CharMatcher)Preconditions.checkNotNull(var1));
   }

   public CharMatcher or(CharMatcher var1) {
      return new CharMatcher.Or(this, (CharMatcher)Preconditions.checkNotNull(var1));
   }

   public CharMatcher precomputed() {
      return Platform.precomputeCharMatcher(this);
   }

   CharMatcher withToString(String var1) {
      throw new UnsupportedOperationException();
   }

   @GwtIncompatible("java.util.BitSet")
   CharMatcher precomputedInternal() {
      BitSet var1 = new BitSet();
      this.setBits(var1);
      int var2 = var1.cardinality();
      if (var2 * 2 <= 65536) {
         return precomputedPositive(var2, var1, this.description);
      } else {
         var1.flip(0, 65536);
         int var3 = 65536 - var2;
         String var4 = ".negate()";
         String var5 = this.description.endsWith(var4) ? this.description.substring(0, this.description.length() - var4.length()) : this.description + var4;
         return new CharMatcher.NegatedFastMatcher(this.toString(), precomputedPositive(var3, var1, var5));
      }
   }

   @GwtIncompatible("java.util.BitSet")
   private static CharMatcher precomputedPositive(int var0, BitSet var1, String var2) {
      switch(var0) {
      case 0:
         return NONE;
      case 1:
         return is((char)var1.nextSetBit(0));
      case 2:
         char var3 = (char)var1.nextSetBit(0);
         char var4 = (char)var1.nextSetBit(var3 + 1);
         return isEither(var3, var4);
      default:
         return (CharMatcher)(var0 <= var1.length() ? SmallCharMatcher.from(var1, var2) : new CharMatcher.BitSetMatcher(var1, var2));
      }
   }

   @GwtIncompatible("java.util.BitSet")
   void setBits(BitSet var1) {
      for(int var2 = 65535; var2 >= 0; --var2) {
         if (this.matches((char)var2)) {
            var1.set(var2);
         }
      }

   }

   public boolean matchesAnyOf(CharSequence var1) {
      return this == var1;
   }

   public boolean matchesAllOf(CharSequence var1) {
      for(int var2 = var1.length() - 1; var2 >= 0; --var2) {
         if (!this.matches(var1.charAt(var2))) {
            return false;
         }
      }

      return true;
   }

   public int indexIn(CharSequence var1) {
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (this.matches(var1.charAt(var3))) {
            return var3;
         }
      }

      return -1;
   }

   public int indexIn(CharSequence var1, int var2) {
      int var3 = var1.length();
      Preconditions.checkPositionIndex(var2, var3);

      for(int var4 = var2; var4 < var3; ++var4) {
         if (this.matches(var1.charAt(var4))) {
            return var4;
         }
      }

      return -1;
   }

   public int lastIndexIn(CharSequence var1) {
      for(int var2 = var1.length() - 1; var2 >= 0; --var2) {
         if (this.matches(var1.charAt(var2))) {
            return var2;
         }
      }

      return -1;
   }

   public int countIn(CharSequence var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         if (this.matches(var1.charAt(var3))) {
            ++var2;
         }
      }

      return var2;
   }

   @CheckReturnValue
   public String removeFrom(CharSequence var1) {
      String var2 = var1.toString();
      int var3 = this.indexIn(var2);
      if (var3 == -1) {
         return var2;
      } else {
         char[] var4 = var2.toCharArray();
         int var5 = 1;

         label25:
         while(true) {
            ++var3;

            while(var3 != var4.length) {
               if (this.matches(var4[var3])) {
                  ++var5;
                  continue label25;
               }

               var4[var3 - var5] = var4[var3];
               ++var3;
            }

            return new String(var4, 0, var3 - var5);
         }
      }
   }

   @CheckReturnValue
   public String retainFrom(CharSequence var1) {
      return this.negate().removeFrom(var1);
   }

   @CheckReturnValue
   public String replaceFrom(CharSequence var1, char var2) {
      String var3 = var1.toString();
      int var4 = this.indexIn(var3);
      if (var4 == -1) {
         return var3;
      } else {
         char[] var5 = var3.toCharArray();
         var5[var4] = var2;

         for(int var6 = var4 + 1; var6 < var5.length; ++var6) {
            if (this.matches(var5[var6])) {
               var5[var6] = var2;
            }
         }

         return new String(var5);
      }
   }

   @CheckReturnValue
   public String replaceFrom(CharSequence var1, CharSequence var2) {
      int var3 = var2.length();
      if (var3 == 0) {
         return this.removeFrom(var1);
      } else if (var3 == 1) {
         return this.replaceFrom(var1, var2.charAt(0));
      } else {
         String var4 = var1.toString();
         int var5 = this.indexIn(var4);
         if (var5 == -1) {
            return var4;
         } else {
            int var6 = var4.length();
            StringBuilder var7 = new StringBuilder(var6 * 3 / 2 + 16);
            int var8 = 0;

            do {
               var7.append(var4, var8, var5);
               var7.append(var2);
               var8 = var5 + 1;
               var5 = this.indexIn(var4, var8);
            } while(var5 != -1);

            var7.append(var4, var8, var6);
            return var7.toString();
         }
      }
   }

   @CheckReturnValue
   public String trimFrom(CharSequence var1) {
      int var2 = var1.length();

      int var3;
      for(var3 = 0; var3 < var2 && this.matches(var1.charAt(var3)); ++var3) {
      }

      int var4;
      for(var4 = var2 - 1; var4 > var3 && this.matches(var1.charAt(var4)); --var4) {
      }

      return var1.subSequence(var3, var4 + 1).toString();
   }

   @CheckReturnValue
   public String trimLeadingFrom(CharSequence var1) {
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (!this.matches(var1.charAt(var3))) {
            return var1.subSequence(var3, var2).toString();
         }
      }

      return "";
   }

   @CheckReturnValue
   public String trimTrailingFrom(CharSequence var1) {
      int var2 = var1.length();

      for(int var3 = var2 - 1; var3 >= 0; --var3) {
         if (!this.matches(var1.charAt(var3))) {
            return var1.subSequence(0, var3 + 1).toString();
         }
      }

      return "";
   }

   @CheckReturnValue
   public String collapseFrom(CharSequence var1, char var2) {
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var1.charAt(var4);
         if (this.matches(var5)) {
            if (var5 != var2 || var4 != var3 - 1 && this.matches(var1.charAt(var4 + 1))) {
               StringBuilder var6 = (new StringBuilder(var3)).append(var1.subSequence(0, var4)).append(var2);
               return this.finishCollapseFrom(var1, var4 + 1, var3, var2, var6, true);
            }

            ++var4;
         }
      }

      return var1.toString();
   }

   @CheckReturnValue
   public String trimAndCollapseFrom(CharSequence var1, char var2) {
      int var3 = var1.length();

      int var4;
      for(var4 = 0; var4 < var3 && this.matches(var1.charAt(var4)); ++var4) {
      }

      int var5;
      for(var5 = var3 - 1; var5 > var4 && this.matches(var1.charAt(var5)); --var5) {
      }

      return var4 == 0 && var5 == var3 - 1 ? this.collapseFrom(var1, var2) : this.finishCollapseFrom(var1, var4, var5 + 1, var2, new StringBuilder(var5 + 1 - var4), false);
   }

   private String finishCollapseFrom(CharSequence var1, int var2, int var3, char var4, StringBuilder var5, boolean var6) {
      for(int var7 = var2; var7 < var3; ++var7) {
         char var8 = var1.charAt(var7);
         if (this.matches(var8)) {
            if (!var6) {
               var5.append(var4);
               var6 = true;
            }
         } else {
            var5.append(var8);
            var6 = false;
         }
      }

      return var5.toString();
   }

   /** @deprecated */
   @Deprecated
   public boolean apply(Character var1) {
      return this.matches(var1);
   }

   public String toString() {
      return this.description;
   }

   public boolean apply(Object var1) {
      return this.apply((Character)var1);
   }

   static {
      StringBuilder var0 = new StringBuilder(31);

      for(int var1 = 0; var1 < 31; ++var1) {
         var0.append((char)("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".charAt(var1) + 9));
      }

      NINES = var0.toString();
      DIGIT = new CharMatcher.RangesMatcher("CharMatcher.DIGIT", "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray(), NINES.toCharArray());
      JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") {
         public boolean matches(char var1) {
            return Character.isDigit(var1);
         }

         public boolean apply(Object var1) {
            return super.apply((Character)var1);
         }
      };
      JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") {
         public boolean matches(char var1) {
            return Character.isLetter(var1);
         }

         public boolean apply(Object var1) {
            return super.apply((Character)var1);
         }
      };
      JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT") {
         public boolean matches(char var1) {
            return Character.isLetterOrDigit(var1);
         }

         public boolean apply(Object var1) {
            return super.apply((Character)var1);
         }
      };
      JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE") {
         public boolean matches(char var1) {
            return Character.isUpperCase(var1);
         }

         public boolean apply(Object var1) {
            return super.apply((Character)var1);
         }
      };
      JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE") {
         public boolean matches(char var1) {
            return Character.isLowerCase(var1);
         }

         public boolean apply(Object var1) {
            return super.apply((Character)var1);
         }
      };
      JAVA_ISO_CONTROL = inRange('\u0000', '\u001f').or(inRange('\u007f', '\u009f')).withToString("CharMatcher.JAVA_ISO_CONTROL");
      INVISIBLE = new CharMatcher.RangesMatcher("CharMatcher.INVISIBLE", "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f ᠎ \u2028 \u2066\u2067\u2068\u2069\u206a　\ud800\ufeff\ufff9\ufffa".toCharArray(), "  \u00ad\u0604\u061c\u06dd\u070f ᠎\u200f \u2064\u2066\u2067\u2068\u2069\u206f　\uf8ff\ufeff\ufff9\ufffb".toCharArray());
      SINGLE_WIDTH = new CharMatcher.RangesMatcher("CharMatcher.SINGLE_WIDTH", "\u0000־א׳\u0600ݐ\u0e00Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ\u0e7f₯℺\ufdff\ufeffￜ".toCharArray());
      ANY = new CharMatcher.FastMatcher("CharMatcher.ANY") {
         public boolean matches(char var1) {
            return true;
         }

         public int indexIn(CharSequence var1) {
            return var1.length() == 0 ? -1 : 0;
         }

         public int indexIn(CharSequence var1, int var2) {
            int var3 = var1.length();
            Preconditions.checkPositionIndex(var2, var3);
            return var2 == var3 ? -1 : var2;
         }

         public int lastIndexIn(CharSequence var1) {
            return var1.length() - 1;
         }

         public boolean matchesAllOf(CharSequence var1) {
            Preconditions.checkNotNull(var1);
            return true;
         }

         public boolean matchesNoneOf(CharSequence var1) {
            return var1.length() == 0;
         }

         public String removeFrom(CharSequence var1) {
            Preconditions.checkNotNull(var1);
            return "";
         }

         public String replaceFrom(CharSequence var1, char var2) {
            char[] var3 = new char[var1.length()];
            Arrays.fill(var3, var2);
            return new String(var3);
         }

         public String replaceFrom(CharSequence var1, CharSequence var2) {
            StringBuilder var3 = new StringBuilder(var1.length() * var2.length());

            for(int var4 = 0; var4 < var1.length(); ++var4) {
               var3.append(var2);
            }

            return var3.toString();
         }

         public String collapseFrom(CharSequence var1, char var2) {
            return var1.length() == 0 ? "" : String.valueOf(var2);
         }

         public String trimFrom(CharSequence var1) {
            Preconditions.checkNotNull(var1);
            return "";
         }

         public int countIn(CharSequence var1) {
            return var1.length();
         }

         public CharMatcher and(CharMatcher var1) {
            return (CharMatcher)Preconditions.checkNotNull(var1);
         }

         public CharMatcher or(CharMatcher var1) {
            Preconditions.checkNotNull(var1);
            return this;
         }

         public CharMatcher negate() {
            return NONE;
         }
      };
      NONE = new CharMatcher.FastMatcher("CharMatcher.NONE") {
         public boolean matches(char var1) {
            return false;
         }

         public int indexIn(CharSequence var1) {
            Preconditions.checkNotNull(var1);
            return -1;
         }

         public int indexIn(CharSequence var1, int var2) {
            int var3 = var1.length();
            Preconditions.checkPositionIndex(var2, var3);
            return -1;
         }

         public int lastIndexIn(CharSequence var1) {
            Preconditions.checkNotNull(var1);
            return -1;
         }

         public boolean matchesAllOf(CharSequence var1) {
            return var1.length() == 0;
         }

         public boolean matchesNoneOf(CharSequence var1) {
            Preconditions.checkNotNull(var1);
            return true;
         }

         public String removeFrom(CharSequence var1) {
            return var1.toString();
         }

         public String replaceFrom(CharSequence var1, char var2) {
            return var1.toString();
         }

         public String replaceFrom(CharSequence var1, CharSequence var2) {
            Preconditions.checkNotNull(var2);
            return var1.toString();
         }

         public String collapseFrom(CharSequence var1, char var2) {
            return var1.toString();
         }

         public String trimFrom(CharSequence var1) {
            return var1.toString();
         }

         public String trimLeadingFrom(CharSequence var1) {
            return var1.toString();
         }

         public String trimTrailingFrom(CharSequence var1) {
            return var1.toString();
         }

         public int countIn(CharSequence var1) {
            Preconditions.checkNotNull(var1);
            return 0;
         }

         public CharMatcher and(CharMatcher var1) {
            Preconditions.checkNotNull(var1);
            return this;
         }

         public CharMatcher or(CharMatcher var1) {
            return (CharMatcher)Preconditions.checkNotNull(var1);
         }

         public CharMatcher negate() {
            return ANY;
         }
      };
      WHITESPACE_SHIFT = Integer.numberOfLeadingZeros(31);
      WHITESPACE = new CharMatcher.FastMatcher("WHITESPACE") {
         public boolean matches(char var1) {
            return " 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　".charAt(1682554634 * var1 >>> WHITESPACE_SHIFT) == var1;
         }

         @GwtIncompatible("java.util.BitSet")
         void setBits(BitSet var1) {
            for(int var2 = 0; var2 < 32; ++var2) {
               var1.set(" 　\r\u0085   　\u2029\u000b　   　 \t     \f 　 　　\u2028\n 　".charAt(var2));
            }

         }
      };
   }

   @GwtIncompatible("java.util.BitSet")
   private static class BitSetMatcher extends CharMatcher.FastMatcher {
      private final BitSet table;

      private BitSetMatcher(BitSet var1, String var2) {
         super(var2);
         if (var1.length() + 64 < var1.size()) {
            var1 = (BitSet)var1.clone();
         }

         this.table = var1;
      }

      public boolean matches(char var1) {
         return this.table.get(var1);
      }

      void setBits(BitSet var1) {
         var1.or(this.table);
      }

      BitSetMatcher(BitSet var1, String var2, Object var3) {
         this(var1, var2);
      }
   }

   static final class NegatedFastMatcher extends CharMatcher.NegatedMatcher {
      NegatedFastMatcher(CharMatcher var1) {
         super(var1);
      }

      NegatedFastMatcher(String var1, CharMatcher var2) {
         super(var1, var2);
      }

      public final CharMatcher precomputed() {
         return this;
      }

      CharMatcher withToString(String var1) {
         return new CharMatcher.NegatedFastMatcher(var1, this.original);
      }
   }

   abstract static class FastMatcher extends CharMatcher {
      FastMatcher() {
      }

      FastMatcher(String var1) {
         super(var1);
      }

      public final CharMatcher precomputed() {
         return this;
      }

      public CharMatcher negate() {
         return new CharMatcher.NegatedFastMatcher(this);
      }

      public boolean apply(Object var1) {
         return super.apply((Character)var1);
      }
   }

   private static class Or extends CharMatcher {
      final CharMatcher first;
      final CharMatcher second;

      Or(CharMatcher var1, CharMatcher var2, String var3) {
         super(var3);
         this.first = (CharMatcher)Preconditions.checkNotNull(var1);
         this.second = (CharMatcher)Preconditions.checkNotNull(var2);
      }

      Or(CharMatcher var1, CharMatcher var2) {
         this(var1, var2, "CharMatcher.or(" + var1 + ", " + var2 + ")");
      }

      @GwtIncompatible("java.util.BitSet")
      void setBits(BitSet var1) {
         this.first.setBits(var1);
         this.second.setBits(var1);
      }

      public boolean matches(char var1) {
         return this.first.matches(var1) || this.second.matches(var1);
      }

      CharMatcher withToString(String var1) {
         return new CharMatcher.Or(this.first, this.second, var1);
      }

      public boolean apply(Object var1) {
         return super.apply((Character)var1);
      }
   }

   private static class And extends CharMatcher {
      final CharMatcher first;
      final CharMatcher second;

      And(CharMatcher var1, CharMatcher var2) {
         this(var1, var2, "CharMatcher.and(" + var1 + ", " + var2 + ")");
      }

      And(CharMatcher var1, CharMatcher var2, String var3) {
         super(var3);
         this.first = (CharMatcher)Preconditions.checkNotNull(var1);
         this.second = (CharMatcher)Preconditions.checkNotNull(var2);
      }

      public boolean matches(char var1) {
         return this.first.matches(var1) && this.second.matches(var1);
      }

      @GwtIncompatible("java.util.BitSet")
      void setBits(BitSet var1) {
         BitSet var2 = new BitSet();
         this.first.setBits(var2);
         BitSet var3 = new BitSet();
         this.second.setBits(var3);
         var2.and(var3);
         var1.or(var2);
      }

      CharMatcher withToString(String var1) {
         return new CharMatcher.And(this.first, this.second, var1);
      }

      public boolean apply(Object var1) {
         return super.apply((Character)var1);
      }
   }

   private static class NegatedMatcher extends CharMatcher {
      final CharMatcher original;

      NegatedMatcher(String var1, CharMatcher var2) {
         super(var1);
         this.original = var2;
      }

      NegatedMatcher(CharMatcher var1) {
         this(var1 + ".negate()", var1);
      }

      public boolean matches(char var1) {
         return !this.original.matches(var1);
      }

      public boolean matchesAllOf(CharSequence var1) {
         return this.original.matchesNoneOf(var1);
      }

      public boolean matchesNoneOf(CharSequence var1) {
         return this.original.matchesAllOf(var1);
      }

      public int countIn(CharSequence var1) {
         return var1.length() - this.original.countIn(var1);
      }

      @GwtIncompatible("java.util.BitSet")
      void setBits(BitSet var1) {
         BitSet var2 = new BitSet();
         this.original.setBits(var2);
         var2.flip(0, 65536);
         var1.or(var2);
      }

      public CharMatcher negate() {
         return this.original;
      }

      CharMatcher withToString(String var1) {
         return new CharMatcher.NegatedMatcher(var1, this.original);
      }

      public boolean apply(Object var1) {
         return super.apply((Character)var1);
      }
   }

   private static class RangesMatcher extends CharMatcher {
      private final char[] rangeStarts;
      private final char[] rangeEnds;

      RangesMatcher(String var1, char[] var2, char[] var3) {
         super(var1);
         this.rangeStarts = var2;
         this.rangeEnds = var3;
         Preconditions.checkArgument(var2.length == var3.length);

         for(int var4 = 0; var4 < var2.length; ++var4) {
            Preconditions.checkArgument(var2[var4] <= var3[var4]);
            if (var4 + 1 < var2.length) {
               Preconditions.checkArgument(var3[var4] < var2[var4 + 1]);
            }
         }

      }

      public boolean matches(char var1) {
         int var2 = Arrays.binarySearch(this.rangeStarts, var1);
         if (var2 >= 0) {
            return true;
         } else {
            var2 = ~var2 - 1;
            return var2 >= 0 && var1 <= this.rangeEnds[var2];
         }
      }

      public boolean apply(Object var1) {
         return super.apply((Character)var1);
      }
   }
}
