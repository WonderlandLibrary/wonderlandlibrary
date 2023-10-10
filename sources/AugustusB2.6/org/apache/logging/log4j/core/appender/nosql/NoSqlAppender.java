// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.nosql;

import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.Property;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;

@Plugin(name = "NoSql", category = "Core", elementType = "appender", printObject = true)
public final class NoSqlAppender extends AbstractDatabaseAppender<NoSqlDatabaseManager<?>>
{
    private final String description;
    
    @Deprecated
    public static NoSqlAppender createAppender(final String name, final String ignore, final Filter filter, final String bufferSize, final NoSqlProvider<?> provider) {
        if (provider == null) {
            NoSqlAppender.LOGGER.error("NoSQL provider not specified for appender [{}].", name);
            return null;
        }
        final int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", provider=" + provider + " }";
        final NoSqlDatabaseManager<?> manager = NoSqlDatabaseManager.getNoSqlDatabaseManager(managerName, bufferSizeInt, provider);
        if (manager == null) {
            return null;
        }
        return new NoSqlAppender(name, filter, null, ignoreExceptions, null, manager);
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    private NoSqlAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final Property[] properties, final NoSqlDatabaseManager<?> manager) {
        super(name, filter, layout, ignoreExceptions, properties, manager);
        this.description = this.getName() + "{ manager=" + ((AbstractDatabaseAppender<Object>)this).getManager() + " }";
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<NoSqlAppender>
    {
        @PluginBuilderAttribute("bufferSize")
        private int bufferSize;
        @PluginElement("NoSqlProvider")
        private NoSqlProvider<?> provider;
        
        @Override
        public NoSqlAppender build() {
            final String name = this.getName();
            if (this.provider == null) {
                NoSqlAppender.LOGGER.error("NoSQL provider not specified for appender [{}].", name);
                return null;
            }
            final String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + this.bufferSize + ", provider=" + this.provider + " }";
            final NoSqlDatabaseManager<?> manager = NoSqlDatabaseManager.getNoSqlDatabaseManager(managerName, this.bufferSize, this.provider);
            if (manager == null) {
                return null;
            }
            return new NoSqlAppender(name, this.getFilter(), this.getLayout(), this.isIgnoreExceptions(), this.getPropertyArray(), manager, null);
        }
        
        public B setBufferSize(final int bufferSize) {
            this.bufferSize = bufferSize;
            return this.asBuilder();
        }
        
        public B setProvider(final NoSqlProvider<?> provider) {
            this.provider = provider;
            return this.asBuilder();
        }
    }
}
