package com.mojang.authlib.exceptions;

public class UserMigratedException extends InvalidCredentialsException {
  public UserMigratedException() {}
  
  public UserMigratedException(String message) {
    super(message);
  }
  
  public UserMigratedException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public UserMigratedException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\exceptions\UserMigratedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */