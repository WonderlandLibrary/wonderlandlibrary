package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(
   name = "map",
   category = "Lookup"
)
public class MapLookup implements StrLookup {
   private final Map map;

   public MapLookup(Map var1) {
      this.map = var1;
   }

   public MapLookup() {
      this.map = null;
   }

   public String lookup(String var1) {
      if (this.map == null) {
         return null;
      } else {
         String var2 = (String)this.map.get(var1);
         return var2 == null ? null : var2;
      }
   }

   public String lookup(LogEvent var1, String var2) {
      if (this.map == null && !(var1.getMessage() instanceof MapMessage)) {
         return null;
      } else {
         if (this.map != null && this.map.containsKey(var2)) {
            String var3 = (String)this.map.get(var2);
            if (var3 != null) {
               return var3;
            }
         }

         return var1.getMessage() instanceof MapMessage ? ((MapMessage)var1.getMessage()).get(var2) : null;
      }
   }
}
