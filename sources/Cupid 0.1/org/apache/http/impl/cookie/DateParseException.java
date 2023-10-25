package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;

@Immutable
public class DateParseException extends Exception {
  private static final long serialVersionUID = 4417696455000643370L;
  
  public DateParseException() {}
  
  public DateParseException(String message) {
    super(message);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\cookie\DateParseException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */