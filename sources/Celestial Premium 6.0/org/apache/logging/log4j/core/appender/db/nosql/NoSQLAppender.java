/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.nosql;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLDatabaseManager;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(name="NoSql", category="Core", elementType="appender", printObject=true)
public final class NoSQLAppender
extends AbstractDatabaseAppender<NoSQLDatabaseManager<?>> {
    private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

    private NoSQLAppender(String name, Filter filter, boolean ignoreExceptions, NoSQLDatabaseManager<?> manager) {
        super(name, filter, ignoreExceptions, manager);
    }

    @Override
    public String toString() {
        return this.description;
    }

    @PluginFactory
    public static NoSQLAppender createAppender(@PluginAttribute(value="name") String name, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginElement(value="Filter") Filter filter, @PluginAttribute(value="bufferSize") String bufferSize, @PluginElement(value="NoSqlProvider") NoSQLProvider<?> provider) {
        if (provider == null) {
            LOGGER.error("NoSQL provider not specified for appender [{}].", name);
            return null;
        }
        int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", provider=" + provider + " }";
        NoSQLDatabaseManager<?> manager = NoSQLDatabaseManager.getNoSQLDatabaseManager(managerName, bufferSizeInt, provider);
        if (manager == null) {
            return null;
        }
        return new NoSQLAppender(name, filter, ignoreExceptions, manager);
    }
}

