package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeUnpairedSurrogateRemover extends CodePointTranslator {
   public boolean translate(int var1, Writer var2) throws IOException {
      return var1 >= 55296 && var1 <= 57343;
   }
}
