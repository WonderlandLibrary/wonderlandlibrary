package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;

public final class LiteralPatternConverter extends LogEventPatternConverter implements ArrayPatternConverter {
   private final String literal;
   private final Configuration config;
   private final boolean substitute;

   public LiteralPatternConverter(Configuration var1, String var2) {
      super("Literal", "literal");
      this.literal = var2;
      this.config = var1;
      this.substitute = var1 != null && var2.contains("${");
   }

   public void format(LogEvent var1, StringBuilder var2) {
      var2.append(this.substitute ? this.config.getStrSubstitutor().replace(var1, this.literal) : this.literal);
   }

   public void format(Object var1, StringBuilder var2) {
      var2.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
   }

   public void format(StringBuilder var1, Object... var2) {
      var1.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
   }

   public String getLiteral() {
      return this.literal;
   }
}
