package org.apache.logging.log4j.core.pattern;

import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "notEmpty", category = "Converter")
@ConverterKeys({"notEmpty", "varsNotEmpty", "variablesNotEmpty"})
@PerformanceSensitive({"allocation"})
public final class VariablesNotEmptyReplacementConverter extends LogEventPatternConverter {
  private final List<PatternFormatter> formatters;
  
  private VariablesNotEmptyReplacementConverter(List<PatternFormatter> formatters) {
    super("notEmpty", "notEmpty");
    this.formatters = formatters;
  }
  
  public static VariablesNotEmptyReplacementConverter newInstance(Configuration config, String[] options) {
    if (options.length != 1) {
      LOGGER.error("Incorrect number of options on varsNotEmpty. Expected 1 received " + options.length);
      return null;
    } 
    if (options[0] == null) {
      LOGGER.error("No pattern supplied on varsNotEmpty");
      return null;
    } 
    PatternParser parser = PatternLayout.createPatternParser(config);
    List<PatternFormatter> formatters = parser.parse(options[0]);
    return new VariablesNotEmptyReplacementConverter(formatters);
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    int start = toAppendTo.length();
    boolean allVarsEmpty = true;
    boolean hasVars = false;
    for (int i = 0; i < this.formatters.size(); i++) {
      PatternFormatter formatter = this.formatters.get(i);
      int formatterStart = toAppendTo.length();
      formatter.format(event, toAppendTo);
      if (formatter.getConverter().isVariable()) {
        hasVars = true;
        allVarsEmpty = (allVarsEmpty && toAppendTo.length() == formatterStart);
      } 
    } 
    if (!hasVars || allVarsEmpty)
      toAppendTo.setLength(start); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\VariablesNotEmptyReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */