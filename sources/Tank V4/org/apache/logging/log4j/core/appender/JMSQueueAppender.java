package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.net.JMSQueueManager;

@Plugin(
   name = "JMSQueue",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class JMSQueueAppender extends AbstractAppender {
   private final JMSQueueManager manager;

   private JMSQueueAppender(String var1, Filter var2, Layout var3, JMSQueueManager var4, boolean var5) {
      super(var1, var2, var3, var5);
      this.manager = var4;
   }

   public void append(LogEvent var1) {
      try {
         this.manager.send(this.getLayout().toSerializable(var1));
      } catch (Exception var3) {
         throw new AppenderLoggingException(var3);
      }
   }

   @PluginFactory
   public static JMSQueueAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("factoryName") String var1, @PluginAttribute("providerURL") String var2, @PluginAttribute("urlPkgPrefixes") String var3, @PluginAttribute("securityPrincipalName") String var4, @PluginAttribute("securityCredentials") String var5, @PluginAttribute("factoryBindingName") String var6, @PluginAttribute("queueBindingName") String var7, @PluginAttribute("userName") String var8, @PluginAttribute("password") String var9, @PluginElement("Layout") Layout var10, @PluginElement("Filter") Filter var11, @PluginAttribute("ignoreExceptions") String var12) {
      if (var0 == null) {
         LOGGER.error("No name provided for JMSQueueAppender");
         return null;
      } else {
         boolean var13 = Booleans.parseBoolean(var12, true);
         JMSQueueManager var14 = JMSQueueManager.getJMSQueueManager(var1, var2, var3, var4, var5, var6, var7, var8, var9);
         if (var14 == null) {
            return null;
         } else {
            if (var10 == null) {
               var10 = SerializedLayout.createLayout();
            }

            return new JMSQueueAppender(var0, var11, (Layout)var10, var14, var13);
         }
      }
   }
}
