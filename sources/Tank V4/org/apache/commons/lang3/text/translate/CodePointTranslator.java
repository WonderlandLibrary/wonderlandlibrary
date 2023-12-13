package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public abstract class CodePointTranslator extends CharSequenceTranslator {
   public final int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      int var4 = Character.codePointAt(var1, var2);
      boolean var5 = this.translate(var4, var3);
      return var5 ? 1 : 0;
   }

   public abstract boolean translate(int var1, Writer var2) throws IOException;
}
