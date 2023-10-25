package org.apache.logging.log4j.core.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "loggers", category = "Core")
public final class LoggersPlugin {
  @PluginFactory
  public static Loggers createLoggers(@PluginElement("Loggers") LoggerConfig[] loggers) {
    ConcurrentMap<String, LoggerConfig> loggerMap = new ConcurrentHashMap<>();
    LoggerConfig root = null;
    for (LoggerConfig logger : loggers) {
      if (logger != null) {
        if (logger.getName().isEmpty()) {
          if (root != null)
            throw new IllegalStateException("Configuration has multiple root loggers. There can be only one."); 
          root = logger;
        } 
        loggerMap.put(logger.getName(), logger);
      } 
    } 
    return new Loggers(loggerMap, root);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\LoggersPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */