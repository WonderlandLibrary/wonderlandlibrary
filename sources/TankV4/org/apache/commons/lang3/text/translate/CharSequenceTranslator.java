package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public abstract class CharSequenceTranslator {
   public abstract int translate(CharSequence var1, int var2, Writer var3) throws IOException;

   public final String translate(CharSequence var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            StringWriter var2 = new StringWriter(var1.length() * 2);
            this.translate(var1, var2);
            return var2.toString();
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   public final void translate(CharSequence var1, Writer var2) throws IOException {
      if (var2 == null) {
         throw new IllegalArgumentException("The Writer must not be null");
      } else if (var1 != null) {
         int var3 = 0;
         int var4 = var1.length();

         while(true) {
            while(var3 < var4) {
               int var5 = this.translate(var1, var3, var2);
               if (var5 == 0) {
                  char[] var7 = Character.toChars(Character.codePointAt(var1, var3));
                  var2.write(var7);
                  var3 += var7.length;
               } else {
                  for(int var6 = 0; var6 < var5; ++var6) {
                     var3 += Character.charCount(Character.codePointAt(var1, var3));
                  }
               }
            }

            return;
         }
      }
   }

   public final CharSequenceTranslator with(CharSequenceTranslator... var1) {
      CharSequenceTranslator[] var2 = new CharSequenceTranslator[var1.length + 1];
      var2[0] = this;
      System.arraycopy(var1, 0, var2, 1, var1.length);
      return new AggregateTranslator(var2);
   }

   public static String hex(int var0) {
      return Integer.toHexString(var0).toUpperCase(Locale.ENGLISH);
   }
}
