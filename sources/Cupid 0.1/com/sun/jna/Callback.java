package com.sun.jna;

import java.util.Arrays;
import java.util.Collection;

public interface Callback {
  public static final String METHOD_NAME = "callback";
  
  public static final Collection FORBIDDEN_NAMES = Arrays.asList(new String[] { "hashCode", "equals", "toString" });
  
  public static interface UncaughtExceptionHandler {
    void uncaughtException(Callback param1Callback, Throwable param1Throwable);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\Callback.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */