package org.apache.http.auth;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class MalformedChallengeException extends ProtocolException {
  private static final long serialVersionUID = 814586927989932284L;
  
  public MalformedChallengeException() {}
  
  public MalformedChallengeException(String message) {
    super(message);
  }
  
  public MalformedChallengeException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\auth\MalformedChallengeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */