package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Shell32 extends ShellAPI, StdCallLibrary {
  public static final Shell32 INSTANCE = (Shell32)Native.loadLibrary("shell32", Shell32.class, W32APIOptions.UNICODE_OPTIONS);
  
  int SHFileOperation(ShellAPI.SHFILEOPSTRUCT paramSHFILEOPSTRUCT);
  
  WinNT.HRESULT SHGetFolderPath(WinDef.HWND paramHWND, int paramInt, WinNT.HANDLE paramHANDLE, WinDef.DWORD paramDWORD, char[] paramArrayOfchar);
  
  WinNT.HRESULT SHGetDesktopFolder(PointerByReference paramPointerByReference);
  
  WinDef.HINSTANCE ShellExecute(WinDef.HWND paramHWND, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Shell32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */