package org.apache.logging.log4j.core.parser;

public class ParseException extends Exception {
  private static final long serialVersionUID = -2739649998196663857L;
  
  public ParseException(String message) {
    super(message);
  }
  
  public ParseException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public ParseException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\parser\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */