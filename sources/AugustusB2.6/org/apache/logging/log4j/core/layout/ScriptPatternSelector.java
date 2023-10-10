// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import javax.script.Bindings;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.core.LogEvent;
import java.util.List;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.script.ScriptRef;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;

@Plugin(name = "ScriptPatternSelector", category = "Core", elementType = "patternSelector", printObject = true)
public class ScriptPatternSelector implements PatternSelector, LocationAware
{
    private final Map<String, PatternFormatter[]> formatterMap;
    private final Map<String, String> patternMap;
    private final PatternFormatter[] defaultFormatters;
    private final String defaultPattern;
    private static Logger LOGGER;
    private final AbstractScript script;
    private final Configuration configuration;
    private final boolean requiresLocation;
    
    @Deprecated
    public ScriptPatternSelector(final AbstractScript script, final PatternMatch[] properties, final String defaultPattern, final boolean alwaysWriteExceptions, final boolean disableAnsi, final boolean noConsoleNoAnsi, final Configuration config) {
        this.formatterMap = new HashMap<String, PatternFormatter[]>();
        this.patternMap = new HashMap<String, String>();
        this.script = script;
        this.configuration = config;
        if (!(script instanceof ScriptRef)) {
            config.getScriptManager().addScript(script);
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        boolean needsLocation = false;
        for (final PatternMatch property : properties) {
            try {
                final List<PatternFormatter> list = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
                final PatternFormatter[] formatters = list.toArray(PatternFormatter.EMPTY_ARRAY);
                this.formatterMap.put(property.getKey(), formatters);
                this.patternMap.put(property.getKey(), property.getPattern());
                for (int i = 0; !needsLocation && i < formatters.length; needsLocation = formatters[i].requiresLocation(), ++i) {}
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
        final SimpleBindings bindings = new SimpleBindings();
        bindings.putAll(this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        bindings.put("logEvent", (Object)event);
        final Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        if (object == null) {
            return this.defaultFormatters;
        }
        final PatternFormatter[] patternFormatter = this.formatterMap.get(object.toString());
        return (patternFormatter == null) ? this.defaultFormatters : patternFormatter;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Deprecated
    public static ScriptPatternSelector createSelector(final AbstractScript script, final PatternMatch[] properties, final String defaultPattern, final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi, final Configuration configuration) {
        final Builder builder = newBuilder();
        builder.setScript(script);
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
        ScriptPatternSelector.LOGGER = StatusLogger.getLogger();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<ScriptPatternSelector>
    {
        @PluginElement("Script")
        private AbstractScript script;
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
        
        private Builder() {
            this.alwaysWriteExceptions = true;
        }
        
        @Override
        public ScriptPatternSelector build() {
            if (this.script == null) {
                ScriptPatternSelector.LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
                return null;
            }
            if (this.script instanceof ScriptRef && this.configuration.getScriptManager().getScript(this.script.getName()) == null) {
                ScriptPatternSelector.LOGGER.error("No script with name {} has been declared.", this.script.getName());
                return null;
            }
            if (this.defaultPattern == null) {
                this.defaultPattern = "%m%n";
            }
            if (this.properties == null || this.properties.length == 0) {
                ScriptPatternSelector.LOGGER.warn("No marker patterns were provided");
                return null;
            }
            return new ScriptPatternSelector(this.script, this.properties, this.defaultPattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.configuration);
        }
        
        public Builder setScript(final AbstractScript script) {
            this.script = script;
            return this;
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
        
        public Builder setConfiguration(final Configuration config) {
            this.configuration = config;
            return this;
        }
    }
}
