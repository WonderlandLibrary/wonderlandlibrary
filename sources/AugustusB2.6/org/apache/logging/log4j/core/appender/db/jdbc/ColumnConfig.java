// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Column", category = "Core", printObject = true)
public final class ColumnConfig
{
    private static final Logger LOGGER;
    private final String columnName;
    private final String columnNameKey;
    private final PatternLayout layout;
    private final String literalValue;
    private final boolean eventTimestamp;
    private final boolean unicode;
    private final boolean clob;
    
    @Deprecated
    public static ColumnConfig createColumnConfig(final Configuration config, final String name, final String pattern, final String literalValue, final String eventTimestamp, final String unicode, final String clob) {
        if (Strings.isEmpty(name)) {
            ColumnConfig.LOGGER.error("The column config is not valid because it does not contain a column name.");
            return null;
        }
        final boolean isEventTimestamp = Boolean.parseBoolean(eventTimestamp);
        final boolean isUnicode = Booleans.parseBoolean(unicode, true);
        final boolean isClob = Boolean.parseBoolean(clob);
        return newBuilder().setConfiguration(config).setName(name).setPattern(pattern).setLiteral(literalValue).setEventTimestamp(isEventTimestamp).setUnicode(isUnicode).setClob(isClob).build();
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private ColumnConfig(final String columnName, final PatternLayout layout, final String literalValue, final boolean eventDate, final boolean unicode, final boolean clob) {
        this.columnName = columnName;
        this.columnNameKey = ColumnMapping.toKey(columnName);
        this.layout = layout;
        this.literalValue = literalValue;
        this.eventTimestamp = eventDate;
        this.unicode = unicode;
        this.clob = clob;
    }
    
    public String getColumnName() {
        return this.columnName;
    }
    
    public String getColumnNameKey() {
        return this.columnNameKey;
    }
    
    public PatternLayout getLayout() {
        return this.layout;
    }
    
    public String getLiteralValue() {
        return this.literalValue;
    }
    
    public boolean isClob() {
        return this.clob;
    }
    
    public boolean isEventTimestamp() {
        return this.eventTimestamp;
    }
    
    public boolean isUnicode() {
        return this.unicode;
    }
    
    @Override
    public String toString() {
        return "{ name=" + this.columnName + ", layout=" + this.layout + ", literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp + " }";
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<ColumnConfig>
    {
        @PluginConfiguration
        private Configuration configuration;
        @PluginBuilderAttribute
        @Required(message = "No name provided")
        private String name;
        @PluginBuilderAttribute
        private String pattern;
        @PluginBuilderAttribute
        private String literal;
        @PluginBuilderAttribute
        private boolean isEventTimestamp;
        @PluginBuilderAttribute
        private boolean isUnicode;
        @PluginBuilderAttribute
        private boolean isClob;
        
        public Builder() {
            this.isUnicode = true;
        }
        
        @Override
        public ColumnConfig build() {
            if (Strings.isEmpty(this.name)) {
                ColumnConfig.LOGGER.error("The column config is not valid because it does not contain a column name.");
                return null;
            }
            final boolean isPattern = Strings.isNotEmpty(this.pattern);
            final boolean isLiteralValue = Strings.isNotEmpty(this.literal);
            if ((isPattern && isLiteralValue) || (isPattern && this.isEventTimestamp) || (isLiteralValue && this.isEventTimestamp)) {
                ColumnConfig.LOGGER.error("The pattern, literal, and isEventTimestamp attributes are mutually exclusive.");
                return null;
            }
            if (this.isEventTimestamp) {
                return new ColumnConfig(this.name, null, null, true, false, false, null);
            }
            if (isLiteralValue) {
                return new ColumnConfig(this.name, null, this.literal, false, false, false, null);
            }
            if (isPattern) {
                final PatternLayout layout = PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).withAlwaysWriteExceptions(false).build();
                return new ColumnConfig(this.name, layout, null, false, this.isUnicode, this.isClob, null);
            }
            ColumnConfig.LOGGER.error("To configure a column you must specify a pattern or literal or set isEventDate to true.");
            return null;
        }
        
        public Builder setClob(final boolean clob) {
            this.isClob = clob;
            return this;
        }
        
        public Builder setConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Builder setEventTimestamp(final boolean eventTimestamp) {
            this.isEventTimestamp = eventTimestamp;
            return this;
        }
        
        public Builder setLiteral(final String literal) {
            this.literal = literal;
            return this;
        }
        
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder setPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        public Builder setUnicode(final boolean unicode) {
            this.isUnicode = unicode;
            return this;
        }
    }
}
