// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.Logger;

public class AbstractDriverManagerConnectionSource extends AbstractConnectionSource
{
    private static final Logger LOGGER;
    private final String actualConnectionString;
    private final String connectionString;
    private final String driverClassName;
    private final char[] password;
    private final Property[] properties;
    private final char[] userName;
    
    public static Logger getLogger() {
        return AbstractDriverManagerConnectionSource.LOGGER;
    }
    
    public AbstractDriverManagerConnectionSource(final String driverClassName, final String connectionString, final String actualConnectionString, final char[] userName, final char[] password, final Property[] properties) {
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
    
    @Override
    public Connection getConnection() throws SQLException {
        this.loadDriver();
        final String actualConnectionString = this.getActualConnectionString();
        AbstractDriverManagerConnectionSource.LOGGER.debug("{} getting connection for '{}'", this.getClass().getSimpleName(), actualConnectionString);
        Connection connection;
        if (this.properties != null && this.properties.length > 0) {
            if (this.userName != null || this.password != null) {
                throw new SQLException("Either set the userName and password, or set the Properties, but not both.");
            }
            connection = DriverManager.getConnection(actualConnectionString, this.toProperties(this.properties));
        }
        else {
            connection = DriverManager.getConnection(actualConnectionString, this.toString(this.userName), this.toString(this.password));
        }
        AbstractDriverManagerConnectionSource.LOGGER.debug("{} acquired connection for '{}': {} ({}@{})", this.getClass().getSimpleName(), actualConnectionString, connection, connection.getClass().getName(), Integer.toHexString(connection.hashCode()));
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
        this.loadDriver(this.driverClassName);
    }
    
    protected void loadDriver(final String className) throws SQLException {
        if (className != null) {
            AbstractDriverManagerConnectionSource.LOGGER.debug("Loading driver class {}", className);
            try {
                Class.forName(className);
            }
            catch (Exception e) {
                throw new SQLException(String.format("The %s could not load the JDBC driver %s: %s", this.getClass().getSimpleName(), className, e.toString()), e);
            }
        }
    }
    
    protected Properties toProperties(final Property[] properties) {
        final Properties props = new Properties();
        for (final Property property : properties) {
            props.setProperty(property.getName(), property.getValue());
        }
        return props;
    }
    
    @Override
    public String toString() {
        return this.connectionString;
    }
    
    protected String toString(final char[] value) {
        return (value == null) ? null : String.valueOf(value);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder<B extends Builder<B>>
    {
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
        
        public B setConnectionString(final String connectionString) {
            this.connectionString = connectionString;
            return this.asBuilder();
        }
        
        public B setDriverClassName(final String driverClassName) {
            this.driverClassName = driverClassName;
            return this.asBuilder();
        }
        
        public B setPassword(final char[] password) {
            this.password = password;
            return this.asBuilder();
        }
        
        public B setProperties(final Property[] properties) {
            this.properties = properties;
            return this.asBuilder();
        }
        
        public B setUserName(final char[] userName) {
            this.userName = userName;
            return this.asBuilder();
        }
    }
}
