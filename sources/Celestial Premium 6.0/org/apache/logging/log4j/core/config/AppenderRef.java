/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="AppenderRef", category="Core", printObject=true)
@PluginAliases(value={"appender-ref"})
public final class AppenderRef {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String ref;
    private final Level level;
    private final Filter filter;

    private AppenderRef(String ref, Level level, Filter filter) {
        this.ref = ref;
        this.level = level;
        this.filter = filter;
    }

    public String getRef() {
        return this.ref;
    }

    public Level getLevel() {
        return this.level;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public String toString() {
        return this.ref;
    }

    @PluginFactory
    public static AppenderRef createAppenderRef(@PluginAttribute(value="ref") String ref, @PluginAttribute(value="level") String levelName, @PluginElement(value="Filters") Filter filter) {
        if (ref == null) {
            LOGGER.error("Appender references must contain a reference");
            return null;
        }
        Level level = null;
        if (levelName != null && (level = Level.toLevel(levelName, null)) == null) {
            LOGGER.error("Invalid level " + levelName + " on Appender reference " + ref);
        }
        return new AppenderRef(ref, level, filter);
    }
}

