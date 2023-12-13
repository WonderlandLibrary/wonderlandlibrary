package joptsimple.util;

import java.util.regex.Pattern;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

public class RegexMatcher implements ValueConverter {
   private final Pattern pattern;

   public RegexMatcher(String var1, int var2) {
      this.pattern = Pattern.compile(var1, var2);
   }

   public static ValueConverter regex(String var0) {
      return new RegexMatcher(var0, 0);
   }

   public String convert(String var1) {
      if (!this.pattern.matcher(var1).matches()) {
         throw new ValueConversionException("Value [" + var1 + "] did not match regex [" + this.pattern.pattern() + ']');
      } else {
         return var1;
      }
   }

   public Class valueType() {
      return String.class;
   }

   public String valuePattern() {
      return this.pattern.pattern();
   }

   public Object convert(String var1) {
      return this.convert(var1);
   }
}
