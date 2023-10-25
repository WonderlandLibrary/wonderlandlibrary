package org.apache.logging.log4j.util;

public final class Constants {
  public static final boolean IS_WEB_APP = PropertiesUtil.getProperties().getBooleanProperty("log4j2.is.webapp", (
      isClassAvailable("javax.servlet.Servlet") || 
      isClassAvailable("jakarta.servlet.Servlet")));
  
  public static final boolean ENABLE_THREADLOCALS = (!IS_WEB_APP && PropertiesUtil.getProperties().getBooleanProperty("log4j2.enable.threadlocals", true));
  
  public static final int JAVA_MAJOR_VERSION = getMajorVersion();
  
  public static final int MAX_REUSABLE_MESSAGE_SIZE = size("log4j.maxReusableMsgSize", 518);
  
  public static final String LOG4J2_DEBUG = "log4j2.debug";
  
  private static int size(String property, int defaultValue) {
    return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
  }
  
  private static boolean isClassAvailable(String className) {
    try {
      return (LoaderUtil.loadClass(className) != null);
    } catch (Throwable e) {
      return false;
    } 
  }
  
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  
  private static int getMajorVersion() {
    return getMajorVersion(System.getProperty("java.version"));
  }
  
  static int getMajorVersion(String version) {
    String[] parts = version.split("-|\\.");
    try {
      int token = Integer.parseInt(parts[0]);
      boolean isJEP223 = (token != 1);
      if (isJEP223)
        return token; 
      return Integer.parseInt(parts[1]);
    } catch (Exception ex) {
      return 0;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\Constants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */