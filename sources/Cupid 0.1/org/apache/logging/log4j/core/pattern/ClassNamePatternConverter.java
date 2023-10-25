package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;

@Plugin(name = "ClassNamePatternConverter", category = "Converter")
@ConverterKeys({"C", "class"})
public final class ClassNamePatternConverter extends NamePatternConverter implements LocationAware {
  private static final String NA = "?";
  
  private ClassNamePatternConverter(String[] options) {
    super("Class Name", "class name", options);
  }
  
  public static ClassNamePatternConverter newInstance(String[] options) {
    return new ClassNamePatternConverter(options);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    StackTraceElement element = event.getSource();
    if (element == null) {
      toAppendTo.append("?");
    } else {
      abbreviate(element.getClassName(), toAppendTo);
    } 
  }
  
  public boolean requiresLocation() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\ClassNamePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */