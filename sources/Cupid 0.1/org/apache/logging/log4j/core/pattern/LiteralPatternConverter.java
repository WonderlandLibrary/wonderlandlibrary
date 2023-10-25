package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.OptionConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({"allocation"})
public final class LiteralPatternConverter extends LogEventPatternConverter implements ArrayPatternConverter {
  private final String literal;
  
  private final Configuration config;
  
  private final boolean substitute;
  
  public LiteralPatternConverter(Configuration config, String literal, boolean convertBackslashes) {
    super("Literal", "literal");
    this.literal = convertBackslashes ? OptionConverter.convertSpecialChars(literal) : literal;
    this.config = config;
    this.substitute = (config != null && containsSubstitutionSequence(literal));
  }
  
  static boolean containsSubstitutionSequence(String literal) {
    return (literal != null && literal.contains("${"));
  }
  
  public void format(LogEvent event, StringBuilder toAppendTo) {
    toAppendTo.append(this.substitute ? this.config.getStrSubstitutor().replace(event, this.literal) : this.literal);
  }
  
  public void format(Object obj, StringBuilder output) {
    output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
  }
  
  public void format(StringBuilder output, Object... objects) {
    output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
  }
  
  public String getLiteral() {
    return this.literal;
  }
  
  public boolean isVariable() {
    return false;
  }
  
  public String toString() {
    return "LiteralPatternConverter[literal=" + this.literal + ", config=" + this.config + ", substitute=" + this.substitute + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\LiteralPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */