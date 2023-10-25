package org.apache.logging.log4j.core.appender.rolling;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public final class FileSize {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final long KB = 1024L;
  
  private static final long MB = 1048576L;
  
  private static final long GB = 1073741824L;
  
  private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
  
  public static long parse(String string, long defaultValue) {
    Matcher matcher = VALUE_PATTERN.matcher(string);
    if (matcher.matches())
      try {
        double value = NumberFormat.getNumberInstance(Locale.ROOT).parse(matcher.group(1)).doubleValue();
        String units = matcher.group(3);
        if (units.isEmpty())
          return (long)value; 
        if (units.equalsIgnoreCase("K"))
          return (long)(value * 1024.0D); 
        if (units.equalsIgnoreCase("M"))
          return (long)(value * 1048576.0D); 
        if (units.equalsIgnoreCase("G"))
          return (long)(value * 1.073741824E9D); 
        LOGGER.error("FileSize units not recognized: " + string);
        return defaultValue;
      } catch (ParseException e) {
        LOGGER.error("FileSize unable to parse numeric part: " + string, e);
        return defaultValue;
      }  
    LOGGER.error("FileSize unable to parse bytes: " + string);
    return defaultValue;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\FileSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */