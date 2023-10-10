package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
public enum CaseFormat {
   LOWER_HYPHEN(CharMatcher.is('-'), "-") {
      String normalizeWord(String var1) {
         return Ascii.toLowerCase(var1);
      }

      String convert(CaseFormat var1, String var2) {
         if (var1 == LOWER_UNDERSCORE) {
            return var2.replace('-', '_');
         } else {
            return var1 == UPPER_UNDERSCORE ? Ascii.toUpperCase(var2.replace('-', '_')) : super.convert(var1, var2);
         }
      }
   },
   LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
      String normalizeWord(String var1) {
         return Ascii.toLowerCase(var1);
      }

      String convert(CaseFormat var1, String var2) {
         if (var1 == LOWER_HYPHEN) {
            return var2.replace('_', '-');
         } else {
            return var1 == UPPER_UNDERSCORE ? Ascii.toUpperCase(var2) : super.convert(var1, var2);
         }
      }
   },
   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
      String normalizeWord(String var1) {
         return CaseFormat.access$100(var1);
      }
   },
   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
      String normalizeWord(String var1) {
         return CaseFormat.access$100(var1);
      }
   },
   UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
      String normalizeWord(String var1) {
         return Ascii.toUpperCase(var1);
      }

      String convert(CaseFormat var1, String var2) {
         if (var1 == LOWER_HYPHEN) {
            return Ascii.toLowerCase(var2.replace('_', '-'));
         } else {
            return var1 == LOWER_UNDERSCORE ? Ascii.toLowerCase(var2) : super.convert(var1, var2);
         }
      }
   };

   private final CharMatcher wordBoundary;
   private final String wordSeparator;
   private static final CaseFormat[] $VALUES = new CaseFormat[]{LOWER_HYPHEN, LOWER_UNDERSCORE, LOWER_CAMEL, UPPER_CAMEL, UPPER_UNDERSCORE};

   private CaseFormat(CharMatcher var3, String var4) {
      this.wordBoundary = var3;
      this.wordSeparator = var4;
   }

   public final String to(CaseFormat var1, String var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      return var1 == this ? var2 : this.convert(var1, var2);
   }

   String convert(CaseFormat var1, String var2) {
      StringBuilder var3 = null;
      int var4 = 0;
      int var5 = -1;

      while(true) {
         ++var5;
         if ((var5 = this.wordBoundary.indexIn(var2, var5)) == -1) {
            return var4 == 0 ? var1.normalizeFirstWord(var2) : var3.append(var1.normalizeWord(var2.substring(var4))).toString();
         }

         if (var4 == 0) {
            var3 = new StringBuilder(var2.length() + 4 * this.wordSeparator.length());
            var3.append(var1.normalizeFirstWord(var2.substring(var4, var5)));
         } else {
            var3.append(var1.normalizeWord(var2.substring(var4, var5)));
         }

         var3.append(var1.wordSeparator);
         var4 = var5 + this.wordSeparator.length();
      }
   }

   @Beta
   public Converter converterTo(CaseFormat var1) {
      return new CaseFormat.StringConverter(this, var1);
   }

   abstract String normalizeWord(String var1);

   private String normalizeFirstWord(String var1) {
      return this == LOWER_CAMEL ? Ascii.toLowerCase(var1) : this.normalizeWord(var1);
   }

   private static String firstCharOnlyToUpper(String var0) {
      return var0.isEmpty() ? var0 : (new StringBuilder(var0.length())).append(Ascii.toUpperCase(var0.charAt(0))).append(Ascii.toLowerCase(var0.substring(1))).toString();
   }

   CaseFormat(CharMatcher var3, String var4, Object var5) {
      this(var3, var4);
   }

   static String access$100(String var0) {
      return firstCharOnlyToUpper(var0);
   }

   private static final class StringConverter extends Converter implements Serializable {
      private final CaseFormat sourceFormat;
      private final CaseFormat targetFormat;
      private static final long serialVersionUID = 0L;

      StringConverter(CaseFormat var1, CaseFormat var2) {
         this.sourceFormat = (CaseFormat)Preconditions.checkNotNull(var1);
         this.targetFormat = (CaseFormat)Preconditions.checkNotNull(var2);
      }

      protected String doForward(String var1) {
         return var1 == null ? null : this.sourceFormat.to(this.targetFormat, var1);
      }

      protected String doBackward(String var1) {
         return var1 == null ? null : this.targetFormat.to(this.sourceFormat, var1);
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof CaseFormat.StringConverter)) {
            return false;
         } else {
            CaseFormat.StringConverter var2 = (CaseFormat.StringConverter)var1;
            return this.sourceFormat.equals(var2.sourceFormat) && this.targetFormat.equals(var2.targetFormat);
         }
      }

      public int hashCode() {
         return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
      }

      public String toString() {
         return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
      }

      protected Object doBackward(Object var1) {
         return this.doBackward((String)var1);
      }

      protected Object doForward(Object var1) {
         return this.doForward((String)var1);
      }
   }
}
