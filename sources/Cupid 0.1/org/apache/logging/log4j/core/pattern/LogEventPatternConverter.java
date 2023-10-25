package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;

public abstract class LogEventPatternConverter extends AbstractPatternConverter {
  protected LogEventPatternConverter(String name, String style) {
    super(name, style);
  }
  
  public abstract void format(LogEvent paramLogEvent, StringBuilder paramStringBuilder);
  
  public void format(Object obj, StringBuilder output) {
    if (obj instanceof LogEvent)
      format((LogEvent)obj, output); 
  }
  
  public boolean handlesThrowable() {
    return false;
  }
  
  public boolean isVariable() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\LogEventPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */