package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "DriverManager",
   category = "Core",
   elementType = "connectionSource",
   printObject = true
)
public final class DriverManagerConnectionSource implements ConnectionSource {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final String databasePassword;
   private final String databaseUrl;
   private final String databaseUsername;
   private final String description;

   private DriverManagerConnectionSource(String var1, String var2, String var3) {
      this.databaseUrl = var1;
      this.databaseUsername = var2;
      this.databasePassword = var3;
      this.description = "driverManager{ url=" + this.databaseUrl + ", username=" + this.databaseUsername + ", passwordHash=" + NameUtil.md5(this.databasePassword + this.getClass().getName()) + " }";
   }

   public Connection getConnection() throws SQLException {
      return this.databaseUsername == null ? DriverManager.getConnection(this.databaseUrl) : DriverManager.getConnection(this.databaseUrl, this.databaseUsername, this.databasePassword);
   }

   public String toString() {
      return this.description;
   }

   @PluginFactory
   public static DriverManagerConnectionSource createConnectionSource(@PluginAttribute("url") String var0, @PluginAttribute("username") String var1, @PluginAttribute("password") String var2) {
      if (Strings.isEmpty(var0)) {
         LOGGER.error("No JDBC URL specified for the database.");
         return null;
      } else {
         Driver var3;
         try {
            var3 = DriverManager.getDriver(var0);
         } catch (SQLException var5) {
            LOGGER.error((String)("No matching driver found for database URL [" + var0 + "]."), (Throwable)var5);
            return null;
         }

         if (var3 == null) {
            LOGGER.error("No matching driver found for database URL [" + var0 + "].");
            return null;
         } else {
            if (var1 == null || var1.trim().isEmpty()) {
               var1 = null;
               var2 = null;
            }

            return new DriverManagerConnectionSource(var0, var1, var2);
         }
      }
   }
}
