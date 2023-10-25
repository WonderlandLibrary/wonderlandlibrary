package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;

@Plugin(name = "FullLocationPatternConverter", category = "Converter")
@ConverterKeys({"l", "location"})
public final class FullLocationPatternConverter extends LogEventPatternConverter implements LocationAware {
  private static final FullLocationPatternConverter INSTANCE = new FullLocationPatternConverter();
  
  private FullLocationPatternConverter() {
    super("Full Location", "fullLocation");
  }
  
  public static FullLocationPatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  public void format(LogEvent event, StringBuilder output) {
    StackTraceElement element = event.getSource();
    if (element != null)
      output.append(element.toString()); 
  }
  
  public boolean requiresLocation() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\FullLocationPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */