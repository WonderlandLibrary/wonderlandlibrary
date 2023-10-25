package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "ConnectionFactory", category = "Core", elementType = "connectionSource", printObject = true)
public final class FactoryMethodConnectionSource extends AbstractConnectionSource {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final DataSource dataSource;
  
  private final String description;
  
  private FactoryMethodConnectionSource(DataSource dataSource, String className, String methodName, String returnType) {
    this.dataSource = dataSource;
    this.description = "factory{ public static " + returnType + ' ' + className + '.' + methodName + "() }";
  }
  
  public Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }
  
  public String toString() {
    return this.description;
  }
  
  @PluginFactory
  public static FactoryMethodConnectionSource createConnectionSource(@PluginAttribute("class") String className, @PluginAttribute("method") String methodName) {
    final Method method;
    DataSource dataSource;
    if (Strings.isEmpty(className) || Strings.isEmpty(methodName)) {
      LOGGER.error("No class name or method name specified for the connection factory method.");
      return null;
    } 
    try {
      Class<?> factoryClass = Loader.loadClass(className);
      method = factoryClass.getMethod(methodName, new Class[0]);
    } catch (Exception e) {
      LOGGER.error(e.toString(), e);
      return null;
    } 
    Class<?> returnType = method.getReturnType();
    String returnTypeString = returnType.getName();
    if (returnType == DataSource.class) {
      try {
        dataSource = (DataSource)method.invoke(null, new Object[0]);
        returnTypeString = returnTypeString + "[" + dataSource + ']';
      } catch (Exception e) {
        LOGGER.error(e.toString(), e);
        return null;
      } 
    } else if (returnType == Connection.class) {
      dataSource = new DataSource() {
          public Connection getConnection() throws SQLException {
            try {
              return (Connection)method.invoke(null, new Object[0]);
            } catch (Exception e) {
              throw new SQLException("Failed to obtain connection from factory method.", e);
            } 
          }
          
          public Connection getConnection(String username, String password) throws SQLException {
            throw new UnsupportedOperationException();
          }
          
          public int getLoginTimeout() throws SQLException {
            throw new UnsupportedOperationException();
          }
          
          public PrintWriter getLogWriter() throws SQLException {
            throw new UnsupportedOperationException();
          }
          
          public Logger getParentLogger() {
            throw new UnsupportedOperationException();
          }
          
          public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
          }
          
          public void setLoginTimeout(int seconds) throws SQLException {
            throw new UnsupportedOperationException();
          }
          
          public void setLogWriter(PrintWriter out) throws SQLException {
            throw new UnsupportedOperationException();
          }
          
          public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
          }
        };
    } else {
      LOGGER.error("Method [{}.{}()] returns unsupported type [{}].", className, methodName, returnType
          .getName());
      return null;
    } 
    return new FactoryMethodConnectionSource(dataSource, className, methodName, returnTypeString);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\db\jdbc\FactoryMethodConnectionSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */