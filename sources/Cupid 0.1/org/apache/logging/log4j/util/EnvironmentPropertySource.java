package org.apache.logging.log4j.util;

import java.util.Map;

public class EnvironmentPropertySource implements PropertySource {
  private static final String PREFIX = "LOG4J_";
  
  private static final int DEFAULT_PRIORITY = -100;
  
  public int getPriority() {
    return -100;
  }
  
  public void forEach(BiConsumer<String, String> action) {
    Map<String, String> getenv;
    try {
      getenv = System.getenv();
    } catch (SecurityException e) {
      LowLevelLogUtil.logException("The system environment variables are not available to Log4j due to security restrictions: " + e, e);
      return;
    } 
    for (Map.Entry<String, String> entry : getenv.entrySet()) {
      String key = entry.getKey();
      if (key.startsWith("LOG4J_"))
        action.accept(key.substring("LOG4J_".length()), entry.getValue()); 
    } 
  }
  
  public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
    StringBuilder sb = new StringBuilder("LOG4J");
    for (CharSequence token : tokens) {
      sb.append('_');
      for (int i = 0; i < token.length(); i++)
        sb.append(Character.toUpperCase(token.charAt(i))); 
    } 
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\EnvironmentPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */