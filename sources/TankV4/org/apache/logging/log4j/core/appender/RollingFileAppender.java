package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

@Plugin(
   name = "RollingFile",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class RollingFileAppender extends AbstractOutputStreamAppender {
   private final String fileName;
   private final String filePattern;
   private Object advertisement;
   private final Advertiser advertiser;

   private RollingFileAppender(String var1, Layout var2, Filter var3, RollingFileManager var4, String var5, String var6, boolean var7, boolean var8, Advertiser var9) {
      super(var1, var2, var3, var7, var8, var4);
      if (var9 != null) {
         HashMap var10 = new HashMap(var2.getContentFormat());
         var10.put("contentType", var2.getContentType());
         var10.put("name", var1);
         this.advertisement = var9.advertise(var10);
      }

      this.fileName = var5;
      this.filePattern = var6;
      this.advertiser = var9;
   }

   public void stop() {
      super.stop();
      if (this.advertiser != null) {
         this.advertiser.unadvertise(this.advertisement);
      }

   }

   public void append(LogEvent var1) {
      ((RollingFileManager)this.getManager()).checkRollover(var1);
      super.append(var1);
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getFilePattern() {
      return this.filePattern;
   }

   @PluginFactory
   public static RollingFileAppender createAppender(@PluginAttribute("fileName") String var0, @PluginAttribute("filePattern") String var1, @PluginAttribute("append") String var2, @PluginAttribute("name") String var3, @PluginAttribute("bufferedIO") String var4, @PluginAttribute("immediateFlush") String var5, @PluginElement("Policy") TriggeringPolicy var6, @PluginElement("Strategy") RolloverStrategy var7, @PluginElement("Layout") Layout var8, @PluginElement("Filter") Filter var9, @PluginAttribute("ignoreExceptions") String var10, @PluginAttribute("advertise") String var11, @PluginAttribute("advertiseURI") String var12, @PluginConfiguration Configuration var13) {
      boolean var14 = Booleans.parseBoolean(var2, true);
      boolean var15 = Booleans.parseBoolean(var10, true);
      boolean var16 = Booleans.parseBoolean(var4, true);
      boolean var17 = Booleans.parseBoolean(var5, true);
      boolean var18 = Boolean.parseBoolean(var11);
      if (var3 == null) {
         LOGGER.error("No name provided for FileAppender");
         return null;
      } else if (var0 == null) {
         LOGGER.error("No filename was provided for FileAppender with name " + var3);
         return null;
      } else if (var1 == null) {
         LOGGER.error("No filename pattern provided for FileAppender with name " + var3);
         return null;
      } else if (var6 == null) {
         LOGGER.error("A TriggeringPolicy must be provided");
         return null;
      } else {
         if (var7 == null) {
            var7 = DefaultRolloverStrategy.createStrategy((String)null, (String)null, (String)null, String.valueOf(-1), var13);
         }

         if (var8 == null) {
            var8 = PatternLayout.createLayout((String)null, (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
         }

         RollingFileManager var19 = RollingFileManager.getFileManager(var0, var1, var14, var16, var6, (RolloverStrategy)var7, var12, (Layout)var8);
         return var19 == null ? null : new RollingFileAppender(var3, (Layout)var8, var9, var19, var0, var1, var15, var17, var18 ? var13.getAdvertiser() : null);
      }
   }
}
