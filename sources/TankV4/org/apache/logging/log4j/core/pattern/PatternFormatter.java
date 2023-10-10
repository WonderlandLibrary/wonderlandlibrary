package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;

public class PatternFormatter {
   private final LogEventPatternConverter converter;
   private final FormattingInfo field;

   public PatternFormatter(LogEventPatternConverter var1, FormattingInfo var2) {
      this.converter = var1;
      this.field = var2;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      int var3 = var2.length();
      this.converter.format(var1, var2);
      this.field.format(var3, var2);
   }

   public LogEventPatternConverter getConverter() {
      return this.converter;
   }

   public FormattingInfo getFormattingInfo() {
      return this.field;
   }

   public boolean handlesThrowable() {
      return this.converter.handlesThrowable();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("[converter=");
      var1.append(this.converter);
      var1.append(", field=");
      var1.append(this.field);
      var1.append("]");
      return var1.toString();
   }
}
