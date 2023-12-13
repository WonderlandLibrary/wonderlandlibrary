package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "MDCPatternConverter",
   category = "Converter"
)
@ConverterKeys({"X", "mdc", "MDC"})
public final class MDCPatternConverter extends LogEventPatternConverter {
   private final String key;

   private MDCPatternConverter(String[] var1) {
      super(var1 != null && var1.length > 0 ? "MDC{" + var1[0] + "}" : "MDC", "mdc");
      this.key = var1 != null && var1.length > 0 ? var1[0] : null;
   }

   public static MDCPatternConverter newInstance(String[] var0) {
      return new MDCPatternConverter(var0);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      Map var3 = var1.getContextMap();
      if (this.key == null) {
         if (var3 == null || var3.size() == 0) {
            var2.append("{}");
            return;
         }

         StringBuilder var4 = new StringBuilder("{");
         TreeSet var5 = new TreeSet(var3.keySet());

         String var7;
         for(Iterator var6 = var5.iterator(); var6.hasNext(); var4.append(var7).append("=").append((String)var3.get(var7))) {
            var7 = (String)var6.next();
            if (var4.length() > 1) {
               var4.append(", ");
            }
         }

         var4.append("}");
         var2.append(var4);
      } else if (var3 != null) {
         Object var8 = var3.get(this.key);
         if (var8 != null) {
            var2.append(var8);
         }
      }

   }
}
