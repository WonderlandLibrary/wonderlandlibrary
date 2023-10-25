package com.ibm.icu.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.MissingResourceException;
import java.util.Properties;

public class ICUConfig {
  public static final String CONFIG_PROPS_FILE = "/com/ibm/icu/ICUConfig.properties";
  
  private static final Properties CONFIG_PROPS = new Properties();
  
  static {
    try {
      InputStream is = ICUData.getStream("/com/ibm/icu/ICUConfig.properties");
      if (is != null)
        CONFIG_PROPS.load(is); 
    } catch (MissingResourceException mre) {
    
    } catch (IOException ioe) {}
  }
  
  public static String get(String name) {
    return get(name, null);
  }
  
  public static String get(String name, String def) {
    String val = null;
    final String fname = name;
    if (System.getSecurityManager() != null) {
      try {
        val = AccessController.<String>doPrivileged(new PrivilegedAction<String>() {
              public String run() {
                return System.getProperty(fname);
              }
            });
      } catch (AccessControlException e) {}
    } else {
      val = System.getProperty(name);
    } 
    if (val == null)
      val = CONFIG_PROPS.getProperty(name, def); 
    return val;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */