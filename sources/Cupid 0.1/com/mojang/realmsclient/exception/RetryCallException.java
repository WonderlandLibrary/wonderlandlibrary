package com.mojang.realmsclient.exception;

public class RetryCallException extends RealmsServiceException {
  public static final int DEFAULT_DELAY = 5;
  
  public final int delaySeconds;
  
  public RetryCallException(int delaySeconds) {
    super(503, "Retry operation", -1, "");
    if (delaySeconds < 0 || delaySeconds > 120) {
      this.delaySeconds = 5;
    } else {
      this.delaySeconds = delaySeconds;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\exception\RetryCallException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */