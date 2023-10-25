package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;

class DefaultLoggerComponentBuilder extends DefaultComponentAndConfigurationBuilder<LoggerComponentBuilder> implements LoggerComponentBuilder {
  public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level) {
    super(builder, name, "Logger");
    if (level != null)
      addAttribute("level", level); 
  }
  
  public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, boolean includeLocation) {
    super(builder, name, "Logger");
    if (level != null)
      addAttribute("level", level); 
    addAttribute("includeLocation", includeLocation);
  }
  
  public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, String type) {
    super(builder, name, type);
    if (level != null)
      addAttribute("level", level); 
  }
  
  public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, String type, boolean includeLocation) {
    super(builder, name, type);
    if (level != null)
      addAttribute("level", level); 
    addAttribute("includeLocation", includeLocation);
  }
  
  public LoggerComponentBuilder add(AppenderRefComponentBuilder builder) {
    return addComponent((ComponentBuilder<?>)builder);
  }
  
  public LoggerComponentBuilder add(FilterComponentBuilder builder) {
    return addComponent((ComponentBuilder<?>)builder);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultLoggerComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */