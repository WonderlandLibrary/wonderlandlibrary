// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DriverManager", category = "Core", elementType = "connectionSource", printObject = true)
public class DriverManagerConnectionSource extends AbstractDriverManagerConnectionSource
{
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public DriverManagerConnectionSource(final String driverClassName, final String connectionString, final String actualConnectionString, final char[] userName, final char[] password, final Property[] properties) {
        super(driverClassName, connectionString, actualConnectionString, userName, password, properties);
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractDriverManagerConnectionSource.Builder<B> implements org.apache.logging.log4j.core.util.Builder<DriverManagerConnectionSource>
    {
        @Override
        public DriverManagerConnectionSource build() {
            return new DriverManagerConnectionSource(this.getDriverClassName(), this.getConnectionString(), this.getConnectionString(), this.getUserName(), this.getPassword(), this.getProperties());
        }
    }
}
