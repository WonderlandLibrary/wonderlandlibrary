package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "FileDatePatternConverter", category = "FileConverter")
@ConverterKeys({"d", "date"})
@PerformanceSensitive({"allocation"})
public final class FileDatePatternConverter {
  public static PatternConverter newInstance(String[] options) {
    if (options == null || options.length == 0)
      return DatePatternConverter.newInstance(new String[] { "yyyy-MM-dd" }); 
    return DatePatternConverter.newInstance(options);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\FileDatePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */