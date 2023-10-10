// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import javax.script.Bindings;
import java.util.Map;
import javax.script.SimpleBindings;
import java.util.List;
import java.nio.file.Path;
import org.apache.logging.log4j.core.script.ScriptRef;
import java.util.Objects;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ScriptCondition", category = "Core", printObject = true)
public class ScriptCondition
{
    private static Logger LOGGER;
    private final AbstractScript script;
    private final Configuration configuration;
    
    public ScriptCondition(final AbstractScript script, final Configuration configuration) {
        this.script = Objects.requireNonNull(script, "script");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        if (!(script instanceof ScriptRef)) {
            configuration.getScriptManager().addScript(script);
        }
    }
    
    public List<PathWithAttributes> selectFilesToDelete(final Path basePath, final List<PathWithAttributes> candidates) {
        final SimpleBindings bindings = new SimpleBindings();
        bindings.put("basePath", (Object)basePath);
        bindings.put("pathList", (Object)candidates);
        bindings.putAll(this.configuration.getProperties());
        bindings.put("configuration", (Object)this.configuration);
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        bindings.put("statusLogger", (Object)ScriptCondition.LOGGER);
        final Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return (List<PathWithAttributes>)object;
    }
    
    @PluginFactory
    public static ScriptCondition createCondition(@PluginElement("Script") final AbstractScript script, @PluginConfiguration final Configuration configuration) {
        if (script == null) {
            ScriptCondition.LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptCondition");
            return null;
        }
        if (script instanceof ScriptRef && configuration.getScriptManager().getScript(script.getName()) == null) {
            ScriptCondition.LOGGER.error("ScriptCondition: No script with name {} has been declared.", script.getName());
            return null;
        }
        return new ScriptCondition(script, configuration);
    }
    
    static {
        ScriptCondition.LOGGER = StatusLogger.getLogger();
    }
}
