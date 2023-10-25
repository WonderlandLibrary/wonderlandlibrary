package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(name = "sd", category = "Lookup")
public class StructuredDataLookup implements StrLookup {
  public String lookup(String key) {
    return null;
  }
  
  public String lookup(LogEvent event, String key) {
    if (event == null || !(event.getMessage() instanceof StructuredDataMessage))
      return null; 
    StructuredDataMessage msg = (StructuredDataMessage)event.getMessage();
    if (key.equalsIgnoreCase("id"))
      return msg.getId().getName(); 
    if (key.equalsIgnoreCase("type"))
      return msg.getType(); 
    return msg.get(key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\StructuredDataLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */