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
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "DataSource", category = "Core", elementType = "connectionSource", printObject = true)
public final class DataSourceConnectionSource extends AbstractConnectionSource {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final DataSource dataSource;
  
  private final String description;
  
  private DataSourceConnectionSource(String dataSourceName, DataSource dataSource) {
    this.dataSource = dataSource;
    this.description = "dataSource{ name=" + dataSourceName + ", value=" + dataSource + " }";
  }
  
  public Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }
  
  public String toString() {
    return this.description;
  }
  
  @PluginFactory
  public static DataSourceConnectionSource createConnectionSource(@PluginAttribute("jndiName") String jndiName) {
    if (Strings.isEmpty(jndiName)) {
      LOGGER.error("No JNDI name provided.");
      return null;
    } 
    try {
      InitialContext context = new InitialContext();
      DataSource dataSource = (DataSource)context.lookup(jndiName);
      if (dataSource == null) {
        LOGGER.error("No data source found with JNDI name [" + jndiName + "].");
        return null;
      } 
      return new DataSourceConnectionSource(jndiName, dataSource);
    } catch (NamingException e) {
      LOGGER.error(e.getMessage(), e);
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\db\jdbc\DataSourceConnectionSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */