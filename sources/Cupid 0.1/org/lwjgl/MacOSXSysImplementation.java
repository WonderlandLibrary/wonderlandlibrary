package org.lwjgl;

import com.apple.eio.FileManager;
import java.awt.Toolkit;

final class MacOSXSysImplementation extends J2SESysImplementation {
  private static final int JNI_VERSION = 25;
  
  static {
    Toolkit.getDefaultToolkit();
  }
  
  public int getRequiredJNIVersion() {
    return 25;
  }
  
  public boolean openURL(String url) {
    try {
      FileManager.openURL(url);
      return true;
    } catch (Exception e) {
      LWJGLUtil.log("Exception occurred while trying to invoke browser: " + e);
      return false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\MacOSXSysImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */