package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public abstract class CodePointTranslator extends CharSequenceTranslator {
  public final int translate(CharSequence input, int index, Writer out) throws IOException {
    int codepoint = Character.codePointAt(input, index);
    boolean consumed = translate(codepoint, out);
    return consumed ? 1 : 0;
  }
  
  public abstract boolean translate(int paramInt, Writer paramWriter) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\translate\CodePointTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */