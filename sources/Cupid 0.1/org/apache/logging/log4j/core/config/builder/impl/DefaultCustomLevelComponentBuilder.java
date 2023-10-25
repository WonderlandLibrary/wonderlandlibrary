package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;

class DefaultCustomLevelComponentBuilder extends DefaultComponentAndConfigurationBuilder<CustomLevelComponentBuilder> implements CustomLevelComponentBuilder {
  public DefaultCustomLevelComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, int level) {
    super(builder, name, "CustomLevel");
    addAttribute("intLevel", level);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultCustomLevelComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */