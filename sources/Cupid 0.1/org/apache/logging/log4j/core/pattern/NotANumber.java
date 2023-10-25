package org.apache.logging.log4j.core.pattern;

public final class NotANumber {
  public static final NotANumber NAN = new NotANumber();
  
  public static final String VALUE = "\000";
  
  public String toString() {
    return "\000";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\NotANumber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */