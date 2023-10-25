package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@Plugin(name = "MarkerPatternConverter", category = "Converter")
@ConverterKeys({"marker"})
@PerformanceSensitive({"allocation"})
public final class MarkerPatternConverter extends LogEventPatternConverter {
  private MarkerPatternConverter(String[] options) {
    super("Marker", "marker");
  }
  
  public static MarkerPatternConverter newInstance(String[] options) {
    return new MarkerPatternConverter(options);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    Marker marker = event.getMarker();
    if (marker != null)
      StringBuilders.appendValue(toAppendTo, marker); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\MarkerPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */