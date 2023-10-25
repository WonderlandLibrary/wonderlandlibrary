package org.apache.logging.log4j.core.util;

public final class Patterns {
  public static final String COMMA_SEPARATOR = toWhitespaceSeparator(",");
  
  public static final String WHITESPACE = "\\s*";
  
  public static String toWhitespaceSeparator(String separator) {
    return "\\s*" + separator + "\\s*";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Patterns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */