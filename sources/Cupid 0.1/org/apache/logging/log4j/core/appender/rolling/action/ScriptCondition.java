package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.script.AbstractScript;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "ScriptCondition", category = "Core", printObject = true)
public class ScriptCondition {
  private static Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final AbstractScript script;
  
  private final Configuration configuration;
  
  public ScriptCondition(AbstractScript script, Configuration configuration) {
    this.script = Objects.<AbstractScript>requireNonNull(script, "script");
    this.configuration = Objects.<Configuration>requireNonNull(configuration, "configuration");
    if (!(script instanceof org.apache.logging.log4j.core.script.ScriptRef))
      configuration.getScriptManager().addScript(script); 
  }
  
  public List<PathWithAttributes> selectFilesToDelete(Path basePath, List<PathWithAttributes> candidates) {
    SimpleBindings bindings = new SimpleBindings();
    bindings.put("basePath", basePath);
    bindings.put("pathList", candidates);
    bindings.putAll(this.configuration.getProperties());
    bindings.put("configuration", this.configuration);
    bindings.put("substitutor", this.configuration.getStrSubstitutor());
    bindings.put("statusLogger", LOGGER);
    Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
    return (List<PathWithAttributes>)object;
  }
  
  @PluginFactory
  public static ScriptCondition createCondition(@PluginElement("Script") AbstractScript script, @PluginConfiguration Configuration configuration) {
    if (script == null) {
      LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptCondition");
      return null;
    } 
    if (script instanceof org.apache.logging.log4j.core.script.ScriptRef && 
      configuration.getScriptManager().getScript(script.getName()) == null) {
      LOGGER.error("ScriptCondition: No script with name {} has been declared.", script.getName());
      return null;
    } 
    return new ScriptCondition(script, configuration);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\ScriptCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */