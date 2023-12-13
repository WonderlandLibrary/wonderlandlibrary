package org.apache.logging.log4j.core.appender.routing;

import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "Route",
   category = "Core",
   printObject = true,
   deferChildren = true
)
public final class Route {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final Node node;
   private final String appenderRef;
   private final String key;

   private Route(Node var1, String var2, String var3) {
      this.node = var1;
      this.appenderRef = var2;
      this.key = var3;
   }

   public Node getNode() {
      return this.node;
   }

   public String getAppenderRef() {
      return this.appenderRef;
   }

   public String getKey() {
      return this.key;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Route(");
      var1.append("type=");
      if (this.appenderRef != null) {
         var1.append("static Reference=").append(this.appenderRef);
      } else if (this.node != null) {
         var1.append("dynamic - type=").append(this.node.getName());
      } else {
         var1.append("invalid Route");
      }

      if (this.key != null) {
         var1.append(" key='").append(this.key).append("'");
      } else {
         var1.append(" default");
      }

      var1.append(")");
      return var1.toString();
   }

   @PluginFactory
   public static Route createRoute(@PluginAttribute("ref") String var0, @PluginAttribute("key") String var1, @PluginNode Node var2) {
      if (var2 != null && var2.hasChildren()) {
         Node var4;
         for(Iterator var3 = var2.getChildren().iterator(); var3.hasNext(); var4 = (Node)var3.next()) {
         }

         if (var0 != null) {
            LOGGER.error("A route cannot be configured with an appender reference and an appender definition");
            return null;
         }
      } else if (var0 == null) {
         LOGGER.error("A route must specify an appender reference or an appender definition");
         return null;
      }

      return new Route(var2, var0, var1);
   }
}
