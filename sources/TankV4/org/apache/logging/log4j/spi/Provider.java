package org.apache.logging.log4j.spi;

import java.net.URL;
import java.util.Properties;

public class Provider {
   private static final Integer DEFAULT_PRIORITY = -1;
   private static final String FACTORY_PRIORITY = "FactoryPriority";
   private static final String THREAD_CONTEXT_MAP = "ThreadContextMap";
   private static final String LOGGER_CONTEXT_FACTORY = "LoggerContextFactory";
   private final Integer priority;
   private final String className;
   private final String threadContextMap;
   private final URL url;

   public Provider(Properties var1, URL var2) {
      this.url = var2;
      String var3 = var1.getProperty("FactoryPriority");
      this.priority = var3 == null ? DEFAULT_PRIORITY : Integer.valueOf(var3);
      this.className = var1.getProperty("LoggerContextFactory");
      this.threadContextMap = var1.getProperty("ThreadContextMap");
   }

   public Integer getPriority() {
      return this.priority;
   }

   public String getClassName() {
      return this.className;
   }

   public String getThreadContextMap() {
      return this.threadContextMap;
   }

   public URL getURL() {
      return this.url;
   }
}
