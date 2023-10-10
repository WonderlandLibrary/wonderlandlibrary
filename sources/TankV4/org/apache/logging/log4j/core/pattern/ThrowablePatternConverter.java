package org.apache.logging.log4j.core.pattern;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;

@Plugin(
   name = "ThrowablePatternConverter",
   category = "Converter"
)
@ConverterKeys({"ex", "throwable", "exception"})
public class ThrowablePatternConverter extends LogEventPatternConverter {
   private String rawOption;
   protected final ThrowableFormatOptions options;

   protected ThrowablePatternConverter(String var1, String var2, String[] var3) {
      super(var1, var2);
      this.options = ThrowableFormatOptions.newInstance(var3);
      if (var3 != null && var3.length > 0) {
         this.rawOption = var3[0];
      }

   }

   public static ThrowablePatternConverter newInstance(String[] var0) {
      return new ThrowablePatternConverter("Throwable", "throwable", var0);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      Throwable var3 = var1.getThrown();
      if (this == false) {
         this.formatSubShortOption(var3, var2);
      } else if (var3 != null && this.options.anyLines()) {
         this.formatOption(var3, var2);
      }

   }

   private void formatSubShortOption(Throwable var1, StringBuilder var2) {
      StackTraceElement var4 = null;
      if (var1 != null) {
         StackTraceElement[] var3 = var1.getStackTrace();
         if (var3 != null && var3.length > 0) {
            var4 = var3[0];
         }
      }

      if (var1 != null && var4 != null) {
         String var6 = "";
         if ("short.className".equalsIgnoreCase(this.rawOption)) {
            var6 = var4.getClassName();
         } else if ("short.methodName".equalsIgnoreCase(this.rawOption)) {
            var6 = var4.getMethodName();
         } else if ("short.lineNumber".equalsIgnoreCase(this.rawOption)) {
            var6 = String.valueOf(var4.getLineNumber());
         } else if ("short.message".equalsIgnoreCase(this.rawOption)) {
            var6 = var1.getMessage();
         } else if ("short.localizedMessage".equalsIgnoreCase(this.rawOption)) {
            var6 = var1.getLocalizedMessage();
         } else if ("short.fileName".equalsIgnoreCase(this.rawOption)) {
            var6 = var4.getFileName();
         }

         int var5 = var2.length();
         if (var5 > 0 && !Character.isWhitespace(var2.charAt(var5 - 1))) {
            var2.append(" ");
         }

         var2.append(var6);
      }

   }

   private void formatOption(Throwable var1, StringBuilder var2) {
      StringWriter var3 = new StringWriter();
      var1.printStackTrace(new PrintWriter(var3));
      int var4 = var2.length();
      if (var4 > 0 && !Character.isWhitespace(var2.charAt(var4 - 1))) {
         var2.append(' ');
      }

      if (this.options.allLines() && Constants.LINE_SEP.equals(this.options.getSeparator())) {
         var2.append(var3.toString());
      } else {
         StringBuilder var5 = new StringBuilder();
         String[] var6 = var3.toString().split(Constants.LINE_SEP);
         int var7 = this.options.minLines(var6.length) - 1;

         for(int var8 = 0; var8 <= var7; ++var8) {
            var5.append(var6[var8]);
            if (var8 < var7) {
               var5.append(this.options.getSeparator());
            }
         }

         var2.append(var5.toString());
      }

   }

   public boolean handlesThrowable() {
      return true;
   }
}
