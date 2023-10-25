package org.apache.logging.log4j.core.pattern;

import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({"allocation"})
public abstract class EqualsBaseReplacementConverter extends LogEventPatternConverter {
  private final List<PatternFormatter> formatters;
  
  private final List<PatternFormatter> substitutionFormatters;
  
  private final String substitution;
  
  private final String testString;
  
  protected EqualsBaseReplacementConverter(String name, String style, List<PatternFormatter> formatters, String testString, String substitution, PatternParser parser) {
    super(name, style);
    this.testString = testString;
    this.substitution = substitution;
    this.formatters = formatters;
    this.substitutionFormatters = substitution.contains("%") ? parser.parse(substitution) : null;
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    int initialSize = toAppendTo.length();
    for (int i = 0; i < this.formatters.size(); i++) {
      PatternFormatter formatter = this.formatters.get(i);
      formatter.format(event, toAppendTo);
    } 
    if (equals(this.testString, toAppendTo, initialSize, toAppendTo.length() - initialSize)) {
      toAppendTo.setLength(initialSize);
      parseSubstitution(event, toAppendTo);
    } 
  }
  
  protected abstract boolean equals(String paramString, StringBuilder paramStringBuilder, int paramInt1, int paramInt2);
  
  void parseSubstitution(LogEvent event, StringBuilder substitutionBuffer) {
    if (this.substitutionFormatters != null) {
      for (int i = 0; i < this.substitutionFormatters.size(); i++) {
        PatternFormatter formatter = this.substitutionFormatters.get(i);
        formatter.format(event, substitutionBuffer);
      } 
    } else {
      substitutionBuffer.append(this.substitution);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\EqualsBaseReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */