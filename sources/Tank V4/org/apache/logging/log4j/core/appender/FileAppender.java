package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
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
   name = "File",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class FileAppender extends AbstractOutputStreamAppender {
   private final String fileName;
   private final Advertiser advertiser;
   private Object advertisement;

   private FileAppender(String var1, Layout var2, Filter var3, FileManager var4, String var5, boolean var6, boolean var7, Advertiser var8) {
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

   public String getFileName() {
      return this.fileName;
   }

   @PluginFactory
   public static FileAppender createAppender(@PluginAttribute("fileName") String var0, @PluginAttribute("append") String var1, @PluginAttribute("locking") String var2, @PluginAttribute("name") String var3, @PluginAttribute("immediateFlush") String var4, @PluginAttribute("ignoreExceptions") String var5, @PluginAttribute("bufferedIO") String var6, @PluginElement("Layout") Layout var7, @PluginElement("Filters") Filter var8, @PluginAttribute("advertise") String var9, @PluginAttribute("advertiseURI") String var10, @PluginConfiguration Configuration var11) {
      boolean var12 = Booleans.parseBoolean(var1, true);
      boolean var13 = Boolean.parseBoolean(var2);
      boolean var14 = Booleans.parseBoolean(var6, true);
      boolean var15 = Boolean.parseBoolean(var9);
      if (var13 && var14) {
         if (var6 != null) {
            LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for " + var0);
         }

         var14 = false;
      }

      boolean var16 = Booleans.parseBoolean(var4, true);
      boolean var17 = Booleans.parseBoolean(var5, true);
      if (var3 == null) {
         LOGGER.error("No name provided for FileAppender");
         return null;
      } else if (var0 == null) {
         LOGGER.error("No filename provided for FileAppender with name " + var3);
         return null;
      } else {
         if (var7 == null) {
            var7 = PatternLayout.createLayout((String)null, (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
         }

         FileManager var18 = FileManager.getFileManager(var0, var12, var13, var14, var10, (Layout)var7);
         return var18 == null ? null : new FileAppender(var3, (Layout)var7, var8, var18, var0, var17, var16, var15 ? var11.getAdvertiser() : null);
      }
   }
}
