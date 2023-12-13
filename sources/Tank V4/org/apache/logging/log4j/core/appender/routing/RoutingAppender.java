package org.apache.logging.log4j.core.appender.routing;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
   name = "Routing",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class RoutingAppender extends AbstractAppender {
   private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
   private final Routes routes;
   private final Route defaultRoute;
   private final Configuration config;
   private final ConcurrentMap appenders = new ConcurrentHashMap();
   private final RewritePolicy rewritePolicy;

   private RoutingAppender(String var1, Filter var2, boolean var3, Routes var4, RewritePolicy var5, Configuration var6) {
      super(var1, var2, (Layout)null, var3);
      this.routes = var4;
      this.config = var6;
      this.rewritePolicy = var5;
      Route var7 = null;
      Route[] var8 = var4.getRoutes();
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         Route var11 = var8[var10];
         if (var11.getKey() == null) {
            if (var7 == null) {
               var7 = var11;
            } else {
               this.error("Multiple default routes. Route " + var11.toString() + " will be ignored");
            }
         }
      }

      this.defaultRoute = var7;
   }

   public void start() {
      Map var1 = this.config.getAppenders();
      Route[] var2 = this.routes.getRoutes();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Route var5 = var2[var4];
         if (var5.getAppenderRef() != null) {
            Appender var6 = (Appender)var1.get(var5.getAppenderRef());
            if (var6 != null) {
               String var7 = var5 == this.defaultRoute ? "ROUTING_APPENDER_DEFAULT" : var5.getKey();
               this.appenders.put(var7, new AppenderControl(var6, (Level)null, (Filter)null));
            } else {
               LOGGER.error("Appender " + var5.getAppenderRef() + " cannot be located. Route ignored");
            }
         }
      }

      super.start();
   }

   public void stop() {
      super.stop();
      Map var1 = this.config.getAppenders();
      Iterator var2 = this.appenders.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = ((AppenderControl)var3.getValue()).getAppender().getName();
         if (!var1.containsKey(var4)) {
            ((AppenderControl)var3.getValue()).getAppender().stop();
         }
      }

   }

   public void append(LogEvent var1) {
      if (this.rewritePolicy != null) {
         var1 = this.rewritePolicy.rewrite(var1);
      }

      String var2 = this.config.getStrSubstitutor().replace(var1, this.routes.getPattern());
      AppenderControl var3 = this.getControl(var2, var1);
      if (var3 != null) {
         var3.callAppender(var1);
      }

   }

   private synchronized AppenderControl getControl(String var1, LogEvent var2) {
      AppenderControl var3 = (AppenderControl)this.appenders.get(var1);
      if (var3 != null) {
         return var3;
      } else {
         Route var4 = null;
         Route[] var5 = this.routes.getRoutes();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Route var8 = var5[var7];
            if (var8.getAppenderRef() == null && var1.equals(var8.getKey())) {
               var4 = var8;
               break;
            }
         }

         if (var4 == null) {
            var4 = this.defaultRoute;
            var3 = (AppenderControl)this.appenders.get("ROUTING_APPENDER_DEFAULT");
            if (var3 != null) {
               return var3;
            }
         }

         if (var4 != null) {
            Appender var9 = this.createAppender(var4, var2);
            if (var9 == null) {
               return null;
            }

            var3 = new AppenderControl(var9, (Level)null, (Filter)null);
            this.appenders.put(var1, var3);
         }

         return var3;
      }
   }

   private Appender createAppender(Route var1, LogEvent var2) {
      Node var3 = var1.getNode();
      Iterator var4 = var3.getChildren().iterator();

      Node var5;
      do {
         if (!var4.hasNext()) {
            LOGGER.error("No Appender was configured for route " + var1.getKey());
            return null;
         }

         var5 = (Node)var4.next();
      } while(!var5.getType().getElementName().equals("appender"));

      Node var6 = new Node(var5);
      this.config.createConfiguration(var6, var2);
      if (var6.getObject() instanceof Appender) {
         Appender var7 = (Appender)var6.getObject();
         var7.start();
         return var7;
      } else {
         LOGGER.error("Unable to create Appender of type " + var5.getName());
         return null;
      }
   }

   @PluginFactory
   public static RoutingAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("ignoreExceptions") String var1, @PluginElement("Routes") Routes var2, @PluginConfiguration Configuration var3, @PluginElement("RewritePolicy") RewritePolicy var4, @PluginElement("Filters") Filter var5) {
      boolean var6 = Booleans.parseBoolean(var1, true);
      if (var0 == null) {
         LOGGER.error("No name provided for RoutingAppender");
         return null;
      } else if (var2 == null) {
         LOGGER.error("No routes defined for RoutingAppender");
         return null;
      } else {
         return new RoutingAppender(var0, var5, var6, var2, var4, var3);
      }
   }
}
