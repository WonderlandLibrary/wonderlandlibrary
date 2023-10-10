package org.apache.logging.log4j.core.config;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.status.StatusLogger;

public final class Configurator {
   protected static final StatusLogger LOGGER = StatusLogger.getLogger();

   private Configurator() {
   }

   public static LoggerContext initialize(String var0, ClassLoader var1, String var2) {
      return initialize(var0, var1, (String)var2, (Object)null);
   }

   public static LoggerContext initialize(String var0, ClassLoader var1, String var2, Object var3) {
      try {
         URI var4 = var2 == null ? null : new URI(var2);
         return initialize(var0, var1, var4, var3);
      } catch (URISyntaxException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public static LoggerContext initialize(String var0, String var1) {
      return initialize(var0, (ClassLoader)null, (String)var1);
   }

   public static LoggerContext initialize(String var0, ClassLoader var1, URI var2) {
      return initialize(var0, var1, (URI)var2, (Object)null);
   }

   public static LoggerContext initialize(String var0, ClassLoader var1, URI var2, Object var3) {
      try {
         org.apache.logging.log4j.spi.LoggerContext var4 = LogManager.getContext(var1, false, var2);
         if (var4 instanceof LoggerContext) {
            LoggerContext var5 = (LoggerContext)var4;
            ContextAnchor.THREAD_CONTEXT.set(var5);
            if (var3 != null) {
               var5.setExternalContext(var3);
            }

            Configuration var6 = ConfigurationFactory.getInstance().getConfiguration(var0, var2);
            var5.start(var6);
            ContextAnchor.THREAD_CONTEXT.remove();
            return var5;
         }

         LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j", new Object[]{var4.getClass().getName(), LoggerContext.class.getName()});
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return null;
   }

   public static LoggerContext initialize(ClassLoader var0, ConfigurationFactory.ConfigurationSource var1) {
      try {
         URI var2 = null;

         try {
            var2 = var1.getLocation() == null ? null : new URI(var1.getLocation());
         } catch (Exception var6) {
         }

         org.apache.logging.log4j.spi.LoggerContext var3 = LogManager.getContext(var0, false, var2);
         if (var3 instanceof LoggerContext) {
            LoggerContext var4 = (LoggerContext)var3;
            ContextAnchor.THREAD_CONTEXT.set(var4);
            Configuration var5 = ConfigurationFactory.getInstance().getConfiguration(var1);
            var4.start(var5);
            ContextAnchor.THREAD_CONTEXT.remove();
            return var4;
         }

         LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j", new Object[]{var3.getClass().getName(), LoggerContext.class.getName()});
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return null;
   }

   public static void shutdown(LoggerContext var0) {
      if (var0 != null) {
         var0.stop();
      }

   }
}
