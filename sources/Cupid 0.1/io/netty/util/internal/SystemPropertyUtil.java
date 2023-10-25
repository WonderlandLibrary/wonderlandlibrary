package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class SystemPropertyUtil {
  private static boolean initializedLogger = false;
  
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(SystemPropertyUtil.class);
  
  private static boolean loggedException;
  
  private static final Pattern INTEGER_PATTERN;
  
  static {
    initializedLogger = true;
    INTEGER_PATTERN = Pattern.compile("-?[0-9]+");
  }
  
  public static boolean contains(String key) {
    return (get(key) != null);
  }
  
  public static String get(String key) {
    return get(key, null);
  }
  
  public static String get(final String key, String def) {
    if (key == null)
      throw new NullPointerException("key"); 
    if (key.isEmpty())
      throw new IllegalArgumentException("key must not be empty."); 
    String value = null;
    try {
      if (System.getSecurityManager() == null) {
        value = System.getProperty(key);
      } else {
        value = AccessController.<String>doPrivileged(new PrivilegedAction<String>() {
              public String run() {
                return System.getProperty(key);
              }
            });
      } 
    } catch (Exception e) {
      if (!loggedException) {
        log("Unable to retrieve a system property '" + key + "'; default values will be used.", e);
        loggedException = true;
      } 
    } 
    if (value == null)
      return def; 
    return value;
  }
  
  public static boolean getBoolean(String key, boolean def) {
    String value = get(key);
    if (value == null)
      return def; 
    value = value.trim().toLowerCase();
    if (value.isEmpty())
      return true; 
    if ("true".equals(value) || "yes".equals(value) || "1".equals(value))
      return true; 
    if ("false".equals(value) || "no".equals(value) || "0".equals(value))
      return false; 
    log("Unable to parse the boolean system property '" + key + "':" + value + " - " + "using the default value: " + def);
    return def;
  }
  
  public static int getInt(String key, int def) {
    String value = get(key);
    if (value == null)
      return def; 
    value = value.trim().toLowerCase();
    if (INTEGER_PATTERN.matcher(value).matches())
      try {
        return Integer.parseInt(value);
      } catch (Exception e) {} 
    log("Unable to parse the integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
    return def;
  }
  
  public static long getLong(String key, long def) {
    String value = get(key);
    if (value == null)
      return def; 
    value = value.trim().toLowerCase();
    if (INTEGER_PATTERN.matcher(value).matches())
      try {
        return Long.parseLong(value);
      } catch (Exception e) {} 
    log("Unable to parse the long integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
    return def;
  }
  
  private static void log(String msg) {
    if (initializedLogger) {
      logger.warn(msg);
    } else {
      Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg);
    } 
  }
  
  private static void log(String msg, Exception e) {
    if (initializedLogger) {
      logger.warn(msg, e);
    } else {
      Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg, e);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\SystemPropertyUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */