package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.status.StatusLogger;

public class AbstractDriverManagerConnectionSource extends AbstractConnectionSource {
  public static class Builder<B extends Builder<B>> {
    @PluginBuilderAttribute
    @Required
    protected String connectionString;
    
    @PluginBuilderAttribute
    protected String driverClassName;
    
    @PluginBuilderAttribute
    protected char[] password;
    
    @PluginElement("Properties")
    protected Property[] properties;
    
    @PluginBuilderAttribute
    protected char[] userName;
    
    protected B asBuilder() {
      return (B)this;
    }
    
    public String getConnectionString() {
      return this.connectionString;
    }
    
    public String getDriverClassName() {
      return this.driverClassName;
    }
    
    public char[] getPassword() {
      return this.password;
    }
    
    public Property[] getProperties() {
      return this.properties;
    }
    
    public char[] getUserName() {
      return this.userName;
    }
    
    public B setConnectionString(String connectionString) {
      this.connectionString = connectionString;
      return asBuilder();
    }
    
    public B setDriverClassName(String driverClassName) {
      this.driverClassName = driverClassName;
      return asBuilder();
    }
    
    public B setPassword(char[] password) {
      this.password = password;
      return asBuilder();
    }
    
    public B setProperties(Property[] properties) {
      this.properties = properties;
      return asBuilder();
    }
    
    public B setUserName(char[] userName) {
      this.userName = userName;
      return asBuilder();
    }
  }
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final String actualConnectionString;
  
  private final String connectionString;
  
  private final String driverClassName;
  
  private final char[] password;
  
  private final Property[] properties;
  
  private final char[] userName;
  
  public static Logger getLogger() {
    return LOGGER;
  }
  
  public AbstractDriverManagerConnectionSource(String driverClassName, String connectionString, String actualConnectionString, char[] userName, char[] password, Property[] properties) {
    this.driverClassName = driverClassName;
    this.connectionString = connectionString;
    this.actualConnectionString = actualConnectionString;
    this.userName = userName;
    this.password = password;
    this.properties = properties;
  }
  
  public String getActualConnectionString() {
    return this.actualConnectionString;
  }
  
  public Connection getConnection() throws SQLException {
    Connection connection;
    loadDriver();
    String actualConnectionString = getActualConnectionString();
    LOGGER.debug("{} getting connection for '{}'", getClass().getSimpleName(), actualConnectionString);
    if (this.properties != null && this.properties.length > 0) {
      if (this.userName != null || this.password != null)
        throw new SQLException("Either set the userName and password, or set the Properties, but not both."); 
      connection = DriverManager.getConnection(actualConnectionString, toProperties(this.properties));
    } else {
      connection = DriverManager.getConnection(actualConnectionString, toString(this.userName), toString(this.password));
    } 
    LOGGER.debug("{} acquired connection for '{}': {} ({}@{})", getClass().getSimpleName(), actualConnectionString, connection, connection
        .getClass().getName(), Integer.toHexString(connection.hashCode()));
    return connection;
  }
  
  public String getConnectionString() {
    return this.connectionString;
  }
  
  public String getDriverClassName() {
    return this.driverClassName;
  }
  
  public char[] getPassword() {
    return this.password;
  }
  
  public Property[] getProperties() {
    return this.properties;
  }
  
  public char[] getUserName() {
    return this.userName;
  }
  
  protected void loadDriver() throws SQLException {
    loadDriver(this.driverClassName);
  }
  
  protected void loadDriver(String className) throws SQLException {
    if (className != null) {
      LOGGER.debug("Loading driver class {}", className);
      try {
        Class.forName(className);
      } catch (Exception e) {
        throw new SQLException(String.format("The %s could not load the JDBC driver %s: %s", new Object[] { getClass().getSimpleName(), className, e.toString() }), e);
      } 
    } 
  }
  
  protected Properties toProperties(Property[] properties) {
    Properties props = new Properties();
    for (Property property : properties)
      props.setProperty(property.getName(), property.getValue()); 
    return props;
  }
  
  public String toString() {
    return this.connectionString;
  }
  
  protected String toString(char[] value) {
    return (value == null) ? null : String.valueOf(value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\db\jdbc\AbstractDriverManagerConnectionSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */