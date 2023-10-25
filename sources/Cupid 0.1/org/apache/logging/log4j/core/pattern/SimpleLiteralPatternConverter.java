package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.util.OptionConverter;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({"allocation"})
abstract class SimpleLiteralPatternConverter extends LogEventPatternConverter implements ArrayPatternConverter {
  private SimpleLiteralPatternConverter() {
    super("SimpleLiteral", "literal");
  }
  
  static LogEventPatternConverter of(String literal, boolean convertBackslashes) {
    String value = convertBackslashes ? OptionConverter.convertSpecialChars(literal) : literal;
    return of(value);
  }
  
  static LogEventPatternConverter of(String literal) {
    if (literal == null || literal.isEmpty())
      return Noop.INSTANCE; 
    if (" ".equals(literal))
      return Space.INSTANCE; 
    return new StringValue(literal);
  }
  
  public final void format(LogEvent ignored, StringBuilder output) {
    format(output);
  }
  
  public final void format(Object ignored, StringBuilder output) {
    format(output);
  }
  
  public final void format(StringBuilder output, Object... args) {
    format(output);
  }
  
  public final boolean isVariable() {
    return false;
  }
  
  public final boolean handlesThrowable() {
    return false;
  }
  
  abstract void format(StringBuilder paramStringBuilder);
  
  private static final class Noop extends SimpleLiteralPatternConverter {
    private static final Noop INSTANCE = new Noop();
    
    void format(StringBuilder output) {}
  }
  
  private static final class Space extends SimpleLiteralPatternConverter {
    private static final Space INSTANCE = new Space();
    
    void format(StringBuilder output) {
      output.append(' ');
    }
  }
  
  private static final class StringValue extends SimpleLiteralPatternConverter {
    private final String literal;
    
    StringValue(String literal) {
      this.literal = literal;
    }
    
    void format(StringBuilder output) {
      output.append(this.literal);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\SimpleLiteralPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */