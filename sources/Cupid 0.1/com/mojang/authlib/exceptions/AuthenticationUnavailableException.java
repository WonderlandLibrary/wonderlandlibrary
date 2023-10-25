package com.mojang.authlib.exceptions;

public class AuthenticationUnavailableException extends AuthenticationException {
  public AuthenticationUnavailableException() {}
  
  public AuthenticationUnavailableException(String message) {
    super(message);
  }
  
  public AuthenticationUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public AuthenticationUnavailableException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\exceptions\AuthenticationUnavailableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */