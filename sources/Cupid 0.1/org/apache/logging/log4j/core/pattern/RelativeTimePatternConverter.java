package org.apache.logging.log4j.core.pattern;

import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "RelativeTimePatternConverter", category = "Converter")
@ConverterKeys({"r", "relative"})
@PerformanceSensitive({"allocation"})
public class RelativeTimePatternConverter extends LogEventPatternConverter {
  private final long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
  
  public RelativeTimePatternConverter() {
    super("Time", "time");
  }
  
  public static RelativeTimePatternConverter newInstance(String[] options) {
    return new RelativeTimePatternConverter();
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    long timestamp = event.getTimeMillis();
    toAppendTo.append(timestamp - this.startTime);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\RelativeTimePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */