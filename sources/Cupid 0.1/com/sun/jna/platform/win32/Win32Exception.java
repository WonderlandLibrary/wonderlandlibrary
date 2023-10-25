package com.sun.jna.platform.win32;

public class Win32Exception extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  private WinNT.HRESULT _hr;
  
  public WinNT.HRESULT getHR() {
    return this._hr;
  }
  
  public Win32Exception(WinNT.HRESULT hr) {
    super(Kernel32Util.formatMessageFromHR(hr));
    this._hr = hr;
  }
  
  public Win32Exception(int code) {
    this(W32Errors.HRESULT_FROM_WIN32(code));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\Win32Exception.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */