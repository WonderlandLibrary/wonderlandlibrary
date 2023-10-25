package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Ole32 extends StdCallLibrary {
  public static final Ole32 INSTANCE = (Ole32)Native.loadLibrary("Ole32", Ole32.class, W32APIOptions.UNICODE_OPTIONS);
  
  WinNT.HRESULT CoCreateGuid(Guid.GUID.ByReference paramByReference);
  
  int StringFromGUID2(Guid.GUID.ByReference paramByReference, char[] paramArrayOfchar, int paramInt);
  
  WinNT.HRESULT IIDFromString(String paramString, Guid.GUID.ByReference paramByReference);
  
  WinNT.HRESULT CoInitializeEx(Pointer paramPointer, int paramInt);
  
  void CoUninitialize();
  
  WinNT.HRESULT CoCreateInstance(Guid.GUID paramGUID1, Pointer paramPointer, int paramInt, Guid.GUID paramGUID2, PointerByReference paramPointerByReference);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Ole32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */