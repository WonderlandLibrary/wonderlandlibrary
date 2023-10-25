package org.apache.logging.log4j.util;

import java.util.Map;
import java.util.Properties;

public class PropertiesPropertySource implements PropertySource {
  private static final String PREFIX = "log4j2.";
  
  private final Properties properties;
  
  public PropertiesPropertySource(Properties properties) {
    this.properties = properties;
  }
  
  public int getPriority() {
    return 0;
  }
  
  public void forEach(BiConsumer<String, String> action) {
    for (Map.Entry<Object, Object> entry : this.properties.entrySet())
      action.accept((String)entry.getKey(), (String)entry.getValue()); 
  }
  
  public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
    return "log4j2." + PropertySource.Util.joinAsCamelCase(tokens);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\PropertiesPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */