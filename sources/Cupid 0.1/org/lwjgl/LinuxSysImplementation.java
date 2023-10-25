package org.lwjgl;

import java.awt.Toolkit;
import java.security.AccessController;
import java.security.PrivilegedAction;

final class LinuxSysImplementation extends J2SESysImplementation {
  private static final int JNI_VERSION = 19;
  
  static {
    Toolkit.getDefaultToolkit();
    AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            try {
              System.loadLibrary("jawt");
            } catch (UnsatisfiedLinkError e) {}
            return null;
          }
        });
  }
  
  public int getRequiredJNIVersion() {
    return 19;
  }
  
  public boolean openURL(String url) {
    String[] browsers = { 
        "sensible-browser", "xdg-open", "google-chrome", "chromium", "firefox", "iceweasel", "mozilla", "opera", "konqueror", "nautilus", 
        "galeon", "netscape" };
    for (String browser : browsers) {
      try {
        LWJGLUtil.execPrivileged(new String[] { browser, url });
        return true;
      } catch (Exception e) {
        e.printStackTrace(System.err);
      } 
    } 
    return false;
  }
  
  public boolean has64Bit() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\LinuxSysImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */