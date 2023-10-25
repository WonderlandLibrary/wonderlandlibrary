package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;

class DefaultScriptComponentBuilder extends DefaultComponentAndConfigurationBuilder<ScriptComponentBuilder> implements ScriptComponentBuilder {
  public DefaultScriptComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String language, String text) {
    super(builder, name, "Script");
    if (language != null)
      addAttribute("language", language); 
    if (text != null)
      addAttribute("text", text); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultScriptComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */