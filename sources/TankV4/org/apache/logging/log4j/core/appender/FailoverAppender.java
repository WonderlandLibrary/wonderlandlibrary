package org.apache.logging.log4j.core.appender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
   name = "Failover",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class FailoverAppender extends AbstractAppender {
   private static final int DEFAULT_INTERVAL_SECONDS = 60;
   private final String primaryRef;
   private final String[] failovers;
   private final Configuration config;
   private AppenderControl primary;
   private final List failoverAppenders = new ArrayList();
   private final long intervalMillis;
   private long nextCheckMillis = 0L;
   private volatile boolean failure = false;

   private FailoverAppender(String var1, Filter var2, String var3, String[] var4, int var5, Configuration var6, boolean var7) {
      super(var1, var2, (Layout)null, var7);
      this.primaryRef = var3;
      this.failovers = var4;
      this.config = var6;
      this.intervalMillis = (long)var5;
   }

   public void start() {
      Map var1 = this.config.getAppenders();
      int var2 = 0;
      if (var1.containsKey(this.primaryRef)) {
         this.primary = new AppenderControl((Appender)var1.get(this.primaryRef), (Level)null, (Filter)null);
      } else {
         LOGGER.error("Unable to locate primary Appender " + this.primaryRef);
         ++var2;
      }

      String[] var3 = this.failovers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var1.containsKey(var6)) {
            this.failoverAppenders.add(new AppenderControl((Appender)var1.get(var6), (Level)null, (Filter)null));
         } else {
            LOGGER.error("Failover appender " + var6 + " is not configured");
         }
      }

      if (this.failoverAppenders.size() == 0) {
         LOGGER.error("No failover appenders are available");
         ++var2;
      }

      if (var2 == 0) {
         super.start();
      }

   }

   public void append(LogEvent var1) {
      if (!this.isStarted()) {
         this.error("FailoverAppender " + this.getName() + " did not start successfully");
      } else {
         if (!this.failure) {
            this.callAppender(var1);
         } else {
            long var2 = System.currentTimeMillis();
            if (var2 >= this.nextCheckMillis) {
               this.callAppender(var1);
            } else {
               this.failover(var1, (Exception)null);
            }
         }

      }
   }

   private void callAppender(LogEvent var1) {
      try {
         this.primary.callAppender(var1);
      } catch (Exception var3) {
         this.nextCheckMillis = System.currentTimeMillis() + this.intervalMillis;
         this.failure = true;
         this.failover(var1, var3);
      }

   }

   private void failover(LogEvent var1, Exception var2) {
      LoggingException var3 = var2 != null ? (var2 instanceof LoggingException ? (LoggingException)var2 : new LoggingException(var2)) : null;
      boolean var4 = false;
      Exception var5 = null;
      Iterator var6 = this.failoverAppenders.iterator();

      while(var6.hasNext()) {
         AppenderControl var7 = (AppenderControl)var6.next();

         try {
            var7.callAppender(var1);
            var4 = true;
            break;
         } catch (Exception var9) {
            if (var5 == null) {
               var5 = var9;
            }
         }
      }

      if (!var4 && !this.ignoreExceptions()) {
         if (var3 != null) {
            throw var3;
         } else {
            throw new LoggingException("Unable to write to failover appenders", var5);
         }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getName());
      var1.append(" primary=").append(this.primary).append(", failover={");
      boolean var2 = true;
      String[] var3 = this.failovers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (!var2) {
            var1.append(", ");
         }

         var1.append(var6);
         var2 = false;
      }

      var1.append("}");
      return var1.toString();
   }

   @PluginFactory
   public static FailoverAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("primary") String var1, @PluginElement("Failovers") String[] var2, @PluginAttribute("retryInterval") String var3, @PluginConfiguration Configuration var4, @PluginElement("Filters") Filter var5, @PluginAttribute("ignoreExceptions") String var6) {
      if (var0 == null) {
         LOGGER.error("A name for the Appender must be specified");
         return null;
      } else if (var1 == null) {
         LOGGER.error("A primary Appender must be specified");
         return null;
      } else if (var2 != null && var2.length != 0) {
         int var7 = parseInt(var3, 60);
         int var8;
         if (var7 >= 0) {
            var8 = var7 * 1000;
         } else {
            LOGGER.warn("Interval " + var3 + " is less than zero. Using default");
            var8 = 60000;
         }

         boolean var9 = Booleans.parseBoolean(var6, true);
         return new FailoverAppender(var0, var5, var1, var2, var8, var4, var9);
      } else {
         LOGGER.error("At least one failover Appender must be specified");
         return null;
      }
   }
}
