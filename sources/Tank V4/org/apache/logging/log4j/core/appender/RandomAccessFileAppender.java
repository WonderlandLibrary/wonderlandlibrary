package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
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
   name = "RandomAccessFile",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class RandomAccessFileAppender extends AbstractOutputStreamAppender {
   private final String fileName;
   private Object advertisement;
   private final Advertiser advertiser;

   private RandomAccessFileAppender(String var1, Layout var2, Filter var3, RandomAccessFileManager var4, String var5, boolean var6, boolean var7, Advertiser var8) {
      super(var1, var2, var3, var6, var7, var4);
      if (var8 != null) {
         HashMap var9 = new HashMap(var2.getContentFormat());
         var9.putAll(var4.getContentFormat());
         var9.put("contentType", var2.getContentType());
         var9.put("name", var1);
         this.advertisement = var8.advertise(var9);
      }

      this.fileName = var5;
      this.advertiser = var8;
   }

   public void stop() {
      super.stop();
      if (this.advertiser != null) {
         this.advertiser.unadvertise(this.advertisement);
      }

   }

   public void append(LogEvent var1) {
      ((RandomAccessFileManager)this.getManager()).setEndOfBatch(var1.isEndOfBatch());
      super.append(var1);
   }

   public String getFileName() {
      return this.fileName;
   }

   @PluginFactory
   public static RandomAccessFileAppender createAppender(@PluginAttribute("fileName") String var0, @PluginAttribute("append") String var1, @PluginAttribute("name") String var2, @PluginAttribute("immediateFlush") String var3, @PluginAttribute("ignoreExceptions") String var4, @PluginElement("Layout") Layout var5, @PluginElement("Filters") Filter var6, @PluginAttribute("advertise") String var7, @PluginAttribute("advertiseURI") String var8, @PluginConfiguration Configuration var9) {
      boolean var10 = Booleans.parseBoolean(var1, true);
      boolean var11 = Booleans.parseBoolean(var3, true);
      boolean var12 = Booleans.parseBoolean(var4, true);
      boolean var13 = Boolean.parseBoolean(var7);
      if (var2 == null) {
         LOGGER.error("No name provided for FileAppender");
         return null;
      } else if (var0 == null) {
         LOGGER.error("No filename provided for FileAppender with name " + var2);
         return null;
      } else {
         if (var5 == null) {
            var5 = PatternLayout.createLayout((String)null, (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
         }

         RandomAccessFileManager var14 = RandomAccessFileManager.getFileManager(var0, var10, var11, var8, (Layout)var5);
         return var14 == null ? null : new RandomAccessFileAppender(var2, (Layout)var5, var6, var14, var0, var12, var11, var13 ? var9.getAdvertiser() : null);
      }
   }
}
