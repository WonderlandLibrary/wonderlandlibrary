package org.apache.commons.lang3.text.translate;

public class JavaUnicodeEscaper extends UnicodeEscaper {
  public static JavaUnicodeEscaper above(int codepoint) {
    return outsideOf(0, codepoint);
  }
  
  public static JavaUnicodeEscaper below(int codepoint) {
    return outsideOf(codepoint, 2147483647);
  }
  
  public static JavaUnicodeEscaper between(int codepointLow, int codepointHigh) {
    return new JavaUnicodeEscaper(codepointLow, codepointHigh, true);
  }
  
  public static JavaUnicodeEscaper outsideOf(int codepointLow, int codepointHigh) {
    return new JavaUnicodeEscaper(codepointLow, codepointHigh, false);
  }
  
  public JavaUnicodeEscaper(int below, int above, boolean between) {
    super(below, above, between);
  }
  
  protected String toUtf16Escape(int codepoint) {
    char[] surrogatePair = Character.toChars(codepoint);
    return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1]);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\translate\JavaUnicodeEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */