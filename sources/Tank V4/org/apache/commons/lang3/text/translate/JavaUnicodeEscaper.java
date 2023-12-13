package org.apache.commons.lang3.text.translate;

public class JavaUnicodeEscaper extends UnicodeEscaper {
   public static JavaUnicodeEscaper above(int var0) {
      return outsideOf(0, var0);
   }

   public static JavaUnicodeEscaper below(int var0) {
      return outsideOf(var0, Integer.MAX_VALUE);
   }

   public static JavaUnicodeEscaper between(int var0, int var1) {
      return new JavaUnicodeEscaper(var0, var1, true);
   }

   public static JavaUnicodeEscaper outsideOf(int var0, int var1) {
      return new JavaUnicodeEscaper(var0, var1, false);
   }

   public JavaUnicodeEscaper(int var1, int var2, boolean var3) {
      super(var1, var2, var3);
   }

   protected String toUtf16Escape(int var1) {
      char[] var2 = Character.toChars(var1);
      return "\\u" + hex(var2[0]) + "\\u" + hex(var2[1]);
   }
}
