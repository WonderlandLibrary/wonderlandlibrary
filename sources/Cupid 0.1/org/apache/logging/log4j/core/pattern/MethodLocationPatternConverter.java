package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;

@Plugin(name = "MethodLocationPatternConverter", category = "Converter")
@ConverterKeys({"M", "method"})
public final class MethodLocationPatternConverter extends LogEventPatternConverter implements LocationAware {
  private static final MethodLocationPatternConverter INSTANCE = new MethodLocationPatternConverter();
  
  private MethodLocationPatternConverter() {
    super("Method", "method");
  }
  
  public static MethodLocationPatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    StackTraceElement element = event.getSource();
    if (element != null)
      toAppendTo.append(element.getMethodName()); 
  }
  
  public boolean requiresLocation() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\MethodLocationPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */