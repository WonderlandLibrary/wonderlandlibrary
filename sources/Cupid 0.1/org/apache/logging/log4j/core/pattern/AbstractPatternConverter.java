package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractPatternConverter implements PatternConverter {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final String name;
  
  private final String style;
  
  protected AbstractPatternConverter(String name, String style) {
    this.name = name;
    this.style = style;
  }
  
  public final String getName() {
    return this.name;
  }
  
  public String getStyleClass(Object e) {
    return this.style;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\AbstractPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */