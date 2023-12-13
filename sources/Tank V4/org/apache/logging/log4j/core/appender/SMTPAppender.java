package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.HTMLLayout;
import org.apache.logging.log4j.core.net.SMTPManager;

@Plugin(
   name = "SMTP",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class SMTPAppender extends AbstractAppender {
   private static final int DEFAULT_BUFFER_SIZE = 512;
   protected final SMTPManager manager;

   private SMTPAppender(String var1, Filter var2, Layout var3, SMTPManager var4, boolean var5) {
      super(var1, var2, var3, var5);
      this.manager = var4;
   }

   @PluginFactory
   public static SMTPAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("to") String var1, @PluginAttribute("cc") String var2, @PluginAttribute("bcc") String var3, @PluginAttribute("from") String var4, @PluginAttribute("replyTo") String var5, @PluginAttribute("subject") String var6, @PluginAttribute("smtpProtocol") String var7, @PluginAttribute("smtpHost") String var8, @PluginAttribute("smtpPort") String var9, @PluginAttribute("smtpUsername") String var10, @PluginAttribute("smtpPassword") String var11, @PluginAttribute("smtpDebug") String var12, @PluginAttribute("bufferSize") String var13, @PluginElement("Layout") Layout var14, @PluginElement("Filter") Filter var15, @PluginAttribute("ignoreExceptions") String var16) {
      if (var0 == null) {
         LOGGER.error("No name provided for SMTPAppender");
         return null;
      } else {
         boolean var17 = Booleans.parseBoolean(var16, true);
         int var18 = AbstractAppender.parseInt(var9, 0);
         boolean var19 = Boolean.parseBoolean(var12);
         int var20 = var13 == null ? 512 : Integer.parseInt(var13);
         if (var14 == null) {
            var14 = HTMLLayout.createLayout((String)null, (String)null, (String)null, (String)null, (String)null, (String)null);
         }

         if (var15 == null) {
            var15 = ThresholdFilter.createFilter((String)null, (String)null, (String)null);
         }

         SMTPManager var21 = SMTPManager.getSMTPManager(var1, var2, var3, var4, var5, var6, var7, var8, var18, var10, var11, var19, var15.toString(), var20);
         return var21 == null ? null : new SMTPAppender(var0, (Filter)var15, (Layout)var14, var21, var17);
      }
   }

   public boolean isFiltered(LogEvent var1) {
      boolean var2 = super.isFiltered(var1);
      if (var2) {
         this.manager.add(var1);
      }

      return var2;
   }

   public void append(LogEvent var1) {
      this.manager.sendEvents(this.getLayout(), var1);
   }
}
