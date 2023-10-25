/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="Column", category="Core", printObject=true)
public final class ColumnConfig {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private final String columnName;
    private final PatternLayout layout;
    private final String literalValue;
    private final boolean eventTimestamp;
    private final boolean unicode;
    private final boolean clob;

    private ColumnConfig(String columnName, PatternLayout layout, String literalValue, boolean eventDate, boolean unicode, boolean clob) {
        this.columnName = columnName;
        this.layout = layout;
        this.literalValue = literalValue;
        this.eventTimestamp = eventDate;
        this.unicode = unicode;
        this.clob = clob;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public PatternLayout getLayout() {
        return this.layout;
    }

    public String getLiteralValue() {
        return this.literalValue;
    }

    public boolean isEventTimestamp() {
        return this.eventTimestamp;
    }

    public boolean isUnicode() {
        return this.unicode;
    }

    public boolean isClob() {
        return this.clob;
    }

    public String toString() {
        return "{ name=" + this.columnName + ", layout=" + this.layout + ", literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp + " }";
    }

    @PluginFactory
    public static ColumnConfig createColumnConfig(@PluginConfiguration Configuration config, @PluginAttribute(value="name") String name, @PluginAttribute(value="pattern") String pattern, @PluginAttribute(value="literal") String literalValue, @PluginAttribute(value="isEventTimestamp") String eventTimestamp, @PluginAttribute(value="isUnicode") String unicode, @PluginAttribute(value="isClob") String clob) {
        if (Strings.isEmpty(name)) {
            LOGGER.error("The column config is not valid because it does not contain a column name.");
            return null;
        }
        boolean isPattern = Strings.isNotEmpty(pattern);
        boolean isLiteralValue = Strings.isNotEmpty(literalValue);
        boolean isEventTimestamp = Boolean.parseBoolean(eventTimestamp);
        boolean isUnicode = Booleans.parseBoolean(unicode, true);
        boolean isClob = Boolean.parseBoolean(clob);
        if (isPattern && isLiteralValue || isPattern && isEventTimestamp || isLiteralValue && isEventTimestamp) {
            LOGGER.error("The pattern, literal, and isEventTimestamp attributes are mutually exclusive.");
            return null;
        }
        if (isEventTimestamp) {
            return new ColumnConfig(name, null, null, true, false, false);
        }
        if (isLiteralValue) {
            return new ColumnConfig(name, null, literalValue, false, false, false);
        }
        if (isPattern) {
            return new ColumnConfig(name, PatternLayout.createLayout(pattern, config, null, null, "false"), null, false, isUnicode, isClob);
        }
        LOGGER.error("To configure a column you must specify a pattern or literal or set isEventDate to true.");
        return null;
    }
}

