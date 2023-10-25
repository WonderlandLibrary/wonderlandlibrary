package com.sun.jna.platform.win32;

public interface NTStatus {
  public static final int STATUS_SUCCESS = 0;
  
  public static final int STATUS_BUFFER_TOO_SMALL = -1073741789;
  
  public static final int STATUS_WAIT_0 = 0;
  
  public static final int STATUS_WAIT_1 = 1;
  
  public static final int STATUS_WAIT_2 = 2;
  
  public static final int STATUS_WAIT_3 = 3;
  
  public static final int STATUS_WAIT_63 = 63;
  
  public static final int STATUS_ABANDONED = 128;
  
  public static final int STATUS_ABANDONED_WAIT_0 = 128;
  
  public static final int STATUS_ABANDONED_WAIT_63 = 191;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\NTStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */