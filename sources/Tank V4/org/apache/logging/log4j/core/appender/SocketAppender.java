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
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.DatagramSocketManager;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.net.TCPSocketManager;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(
   name = "Socket",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public class SocketAppender extends AbstractOutputStreamAppender {
   private Object advertisement;
   private final Advertiser advertiser;

   protected SocketAppender(String var1, Layout var2, Filter var3, AbstractSocketManager var4, boolean var5, boolean var6, Advertiser var7) {
      super(var1, var2, var3, var5, var6, var4);
      if (var7 != null) {
         HashMap var8 = new HashMap(var2.getContentFormat());
         var8.putAll(var4.getContentFormat());
         var8.put("contentType", var2.getContentType());
         var8.put("name", var1);
         this.advertisement = var7.advertise(var8);
      }

      this.advertiser = var7;
   }

   public void stop() {
      super.stop();
      if (this.advertiser != null) {
         this.advertiser.unadvertise(this.advertisement);
      }

   }

   @PluginFactory
   public static SocketAppender createAppender(@PluginAttribute("host") String var0, @PluginAttribute("port") String var1, @PluginAttribute("protocol") String var2, @PluginAttribute("reconnectionDelay") String var3, @PluginAttribute("immediateFail") String var4, @PluginAttribute("name") String var5, @PluginAttribute("immediateFlush") String var6, @PluginAttribute("ignoreExceptions") String var7, @PluginElement("Layout") Layout var8, @PluginElement("Filters") Filter var9, @PluginAttribute("advertise") String var10, @PluginConfiguration Configuration var11) {
      boolean var12 = Booleans.parseBoolean(var6, true);
      boolean var13 = Boolean.parseBoolean(var10);
      boolean var14 = Booleans.parseBoolean(var7, true);
      boolean var15 = Booleans.parseBoolean(var4, true);
      int var16 = AbstractAppender.parseInt(var3, 0);
      int var17 = AbstractAppender.parseInt(var1, 0);
      if (var8 == null) {
         var8 = SerializedLayout.createLayout();
      }

      if (var5 == null) {
         LOGGER.error("No name provided for SocketAppender");
         return null;
      } else {
         Protocol var18 = (Protocol)EnglishEnums.valueOf(Protocol.class, var2 != null ? var2 : Protocol.TCP.name());
         if (var18.equals(Protocol.UDP)) {
            var12 = true;
         }

         AbstractSocketManager var19 = createSocketManager(var18, var0, var17, var16, var15, (Layout)var8);
         return var19 == null ? null : new SocketAppender(var5, (Layout)var8, var9, var19, var14, var12, var13 ? var11.getAdvertiser() : null);
      }
   }

   protected static AbstractSocketManager createSocketManager(Protocol var0, String var1, int var2, int var3, boolean var4, Layout var5) {
      switch(var0) {
      case TCP:
         return TCPSocketManager.getSocketManager(var1, var2, var3, var4, var5);
      case UDP:
         return DatagramSocketManager.getSocketManager(var1, var2, var5);
      default:
         return null;
      }
   }
}
