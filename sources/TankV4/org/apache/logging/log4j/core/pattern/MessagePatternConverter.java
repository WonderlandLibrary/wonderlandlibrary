package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

@Plugin(
   name = "MessagePatternConverter",
   category = "Converter"
)
@ConverterKeys({"m", "msg", "message"})
public final class MessagePatternConverter extends LogEventPatternConverter {
   private final String[] formats;
   private final Configuration config;

   private MessagePatternConverter(Configuration var1, String[] var2) {
      super("Message", "message");
      this.formats = var2;
      this.config = var1;
   }

   public static MessagePatternConverter newInstance(Configuration var0, String[] var1) {
      return new MessagePatternConverter(var0, var1);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      Message var3 = var1.getMessage();
      if (var3 != null) {
         String var4;
         if (var3 instanceof MultiformatMessage) {
            var4 = ((MultiformatMessage)var3).getFormattedMessage(this.formats);
         } else {
            var4 = var3.getFormattedMessage();
         }

         if (var4 != null) {
            var2.append(this.config != null && var4.contains("${") ? this.config.getStrSubstitutor().replace(var1, var4) : var4);
         } else {
            var2.append("null");
         }
      }

   }
}
