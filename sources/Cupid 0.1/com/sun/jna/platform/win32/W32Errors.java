package com.sun.jna.platform.win32;

public abstract class W32Errors implements WinError {
  public static final boolean SUCCEEDED(int hr) {
    return (hr >= 0);
  }
  
  public static final boolean FAILED(int hr) {
    return (hr < 0);
  }
  
  public static final int HRESULT_CODE(int hr) {
    return hr & 0xFFFF;
  }
  
  public static final int SCODE_CODE(int sc) {
    return sc & 0xFFFF;
  }
  
  public static final int HRESULT_FACILITY(int hr) {
    return (hr >>= 16) & 0x1FFF;
  }
  
  public static final int SCODE_FACILITY(short sc) {
    return (sc = (short)(sc >> 16)) & 0x1FFF;
  }
  
  public static short HRESULT_SEVERITY(int hr) {
    return (short)((hr >>= 31) & 0x1);
  }
  
  public static short SCODE_SEVERITY(short sc) {
    return (short)((sc = (short)(sc >> 31)) & 0x1);
  }
  
  public static int MAKE_HRESULT(short sev, short fac, short code) {
    return sev << 31 | fac << 16 | code;
  }
  
  public static final int MAKE_SCODE(short sev, short fac, short code) {
    return sev << 31 | fac << 16 | code;
  }
  
  public static final WinNT.HRESULT HRESULT_FROM_WIN32(int x) {
    int f = 7;
    return new WinNT.HRESULT((x <= 0) ? x : (x & 0xFFFF | (f <<= 16) | Integer.MIN_VALUE));
  }
  
  public static final int FILTER_HRESULT_FROM_FLT_NTSTATUS(int x) {
    int f = 31;
    return x & 0x8000FFFF | (f <<= 16);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\W32Errors.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */