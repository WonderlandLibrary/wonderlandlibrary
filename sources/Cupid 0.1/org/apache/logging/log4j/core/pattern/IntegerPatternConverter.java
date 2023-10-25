package org.apache.logging.log4j.core.pattern;

import java.util.Date;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "IntegerPatternConverter", category = "FileConverter")
@ConverterKeys({"i", "index"})
@PerformanceSensitive({"allocation"})
public final class IntegerPatternConverter extends AbstractPatternConverter implements ArrayPatternConverter {
  private static final IntegerPatternConverter INSTANCE = new IntegerPatternConverter();
  
  private IntegerPatternConverter() {
    super("Integer", "integer");
  }
  
  public static IntegerPatternConverter newInstance(String[] options) {
    return INSTANCE;
  }
  
  public void format(StringBuilder toAppendTo, Object... objects) {
    for (int i = 0; i < objects.length; i++) {
      if (objects[i] instanceof Integer) {
        format(objects[i], toAppendTo);
        break;
      } 
      if (objects[i] instanceof NotANumber) {
        toAppendTo.append("\000");
        break;
      } 
    } 
  }
  
  public void format(Object obj, StringBuilder toAppendTo) {
    if (obj instanceof Integer) {
      toAppendTo.append(((Integer)obj).intValue());
    } else if (obj instanceof Date) {
      toAppendTo.append(((Date)obj).getTime());
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\IntegerPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */