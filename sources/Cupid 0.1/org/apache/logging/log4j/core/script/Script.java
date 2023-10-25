package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;

@Plugin(name = "Script", category = "Core", printObject = true)
public class Script extends AbstractScript {
  private static final String ATTR_LANGUAGE = "language";
  
  private static final String ATTR_SCRIPT_TEXT = "scriptText";
  
  static final String PLUGIN_NAME = "Script";
  
  public Script(String name, String language, String scriptText) {
    super(name, language, scriptText);
  }
  
  @PluginFactory
  public static Script createScript(@PluginAttribute("name") String name, @PluginAttribute("language") String language, @PluginValue("scriptText") String scriptText) {
    if (language == null) {
      LOGGER.error("No '{}' attribute provided for {} plugin '{}'", "language", "Script", name);
      language = "JavaScript";
    } 
    if (scriptText == null) {
      LOGGER.error("No '{}' attribute provided for {} plugin '{}'", "scriptText", "Script", name);
      return null;
    } 
    return new Script(name, language, scriptText);
  }
  
  public String toString() {
    return (getName() != null) ? getName() : super.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\script\Script.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */