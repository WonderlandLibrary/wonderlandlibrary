/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternMatch;
import org.apache.logging.log4j.core.layout.PatternSelector;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.script.ScriptRef;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="ScriptPatternSelector", category="Core", elementType="patternSelector", printObject=true)
public class ScriptPatternSelector
implements PatternSelector,
LocationAware {
    private final Map<String, PatternFormatter[]> formatterMap = new HashMap<String, PatternFormatter[]>();
    private final Map<String, String> patternMap = new HashMap<String, String>();
    private final PatternFormatter[] defaultFormatters;
    private final String defaultPattern;
    private static Logger LOGGER = StatusLogger.getLogger();
    private final AbstractScript script;
    private final Configuration configuration;
    private final boolean requiresLocation;

    @Deprecated
    public ScriptPatternSelector(AbstractScript script, PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, Configuration config) {
        this.script = script;
        this.configuration = config;
        if (!(script instanceof ScriptRef)) {
            config.getScriptManager().addScript(script);
        }
        PatternParser parser = PatternLayout.createPatternParser(config);
        boolean needsLocation = false;
        for (PatternMatch property : properties) {
            try {
                List list = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
                PatternFormatter[] formatters = list.toArray(new PatternFormatter[0]);
                this.formatterMap.put(property.getKey(), formatters);
                this.patternMap.put(property.getKey(), property.getPattern());
                for (int i = 0; !needsLocation && i < formatters.length; ++i) {
                    needsLocation = formatters[i].requiresLocation();
                }
            }
            catch (RuntimeException ex) {
                throw new IllegalArgumentException("Cannot parse pattern '" + property.getPattern() + "'", ex);
            }
        }
        try {
            List list = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
            this.defaultFormatters = list.toArray(new PatternFormatter[0]);
            this.defaultPattern = defaultPattern;
            for (int i = 0; !needsLocation && i < this.defaultFormatters.length; ++i) {
                needsLocation = this.defaultFormatters[i].requiresLocation();
            }
        }
        catch (RuntimeException ex) {
            throw new IllegalArgumentException("Cannot parse pattern '" + defaultPattern + "'", ex);
        }
        this.requiresLocation = needsLocation;
    }

    @Override
    public boolean requiresLocation() {
        return this.requiresLocation;
    }

    @Override
    public PatternFormatter[] getFormatters(LogEvent event) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.putAll((Map<? extends String, ? extends Object>)this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        bindings.put("logEvent", (Object)event);
        Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        if (object == null) {
            return this.defaultFormatters;
        }
        PatternFormatter[] patternFormatter = this.formatterMap.get(object.toString());
        return patternFormatter == null ? this.defaultFormatters : patternFormatter;
    }

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    @Deprecated
    public static ScriptPatternSelector createSelector(AbstractScript script, PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, Configuration configuration) {
        Builder builder = ScriptPatternSelector.newBuilder();
        builder.setScript(script);
        builder.setProperties(properties);
        builder.setDefaultPattern(defaultPattern);
        builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
        builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
        builder.setConfiguration(configuration);
        return builder.build();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : this.patternMap.entrySet()) {
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

    public static class Builder
    implements org.apache.logging.log4j.core.util.Builder<ScriptPatternSelector> {
        @PluginElement(value="Script")
        private AbstractScript script;
        @PluginElement(value="PatternMatch")
        private PatternMatch[] properties;
        @PluginBuilderAttribute(value="defaultPattern")
        private String defaultPattern;
        @PluginBuilderAttribute(value="alwaysWriteExceptions")
        private boolean alwaysWriteExceptions = true;
        @PluginBuilderAttribute(value="disableAnsi")
        private boolean disableAnsi;
        @PluginBuilderAttribute(value="noConsoleNoAnsi")
        private boolean noConsoleNoAnsi;
        @PluginConfiguration
        private Configuration configuration;

        private Builder() {
        }

        @Override
        public ScriptPatternSelector build() {
            if (this.script == null) {
                LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
                return null;
            }
            if (this.script instanceof ScriptRef && this.configuration.getScriptManager().getScript(this.script.getName()) == null) {
                LOGGER.error("No script with name {} has been declared.", this.script.getName());
                return null;
            }
            if (this.defaultPattern == null) {
                this.defaultPattern = "%m%n";
            }
            if (this.properties == null || this.properties.length == 0) {
                LOGGER.warn("No marker patterns were provided");
                return null;
            }
            return new ScriptPatternSelector(this.script, this.properties, this.defaultPattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.configuration);
        }

        public Builder setScript(AbstractScript script) {
            this.script = script;
            return this;
        }

        public Builder setProperties(PatternMatch[] properties) {
            this.properties = properties;
            return this;
        }

        public Builder setDefaultPattern(String defaultPattern) {
            this.defaultPattern = defaultPattern;
            return this;
        }

        public Builder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
            this.alwaysWriteExceptions = alwaysWriteExceptions;
            return this;
        }

        public Builder setDisableAnsi(boolean disableAnsi) {
            this.disableAnsi = disableAnsi;
            return this;
        }

        public Builder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
            this.noConsoleNoAnsi = noConsoleNoAnsi;
            return this;
        }

        public Builder setConfiguration(Configuration config) {
            this.configuration = config;
            return this;
        }
    }
}

