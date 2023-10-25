package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "env", category = "Lookup")
public class EnvironmentLookup extends AbstractLookup {
  public String lookup(LogEvent event, String key) {
    return System.getenv(key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\EnvironmentLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */