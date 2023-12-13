package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
   name = "JDBC",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class JDBCAppender extends AbstractDatabaseAppender {
   private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

   private JDBCAppender(String var1, Filter var2, boolean var3, JDBCDatabaseManager var4) {
      super(var1, var2, var3, var4);
   }

   public String toString() {
      return this.description;
   }

   @PluginFactory
   public static JDBCAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("ignoreExceptions") String var1, @PluginElement("Filter") Filter var2, @PluginElement("ConnectionSource") ConnectionSource var3, @PluginAttribute("bufferSize") String var4, @PluginAttribute("tableName") String var5, @PluginElement("ColumnConfigs") ColumnConfig[] var6) {
      int var7 = AbstractAppender.parseInt(var4, 0);
      boolean var8 = Booleans.parseBoolean(var1, true);
      StringBuilder var9 = (new StringBuilder("jdbcManager{ description=")).append(var0).append(", bufferSize=").append(var7).append(", connectionSource=").append(var3.toString()).append(", tableName=").append(var5).append(", columns=[ ");
      int var10 = 0;
      ColumnConfig[] var11 = var6;
      int var12 = var6.length;

      for(int var13 = 0; var13 < var12; ++var13) {
         ColumnConfig var14 = var11[var13];
         if (var10++ > 0) {
            var9.append(", ");
         }

         var9.append(var14.toString());
      }

      var9.append(" ] }");
      JDBCDatabaseManager var15 = JDBCDatabaseManager.getJDBCDatabaseManager(var9.toString(), var7, var3, var5, var6);
      if (var15 == null) {
         return null;
      } else {
         return new JDBCAppender(var0, var2, var8, var15);
      }
   }
}
