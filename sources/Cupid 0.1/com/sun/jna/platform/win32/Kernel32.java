package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;
import java.nio.Buffer;

public interface Kernel32 extends WinNT {
  public static final Kernel32 INSTANCE = (Kernel32)Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.UNICODE_OPTIONS);
  
  int FormatMessage(int paramInt1, Pointer paramPointer1, int paramInt2, int paramInt3, Buffer paramBuffer, int paramInt4, Pointer paramPointer2);
  
  boolean ReadFile(WinNT.HANDLE paramHANDLE, Buffer paramBuffer, int paramInt, IntByReference paramIntByReference, WinBase.OVERLAPPED paramOVERLAPPED);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Kernel32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */