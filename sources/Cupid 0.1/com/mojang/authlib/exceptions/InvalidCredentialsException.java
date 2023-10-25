package com.mojang.authlib.exceptions;

public class InvalidCredentialsException extends AuthenticationException {
  public InvalidCredentialsException() {}
  
  public InvalidCredentialsException(String message) {
    super(message);
  }
  
  public InvalidCredentialsException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public InvalidCredentialsException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\exceptions\InvalidCredentialsException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */