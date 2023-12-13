package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "FileLocationPatternConverter",
   category = "Converter"
)
@ConverterKeys({"F", "file"})
public final class FileLocationPatternConverter extends LogEventPatternConverter {
   private static final FileLocationPatternConverter INSTANCE = new FileLocationPatternConverter();

   private FileLocationPatternConverter() {
      super("File Location", "file");
   }

   public static FileLocationPatternConverter newInstance(String[] var0) {
      return INSTANCE;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StackTraceElement var3 = var1.getSource();
      if (var3 != null) {
         var2.append(var3.getFileName());
      }

   }
}
