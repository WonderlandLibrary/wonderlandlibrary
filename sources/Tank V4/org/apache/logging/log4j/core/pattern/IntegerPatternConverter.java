package org.apache.logging.log4j.core.pattern;

import java.util.Date;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "IntegerPatternConverter",
   category = "FileConverter"
)
@ConverterKeys({"i", "index"})
public final class IntegerPatternConverter extends AbstractPatternConverter implements ArrayPatternConverter {
   private static final IntegerPatternConverter INSTANCE = new IntegerPatternConverter();

   private IntegerPatternConverter() {
      super("Integer", "integer");
   }

   public static IntegerPatternConverter newInstance(String[] var0) {
      return INSTANCE;
   }

   public void format(StringBuilder var1, Object... var2) {
      Object[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if (var6 instanceof Integer) {
            this.format(var6, var1);
            break;
         }
      }

   }

   public void format(Object var1, StringBuilder var2) {
      if (var1 instanceof Integer) {
         var2.append(var1.toString());
      }

      if (var1 instanceof Date) {
         var2.append(Long.toString(((Date)var1).getTime()));
      }

   }
}
