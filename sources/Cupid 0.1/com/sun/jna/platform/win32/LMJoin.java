package com.sun.jna.platform.win32;

import com.sun.jna.win32.StdCallLibrary;

public interface LMJoin extends StdCallLibrary {
  public static abstract class NETSETUP_JOIN_STATUS {
    public static final int NetSetupUnknownStatus = 0;
    
    public static final int NetSetupUnjoined = 1;
    
    public static final int NetSetupWorkgroupName = 2;
    
    public static final int NetSetupDomainName = 3;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\LMJoin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */