package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

@Plugin(
   name = "logger",
   category = "Core",
   printObject = true
)
public class LoggerConfig extends AbstractFilterable {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private static final int MAX_RETRIES = 3;
   private static final long WAIT_TIME = 1000L;
   private static LogEventFactory LOG_EVENT_FACTORY = null;
   private List appenderRefs = new ArrayList();
   private final Map appenders = new ConcurrentHashMap();
   private final String name;
   private LogEventFactory logEventFactory;
   private Level level;
   private boolean additive = true;
   private boolean includeLocation = true;
   private LoggerConfig parent;
   private final AtomicInteger counter = new AtomicInteger();
   private boolean shutdown = false;
   private final Map properties;
   private final Configuration config;

   public LoggerConfig() {
      this.logEventFactory = LOG_EVENT_FACTORY;
      this.level = Level.ERROR;
      this.name = "";
      this.properties = null;
      this.config = null;
   }

   public LoggerConfig(String var1, Level var2, boolean var3) {
      this.logEventFactory = LOG_EVENT_FACTORY;
      this.name = var1;
      this.level = var2;
      this.additive = var3;
      this.properties = null;
      this.config = null;
   }

   protected LoggerConfig(String var1, List var2, Filter var3, Level var4, boolean var5, Property[] var6, Configuration var7, boolean var8) {
      super(var3);
      this.logEventFactory = LOG_EVENT_FACTORY;
      this.name = var1;
      this.appenderRefs = var2;
      this.level = var4;
      this.additive = var5;
      this.includeLocation = var8;
      this.config = var7;
      if (var6 != null && var6.length > 0) {
         this.properties = new HashMap(var6.length);
         Property[] var9 = var6;
         int var10 = var6.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Property var12 = var9[var11];
            boolean var13 = var12.getValue().contains("${");
            this.properties.put(var12, var13);
         }
      } else {
         this.properties = null;
      }

   }

   public Filter getFilter() {
      return super.getFilter();
   }

   public String getName() {
      return this.name;
   }

   public void setParent(LoggerConfig var1) {
      this.parent = var1;
   }

   public LoggerConfig getParent() {
      return this.parent;
   }

   public void addAppender(Appender var1, Level var2, Filter var3) {
      this.appenders.put(var1.getName(), new AppenderControl(var1, var2, var3));
   }

   public void removeAppender(String var1) {
      AppenderControl var2 = (AppenderControl)this.appenders.remove(var1);
      if (var2 != null) {
         this.cleanupFilter(var2);
      }

   }

   public Map getAppenders() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.appenders.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(var3.getKey(), ((AppenderControl)var3.getValue()).getAppender());
      }

      return var1;
   }

   protected void clearAppenders() {
      this.waitForCompletion();
      Collection var1 = this.appenders.values();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         AppenderControl var3 = (AppenderControl)var2.next();
         var2.remove();
         this.cleanupFilter(var3);
      }

   }

   private void cleanupFilter(AppenderControl var1) {
      Filter var2 = var1.getFilter();
      if (var2 != null) {
         var1.removeFilter(var2);
         if (var2 instanceof LifeCycle) {
            ((LifeCycle)var2).stop();
         }
      }

   }

   public List getAppenderRefs() {
      return this.appenderRefs;
   }

   public void setLevel(Level var1) {
      this.level = var1;
   }

   public Level getLevel() {
      return this.level;
   }

   public LogEventFactory getLogEventFactory() {
      return this.logEventFactory;
   }

   public void setLogEventFactory(LogEventFactory var1) {
      this.logEventFactory = var1;
   }

   public boolean isAdditive() {
      return this.additive;
   }

   public void setAdditive(boolean var1) {
      this.additive = var1;
   }

   public boolean isIncludeLocation() {
      return this.includeLocation;
   }

   public Map getProperties() {
      return this.properties == null ? null : Collections.unmodifiableMap(this.properties);
   }

   public void log(String var1, Marker var2, String var3, Level var4, Message var5, Throwable var6) {
      ArrayList var7 = null;
      if (this.properties != null) {
         var7 = new ArrayList(this.properties.size());
         Iterator var8 = this.properties.entrySet().iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            Property var10 = (Property)var9.getKey();
            String var11 = (Boolean)var9.getValue() ? this.config.getStrSubstitutor().replace(var10.getValue()) : var10.getValue();
            var7.add(Property.createProperty(var10.getName(), var11));
         }
      }

      LogEvent var12 = this.logEventFactory.createEvent(var1, var2, var3, var4, var5, var7, var6);
      this.log(var12);
   }

   private synchronized void waitForCompletion() {
      if (!this.shutdown) {
         this.shutdown = true;
         int var1 = 0;

         while(this.counter.get() > 0) {
            try {
               this.wait(1000L * (long)(var1 + 1));
            } catch (InterruptedException var3) {
               ++var1;
               if (var1 > 3) {
                  break;
               }
            }
         }

      }
   }

   public void log(LogEvent var1) {
      this.counter.incrementAndGet();
      if (this.isFiltered(var1)) {
         if (this.counter.decrementAndGet() == 0) {
            synchronized(this){}
            if (this.shutdown) {
               this.notifyAll();
            }
         }

      } else {
         var1.setIncludeLocation(this.isIncludeLocation());
         this.callAppenders(var1);
         if (this.additive && this.parent != null) {
            this.parent.log(var1);
         }

         if (this.counter.decrementAndGet() == 0) {
            synchronized(this){}
            if (this.shutdown) {
               this.notifyAll();
            }
         }

      }
   }

   protected void callAppenders(LogEvent var1) {
      Iterator var2 = this.appenders.values().iterator();

      while(var2.hasNext()) {
         AppenderControl var3 = (AppenderControl)var2.next();
         var3.callAppender(var1);
      }

   }

   public String toString() {
      return Strings.isEmpty(this.name) ? "root" : this.name;
   }

   @PluginFactory
   public static LoggerConfig createLogger(@PluginAttribute("additivity") String var0, @PluginAttribute("level") String var1, @PluginAttribute("name") String var2, @PluginAttribute("includeLocation") String var3, @PluginElement("AppenderRef") AppenderRef[] var4, @PluginElement("Properties") Property[] var5, @PluginConfiguration Configuration var6, @PluginElement("Filters") Filter var7) {
      if (var2 == null) {
         LOGGER.error("Loggers cannot be configured without a name");
         return null;
      } else {
         List var8 = Arrays.asList(var4);

         Level var9;
         try {
            var9 = Level.toLevel(var1, Level.ERROR);
         } catch (Exception var12) {
            LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", var1);
            var9 = Level.ERROR;
         }

         String var10 = var2.equals("root") ? "" : var2;
         boolean var11 = Booleans.parseBoolean(var0, true);
         return new LoggerConfig(var10, var8, var7, var9, var11, var5, var6, includeLocation(var3));
      }
   }

   protected static boolean includeLocation(String var0) {
      if (var0 == null) {
         boolean var1 = !AsyncLoggerContextSelector.class.getName().equals(System.getProperty("Log4jContextSelector"));
         return var1;
      } else {
         return Boolean.parseBoolean(var0);
      }
   }

   static {
      String var0 = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
      if (var0 != null) {
         try {
            Class var1 = Loader.loadClass(var0);
            if (var1 != null && LogEventFactory.class.isAssignableFrom(var1)) {
               LOG_EVENT_FACTORY = (LogEventFactory)var1.newInstance();
            }
         } catch (Exception var2) {
            LOGGER.error((String)("Unable to create LogEventFactory " + var0), (Throwable)var2);
         }
      }

      if (LOG_EVENT_FACTORY == null) {
         LOG_EVENT_FACTORY = new DefaultLogEventFactory();
      }

   }

   @Plugin(
      name = "root",
      category = "Core",
      printObject = true
   )
   public static class RootLogger extends LoggerConfig {
      @PluginFactory
      public static LoggerConfig createLogger(@PluginAttribute("additivity") String var0, @PluginAttribute("level") String var1, @PluginAttribute("includeLocation") String var2, @PluginElement("AppenderRef") AppenderRef[] var3, @PluginElement("Properties") Property[] var4, @PluginConfiguration Configuration var5, @PluginElement("Filters") Filter var6) {
         List var7 = Arrays.asList(var3);

         Level var8;
         try {
            var8 = Level.toLevel(var1, Level.ERROR);
         } catch (Exception var10) {
            LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", var1);
            var8 = Level.ERROR;
         }

         boolean var9 = Booleans.parseBoolean(var0, true);
         return new LoggerConfig("", var7, var6, var8, var9, var4, var5, includeLocation(var2));
      }
   }
}
