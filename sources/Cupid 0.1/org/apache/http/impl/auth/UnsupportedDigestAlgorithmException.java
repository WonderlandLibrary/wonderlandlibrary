package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;

@Immutable
public class UnsupportedDigestAlgorithmException extends RuntimeException {
  private static final long serialVersionUID = 319558534317118022L;
  
  public UnsupportedDigestAlgorithmException() {}
  
  public UnsupportedDigestAlgorithmException(String message) {
    super(message);
  }
  
  public UnsupportedDigestAlgorithmException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\auth\UnsupportedDigestAlgorithmException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */