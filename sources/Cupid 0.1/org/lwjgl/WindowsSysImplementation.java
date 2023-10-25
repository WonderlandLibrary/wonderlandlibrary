package org.lwjgl;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.opengl.Display;

final class WindowsSysImplementation extends DefaultSysImplementation {
  private static final int JNI_VERSION = 24;
  
  static {
    Sys.initialize();
  }
  
  public int getRequiredJNIVersion() {
    return 24;
  }
  
  public long getTimerResolution() {
    return 1000L;
  }
  
  public long getTime() {
    return nGetTime();
  }
  
  public boolean has64Bit() {
    return true;
  }
  
  private static long getHwnd() {
    if (!Display.isCreated())
      return 0L; 
    try {
      return ((Long)AccessController.<Long>doPrivileged(new PrivilegedExceptionAction<Long>() {
            public Long run() throws Exception {
              Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation", new Class[0]);
              getImplementation_method.setAccessible(true);
              Object display_impl = getImplementation_method.invoke(null, new Object[0]);
              Class<?> WindowsDisplay_class = Class.forName("org.lwjgl.opengl.WindowsDisplay");
              Method getHwnd_method = WindowsDisplay_class.getDeclaredMethod("getHwnd", new Class[0]);
              getHwnd_method.setAccessible(true);
              return (Long)getHwnd_method.invoke(display_impl, new Object[0]);
            }
          })).longValue();
    } catch (PrivilegedActionException e) {
      throw new Error(e);
    } 
  }
  
  public void alert(String title, String message) {
    if (!Display.isCreated())
      initCommonControls(); 
    LWJGLUtil.log(String.format("*** Alert *** %s\n%s\n", new Object[] { title, message }));
    ByteBuffer titleText = MemoryUtil.encodeUTF16(title);
    ByteBuffer messageText = MemoryUtil.encodeUTF16(message);
    nAlert(getHwnd(), MemoryUtil.getAddress(titleText), MemoryUtil.getAddress(messageText));
  }
  
  public boolean openURL(String url) {
    try {
      LWJGLUtil.execPrivileged(new String[] { "rundll32", "url.dll,FileProtocolHandler", url });
      return true;
    } catch (Exception e) {
      LWJGLUtil.log("Failed to open url (" + url + "): " + e.getMessage());
      return false;
    } 
  }
  
  public String getClipboard() {
    return nGetClipboard();
  }
  
  private static native long nGetTime();
  
  private static native void nAlert(long paramLong1, long paramLong2, long paramLong3);
  
  private static native void initCommonControls();
  
  private static native String nGetClipboard();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\WindowsSysImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */