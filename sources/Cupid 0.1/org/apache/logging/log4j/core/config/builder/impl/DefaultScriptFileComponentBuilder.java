package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;

class DefaultScriptFileComponentBuilder extends DefaultComponentAndConfigurationBuilder<ScriptFileComponentBuilder> implements ScriptFileComponentBuilder {
  public DefaultScriptFileComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String path) {
    super(builder, (name != null) ? name : path, "ScriptFile");
    addAttribute("path", path);
  }
  
  public DefaultScriptFileComponentBuilder addLanguage(String language) {
    addAttribute("language", language);
    return this;
  }
  
  public DefaultScriptFileComponentBuilder addIsWatched(boolean isWatched) {
    addAttribute("isWatched", Boolean.toString(isWatched));
    return this;
  }
  
  public DefaultScriptFileComponentBuilder addIsWatched(String isWatched) {
    addAttribute("isWatched", isWatched);
    return this;
  }
  
  public DefaultScriptFileComponentBuilder addCharset(String charset) {
    addAttribute("charset", charset);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultScriptFileComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */