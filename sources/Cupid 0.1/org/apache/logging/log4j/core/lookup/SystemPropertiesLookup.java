package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "sys", category = "Lookup")
public class SystemPropertiesLookup extends AbstractLookup {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
  
  public String lookup(LogEvent event, String key) {
    try {
      return System.getProperty(key);
    } catch (Exception ex) {
      LOGGER.warn(LOOKUP, "Error while getting system property [{}].", key, ex);
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\SystemPropertiesLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */