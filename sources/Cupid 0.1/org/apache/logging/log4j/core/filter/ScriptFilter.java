package org.apache.logging.log4j.core.filter;

import javax.script.SimpleBindings;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "ScriptFilter", category = "Core", elementType = "filter", printObject = true)
public final class ScriptFilter extends AbstractFilter {
  private static Logger logger = (Logger)StatusLogger.getLogger();
  
  private final AbstractScript script;
  
  private final Configuration configuration;
  
  private ScriptFilter(AbstractScript script, Configuration configuration, Filter.Result onMatch, Filter.Result onMismatch) {
    super(onMatch, onMismatch);
    this.script = script;
    this.configuration = configuration;
    if (!(script instanceof org.apache.logging.log4j.core.script.ScriptRef))
      configuration.getScriptManager().addScript(script); 
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    SimpleBindings bindings = new SimpleBindings();
    bindings.put("logger", logger);
    bindings.put("level", level);
    bindings.put("marker", marker);
    bindings.put("message", new SimpleMessage(msg));
    bindings.put("parameters", params);
    bindings.put("throwable", (Object)null);
    bindings.putAll(this.configuration.getProperties());
    bindings.put("substitutor", this.configuration.getStrSubstitutor());
    Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
    return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    SimpleBindings bindings = new SimpleBindings();
    bindings.put("logger", logger);
    bindings.put("level", level);
    bindings.put("marker", marker);
    bindings.put("message", (msg instanceof String) ? new SimpleMessage((String)msg) : new ObjectMessage(msg));
    bindings.put("parameters", (Object)null);
    bindings.put("throwable", t);
    bindings.putAll(this.configuration.getProperties());
    bindings.put("substitutor", this.configuration.getStrSubstitutor());
    Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
    return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    SimpleBindings bindings = new SimpleBindings();
    bindings.put("logger", logger);
    bindings.put("level", level);
    bindings.put("marker", marker);
    bindings.put("message", msg);
    bindings.put("parameters", (Object)null);
    bindings.put("throwable", t);
    bindings.putAll(this.configuration.getProperties());
    bindings.put("substitutor", this.configuration.getStrSubstitutor());
    Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
    return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
  }
  
  public Filter.Result filter(LogEvent event) {
    SimpleBindings bindings = new SimpleBindings();
    bindings.put("logEvent", event);
    bindings.putAll(this.configuration.getProperties());
    bindings.put("substitutor", this.configuration.getStrSubstitutor());
    Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
    return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
  }
  
  public String toString() {
    return this.script.getName();
  }
  
  @PluginFactory
  public static ScriptFilter createFilter(@PluginElement("Script") AbstractScript script, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch, @PluginConfiguration Configuration configuration) {
    if (script == null) {
      LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
      return null;
    } 
    if (script instanceof org.apache.logging.log4j.core.script.ScriptRef && 
      configuration.getScriptManager().getScript(script.getName()) == null) {
      logger.error("No script with name {} has been declared.", script.getName());
      return null;
    } 
    return new ScriptFilter(script, configuration, match, mismatch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\ScriptFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */