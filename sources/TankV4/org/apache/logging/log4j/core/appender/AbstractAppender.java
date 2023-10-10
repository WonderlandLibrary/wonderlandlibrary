package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.Integers;

public abstract class AbstractAppender extends AbstractFilterable implements Appender {
   private final boolean ignoreExceptions;
   private ErrorHandler handler;
   private final Layout layout;
   private final String name;
   private boolean started;

   public static int parseInt(String var0, int var1) {
      try {
         return Integers.parseInt(var0, var1);
      } catch (NumberFormatException var3) {
         LOGGER.error("Could not parse \"{}\" as an integer,  using default value {}: {}", var0, var1, var3);
         return var1;
      }
   }

   protected AbstractAppender(String var1, Filter var2, Layout var3) {
      this(var1, var2, var3, true);
   }

   protected AbstractAppender(String var1, Filter var2, Layout var3, boolean var4) {
      super(var2);
      this.handler = new DefaultErrorHandler(this);
      this.started = false;
      this.name = var1;
      this.layout = var3;
      this.ignoreExceptions = var4;
   }

   public void error(String var1) {
      this.handler.error(var1);
   }

   public void error(String var1, LogEvent var2, Throwable var3) {
      this.handler.error(var1, var2, var3);
   }

   public void error(String var1, Throwable var2) {
      this.handler.error(var1, var2);
   }

   public ErrorHandler getHandler() {
      return this.handler;
   }

   public Layout getLayout() {
      return this.layout;
   }

   public String getName() {
      return this.name;
   }

   public boolean ignoreExceptions() {
      return this.ignoreExceptions;
   }

   public boolean isStarted() {
      return this.started;
   }

   public void setHandler(ErrorHandler var1) {
      if (var1 == null) {
         LOGGER.error("The handler cannot be set to null");
      }

      if (this.isStarted()) {
         LOGGER.error("The handler cannot be changed once the appender is started");
      } else {
         this.handler = var1;
      }
   }

   public void start() {
      this.startFilter();
      this.started = true;
   }

   public void stop() {
      this.started = false;
      this.stopFilter();
   }

   public String toString() {
      return this.name;
   }
}
