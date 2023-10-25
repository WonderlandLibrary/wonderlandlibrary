/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.util.Map;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.core.script.ScriptRef;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="ScriptFilter", category="Core", elementType="filter", printObject=true)
public final class ScriptFilter
extends AbstractFilter {
    private static Logger logger = StatusLogger.getLogger();
    private final AbstractScript script;
    private final Configuration configuration;

    private ScriptFilter(AbstractScript script, Configuration configuration, Filter.Result onMatch, Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.script = script;
        this.configuration = configuration;
        if (!(script instanceof ScriptRef)) {
            configuration.getScriptManager().addScript(script);
        }
    }

    @Override
    public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, String msg, Object ... params) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("logger", (Object)logger);
        bindings.put("level", (Object)level);
        bindings.put("marker", (Object)marker);
        bindings.put("message", (Object)new SimpleMessage(msg));
        bindings.put("parameters", (Object)params);
        bindings.put("throwable", (Object)null);
        bindings.putAll((Map<? extends String, ? extends Object>)this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return object == null || !Boolean.TRUE.equals(object) ? this.onMismatch : this.onMatch;
    }

    @Override
    public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("logger", (Object)logger);
        bindings.put("level", (Object)level);
        bindings.put("marker", (Object)marker);
        bindings.put("message", (Object)(msg instanceof String ? new SimpleMessage((String)msg) : new ObjectMessage(msg)));
        bindings.put("parameters", (Object)null);
        bindings.put("throwable", (Object)t);
        bindings.putAll((Map<? extends String, ? extends Object>)this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return object == null || !Boolean.TRUE.equals(object) ? this.onMismatch : this.onMatch;
    }

    @Override
    public Filter.Result filter(org.apache.logging.log4j.core.Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("logger", (Object)logger);
        bindings.put("level", (Object)level);
        bindings.put("marker", (Object)marker);
        bindings.put("message", (Object)msg);
        bindings.put("parameters", (Object)null);
        bindings.put("throwable", (Object)t);
        bindings.putAll((Map<? extends String, ? extends Object>)this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return object == null || !Boolean.TRUE.equals(object) ? this.onMismatch : this.onMatch;
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("logEvent", (Object)event);
        bindings.putAll((Map<? extends String, ? extends Object>)this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return object == null || !Boolean.TRUE.equals(object) ? this.onMismatch : this.onMatch;
    }

    @Override
    public String toString() {
        return this.script.getName();
    }

    @PluginFactory
    public static ScriptFilter createFilter(@PluginElement(value="Script") AbstractScript script, @PluginAttribute(value="onMatch") Filter.Result match, @PluginAttribute(value="onMismatch") Filter.Result mismatch, @PluginConfiguration Configuration configuration) {
        if (script == null) {
            LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
            return null;
        }
        if (script instanceof ScriptRef && configuration.getScriptManager().getScript(script.getName()) == null) {
            logger.error("No script with name {} has been declared.", script.getName());
            return null;
        }
        return new ScriptFilter(script, configuration, match, mismatch);
    }
}

