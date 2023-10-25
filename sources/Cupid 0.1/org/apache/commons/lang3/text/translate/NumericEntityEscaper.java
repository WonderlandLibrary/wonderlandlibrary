package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class NumericEntityEscaper extends CodePointTranslator {
  private final int below;
  
  private final int above;
  
  private final boolean between;
  
  private NumericEntityEscaper(int below, int above, boolean between) {
    this.below = below;
    this.above = above;
    this.between = between;
  }
  
  public NumericEntityEscaper() {
    this(0, 2147483647, true);
  }
  
  public static NumericEntityEscaper below(int codepoint) {
    return outsideOf(codepoint, 2147483647);
  }
  
  public static NumericEntityEscaper above(int codepoint) {
    return outsideOf(0, codepoint);
  }
  
  public static NumericEntityEscaper between(int codepointLow, int codepointHigh) {
    return new NumericEntityEscaper(codepointLow, codepointHigh, true);
  }
  
  public static NumericEntityEscaper outsideOf(int codepointLow, int codepointHigh) {
    return new NumericEntityEscaper(codepointLow, codepointHigh, false);
  }
  
  public boolean translate(int codepoint, Writer out) throws IOException {
    if (this.between) {
      if (codepoint < this.below || codepoint > this.above)
        return false; 
    } else if (codepoint >= this.below && codepoint <= this.above) {
      return false;
    } 
    out.write("&#");
    out.write(Integer.toString(codepoint, 10));
    out.write(59);
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\translate\NumericEntityEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */