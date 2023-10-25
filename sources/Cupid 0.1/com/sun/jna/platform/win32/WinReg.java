package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface WinReg extends StdCallLibrary {
  public static class HKEY extends WinNT.HANDLE {
    public HKEY() {}
    
    public HKEY(Pointer p) {
      super(p);
    }
    
    public HKEY(int value) {
      super(new Pointer(value));
    }
  }
  
  public static class HKEYByReference extends ByReference {
    public HKEYByReference() {
      this(null);
    }
    
    public HKEYByReference(WinReg.HKEY h) {
      super(Pointer.SIZE);
      setValue(h);
    }
    
    public void setValue(WinReg.HKEY h) {
      getPointer().setPointer(0L, (h != null) ? h.getPointer() : null);
    }
    
    public WinReg.HKEY getValue() {
      Pointer p = getPointer().getPointer(0L);
      if (p == null)
        return null; 
      if (WinBase.INVALID_HANDLE_VALUE.getPointer().equals(p))
        return (WinReg.HKEY)WinBase.INVALID_HANDLE_VALUE; 
      WinReg.HKEY h = new WinReg.HKEY();
      h.setPointer(p);
      return h;
    }
  }
  
  public static final HKEY HKEY_CLASSES_ROOT = new HKEY(-2147483648);
  
  public static final HKEY HKEY_CURRENT_USER = new HKEY(-2147483647);
  
  public static final HKEY HKEY_LOCAL_MACHINE = new HKEY(-2147483646);
  
  public static final HKEY HKEY_USERS = new HKEY(-2147483645);
  
  public static final HKEY HKEY_PERFORMANCE_DATA = new HKEY(-2147483644);
  
  public static final HKEY HKEY_PERFORMANCE_TEXT = new HKEY(-2147483568);
  
  public static final HKEY HKEY_PERFORMANCE_NLSTEXT = new HKEY(-2147483552);
  
  public static final HKEY HKEY_CURRENT_CONFIG = new HKEY(-2147483643);
  
  public static final HKEY HKEY_DYN_DATA = new HKEY(-2147483642);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\WinReg.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */