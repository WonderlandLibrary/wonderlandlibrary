package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeEscaper extends CodePointTranslator {
  private final int below;
  
  private final int above;
  
  private final boolean between;
  
  public UnicodeEscaper() {
    this(0, 2147483647, true);
  }
  
  protected UnicodeEscaper(int below, int above, boolean between) {
    this.below = below;
    this.above = above;
    this.between = between;
  }
  
  public static UnicodeEscaper below(int codepoint) {
    return outsideOf(codepoint, 2147483647);
  }
  
  public static UnicodeEscaper above(int codepoint) {
    return outsideOf(0, codepoint);
  }
  
  public static UnicodeEscaper outsideOf(int codepointLow, int codepointHigh) {
    return new UnicodeEscaper(codepointLow, codepointHigh, false);
  }
  
  public static UnicodeEscaper between(int codepointLow, int codepointHigh) {
    return new UnicodeEscaper(codepointLow, codepointHigh, true);
  }
  
  public boolean translate(int codepoint, Writer out) throws IOException {
    if (this.between) {
      if (codepoint < this.below || codepoint > this.above)
        return false; 
    } else if (codepoint >= this.below && codepoint <= this.above) {
      return false;
    } 
    if (codepoint > 65535) {
      out.write(toUtf16Escape(codepoint));
    } else if (codepoint > 4095) {
      out.write("\\u" + hex(codepoint));
    } else if (codepoint > 255) {
      out.write("\\u0" + hex(codepoint));
    } else if (codepoint > 15) {
      out.write("\\u00" + hex(codepoint));
    } else {
      out.write("\\u000" + hex(codepoint));
    } 
    return true;
  }
  
  protected String toUtf16Escape(int codepoint) {
    return "\\u" + hex(codepoint);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\translate\UnicodeEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */