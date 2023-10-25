package org.apache.logging.log4j.util;

public final class Chars {
  public static final char CR = '\r';
  
  public static final char DQUOTE = '"';
  
  public static final char EQ = '=';
  
  public static final char LF = '\n';
  
  public static final char NUL = '\000';
  
  public static final char QUOTE = '\'';
  
  public static final char SPACE = ' ';
  
  public static final char TAB = '\t';
  
  public static char getUpperCaseHex(int digit) {
    if (digit < 0 || digit >= 16)
      return Character.MIN_VALUE; 
    return (digit < 10) ? getNumericalDigit(digit) : getUpperCaseAlphaDigit(digit);
  }
  
  public static char getLowerCaseHex(int digit) {
    if (digit < 0 || digit >= 16)
      return Character.MIN_VALUE; 
    return (digit < 10) ? getNumericalDigit(digit) : getLowerCaseAlphaDigit(digit);
  }
  
  private static char getNumericalDigit(int digit) {
    return (char)(48 + digit);
  }
  
  private static char getUpperCaseAlphaDigit(int digit) {
    return (char)(65 + digit - 10);
  }
  
  private static char getLowerCaseAlphaDigit(int digit) {
    return (char)(97 + digit - 10);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\Chars.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */