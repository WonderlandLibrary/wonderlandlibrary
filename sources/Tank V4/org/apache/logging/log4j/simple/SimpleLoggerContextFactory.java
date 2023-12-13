package org.apache.logging.log4j.simple;

import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class SimpleLoggerContextFactory implements LoggerContextFactory {
   private static LoggerContext context = new SimpleLoggerContext();

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3) {
      return context;
   }

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4) {
      return context;
   }

   public void removeContext(LoggerContext var1) {
   }
}
