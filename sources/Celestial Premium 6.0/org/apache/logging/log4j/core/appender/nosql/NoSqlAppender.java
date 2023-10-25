/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.nosql;

import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.appender.nosql.NoSqlDatabaseManager;
import org.apache.logging.log4j.core.appender.nosql.NoSqlProvider;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.Booleans;

@Plugin(name="NoSql", category="Core", elementType="appender", printObject=true)
public final class NoSqlAppender
extends AbstractDatabaseAppender<NoSqlDatabaseManager<?>> {
    private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

    @Deprecated
    public static NoSqlAppender createAppender(String name, String ignore, Filter filter, String bufferSize, NoSqlProvider<?> provider) {
        if (provider == null) {
            LOGGER.error("NoSQL provider not specified for appender [{}].", name);
            return null;
        }
        int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", provider=" + provider + " }";
        NoSqlDatabaseManager<?> manager = NoSqlDatabaseManager.getNoSqlDatabaseManager(managerName, bufferSizeInt, provider);
        if (manager == null) {
            return null;
        }
        return new NoSqlAppender(name, filter, null, ignoreExceptions, null, manager);
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return (B)((Builder)new Builder().asBuilder());
    }

    private NoSqlAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, NoSqlDatabaseManager<?> manager) {
        super(name, filter, layout, ignoreExceptions, properties, manager);
    }

    @Override
    public String toString() {
        return this.description;
    }

    public static class Builder<B extends Builder<B>>
    extends AbstractAppender.Builder<B>
    implements org.apache.logging.log4j.core.util.Builder<NoSqlAppender> {
        @PluginBuilderAttribute(value="bufferSize")
        private int bufferSize;
        @PluginElement(value="NoSqlProvider")
        private NoSqlProvider<?> provider;

        @Override
        public NoSqlAppender build() {
            String name = this.getName();
            if (this.provider == null) {
                LOGGER.error("NoSQL provider not specified for appender [{}].", name);
                return null;
            }
            String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + this.bufferSize + ", provider=" + this.provider + " }";
            NoSqlDatabaseManager<?> manager = NoSqlDatabaseManager.getNoSqlDatabaseManager(managerName, this.bufferSize, this.provider);
            if (manager == null) {
                return null;
            }
            return new NoSqlAppender(name, this.getFilter(), this.getLayout(), this.isIgnoreExceptions(), this.getPropertyArray(), manager);
        }

        public B setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return (B)((Builder)this.asBuilder());
        }

        public B setProvider(NoSqlProvider<?> provider) {
            this.provider = provider;
            return (B)((Builder)this.asBuilder());
        }
    }
}

