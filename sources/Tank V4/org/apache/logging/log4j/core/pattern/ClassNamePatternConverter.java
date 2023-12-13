package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "ClassNamePatternConverter",
   category = "Converter"
)
@ConverterKeys({"C", "class"})
public final class ClassNamePatternConverter extends NamePatternConverter {
   private static final String NA = "?";

   private ClassNamePatternConverter(String[] var1) {
      super("Class Name", "class name", var1);
   }

   public static ClassNamePatternConverter newInstance(String[] var0) {
      return new ClassNamePatternConverter(var0);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StackTraceElement var3 = var1.getSource();
      if (var3 == null) {
         var2.append("?");
      } else {
         var2.append(this.abbreviate(var3.getClassName()));
      }

   }
}
