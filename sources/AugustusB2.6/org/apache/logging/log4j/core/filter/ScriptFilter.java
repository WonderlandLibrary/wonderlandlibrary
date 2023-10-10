// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;
import javax.script.Bindings;
import java.util.Map;
import org.apache.logging.log4j.message.SimpleMessage;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.script.ScriptRef;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ScriptFilter", category = "Core", elementType = "filter", printObject = true)
public final class ScriptFilter extends AbstractFilter
{
    private static Logger logger;
    private final AbstractScript script;
    private final Configuration configuration;
    
    private ScriptFilter(final AbstractScript script, final Configuration configuration, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.script = script;
        this.configuration = configuration;
        if (!(script instanceof ScriptRef)) {
            configuration.getScriptManager().addScript(script);
        }
    }
    
    @Override
    public Filter.Result filter(final org.apache.logging.log4j.core.Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        final SimpleBindings bindings = new SimpleBindings();
        bindings.put("logger", (Object)logger);
        bindings.put("level", (Object)level);
        bindings.put("marker", (Object)marker);
        bindings.put("message", (Object)new SimpleMessage(msg));
        bindings.put("parameters", (Object)params);
        bindings.put("throwable", (Object)null);
        bindings.putAll(this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        final Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
    }
    
    @Override
    public Filter.Result filter(final org.apache.logging.log4j.core.Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        final SimpleBindings bindings = new SimpleBindings();
        bindings.put("logger", (Object)logger);
        bindings.put("level", (Object)level);
        bindings.put("marker", (Object)marker);
        bindings.put("message", (Object)((msg instanceof String) ? new SimpleMessage((String)msg) : new ObjectMessage(msg)));
        bindings.put("parameters", (Object)null);
        bindings.put("throwable", (Object)t);
        bindings.putAll(this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        final Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
    }
    
    @Override
    public Filter.Result filter(final org.apache.logging.log4j.core.Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        final SimpleBindings bindings = new SimpleBindings();
        bindings.put("logger", (Object)logger);
        bindings.put("level", (Object)level);
        bindings.put("marker", (Object)marker);
        bindings.put("message", (Object)msg);
        bindings.put("parameters", (Object)null);
        bindings.put("throwable", (Object)t);
        bindings.putAll(this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        final Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final SimpleBindings bindings = new SimpleBindings();
        bindings.put("logEvent", (Object)event);
        bindings.putAll(this.configuration.getProperties());
        bindings.put("substitutor", (Object)this.configuration.getStrSubstitutor());
        final Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
        return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
    }
    
    @Override
    public String toString() {
        return this.script.getName();
    }
    
    @PluginFactory
    public static ScriptFilter createFilter(@PluginElement("Script") final AbstractScript script, @PluginAttribute("onMatch") final Filter.Result match, @PluginAttribute("onMismatch") final Filter.Result mismatch, @PluginConfiguration final Configuration configuration) {
        if (script == null) {
            ScriptFilter.LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
            return null;
        }
        if (script instanceof ScriptRef && configuration.getScriptManager().getScript(script.getName()) == null) {
            ScriptFilter.logger.error("No script with name {} has been declared.", script.getName());
            return null;
        }
        return new ScriptFilter(script, configuration, match, mismatch);
    }
    
    static {
        ScriptFilter.logger = StatusLogger.getLogger();
    }
}
