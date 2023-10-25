package com.mojang.authlib.yggdrasil;

public class ProfileIncompleteException extends RuntimeException {
  public ProfileIncompleteException() {}
  
  public ProfileIncompleteException(String message) {
    super(message);
  }
  
  public ProfileIncompleteException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public ProfileIncompleteException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\ProfileIncompleteException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */