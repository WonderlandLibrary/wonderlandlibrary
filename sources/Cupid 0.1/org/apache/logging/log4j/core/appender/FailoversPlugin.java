package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "failovers", category = "Core")
public final class FailoversPlugin {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  @PluginFactory
  public static String[] createFailovers(@PluginElement("AppenderRef") AppenderRef... refs) {
    if (refs == null) {
      LOGGER.error("failovers must contain an appender reference");
      return null;
    } 
    String[] arr = new String[refs.length];
    for (int i = 0; i < refs.length; i++)
      arr[i] = refs[i].getRef(); 
    return arr;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\FailoversPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */