package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.ArrayUtils;

public class AggregateTranslator extends CharSequenceTranslator {
   private final CharSequenceTranslator[] translators;

   public AggregateTranslator(CharSequenceTranslator... var1) {
      this.translators = (CharSequenceTranslator[])ArrayUtils.clone((Object[])var1);
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      CharSequenceTranslator[] var4 = this.translators;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CharSequenceTranslator var7 = var4[var6];
         int var8 = var7.translate(var1, var2, var3);
         if (var8 != 0) {
            return var8;
         }
      }

      return 0;
   }
}
