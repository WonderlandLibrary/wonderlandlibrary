package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "ScriptRef", category = "Core", printObject = true)
public class ScriptRef extends AbstractScript {
  private final ScriptManager scriptManager;
  
  public ScriptRef(String name, ScriptManager scriptManager) {
    super(name, null, null);
    this.scriptManager = scriptManager;
  }
  
  public String getLanguage() {
    AbstractScript script = this.scriptManager.getScript(getName());
    return (script != null) ? script.getLanguage() : null;
  }
  
  public String getScriptText() {
    AbstractScript script = this.scriptManager.getScript(getName());
    return (script != null) ? script.getScriptText() : null;
  }
  
  @PluginFactory
  public static ScriptRef createReference(@PluginAttribute("ref") String name, @PluginConfiguration Configuration configuration) {
    if (name == null) {
      LOGGER.error("No script name provided");
      return null;
    } 
    return new ScriptRef(name, configuration.getScriptManager());
  }
  
  public String toString() {
    return "ref=" + getName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\script\ScriptRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */