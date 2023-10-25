/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.PathWithAttributes;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.script.ScriptRef;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="ScriptCondition", category="Core", printObject=true)
public class ScriptCondition {
    private static Logger LOGGER = StatusLogger.getLogger();
    private final AbstractScript script;
    private final Configuration configuration;

    public ScriptCondition(AbstractScript script, Configuration configuration) {
        this.script = Objects.requireNonNull(script, "script");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
        if (!(script instanceof ScriptRef)) {
            configuration.getScriptManager().addScript(script);
        }
    }

    public List<PathWithAttributes> selectFilesToDelete(Path basePath, List<PathWithAttributes> candidates) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("basePath", (Object)basePath);
        bindings.put("pathList", (Object)candidates);
        bindings.putAll((Map<? extends String, ? extends Object>)this.configuration.getProperties());
        bindings.put("configuration", (Object)this.configuration);
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        bindings.put("statusLogger", (Object)LOGGER);
        Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return (List)object;
    }

    @PluginFactory
    public static ScriptCondition createCondition(@PluginElement(value="Script") AbstractScript script, @PluginConfiguration Configuration configuration) {
        if (script == null) {
            LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptCondition");
            return null;
        }
        if (script instanceof ScriptRef && configuration.getScriptManager().getScript(script.getName()) == null) {
            LOGGER.error("ScriptCondition: No script with name {} has been declared.", script.getName());
            return null;
        }
        return new ScriptCondition(script, configuration);
    }
}

