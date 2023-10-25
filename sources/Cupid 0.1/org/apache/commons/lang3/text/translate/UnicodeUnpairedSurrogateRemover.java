package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeUnpairedSurrogateRemover extends CodePointTranslator {
  public boolean translate(int codepoint, Writer out) throws IOException {
    if (codepoint >= 55296 && codepoint <= 57343)
      return true; 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\translate\UnicodeUnpairedSurrogateRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */