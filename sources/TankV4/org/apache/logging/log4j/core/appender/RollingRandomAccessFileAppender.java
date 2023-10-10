package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
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
   name = "RollingRandomAccessFile",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class RollingRandomAccessFileAppender extends AbstractOutputStreamAppender {
   private final String fileName;
   private final String filePattern;
   private Object advertisement;
   private final Advertiser advertiser;

   private RollingRandomAccessFileAppender(String var1, Layout var2, Filter var3, RollingFileManager var4, String var5, String var6, boolean var7, boolean var8, Advertiser var9) {
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
      RollingRandomAccessFileManager var2 = (RollingRandomAccessFileManager)this.getManager();
      var2.checkRollover(var1);
      var2.setEndOfBatch(var1.isEndOfBatch());
      super.append(var1);
   }

   public String getFileName() {
      return this.fileName;
   }

   public String getFilePattern() {
      return this.filePattern;
   }

   @PluginFactory
   public static RollingRandomAccessFileAppender createAppender(@PluginAttribute("fileName") String var0, @PluginAttribute("filePattern") String var1, @PluginAttribute("append") String var2, @PluginAttribute("name") String var3, @PluginAttribute("immediateFlush") String var4, @PluginElement("Policy") TriggeringPolicy var5, @PluginElement("Strategy") RolloverStrategy var6, @PluginElement("Layout") Layout var7, @PluginElement("Filter") Filter var8, @PluginAttribute("ignoreExceptions") String var9, @PluginAttribute("advertise") String var10, @PluginAttribute("advertiseURI") String var11, @PluginConfiguration Configuration var12) {
      boolean var13 = Booleans.parseBoolean(var2, true);
      boolean var14 = Booleans.parseBoolean(var9, true);
      boolean var15 = Booleans.parseBoolean(var4, true);
      boolean var16 = Boolean.parseBoolean(var10);
      if (var3 == null) {
         LOGGER.error("No name provided for FileAppender");
         return null;
      } else if (var0 == null) {
         LOGGER.error("No filename was provided for FileAppender with name " + var3);
         return null;
      } else if (var1 == null) {
         LOGGER.error("No filename pattern provided for FileAppender with name " + var3);
         return null;
      } else if (var5 == null) {
         LOGGER.error("A TriggeringPolicy must be provided");
         return null;
      } else {
         if (var6 == null) {
            var6 = DefaultRolloverStrategy.createStrategy((String)null, (String)null, (String)null, String.valueOf(-1), var12);
         }

         if (var7 == null) {
            var7 = PatternLayout.createLayout((String)null, (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
         }

         RollingRandomAccessFileManager var17 = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(var0, var1, var13, var15, var5, (RolloverStrategy)var6, var11, (Layout)var7);
         return var17 == null ? null : new RollingRandomAccessFileAppender(var3, (Layout)var7, var8, var17, var0, var1, var14, var15, var16 ? var12.getAdvertiser() : null);
      }
   }
}
