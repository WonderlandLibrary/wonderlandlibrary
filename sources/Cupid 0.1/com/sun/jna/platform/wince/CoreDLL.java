package com.sun.jna.platform.wince;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public interface CoreDLL extends WinNT {
  public static final CoreDLL INSTANCE = (CoreDLL)Native.loadLibrary("coredll", CoreDLL.class, W32APIOptions.UNICODE_OPTIONS);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\wince\CoreDLL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */