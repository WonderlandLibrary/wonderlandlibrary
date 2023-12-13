package org.apache.logging.log4j.core.appender.db.nosql;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
   name = "NoSql",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class NoSQLAppender extends AbstractDatabaseAppender {
   private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

   private NoSQLAppender(String var1, Filter var2, boolean var3, NoSQLDatabaseManager var4) {
      super(var1, var2, var3, var4);
   }

   public String toString() {
      return this.description;
   }

   @PluginFactory
   public static NoSQLAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("ignoreExceptions") String var1, @PluginElement("Filter") Filter var2, @PluginAttribute("bufferSize") String var3, @PluginElement("NoSqlProvider") NoSQLProvider var4) {
      if (var4 == null) {
         LOGGER.error("NoSQL provider not specified for appender [{}].", var0);
         return null;
      } else {
         int var5 = AbstractAppender.parseInt(var3, 0);
         boolean var6 = Booleans.parseBoolean(var1, true);
         String var7 = "noSqlManager{ description=" + var0 + ", bufferSize=" + var5 + ", provider=" + var4 + " }";
         NoSQLDatabaseManager var8 = NoSQLDatabaseManager.getNoSQLDatabaseManager(var7, var5, var4);
         return var8 == null ? null : new NoSQLAppender(var0, var2, var6, var8);
      }
   }
}
