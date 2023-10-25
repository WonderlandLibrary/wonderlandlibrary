package joptsimple.util;

import java.util.regex.Pattern;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

public class RegexMatcher implements ValueConverter<String> {
  private final Pattern pattern;
  
  public RegexMatcher(String pattern, int flags) {
    this.pattern = Pattern.compile(pattern, flags);
  }
  
  public static ValueConverter<String> regex(String pattern) {
    return new RegexMatcher(pattern, 0);
  }
  
  public String convert(String value) {
    if (!this.pattern.matcher(value).matches())
      throw new ValueConversionException("Value [" + value + "] did not match regex [" + this.pattern.pattern() + ']'); 
    return value;
  }
  
  public Class<String> valueType() {
    return String.class;
  }
  
  public String valuePattern() {
    return this.pattern.pattern();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimpl\\util\RegexMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */