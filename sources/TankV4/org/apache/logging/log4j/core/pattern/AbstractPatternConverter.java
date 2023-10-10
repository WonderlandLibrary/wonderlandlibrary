package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractPatternConverter implements PatternConverter {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private final String name;
   private final String style;

   protected AbstractPatternConverter(String var1, String var2) {
      this.name = var1;
      this.style = var2;
   }

   public final String getName() {
      return this.name;
   }

   public String getStyleClass(Object var1) {
      return this.style;
   }
}
