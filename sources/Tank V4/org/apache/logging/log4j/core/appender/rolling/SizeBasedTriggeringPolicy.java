package org.apache.logging.log4j.core.appender.rolling;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "SizeBasedTriggeringPolicy",
   category = "Core",
   printObject = true
)
public class SizeBasedTriggeringPolicy implements TriggeringPolicy {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private static final long KB = 1024L;
   private static final long MB = 1048576L;
   private static final long GB = 1073741824L;
   private static final long MAX_FILE_SIZE = 10485760L;
   private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
   private final long maxFileSize;
   private RollingFileManager manager;

   protected SizeBasedTriggeringPolicy() {
      this.maxFileSize = 10485760L;
   }

   protected SizeBasedTriggeringPolicy(long var1) {
      this.maxFileSize = var1;
   }

   public void initialize(RollingFileManager var1) {
      this.manager = var1;
   }

   public boolean isTriggeringEvent(LogEvent var1) {
      return this.manager.getFileSize() > this.maxFileSize;
   }

   public String toString() {
      return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ")";
   }

   @PluginFactory
   public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute("size") String var0) {
      long var1 = var0 == null ? 10485760L : valueOf(var0);
      return new SizeBasedTriggeringPolicy(var1);
   }

   private static long valueOf(String var0) {
      Matcher var1 = VALUE_PATTERN.matcher(var0);
      if (var1.matches()) {
         try {
            long var2 = NumberFormat.getNumberInstance(Locale.getDefault()).parse(var1.group(1)).longValue();
            String var4 = var1.group(3);
            if (var4.equalsIgnoreCase("")) {
               return var2;
            } else if (var4.equalsIgnoreCase("K")) {
               return var2 * 1024L;
            } else if (var4.equalsIgnoreCase("M")) {
               return var2 * 1048576L;
            } else if (var4.equalsIgnoreCase("G")) {
               return var2 * 1073741824L;
            } else {
               LOGGER.error("Units not recognized: " + var0);
               return 10485760L;
            }
         } catch (ParseException var5) {
            LOGGER.error((String)("Unable to parse numeric part: " + var0), (Throwable)var5);
            return 10485760L;
         }
      } else {
         LOGGER.error("Unable to parse bytes: " + var0);
         return 10485760L;
      }
   }
}
