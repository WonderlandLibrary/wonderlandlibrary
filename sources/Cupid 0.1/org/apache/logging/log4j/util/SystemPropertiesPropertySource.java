package org.apache.logging.log4j.util;

import java.util.Objects;
import java.util.Properties;

public class SystemPropertiesPropertySource implements PropertySource {
  private static final int DEFAULT_PRIORITY = 100;
  
  private static final String PREFIX = "log4j2.";
  
  public int getPriority() {
    return 100;
  }
  
  public void forEach(BiConsumer<String, String> action) {
    Properties properties;
    Object[] keySet;
    try {
      properties = System.getProperties();
    } catch (SecurityException e) {
      return;
    } 
    synchronized (properties) {
      keySet = properties.keySet().toArray();
    } 
    for (Object key : keySet) {
      String keyStr = Objects.toString(key, null);
      action.accept(keyStr, properties.getProperty(keyStr));
    } 
  }
  
  public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
    return "log4j2." + PropertySource.Util.joinAsCamelCase(tokens);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\SystemPropertiesPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */