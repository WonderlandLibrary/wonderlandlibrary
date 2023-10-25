package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "repeat", category = "Converter")
@ConverterKeys({":|", "repeat"})
@PerformanceSensitive({"allocation"})
public final class RepeatPatternConverter extends LogEventPatternConverter {
  private final String result;
  
  public static RepeatPatternConverter newInstance(Configuration config, String[] options) {
    if (options.length != 2) {
      LOGGER.error("Incorrect number of options on repeat. Expected 2 received " + options.length);
      return null;
    } 
    if (options[0] == null) {
      LOGGER.error("No string supplied on repeat");
      return null;
    } 
    if (options[1] == null) {
      LOGGER.error("No repeat count supplied on repeat");
      return null;
    } 
    int count = 0;
    String result = options[0];
    try {
      count = Integer.parseInt(options[1].trim());
      result = Strings.repeat(options[0], count);
    } catch (Exception ex) {
      LOGGER.error("The repeat count is not an integer: {}", options[1].trim());
    } 
    return new RepeatPatternConverter(result);
  }
  
  private RepeatPatternConverter(String result) {
    super("repeat", "repeat");
    this.result = result;
  }
  
  public void format(Object obj, StringBuilder toAppendTo) {
    format(toAppendTo);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    format(toAppendTo);
  }
  
  private void format(StringBuilder toAppendTo) {
    if (this.result != null)
      toAppendTo.append(this.result); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\RepeatPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */