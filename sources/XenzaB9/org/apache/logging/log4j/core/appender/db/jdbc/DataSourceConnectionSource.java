// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import javax.naming.NamingException;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Objects;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DataSource", category = "Core", elementType = "connectionSource", printObject = true)
public final class DataSourceConnectionSource extends AbstractConnectionSource
{
    private static final Logger LOGGER;
    private final DataSource dataSource;
    private final String description;
    
    private DataSourceConnectionSource(final String dataSourceName, final DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource");
        this.description = "dataSource{ name=" + dataSourceName + ", value=" + dataSource + " }";
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static DataSourceConnectionSource createConnectionSource(@PluginAttribute("jndiName") final String jndiName) {
        if (!JndiManager.isJndiJdbcEnabled()) {
            DataSourceConnectionSource.LOGGER.error("JNDI must be enabled by setting log4j2.enableJndiJdbc=true");
            return null;
        }
        if (Strings.isEmpty(jndiName)) {
            DataSourceConnectionSource.LOGGER.error("No JNDI name provided.");
            return null;
        }
        try {
            final DataSource dataSource = JndiManager.getDefaultManager(DataSourceConnectionSource.class.getCanonicalName()).lookup(jndiName);
            if (dataSource == null) {
                DataSourceConnectionSource.LOGGER.error("No DataSource found with JNDI name [" + jndiName + "].");
                return null;
            }
            return new DataSourceConnectionSource(jndiName, dataSource);
        }
        catch (final NamingException e) {
            DataSourceConnectionSource.LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
