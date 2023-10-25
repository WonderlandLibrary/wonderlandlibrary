package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthenticationException;

@Immutable
public class NTLMEngineException extends AuthenticationException {
  private static final long serialVersionUID = 6027981323731768824L;
  
  public NTLMEngineException() {}
  
  public NTLMEngineException(String message) {
    super(message);
  }
  
  public NTLMEngineException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\auth\NTLMEngineException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */