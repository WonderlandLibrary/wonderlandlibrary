package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeUnescaper extends CharSequenceTranslator {
  public int translate(CharSequence input, int index, Writer out) throws IOException {
    if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u') {
      int i = 2;
      while (index + i < input.length() && input.charAt(index + i) == 'u')
        i++; 
      if (index + i < input.length() && input.charAt(index + i) == '+')
        i++; 
      if (index + i + 4 <= input.length()) {
        CharSequence unicode = input.subSequence(index + i, index + i + 4);
        try {
          int value = Integer.parseInt(unicode.toString(), 16);
          out.write((char)value);
        } catch (NumberFormatException nfe) {
          throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
        } 
        return i + 4;
      } 
      throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + input.subSequence(index, input.length()) + "' due to end of CharSequence");
    } 
    return 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\translate\UnicodeUnescaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */