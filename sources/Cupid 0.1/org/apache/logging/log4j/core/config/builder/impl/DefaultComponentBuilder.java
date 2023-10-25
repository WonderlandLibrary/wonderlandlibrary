package org.apache.logging.log4j.core.config.builder.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;

class DefaultComponentBuilder<T extends ComponentBuilder<T>, CB extends ConfigurationBuilder<? extends Configuration>> implements ComponentBuilder<T> {
  private final CB builder;
  
  private final String type;
  
  private final Map<String, String> attributes = new LinkedHashMap<>();
  
  private final List<Component> components = new ArrayList<>();
  
  private final String name;
  
  private final String value;
  
  public DefaultComponentBuilder(CB builder, String type) {
    this(builder, null, type, null);
  }
  
  public DefaultComponentBuilder(CB builder, String name, String type) {
    this(builder, name, type, null);
  }
  
  public DefaultComponentBuilder(CB builder, String name, String type, String value) {
    this.type = type;
    this.builder = builder;
    this.name = name;
    this.value = value;
  }
  
  public T addAttribute(String key, boolean value) {
    return put(key, Boolean.toString(value));
  }
  
  public T addAttribute(String key, Enum<?> value) {
    return put(key, value.name());
  }
  
  public T addAttribute(String key, int value) {
    return put(key, Integer.toString(value));
  }
  
  public T addAttribute(String key, Level level) {
    return put(key, level.toString());
  }
  
  public T addAttribute(String key, Object value) {
    return put(key, value.toString());
  }
  
  public T addAttribute(String key, String value) {
    return put(key, value);
  }
  
  public T addComponent(ComponentBuilder<?> builder) {
    this.components.add(builder.build());
    return (T)this;
  }
  
  public Component build() {
    Component component = new Component(this.type, this.name, this.value);
    component.getAttributes().putAll(this.attributes);
    component.getComponents().addAll(this.components);
    return component;
  }
  
  public CB getBuilder() {
    return this.builder;
  }
  
  public String getName() {
    return this.name;
  }
  
  protected T put(String key, String value) {
    this.attributes.put(key, value);
    return (T)this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */