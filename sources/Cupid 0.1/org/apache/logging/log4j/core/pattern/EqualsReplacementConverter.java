package org.apache.logging.log4j.core.pattern;

import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@Plugin(name = "equals", category = "Converter")
@ConverterKeys({"equals"})
@PerformanceSensitive({"allocation"})
public final class EqualsReplacementConverter extends EqualsBaseReplacementConverter {
  public static EqualsReplacementConverter newInstance(Configuration config, String[] options) {
    if (options.length != 3) {
      LOGGER.error("Incorrect number of options on equals. Expected 3 received " + options.length);
      return null;
    } 
    if (options[0] == null) {
      LOGGER.error("No pattern supplied on equals");
      return null;
    } 
    if (options[1] == null) {
      LOGGER.error("No test string supplied on equals");
      return null;
    } 
    if (options[2] == null) {
      LOGGER.error("No substitution supplied on equals");
      return null;
    } 
    String p = options[1];
    PatternParser parser = PatternLayout.createPatternParser(config);
    List<PatternFormatter> formatters = parser.parse(options[0]);
    return new EqualsReplacementConverter(formatters, p, options[2], parser);
  }
  
  private EqualsReplacementConverter(List<PatternFormatter> formatters, String testString, String substitution, PatternParser parser) {
    super("equals", "equals", formatters, testString, substitution, parser);
  }
  
  protected boolean equals(String str, StringBuilder buff, int from, int len) {
    return StringBuilders.equals(str, 0, str.length(), buff, from, len);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\EqualsReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */