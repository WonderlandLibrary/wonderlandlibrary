package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;

public abstract class LogEventPatternConverter extends AbstractPatternConverter {
   protected LogEventPatternConverter(String var1, String var2) {
      super(var1, var2);
   }

   public abstract void format(LogEvent var1, StringBuilder var2);

   public void format(Object var1, StringBuilder var2) {
      if (var1 instanceof LogEvent) {
         this.format((LogEvent)var1, var2);
      }

   }

   public boolean handlesThrowable() {
      return false;
   }
}
