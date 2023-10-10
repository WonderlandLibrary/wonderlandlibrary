// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import javax.script.Bindings;
import org.apache.logging.log4j.core.script.ScriptManager;
import java.util.Objects;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ScriptAppenderSelector", category = "Core", elementType = "appender", printObject = true)
public class ScriptAppenderSelector extends AbstractAppender
{
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private ScriptAppenderSelector(final String name, final Filter filter, final Layout<? extends Serializable> layout, final Property[] properties) {
        super(name, filter, layout, true, Property.EMPTY_ARRAY);
    }
    
    @Override
    public void append(final LogEvent event) {
    }
    
    public static final class Builder implements org.apache.logging.log4j.core.util.Builder<Appender>
    {
        @PluginElement("AppenderSet")
        @Required
        private AppenderSet appenderSet;
        @PluginConfiguration
        @Required
        private Configuration configuration;
        @PluginBuilderAttribute
        @Required
        private String name;
        @PluginElement("Script")
        @Required
        private AbstractScript script;
        
        @Override
        public Appender build() {
            if (this.name == null) {
                ScriptAppenderSelector.LOGGER.error("Name missing.");
                return null;
            }
            if (this.script == null) {
                ScriptAppenderSelector.LOGGER.error("Script missing for ScriptAppenderSelector appender {}", this.name);
                return null;
            }
            if (this.appenderSet == null) {
                ScriptAppenderSelector.LOGGER.error("AppenderSet missing for ScriptAppenderSelector appender {}", this.name);
                return null;
            }
            if (this.configuration == null) {
                ScriptAppenderSelector.LOGGER.error("Configuration missing for ScriptAppenderSelector appender {}", this.name);
                return null;
            }
            final ScriptManager scriptManager = this.configuration.getScriptManager();
            scriptManager.addScript(this.script);
            final Bindings bindings = scriptManager.createBindings(this.script);
            ScriptAppenderSelector.LOGGER.debug("ScriptAppenderSelector '{}' executing {} '{}': {}", this.name, this.script.getLanguage(), this.script.getName(), this.script.getScriptText());
            final Object object = scriptManager.execute(this.script.getName(), bindings);
            final String actualAppenderName = Objects.toString(object, null);
            ScriptAppenderSelector.LOGGER.debug("ScriptAppenderSelector '{}' selected '{}'", this.name, actualAppenderName);
            return this.appenderSet.createAppender(actualAppenderName, this.name);
        }
        
        public AppenderSet getAppenderSet() {
            return this.appenderSet;
        }
        
        public Configuration getConfiguration() {
            return this.configuration;
        }
        
        public String getName() {
            return this.name;
        }
        
        public AbstractScript getScript() {
            return this.script;
        }
        
        public Builder withAppenderNodeSet(final AppenderSet appenderSet) {
            this.appenderSet = appenderSet;
            return this;
        }
        
        public Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Builder withName(final String name) {
            this.name = name;
            return this;
        }
        
        public Builder withScript(final AbstractScript script) {
            this.script = script;
            return this;
        }
    }
}
