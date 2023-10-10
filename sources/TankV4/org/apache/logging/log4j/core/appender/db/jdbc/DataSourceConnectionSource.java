package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "DataSource",
   category = "Core",
   elementType = "connectionSource",
   printObject = true
)
public final class DataSourceConnectionSource implements ConnectionSource {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final DataSource dataSource;
   private final String description;

   private DataSourceConnectionSource(String var1, DataSource var2) {
      this.dataSource = var2;
      this.description = "dataSource{ name=" + var1 + ", value=" + var2 + " }";
   }

   public Connection getConnection() throws SQLException {
      return this.dataSource.getConnection();
   }

   public String toString() {
      return this.description;
   }

   @PluginFactory
   public static DataSourceConnectionSource createConnectionSource(@PluginAttribute("jndiName") String var0) {
      if (Strings.isEmpty(var0)) {
         LOGGER.error("No JNDI name provided.");
         return null;
      } else {
         try {
            InitialContext var1 = new InitialContext();
            DataSource var2 = (DataSource)var1.lookup(var0);
            if (var2 == null) {
               LOGGER.error("No data source found with JNDI name [" + var0 + "].");
               return null;
            } else {
               return new DataSourceConnectionSource(var0, var2);
            }
         } catch (NamingException var3) {
            LOGGER.error((String)var3.getMessage(), (Throwable)var3);
            return null;
         }
      }
   }
}
