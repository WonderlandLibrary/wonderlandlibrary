package com.sun.jna.win32;

import java.util.HashMap;
import java.util.Map;

public interface W32APIOptions extends StdCallLibrary {
  public static final Map UNICODE_OPTIONS = new HashMap() {
    
    };
  
  public static final Map ASCII_OPTIONS = new HashMap() {
    
    };
  
  public static final Map DEFAULT_OPTIONS = Boolean.getBoolean("w32.ascii") ? ASCII_OPTIONS : UNICODE_OPTIONS;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\win32\W32APIOptions.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */