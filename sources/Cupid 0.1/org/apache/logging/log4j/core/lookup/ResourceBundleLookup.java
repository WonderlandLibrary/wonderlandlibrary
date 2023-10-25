package org.apache.logging.log4j.core.lookup;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "bundle", category = "Lookup")
public class ResourceBundleLookup extends AbstractLookup {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
  
  public String lookup(LogEvent event, String key) {
    if (key == null)
      return null; 
    String[] keys = key.split(":");
    int keyLen = keys.length;
    if (keyLen != 2) {
      LOGGER.warn(LOOKUP, "Bad ResourceBundle key format [{}]. Expected format is BundleName:KeyName.", key);
      return null;
    } 
    String bundleName = keys[0];
    String bundleKey = keys[1];
    try {
      return ResourceBundle.getBundle(bundleName).getString(bundleKey);
    } catch (MissingResourceException e) {
      LOGGER.warn(LOOKUP, "Error looking up ResourceBundle [{}].", bundleName, e);
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\ResourceBundleLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */