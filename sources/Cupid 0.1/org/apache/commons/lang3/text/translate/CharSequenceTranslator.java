package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public abstract class CharSequenceTranslator {
  public abstract int translate(CharSequence paramCharSequence, int paramInt, Writer paramWriter) throws IOException;
  
  public final String translate(CharSequence input) {
    if (input == null)
      return null; 
    try {
      StringWriter writer = new StringWriter(input.length() * 2);
      translate(input, writer);
      return writer.toString();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } 
  }
  
  public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null)
      throw new IllegalArgumentException("The Writer must not be null"); 
    if (input == null)
      return; 
    int pos = 0;
    int len = input.length();
    while (pos < len) {
      int consumed = translate(input, pos, out);
      if (consumed == 0) {
        char[] c = Character.toChars(Character.codePointAt(input, pos));
        out.write(c);
        pos += c.length;
        continue;
      } 
      for (int pt = 0; pt < consumed; pt++)
        pos += Character.charCount(Character.codePointAt(input, pos)); 
    } 
  }
  
  public final CharSequenceTranslator with(CharSequenceTranslator... translators) {
    CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
    newArray[0] = this;
    System.arraycopy(translators, 0, newArray, 1, translators.length);
    return new AggregateTranslator(newArray);
  }
  
  public static String hex(int codepoint) {
    return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\translate\CharSequenceTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */