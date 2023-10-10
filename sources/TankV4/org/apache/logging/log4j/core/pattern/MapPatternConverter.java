package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(
   name = "MapPatternConverter",
   category = "Converter"
)
@ConverterKeys({"K", "map", "MAP"})
public final class MapPatternConverter extends LogEventPatternConverter {
   private final String key;

   private MapPatternConverter(String[] var1) {
      super(var1 != null && var1.length > 0 ? "MAP{" + var1[0] + "}" : "MAP", "map");
      this.key = var1 != null && var1.length > 0 ? var1[0] : null;
   }

   public static MapPatternConverter newInstance(String[] var0) {
      return new MapPatternConverter(var0);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      if (var1.getMessage() instanceof MapMessage) {
         MapMessage var3 = (MapMessage)var1.getMessage();
         Map var4 = var3.getData();
         if (this.key == null) {
            if (var4.size() == 0) {
               var2.append("{}");
               return;
            }

            StringBuilder var5 = new StringBuilder("{");
            TreeSet var6 = new TreeSet(var4.keySet());

            String var8;
            for(Iterator var7 = var6.iterator(); var7.hasNext(); var5.append(var8).append("=").append((String)var4.get(var8))) {
               var8 = (String)var7.next();
               if (var5.length() > 1) {
                  var5.append(", ");
               }
            }

            var5.append("}");
            var2.append(var5);
         } else {
            String var9 = (String)var4.get(this.key);
            if (var9 != null) {
               var2.append(var9);
            }
         }

      }
   }
}
