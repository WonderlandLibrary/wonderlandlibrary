package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;

class DefaultFilterComponentBuilder extends DefaultComponentAndConfigurationBuilder<FilterComponentBuilder> implements FilterComponentBuilder {
  public DefaultFilterComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String type, String onMatch, String onMismatch) {
    super(builder, type);
    addAttribute("onMatch", onMatch);
    addAttribute("onMismatch", onMismatch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultFilterComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */