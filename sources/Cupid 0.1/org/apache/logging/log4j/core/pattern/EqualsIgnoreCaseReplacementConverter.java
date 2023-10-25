package org.apache.logging.log4j.core.pattern;

import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@Plugin(name = "equalsIgnoreCase", category = "Converter")
@ConverterKeys({"equalsIgnoreCase"})
@PerformanceSensitive({"allocation"})
public final class EqualsIgnoreCaseReplacementConverter extends EqualsBaseReplacementConverter {
  public static EqualsIgnoreCaseReplacementConverter newInstance(Configuration config, String[] options) {
    if (options.length != 3) {
      LOGGER.error("Incorrect number of options on equalsIgnoreCase. Expected 3 received " + options.length);
      return null;
    } 
    if (options[0] == null) {
      LOGGER.error("No pattern supplied on equalsIgnoreCase");
      return null;
    } 
    if (options[1] == null) {
      LOGGER.error("No test string supplied on equalsIgnoreCase");
      return null;
    } 
    if (options[2] == null) {
      LOGGER.error("No substitution supplied on equalsIgnoreCase");
      return null;
    } 
    String p = options[1];
    PatternParser parser = PatternLayout.createPatternParser(config);
    List<PatternFormatter> formatters = parser.parse(options[0]);
    return new EqualsIgnoreCaseReplacementConverter(formatters, p, options[2], parser);
  }
  
  private EqualsIgnoreCaseReplacementConverter(List<PatternFormatter> formatters, String testString, String substitution, PatternParser parser) {
    super("equalsIgnoreCase", "equalsIgnoreCase", formatters, testString, substitution, parser);
  }
  
  protected boolean equals(String str, StringBuilder buff, int from, int len) {
    return StringBuilders.equalsIgnoreCase(str, 0, str.length(), buff, from, len);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\EqualsIgnoreCaseReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */