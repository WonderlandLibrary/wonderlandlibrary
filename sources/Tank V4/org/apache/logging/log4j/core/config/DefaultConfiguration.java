package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.util.PropertiesUtil;

public class DefaultConfiguration extends BaseConfiguration {
   public static final String DEFAULT_NAME = "Default";
   public static final String DEFAULT_LEVEL = "org.apache.logging.log4j.level";

   public DefaultConfiguration() {
      this.setName("Default");
      PatternLayout var1 = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
      ConsoleAppender var2 = ConsoleAppender.createAppender(var1, (Filter)null, "SYSTEM_OUT", "Console", "false", "true");
      var2.start();
      this.addAppender(var2);
      LoggerConfig var3 = this.getRootLogger();
      var3.addAppender(var2, (Level)null, (Filter)null);
      String var4 = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
      Level var5 = var4 != null && Level.valueOf(var4) != null ? Level.valueOf(var4) : Level.ERROR;
      var3.setLevel(var5);
   }

   protected void doConfigure() {
   }
}
