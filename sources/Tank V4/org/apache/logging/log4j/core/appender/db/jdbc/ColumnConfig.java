package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "Column",
   category = "Core",
   printObject = true
)
public final class ColumnConfig {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final String columnName;
   private final PatternLayout layout;
   private final String literalValue;
   private final boolean eventTimestamp;
   private final boolean unicode;
   private final boolean clob;

   private ColumnConfig(String var1, PatternLayout var2, String var3, boolean var4, boolean var5, boolean var6) {
      this.columnName = var1;
      this.layout = var2;
      this.literalValue = var3;
      this.eventTimestamp = var4;
      this.unicode = var5;
      this.clob = var6;
   }

   public String getColumnName() {
      return this.columnName;
   }

   public PatternLayout getLayout() {
      return this.layout;
   }

   public String getLiteralValue() {
      return this.literalValue;
   }

   public boolean isEventTimestamp() {
      return this.eventTimestamp;
   }

   public boolean isUnicode() {
      return this.unicode;
   }

   public boolean isClob() {
      return this.clob;
   }

   public String toString() {
      return "{ name=" + this.columnName + ", layout=" + this.layout + ", literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp + " }";
   }

   @PluginFactory
   public static ColumnConfig createColumnConfig(@PluginConfiguration Configuration var0, @PluginAttribute("name") String var1, @PluginAttribute("pattern") String var2, @PluginAttribute("literal") String var3, @PluginAttribute("isEventTimestamp") String var4, @PluginAttribute("isUnicode") String var5, @PluginAttribute("isClob") String var6) {
      if (Strings.isEmpty(var1)) {
         LOGGER.error("The column config is not valid because it does not contain a column name.");
         return null;
      } else {
         boolean var7 = Strings.isNotEmpty(var2);
         boolean var8 = Strings.isNotEmpty(var3);
         boolean var9 = Boolean.parseBoolean(var4);
         boolean var10 = Booleans.parseBoolean(var5, true);
         boolean var11 = Boolean.parseBoolean(var6);
         if ((!var7 || !var8) && (!var7 || !var9) && (!var8 || !var9)) {
            if (var9) {
               return new ColumnConfig(var1, (PatternLayout)null, (String)null, true, false, false);
            } else if (var8) {
               return new ColumnConfig(var1, (PatternLayout)null, var3, false, false, false);
            } else if (var7) {
               return new ColumnConfig(var1, PatternLayout.createLayout(var2, var0, (RegexReplacement)null, (String)null, "false"), (String)null, false, var10, var11);
            } else {
               LOGGER.error("To configure a column you must specify a pattern or literal or set isEventDate to true.");
               return null;
            }
         } else {
            LOGGER.error("The pattern, literal, and isEventTimestamp attributes are mutually exclusive.");
            return null;
         }
      }
   }
}
