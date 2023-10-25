package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "event", category = "Lookup")
public class EventLookup extends AbstractLookup {
  public String lookup(LogEvent event, String key) {
    if (event == null)
      return null; 
    switch (key) {
      case "Marker":
        return (event.getMarker() != null) ? event.getMarker().getName() : null;
      case "ThreadName":
        return event.getThreadName();
      case "Level":
        return event.getLevel().toString();
      case "ThreadId":
        return Long.toString(event.getThreadId());
      case "Timestamp":
        return Long.toString(event.getTimeMillis());
      case "Exception":
        if (event.getThrown() != null)
          return event.getThrown().getClass().getSimpleName(); 
        if (event.getThrownProxy() != null)
          return event.getThrownProxy().getName(); 
        return null;
      case "Logger":
        return event.getLoggerName();
      case "Message":
        return event.getMessage().getFormattedMessage();
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\EventLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */