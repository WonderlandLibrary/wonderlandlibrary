package org.apache.logging.log4j.core.config;

import java.util.Objects;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "property", category = "Core", printObject = true)
public final class Property {
  public static final Property[] EMPTY_ARRAY = new Property[0];
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final String name;
  
  private final String value;
  
  private final boolean valueNeedsLookup;
  
  private Property(String name, String value) {
    this.name = name;
    this.value = value;
    this.valueNeedsLookup = (value != null && value.contains("${"));
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return Objects.toString(this.value, "");
  }
  
  public boolean isValueNeedsLookup() {
    return this.valueNeedsLookup;
  }
  
  @PluginFactory
  public static Property createProperty(@PluginAttribute("name") String name, @PluginValue("value") String value) {
    if (name == null)
      LOGGER.error("Property name cannot be null"); 
    return new Property(name, value);
  }
  
  public String toString() {
    return this.name + '=' + getValue();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */