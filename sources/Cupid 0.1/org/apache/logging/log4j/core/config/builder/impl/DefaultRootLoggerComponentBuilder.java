package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;

class DefaultRootLoggerComponentBuilder extends DefaultComponentAndConfigurationBuilder<RootLoggerComponentBuilder> implements RootLoggerComponentBuilder {
  public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level) {
    super(builder, "", "Root");
    if (level != null)
      addAttribute("level", level); 
  }
  
  public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, boolean includeLocation) {
    super(builder, "", "Root");
    if (level != null)
      addAttribute("level", level); 
    addAttribute("includeLocation", includeLocation);
  }
  
  public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type) {
    super(builder, "", type);
    if (level != null)
      addAttribute("level", level); 
  }
  
  public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type, boolean includeLocation) {
    super(builder, "", type);
    if (level != null)
      addAttribute("level", level); 
    addAttribute("includeLocation", includeLocation);
  }
  
  public RootLoggerComponentBuilder add(AppenderRefComponentBuilder builder) {
    return addComponent((ComponentBuilder<?>)builder);
  }
  
  public RootLoggerComponentBuilder add(FilterComponentBuilder builder) {
    return addComponent((ComponentBuilder<?>)builder);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultRootLoggerComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */