package com.mojang.authlib.yggdrasil;

public class ProfileNotFoundException extends RuntimeException {
  public ProfileNotFoundException() {}
  
  public ProfileNotFoundException(String message) {
    super(message);
  }
  
  public ProfileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public ProfileNotFoundException(Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\authlib\yggdrasil\ProfileNotFoundException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */