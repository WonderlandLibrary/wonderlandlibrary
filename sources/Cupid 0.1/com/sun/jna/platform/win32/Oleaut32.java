package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Oleaut32 extends StdCallLibrary {
  public static final Oleaut32 INSTANCE = (Oleaut32)Native.loadLibrary("Oleaut32", Oleaut32.class, W32APIOptions.UNICODE_OPTIONS);
  
  Pointer SysAllocString(String paramString);
  
  void SysFreeString(Pointer paramPointer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Oleaut32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */