package com.sun.jna.win32;

import com.sun.jna.Callback;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;

public interface StdCallLibrary extends Library, StdCall {
  public static final int STDCALL_CONVENTION = 1;
  
  public static final FunctionMapper FUNCTION_MAPPER = new StdCallFunctionMapper();
  
  public static interface StdCallCallback extends Callback, StdCall {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\win32\StdCallLibrary.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */