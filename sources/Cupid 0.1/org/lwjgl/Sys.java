package org.lwjgl;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.input.Mouse;

public final class Sys {
  private static final String JNI_LIBRARY_NAME = "lwjgl";
  
  private static final String VERSION = "2.9.4";
  
  private static final String POSTFIX64BIT = "64";
  
  private static void doLoadLibrary(final String lib_name) {
    AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            String library_path = System.getProperty("org.lwjgl.librarypath");
            if (library_path != null) {
              System.load(library_path + File.separator + LWJGLUtil.mapLibraryName(lib_name));
            } else {
              System.loadLibrary(lib_name);
            } 
            return null;
          }
        });
  }
  
  private static void loadLibrary(String lib_name) {
    String osArch = System.getProperty("os.arch");
    boolean try64First = (LWJGLUtil.getPlatform() != 2 && ("amd64".equals(osArch) || "x86_64".equals(osArch)));
    Error err = null;
    if (try64First)
      try {
        doLoadLibrary(lib_name + "64");
        return;
      } catch (UnsatisfiedLinkError e) {
        err = e;
      }  
    try {
      doLoadLibrary(lib_name);
    } catch (UnsatisfiedLinkError e) {
      if (try64First)
        throw err; 
      if (implementation.has64Bit())
        try {
          doLoadLibrary(lib_name + "64");
          return;
        } catch (UnsatisfiedLinkError e2) {
          LWJGLUtil.log("Failed to load 64 bit library: " + e2.getMessage());
        }  
      throw e;
    } 
  }
  
  private static final SysImplementation implementation = createImplementation();
  
  static {
    loadLibrary("lwjgl");
  }
  
  private static final boolean is64Bit = (implementation.getPointerSize() == 8);
  
  static {
    int native_jni_version = implementation.getJNIVersion();
    int required_version = implementation.getRequiredJNIVersion();
    if (native_jni_version != required_version)
      throw new LinkageError("Version mismatch: jar version is '" + required_version + "', native library version is '" + native_jni_version + "'"); 
    implementation.setDebug(LWJGLUtil.DEBUG);
  }
  
  private static SysImplementation createImplementation() {
    switch (LWJGLUtil.getPlatform()) {
      case 1:
        return new LinuxSysImplementation();
      case 3:
        return new WindowsSysImplementation();
      case 2:
        return new MacOSXSysImplementation();
    } 
    throw new IllegalStateException("Unsupported platform");
  }
  
  public static String getVersion() {
    return "2.9.4";
  }
  
  public static void initialize() {}
  
  public static boolean is64Bit() {
    return is64Bit;
  }
  
  public static long getTimerResolution() {
    return implementation.getTimerResolution();
  }
  
  public static long getTime() {
    return implementation.getTime() & Long.MAX_VALUE;
  }
  
  public static void alert(String title, String message) {
    boolean grabbed = Mouse.isGrabbed();
    if (grabbed)
      Mouse.setGrabbed(false); 
    if (title == null)
      title = ""; 
    if (message == null)
      message = ""; 
    implementation.alert(title, message);
    if (grabbed)
      Mouse.setGrabbed(true); 
  }
  
  public static boolean openURL(String url) {
    try {
      final Class<?> serviceManagerClass = Class.forName("javax.jnlp.ServiceManager");
      Method lookupMethod = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
            public Method run() throws Exception {
              return serviceManagerClass.getMethod("lookup", new Class[] { String.class });
            }
          });
      Object basicService = lookupMethod.invoke(serviceManagerClass, new Object[] { "javax.jnlp.BasicService" });
      final Class<?> basicServiceClass = Class.forName("javax.jnlp.BasicService");
      Method showDocumentMethod = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
            public Method run() throws Exception {
              return basicServiceClass.getMethod("showDocument", new Class[] { URL.class });
            }
          });
      try {
        Boolean ret = (Boolean)showDocumentMethod.invoke(basicService, new Object[] { new URL(url) });
        return ret.booleanValue();
      } catch (MalformedURLException e) {
        e.printStackTrace(System.err);
        return false;
      } 
    } catch (Exception ue) {
      return implementation.openURL(url);
    } 
  }
  
  public static String getClipboard() {
    return implementation.getClipboard();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\Sys.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */