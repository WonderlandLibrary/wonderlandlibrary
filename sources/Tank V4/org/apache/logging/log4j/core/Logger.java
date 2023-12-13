package org.apache.logging.log4j.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.AbstractLogger;

public class Logger extends AbstractLogger {
   protected volatile Logger.PrivateConfig config;
   private final LoggerContext context;

   protected Logger(LoggerContext var1, String var2, MessageFactory var3) {
      super(var2, var3);
      this.context = var1;
      this.config = new Logger.PrivateConfig(this, var1.getConfiguration(), this);
   }

   public Logger getParent() {
      LoggerConfig var1 = this.config.loggerConfig.getName().equals(this.getName()) ? this.config.loggerConfig.getParent() : this.config.loggerConfig;
      if (var1 == null) {
         return null;
      } else {
         return this.context.hasLogger(var1.getName()) ? this.context.getLogger(var1.getName(), this.getMessageFactory()) : new Logger(this.context, var1.getName(), this.getMessageFactory());
      }
   }

   public LoggerContext getContext() {
      return this.context;
   }

   public synchronized void setLevel(Level var1) {
      if (var1 != null) {
         this.config = new Logger.PrivateConfig(this, this.config, var1);
      }

   }

   public Level getLevel() {
      return Logger.PrivateConfig.access$000(this.config);
   }

   public void log(Marker var1, String var2, Level var3, Message var4, Throwable var5) {
      if (var4 == null) {
         var4 = new SimpleMessage("");
      }

      this.config.config.getConfigurationMonitor().checkConfiguration();
      this.config.loggerConfig.log(this.getName(), var1, var2, var3, (Message)var4, var5);
   }

   public boolean isEnabled(Level var1, Marker var2, String var3) {
      return this.config.filter(var1, var2, var3);
   }

   public boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4) {
      return this.config.filter(var1, var2, var3, var4);
   }

   public boolean isEnabled(Level var1, Marker var2, String var3, Object... var4) {
      return this.config.filter(var1, var2, var3, var4);
   }

   public boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4) {
      return this.config.filter(var1, var2, var3, var4);
   }

   public boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4) {
      return this.config.filter(var1, var2, var3, var4);
   }

   public void addAppender(Appender var1) {
      this.config.config.addLoggerAppender(this, var1);
   }

   public void removeAppender(Appender var1) {
      this.config.loggerConfig.removeAppender(var1.getName());
   }

   public Map getAppenders() {
      return this.config.loggerConfig.getAppenders();
   }

   public Iterator getFilters() {
      Filter var1 = this.config.loggerConfig.getFilter();
      if (var1 == null) {
         return (new ArrayList()).iterator();
      } else if (var1 instanceof CompositeFilter) {
         return ((CompositeFilter)var1).iterator();
      } else {
         ArrayList var2 = new ArrayList();
         var2.add(var1);
         return var2.iterator();
      }
   }

   public int filterCount() {
      Filter var1 = this.config.loggerConfig.getFilter();
      if (var1 == null) {
         return 0;
      } else {
         return var1 instanceof CompositeFilter ? ((CompositeFilter)var1).size() : 1;
      }
   }

   public void addFilter(Filter var1) {
      this.config.config.addLoggerFilter(this, var1);
   }

   public boolean isAdditive() {
      return this.config.loggerConfig.isAdditive();
   }

   public void setAdditive(boolean var1) {
      this.config.config.setLoggerAdditive(this, var1);
   }

   void updateConfiguration(Configuration var1) {
      this.config = new Logger.PrivateConfig(this, var1, this);
   }

   public String toString() {
      String var1 = "" + this.getName() + ":" + this.getLevel();
      if (this.context == null) {
         return var1;
      } else {
         String var2 = this.context.getName();
         return var2 == null ? var1 : var1 + " in " + var2;
      }
   }

   protected class PrivateConfig {
      public final LoggerConfig loggerConfig;
      public final Configuration config;
      private final Level level;
      private final int intLevel;
      private final Logger logger;
      final Logger this$0;

      public PrivateConfig(Logger var1, Configuration var2, Logger var3) {
         this.this$0 = var1;
         this.config = var2;
         this.loggerConfig = var2.getLoggerConfig(var1.getName());
         this.level = this.loggerConfig.getLevel();
         this.intLevel = this.level.intLevel();
         this.logger = var3;
      }

      public PrivateConfig(Logger var1, Logger.PrivateConfig var2, Level var3) {
         this.this$0 = var1;
         this.config = var2.config;
         this.loggerConfig = var2.loggerConfig;
         this.level = var3;
         this.intLevel = this.level.intLevel();
         this.logger = var2.logger;
      }

      public PrivateConfig(Logger var1, Logger.PrivateConfig var2, LoggerConfig var3) {
         this.this$0 = var1;
         this.config = var2.config;
         this.loggerConfig = var3;
         this.level = var3.getLevel();
         this.intLevel = this.level.intLevel();
         this.logger = var2.logger;
      }

      public void logEvent(LogEvent var1) {
         this.config.getConfigurationMonitor().checkConfiguration();
         this.loggerConfig.log(var1);
      }

      boolean filter(Level var1, Marker var2, String var3) {
         this.config.getConfigurationMonitor().checkConfiguration();
         Filter var4 = this.config.getFilter();
         if (var4 != null) {
            Filter.Result var5 = var4.filter(this.logger, var1, var2, var3);
            if (var5 != Filter.Result.NEUTRAL) {
               return var5 == Filter.Result.ACCEPT;
            }
         }

         return this.intLevel >= var1.intLevel();
      }

      boolean filter(Level var1, Marker var2, String var3, Throwable var4) {
         this.config.getConfigurationMonitor().checkConfiguration();
         Filter var5 = this.config.getFilter();
         if (var5 != null) {
            Filter.Result var6 = var5.filter(this.logger, var1, var2, (Object)var3, (Throwable)var4);
            if (var6 != Filter.Result.NEUTRAL) {
               return var6 == Filter.Result.ACCEPT;
            }
         }

         return this.intLevel >= var1.intLevel();
      }

      boolean filter(Level var1, Marker var2, String var3, Object... var4) {
         this.config.getConfigurationMonitor().checkConfiguration();
         Filter var5 = this.config.getFilter();
         if (var5 != null) {
            Filter.Result var6 = var5.filter(this.logger, var1, var2, var3, var4);
            if (var6 != Filter.Result.NEUTRAL) {
               return var6 == Filter.Result.ACCEPT;
            }
         }

         return this.intLevel >= var1.intLevel();
      }

      boolean filter(Level var1, Marker var2, Object var3, Throwable var4) {
         this.config.getConfigurationMonitor().checkConfiguration();
         Filter var5 = this.config.getFilter();
         if (var5 != null) {
            Filter.Result var6 = var5.filter(this.logger, var1, var2, var3, var4);
            if (var6 != Filter.Result.NEUTRAL) {
               return var6 == Filter.Result.ACCEPT;
            }
         }

         return this.intLevel >= var1.intLevel();
      }

      boolean filter(Level var1, Marker var2, Message var3, Throwable var4) {
         this.config.getConfigurationMonitor().checkConfiguration();
         Filter var5 = this.config.getFilter();
         if (var5 != null) {
            Filter.Result var6 = var5.filter(this.logger, var1, var2, var3, var4);
            if (var6 != Filter.Result.NEUTRAL) {
               return var6 == Filter.Result.ACCEPT;
            }
         }

         return this.intLevel >= var1.intLevel();
      }

      static Level access$000(Logger.PrivateConfig var0) {
         return var0.level;
      }
   }
}
