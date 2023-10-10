package org.apache.logging.log4j.core.impl;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class Log4jContextFactory implements LoggerContextFactory {
   private static final StatusLogger LOGGER = StatusLogger.getLogger();
   private ContextSelector selector;

   public Log4jContextFactory() {
      String var1 = PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector");
      if (var1 != null) {
         try {
            Class var2 = Loader.loadClass(var1);
            if (var2 != null && ContextSelector.class.isAssignableFrom(var2)) {
               this.selector = (ContextSelector)var2.newInstance();
            }
         } catch (Exception var4) {
            LOGGER.error("Unable to create context " + var1, var4);
         }
      }

      if (this.selector == null) {
         this.selector = new ClassLoaderContextSelector();
      }

      try {
         Server.registerMBeans(this.selector);
      } catch (Exception var3) {
         LOGGER.error("Could not start JMX", var3);
      }

   }

   public ContextSelector getSelector() {
      return this.selector;
   }

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3) {
      LoggerContext var4 = this.selector.getContext(var1, var2, var3);
      if (var4.getStatus() == LoggerContext.Status.INITIALIZED) {
         var4.start();
      }

      return var4;
   }

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4) {
      LoggerContext var5 = this.selector.getContext(var1, var2, var3, var4);
      if (var5.getStatus() == LoggerContext.Status.INITIALIZED) {
         var5.start();
      }

      return var5;
   }

   public void removeContext(org.apache.logging.log4j.spi.LoggerContext var1) {
      if (var1 instanceof LoggerContext) {
         this.selector.removeContext((LoggerContext)var1);
      }

   }

   public org.apache.logging.log4j.spi.LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4) {
      return this.getContext(var1, var2, var3, var4);
   }

   public org.apache.logging.log4j.spi.LoggerContext getContext(String var1, ClassLoader var2, boolean var3) {
      return this.getContext(var1, var2, var3);
   }
}
