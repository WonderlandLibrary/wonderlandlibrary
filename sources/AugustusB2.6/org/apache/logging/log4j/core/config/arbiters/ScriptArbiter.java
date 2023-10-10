// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.arbiters;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import javax.script.Bindings;
import java.util.Map;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.core.script.ScriptRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ScriptArbiter", category = "Core", elementType = "Arbiter", deferChildren = true, printObject = true)
public class ScriptArbiter implements Arbiter
{
    private final AbstractScript script;
    private final Configuration configuration;
    
    private ScriptArbiter(final Configuration configuration, final AbstractScript script) {
        this.configuration = configuration;
        this.script = script;
        if (!(script instanceof ScriptRef)) {
            configuration.getScriptManager().addScript(script);
        }
    }
    
    @Override
    public boolean isCondition() {
        final SimpleBindings bindings = new SimpleBindings();
        bindings.putAll(this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        final Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return Boolean.parseBoolean(object.toString());
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<ScriptArbiter>
    {
        private static final Logger LOGGER;
        @PluginConfiguration
        private AbstractConfiguration configuration;
        @PluginNode
        private Node node;
        
        public Builder setConfiguration(final AbstractConfiguration configuration) {
            this.configuration = configuration;
            return this.asBuilder();
        }
        
        public Builder setNode(final Node node) {
            this.node = node;
            return this.asBuilder();
        }
        
        public Builder asBuilder() {
            return this;
        }
        
        @Override
        public ScriptArbiter build() {
            AbstractScript script = null;
            for (final Node child : this.node.getChildren()) {
                final PluginType<?> type = child.getType();
                if (type == null) {
                    Builder.LOGGER.error("Node {} is missing a Plugintype", child.getName());
                }
                else {
                    if (AbstractScript.class.isAssignableFrom(type.getPluginClass())) {
                        script = (AbstractScript)this.configuration.createPluginObject(type, child);
                        this.node.getChildren().remove(child);
                        break;
                    }
                    continue;
                }
            }
            if (script == null) {
                Builder.LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
                return null;
            }
            if (script instanceof ScriptRef && this.configuration.getScriptManager().getScript(script.getName()) == null) {
                Builder.LOGGER.error("No script with name {} has been declared.", script.getName());
                return null;
            }
            return new ScriptArbiter(this.configuration, script, null);
        }
        
        static {
            LOGGER = StatusLogger.getLogger();
        }
    }
}
