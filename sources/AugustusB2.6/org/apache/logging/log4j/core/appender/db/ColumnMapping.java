// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db;

import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Date;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Locale;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ColumnMapping", category = "Core", printObject = true)
public class ColumnMapping
{
    public static final ColumnMapping[] EMPTY_ARRAY;
    private static final Logger LOGGER;
    private final StringLayout layout;
    private final String literalValue;
    private final String name;
    private final String nameKey;
    private final String parameter;
    private final String source;
    private final Class<?> type;
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static String toKey(final String name) {
        return name.toUpperCase(Locale.ROOT);
    }
    
    private ColumnMapping(final String name, final String source, final StringLayout layout, final String literalValue, final String parameter, final Class<?> type) {
        this.name = name;
        this.nameKey = toKey(name);
        this.source = source;
        this.layout = layout;
        this.literalValue = literalValue;
        this.parameter = parameter;
        this.type = type;
    }
    
    public StringLayout getLayout() {
        return this.layout;
    }
    
    public String getLiteralValue() {
        return this.literalValue;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNameKey() {
        return this.nameKey;
    }
    
    public String getParameter() {
        return this.parameter;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return "ColumnMapping [name=" + this.name + ", source=" + this.source + ", literalValue=" + this.literalValue + ", parameter=" + this.parameter + ", type=" + this.type + ", layout=" + this.layout + "]";
    }
    
    static {
        EMPTY_ARRAY = new ColumnMapping[0];
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<ColumnMapping>
    {
        @PluginConfiguration
        private Configuration configuration;
        @PluginElement("Layout")
        private StringLayout layout;
        @PluginBuilderAttribute
        private String literal;
        @PluginBuilderAttribute
        @Required(message = "No column name provided")
        private String name;
        @PluginBuilderAttribute
        private String parameter;
        @PluginBuilderAttribute
        private String pattern;
        @PluginBuilderAttribute
        private String source;
        @PluginBuilderAttribute
        @Required(message = "No conversion type provided")
        private Class<?> type;
        
        public Builder() {
            this.type = String.class;
        }
        
        @Override
        public ColumnMapping build() {
            if (this.pattern != null) {
                this.layout = PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).withAlwaysWriteExceptions(false).build();
            }
            if (this.layout != null && this.literal != null && !Date.class.isAssignableFrom(this.type) && !ReadOnlyStringMap.class.isAssignableFrom(this.type) && !ThreadContextMap.class.isAssignableFrom(this.type) && !ThreadContextStack.class.isAssignableFrom(this.type)) {
                ColumnMapping.LOGGER.error("No 'layout' or 'literal' value specified and type ({}) is not compatible with ThreadContextMap, ThreadContextStack, or java.util.Date for the mapping", this.type, this);
                return null;
            }
            if (this.literal != null && this.parameter != null) {
                ColumnMapping.LOGGER.error("Only one of 'literal' or 'parameter' can be set on the column mapping {}", this);
                return null;
            }
            return new ColumnMapping(this.name, this.source, this.layout, this.literal, this.parameter, this.type, null);
        }
        
        public Builder setConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Builder setLayout(final StringLayout layout) {
            this.layout = layout;
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
        
        public Builder setParameter(final String parameter) {
            this.parameter = parameter;
            return this;
        }
        
        public Builder setPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        public Builder setSource(final String source) {
            this.source = source;
            return this;
        }
        
        public Builder setType(final Class<?> type) {
            this.type = type;
            return this;
        }
        
        @Override
        public String toString() {
            return "Builder [name=" + this.name + ", source=" + this.source + ", literal=" + this.literal + ", parameter=" + this.parameter + ", pattern=" + this.pattern + ", type=" + this.type + ", layout=" + this.layout + "]";
        }
    }
}
