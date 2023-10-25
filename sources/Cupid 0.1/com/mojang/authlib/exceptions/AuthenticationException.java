package com.mojang.authlib.exceptions;

public class AuthenticationException extends Exception {
  public AuthenticationException() {}
  
  public AuthenticationException(String message) {
    super(message);
  }
  
  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public AuthenticationException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\exceptions\AuthenticationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */