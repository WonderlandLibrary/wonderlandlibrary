package org.apache.commons.compress.archivers.zip;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ZipEncodingHelper {
   private static final Map simpleEncodings;
   private static final byte[] HEX_DIGITS;
   static final String UTF8 = "UTF8";
   static final ZipEncoding UTF8_ZIP_ENCODING;

   static ByteBuffer growBuffer(ByteBuffer var0, int var1) {
      var0.limit(var0.position());
      var0.rewind();
      int var2 = var0.capacity() * 2;
      ByteBuffer var3 = ByteBuffer.allocate(var2 < var1 ? var1 : var2);
      var3.put(var0);
      return var3;
   }

   static void appendSurrogate(ByteBuffer var0, char var1) {
      var0.put((byte)37);
      var0.put((byte)85);
      var0.put(HEX_DIGITS[var1 >> 12 & 15]);
      var0.put(HEX_DIGITS[var1 >> 8 & 15]);
      var0.put(HEX_DIGITS[var1 >> 4 & 15]);
      var0.put(HEX_DIGITS[var1 & 15]);
   }

   public static ZipEncoding getZipEncoding(String var0) {
      if (var0 == null) {
         return UTF8_ZIP_ENCODING;
      } else if (var0 == null) {
         return new FallbackZipEncoding();
      } else {
         ZipEncodingHelper.SimpleEncodingHolder var1 = (ZipEncodingHelper.SimpleEncodingHolder)simpleEncodings.get(var0);
         if (var1 != null) {
            return var1.getEncoding();
         } else {
            try {
               Charset var2 = Charset.forName(var0);
               return new NioZipEncoding(var2);
            } catch (UnsupportedCharsetException var3) {
               return new FallbackZipEncoding(var0);
            }
         }
      }
   }

   static {
      HashMap var0 = new HashMap();
      char[] var1 = new char[]{'Ç', 'ü', 'é', 'â', 'ä', 'à', 'å', 'ç', 'ê', 'ë', 'è', 'ï', 'î', 'ì', 'Ä', 'Å', 'É', 'æ', 'Æ', 'ô', 'ö', 'ò', 'û', 'ù', 'ÿ', 'Ö', 'Ü', '¢', '£', '¥', '₧', 'ƒ', 'á', 'í', 'ó', 'ú', 'ñ', 'Ñ', 'ª', 'º', '¿', '⌐', '¬', '½', '¼', '¡', '«', '»', '░', '▒', '▓', '│', '┤', '╡', '╢', '╖', '╕', '╣', '║', '╗', '╝', '╜', '╛', '┐', '└', '┴', '┬', '├', '─', '┼', '╞', '╟', '╚', '╔', '╩', '╦', '╠', '═', '╬', '╧', '╨', '╤', '╥', '╙', '╘', '╒', '╓', '╫', '╪', '┘', '┌', '█', '▄', '▌', '▐', '▀', 'α', 'ß', 'Γ', 'π', 'Σ', 'σ', 'µ', 'τ', 'Φ', 'Θ', 'Ω', 'δ', '∞', 'φ', 'ε', '∩', '≡', '±', '≥', '≤', '⌠', '⌡', '÷', '≈', '°', '∙', '·', '√', 'ⁿ', '²', '■', ' '};
      ZipEncodingHelper.SimpleEncodingHolder var2 = new ZipEncodingHelper.SimpleEncodingHolder(var1);
      var0.put("CP437", var2);
      var0.put("Cp437", var2);
      var0.put("cp437", var2);
      var0.put("IBM437", var2);
      var0.put("ibm437", var2);
      char[] var3 = new char[]{'Ç', 'ü', 'é', 'â', 'ä', 'à', 'å', 'ç', 'ê', 'ë', 'è', 'ï', 'î', 'ì', 'Ä', 'Å', 'É', 'æ', 'Æ', 'ô', 'ö', 'ò', 'û', 'ù', 'ÿ', 'Ö', 'Ü', 'ø', '£', 'Ø', '×', 'ƒ', 'á', 'í', 'ó', 'ú', 'ñ', 'Ñ', 'ª', 'º', '¿', '®', '¬', '½', '¼', '¡', '«', '»', '░', '▒', '▓', '│', '┤', 'Á', 'Â', 'À', '©', '╣', '║', '╗', '╝', '¢', '¥', '┐', '└', '┴', '┬', '├', '─', '┼', 'ã', 'Ã', '╚', '╔', '╩', '╦', '╠', '═', '╬', '¤', 'ð', 'Ð', 'Ê', 'Ë', 'È', 'ı', 'Í', 'Î', 'Ï', '┘', '┌', '█', '▄', '¦', 'Ì', '▀', 'Ó', 'ß', 'Ô', 'Ò', 'õ', 'Õ', 'µ', 'þ', 'Þ', 'Ú', 'Û', 'Ù', 'ý', 'Ý', '¯', '´', '\u00ad', '±', '‗', '¾', '¶', '§', '÷', '¸', '°', '¨', '·', '¹', '³', '²', '■', ' '};
      ZipEncodingHelper.SimpleEncodingHolder var4 = new ZipEncodingHelper.SimpleEncodingHolder(var3);
      var0.put("CP850", var4);
      var0.put("Cp850", var4);
      var0.put("cp850", var4);
      var0.put("IBM850", var4);
      var0.put("ibm850", var4);
      simpleEncodings = Collections.unmodifiableMap(var0);
      HEX_DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
      UTF8_ZIP_ENCODING = new FallbackZipEncoding("UTF8");
   }

   private static class SimpleEncodingHolder {
      private final char[] highChars;
      private Simple8BitZipEncoding encoding;

      SimpleEncodingHolder(char[] var1) {
         this.highChars = var1;
      }

      public synchronized Simple8BitZipEncoding getEncoding() {
         if (this.encoding == null) {
            this.encoding = new Simple8BitZipEncoding(this.highChars);
         }

         return this.encoding;
      }
   }
}
