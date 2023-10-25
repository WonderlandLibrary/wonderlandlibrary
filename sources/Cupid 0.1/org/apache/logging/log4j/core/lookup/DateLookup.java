package org.apache.logging.log4j.core.lookup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "date", category = "Lookup")
public class DateLookup implements StrLookup {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
  
  public String lookup(String key) {
    return formatDate(System.currentTimeMillis(), key);
  }
  
  public String lookup(LogEvent event, String key) {
    return (event == null) ? lookup(key) : formatDate(event.getTimeMillis(), key);
  }
  
  private String formatDate(long date, String format) {
    DateFormat dateFormat = null;
    if (format != null)
      try {
        dateFormat = new SimpleDateFormat(format);
      } catch (Exception ex) {
        LOGGER.error(LOOKUP, "Invalid date format: [{}], using default", format, ex);
      }  
    if (dateFormat == null)
      dateFormat = DateFormat.getInstance(); 
    return dateFormat.format(new Date(date));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\DateLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */