package org.lwjgl;

abstract class DefaultSysImplementation implements SysImplementation {
  public native int getJNIVersion();
  
  public native int getPointerSize();
  
  public native void setDebug(boolean paramBoolean);
  
  public long getTimerResolution() {
    return 1000L;
  }
  
  public boolean has64Bit() {
    return false;
  }
  
  public abstract long getTime();
  
  public abstract void alert(String paramString1, String paramString2);
  
  public abstract String getClipboard();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\DefaultSysImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */