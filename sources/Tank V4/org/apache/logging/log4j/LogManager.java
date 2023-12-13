package org.apache.logging.log4j;

import java.net.URI;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;

public class LogManager {
   private static LoggerContextFactory factory;
   private static final String FACTORY_PROPERTY_NAME = "log4j2.loggerContextFactory";
   private static final Logger LOGGER = StatusLogger.getLogger();
   public static final String ROOT_LOGGER_NAME = "";

   private static String getClassName(int var0) {
      return (new Throwable()).getStackTrace()[var0].getClassName();
   }

   public static LoggerContext getContext() {
      return factory.getContext(LogManager.class.getName(), (ClassLoader)null, true);
   }

   public static LoggerContext getContext(boolean var0) {
      return factory.getContext(LogManager.class.getName(), (ClassLoader)null, var0);
   }

   public static LoggerContext getContext(ClassLoader var0, boolean var1) {
      return factory.getContext(LogManager.class.getName(), var0, var1);
   }

   public static LoggerContext getContext(ClassLoader var0, boolean var1, URI var2) {
      return factory.getContext(LogManager.class.getName(), var0, var1, var2);
   }

   protected static LoggerContext getContext(String var0, boolean var1) {
      return factory.getContext(var0, (ClassLoader)null, var1);
   }

   protected static LoggerContext getContext(String var0, ClassLoader var1, boolean var2) {
      return factory.getContext(var0, var1, var2);
   }

   public static LoggerContextFactory getFactory() {
      return factory;
   }

   public static Logger getFormatterLogger(Class var0) {
      return getLogger((String)(var0 != null ? var0.getName() : getClassName(2)), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
   }

   public static Logger getFormatterLogger(Object var0) {
      return getLogger((String)(var0 != null ? var0.getClass().getName() : getClassName(2)), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
   }

   public static Logger getFormatterLogger(String var0) {
      return getLogger((String)(var0 != null ? var0 : getClassName(2)), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
   }

   public static Logger getLogger() {
      return getLogger(getClassName(2));
   }

   public static Logger getLogger(Class var0) {
      return getLogger(var0 != null ? var0.getName() : getClassName(2));
   }

   public static Logger getLogger(Class var0, MessageFactory var1) {
      return getLogger(var0 != null ? var0.getName() : getClassName(2), var1);
   }

   public static Logger getLogger(MessageFactory var0) {
      return getLogger(getClassName(2), var0);
   }

   public static Logger getLogger(Object var0) {
      return getLogger(var0 != null ? var0.getClass().getName() : getClassName(2));
   }

   public static Logger getLogger(Object var0, MessageFactory var1) {
      return getLogger(var0 != null ? var0.getClass().getName() : getClassName(2), var1);
   }

   public static Logger getLogger(String var0) {
      String var1 = var0 != null ? var0 : getClassName(2);
      return factory.getContext(LogManager.class.getName(), (ClassLoader)null, false).getLogger(var1);
   }

   public static Logger getLogger(String var0, MessageFactory var1) {
      String var2 = var0 != null ? var0 : getClassName(2);
      return factory.getContext(LogManager.class.getName(), (ClassLoader)null, false).getLogger(var2, var1);
   }

   protected static Logger getLogger(String var0, String var1) {
      return factory.getContext(var0, (ClassLoader)null, false).getLogger(var1);
   }

   public static Logger getRootLogger() {
      return getLogger("");
   }

   protected LogManager() {
   }

   static {
      PropertiesUtil var0 = PropertiesUtil.getProperties();
      String var1 = var0.getStringProperty("log4j2.loggerContextFactory");
      ClassLoader var2 = ProviderUtil.findClassLoader();
      if (var1 != null) {
         try {
            Class var3 = var2.loadClass(var1);
            if (LoggerContextFactory.class.isAssignableFrom(var3)) {
               factory = (LoggerContextFactory)var3.newInstance();
            }
         } catch (ClassNotFoundException var11) {
            LOGGER.error("Unable to locate configured LoggerContextFactory {}", var1);
         } catch (Exception var12) {
            LOGGER.error("Unable to create configured LoggerContextFactory {}", var1, var12);
         }
      }

      if (factory == null) {
         TreeMap var13 = new TreeMap();
         if (ProviderUtil.hasProviders()) {
            Iterator var4 = ProviderUtil.getProviders();

            while(var4.hasNext()) {
               Provider var5 = (Provider)var4.next();
               String var6 = var5.getClassName();
               if (var6 != null) {
                  try {
                     Class var7 = var2.loadClass(var6);
                     if (LoggerContextFactory.class.isAssignableFrom(var7)) {
                        var13.put(var5.getPriority(), (LoggerContextFactory)var7.newInstance());
                     } else {
                        LOGGER.error(var6 + " does not implement " + LoggerContextFactory.class.getName());
                     }
                  } catch (ClassNotFoundException var8) {
                     LOGGER.error((String)("Unable to locate class " + var6 + " specified in " + var5.getURL().toString()), (Throwable)var8);
                  } catch (IllegalAccessException var9) {
                     LOGGER.error((String)("Unable to create class " + var6 + " specified in " + var5.getURL().toString()), (Throwable)var9);
                  } catch (Exception var10) {
                     LOGGER.error((String)("Unable to create class " + var6 + " specified in " + var5.getURL().toString()), (Throwable)var10);
                     var10.printStackTrace();
                  }
               }
            }

            if (var13.size() == 0) {
               LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
               factory = new SimpleLoggerContextFactory();
            } else {
               StringBuilder var14 = new StringBuilder("Multiple logging implementations found: \n");
               Iterator var15 = var13.entrySet().iterator();

               while(var15.hasNext()) {
                  Entry var16 = (Entry)var15.next();
                  var14.append("Factory: ").append(((LoggerContextFactory)var16.getValue()).getClass().getName());
                  var14.append(", Weighting: ").append(var16.getKey()).append("\n");
               }

               factory = (LoggerContextFactory)var13.get(var13.lastKey());
               var14.append("Using factory: ").append(factory.getClass().getName());
               LOGGER.warn(var14.toString());
            }
         } else {
            LOGGER.error("Unable to locate a logging implementation, using SimpleLogger");
            factory = new SimpleLoggerContextFactory();
         }
      }

   }
}
