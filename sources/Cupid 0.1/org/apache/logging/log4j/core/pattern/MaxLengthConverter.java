package org.apache.logging.log4j.core.pattern;

import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "maxLength", category = "Converter")
@ConverterKeys({"maxLength", "maxLen"})
@PerformanceSensitive({"allocation"})
public final class MaxLengthConverter extends LogEventPatternConverter {
  private final List<PatternFormatter> formatters;
  
  private final int maxLength;
  
  public static MaxLengthConverter newInstance(Configuration config, String[] options) {
    if (options.length != 2) {
      LOGGER.error("Incorrect number of options on maxLength: expected 2 received {}: {}", Integer.valueOf(options.length), options);
      return null;
    } 
    if (options[0] == null) {
      LOGGER.error("No pattern supplied on maxLength");
      return null;
    } 
    if (options[1] == null) {
      LOGGER.error("No length supplied on maxLength");
      return null;
    } 
    PatternParser parser = PatternLayout.createPatternParser(config);
    List<PatternFormatter> formatters = parser.parse(options[0]);
    return new MaxLengthConverter(formatters, AbstractAppender.parseInt(options[1], 100));
  }
  
  private MaxLengthConverter(List<PatternFormatter> formatters, int maxLength) {
    super("MaxLength", "maxLength");
    this.maxLength = maxLength;
    this.formatters = formatters;
    LOGGER.trace("new MaxLengthConverter with {}", Integer.valueOf(maxLength));
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    int initialLength = toAppendTo.length();
    for (int i = 0; i < this.formatters.size(); i++) {
      PatternFormatter formatter = this.formatters.get(i);
      formatter.format(event, toAppendTo);
      if (toAppendTo.length() > initialLength + this.maxLength)
        break; 
    } 
    if (toAppendTo.length() > initialLength + this.maxLength) {
      toAppendTo.setLength(initialLength + this.maxLength);
      if (this.maxLength > 20)
        toAppendTo.append("..."); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\MaxLengthConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */