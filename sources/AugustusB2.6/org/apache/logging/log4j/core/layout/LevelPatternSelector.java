// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import java.util.Iterator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import java.util.List;
import org.apache.logging.log4j.core.pattern.PatternParser;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;

@Plugin(name = "LevelPatternSelector", category = "Core", elementType = "patternSelector", printObject = true)
public class LevelPatternSelector implements PatternSelector, LocationAware
{
    private final Map<String, PatternFormatter[]> formatterMap;
    private final Map<String, String> patternMap;
    private final PatternFormatter[] defaultFormatters;
    private final String defaultPattern;
    private static Logger LOGGER;
    private final boolean requiresLocation;
    
    @Deprecated
    public LevelPatternSelector(final PatternMatch[] properties, final String defaultPattern, final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi, final Configuration config) {
        this(properties, defaultPattern, alwaysWriteExceptions, false, noConsoleNoAnsi, config);
    }
    
    private LevelPatternSelector(final PatternMatch[] properties, final String defaultPattern, final boolean alwaysWriteExceptions, final boolean disableAnsi, final boolean noConsoleNoAnsi, final Configuration config) {
        this.formatterMap = new HashMap<String, PatternFormatter[]>();
        this.patternMap = new HashMap<String, String>();
        boolean needsLocation = false;
        final PatternParser parser = PatternLayout.createPatternParser(config);
        for (final PatternMatch property : properties) {
            try {
                final List<PatternFormatter> list = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
                final PatternFormatter[] formatters = list.toArray(PatternFormatter.EMPTY_ARRAY);
                this.formatterMap.put(property.getKey(), formatters);
                for (int i = 0; !needsLocation && i < formatters.length; needsLocation = formatters[i].requiresLocation(), ++i) {}
                this.patternMap.put(property.getKey(), property.getPattern());
            }
            catch (RuntimeException ex) {
                throw new IllegalArgumentException("Cannot parse pattern '" + property.getPattern() + "'", ex);
            }
        }
        try {
            final List<PatternFormatter> list2 = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
            this.defaultFormatters = list2.toArray(PatternFormatter.EMPTY_ARRAY);
            this.defaultPattern = defaultPattern;
            for (int j = 0; !needsLocation && j < this.defaultFormatters.length; needsLocation = this.defaultFormatters[j].requiresLocation(), ++j) {}
        }
        catch (RuntimeException ex2) {
            throw new IllegalArgumentException("Cannot parse pattern '" + defaultPattern + "'", ex2);
        }
        this.requiresLocation = needsLocation;
    }
    
    @Override
    public boolean requiresLocation() {
        return this.requiresLocation;
    }
    
    @Override
    public PatternFormatter[] getFormatters(final LogEvent event) {
        final Level level = event.getLevel();
        if (level == null) {
            return this.defaultFormatters;
        }
        for (final String key : this.formatterMap.keySet()) {
            if (level.name().equalsIgnoreCase(key)) {
                return this.formatterMap.get(key);
            }
        }
        return this.defaultFormatters;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Deprecated
    public static LevelPatternSelector createSelector(final PatternMatch[] properties, final String defaultPattern, final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi, final Configuration configuration) {
        final Builder builder = newBuilder();
        builder.setProperties(properties);
        builder.setDefaultPattern(defaultPattern);
        builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
        builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
        builder.setConfiguration(configuration);
        return builder.build();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final Map.Entry<String, String> entry : this.patternMap.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append("key=\"").append(entry.getKey()).append("\", pattern=\"").append(entry.getValue()).append("\"");
            first = false;
        }
        if (!first) {
            sb.append(", ");
        }
        sb.append("default=\"").append(this.defaultPattern).append("\"");
        return sb.toString();
    }
    
    static {
        LevelPatternSelector.LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<LevelPatternSelector>
    {
        @PluginElement("PatternMatch")
        private PatternMatch[] properties;
        @PluginBuilderAttribute("defaultPattern")
        private String defaultPattern;
        @PluginBuilderAttribute("alwaysWriteExceptions")
        private boolean alwaysWriteExceptions;
        @PluginBuilderAttribute("disableAnsi")
        private boolean disableAnsi;
        @PluginBuilderAttribute("noConsoleNoAnsi")
        private boolean noConsoleNoAnsi;
        @PluginConfiguration
        private Configuration configuration;
        
        public Builder() {
            this.alwaysWriteExceptions = true;
        }
        
        @Override
        public LevelPatternSelector build() {
            if (this.defaultPattern == null) {
                this.defaultPattern = "%m%n";
            }
            if (this.properties == null || this.properties.length == 0) {
                LevelPatternSelector.LOGGER.warn("No marker patterns were provided with PatternMatch");
                return null;
            }
            return new LevelPatternSelector(this.properties, this.defaultPattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.configuration, null);
        }
        
        public Builder setProperties(final PatternMatch[] properties) {
            this.properties = properties;
            return this;
        }
        
        public Builder setDefaultPattern(final String defaultPattern) {
            this.defaultPattern = defaultPattern;
            return this;
        }
        
        public Builder setAlwaysWriteExceptions(final boolean alwaysWriteExceptions) {
            this.alwaysWriteExceptions = alwaysWriteExceptions;
            return this;
        }
        
        public Builder setDisableAnsi(final boolean disableAnsi) {
            this.disableAnsi = disableAnsi;
            return this;
        }
        
        public Builder setNoConsoleNoAnsi(final boolean noConsoleNoAnsi) {
            this.noConsoleNoAnsi = noConsoleNoAnsi;
            return this;
        }
        
        public Builder setConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
    }
}
