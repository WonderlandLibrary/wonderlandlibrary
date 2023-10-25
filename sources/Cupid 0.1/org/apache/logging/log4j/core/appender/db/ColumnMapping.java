package org.apache.logging.log4j.core.appender.db;

import java.util.Date;
import java.util.Locale;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

@Plugin(name = "ColumnMapping", category = "Core", printObject = true)
public class ColumnMapping {
  public static final ColumnMapping[] EMPTY_ARRAY = new ColumnMapping[0];
  
  public static class Builder implements org.apache.logging.log4j.core.util.Builder<ColumnMapping> {
    @PluginConfiguration
    private Configuration configuration;
    
    @PluginElement("Layout")
    private StringLayout layout;
    
    @PluginBuilderAttribute
    private String literal;
    
    @PluginBuilderAttribute
    @Required(message = "No column name provided")
    private String name;
    
    @PluginBuilderAttribute
    private String parameter;
    
    @PluginBuilderAttribute
    private String pattern;
    
    @PluginBuilderAttribute
    private String source;
    
    @PluginBuilderAttribute
    @Required(message = "No conversion type provided")
    private Class<?> type = String.class;
    
    public ColumnMapping build() {
      if (this.pattern != null)
        this
          
          .layout = (StringLayout)PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).withAlwaysWriteExceptions(false).build(); 
      if (this.layout != null && this.literal != null && 
        
        !Date.class.isAssignableFrom(this.type) && 
        !ReadOnlyStringMap.class.isAssignableFrom(this.type) && 
        !ThreadContextMap.class.isAssignableFrom(this.type) && 
        !ThreadContextStack.class.isAssignableFrom(this.type)) {
        ColumnMapping.LOGGER.error("No 'layout' or 'literal' value specified and type ({}) is not compatible with ThreadContextMap, ThreadContextStack, or java.util.Date for the mapping", this.type, this);
        return null;
      } 
      if (this.literal != null && this.parameter != null) {
        ColumnMapping.LOGGER.error("Only one of 'literal' or 'parameter' can be set on the column mapping {}", this);
        return null;
      } 
      return new ColumnMapping(this.name, this.source, this.layout, this.literal, this.parameter, this.type);
    }
    
    public Builder setConfiguration(Configuration configuration) {
      this.configuration = configuration;
      return this;
    }
    
    public Builder setLayout(StringLayout layout) {
      this.layout = layout;
      return this;
    }
    
    public Builder setLiteral(String literal) {
      this.literal = literal;
      return this;
    }
    
    public Builder setName(String name) {
      this.name = name;
      return this;
    }
    
    public Builder setParameter(String parameter) {
      this.parameter = parameter;
      return this;
    }
    
    public Builder setPattern(String pattern) {
      this.pattern = pattern;
      return this;
    }
    
    public Builder setSource(String source) {
      this.source = source;
      return this;
    }
    
    public Builder setType(Class<?> type) {
      this.type = type;
      return this;
    }
    
    public String toString() {
      return "Builder [name=" + this.name + ", source=" + this.source + ", literal=" + this.literal + ", parameter=" + this.parameter + ", pattern=" + this.pattern + ", type=" + this.type + ", layout=" + this.layout + "]";
    }
  }
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final StringLayout layout;
  
  private final String literalValue;
  
  private final String name;
  
  private final String nameKey;
  
  private final String parameter;
  
  private final String source;
  
  private final Class<?> type;
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public static String toKey(String name) {
    return name.toUpperCase(Locale.ROOT);
  }
  
  private ColumnMapping(String name, String source, StringLayout layout, String literalValue, String parameter, Class<?> type) {
    this.name = name;
    this.nameKey = toKey(name);
    this.source = source;
    this.layout = layout;
    this.literalValue = literalValue;
    this.parameter = parameter;
    this.type = type;
  }
  
  public StringLayout getLayout() {
    return this.layout;
  }
  
  public String getLiteralValue() {
    return this.literalValue;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getNameKey() {
    return this.nameKey;
  }
  
  public String getParameter() {
    return this.parameter;
  }
  
  public String getSource() {
    return this.source;
  }
  
  public Class<?> getType() {
    return this.type;
  }
  
  public String toString() {
    return "ColumnMapping [name=" + this.name + ", source=" + this.source + ", literalValue=" + this.literalValue + ", parameter=" + this.parameter + ", type=" + this.type + ", layout=" + this.layout + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\db\ColumnMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */