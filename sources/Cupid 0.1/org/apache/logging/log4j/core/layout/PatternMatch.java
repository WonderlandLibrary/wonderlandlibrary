package org.apache.logging.log4j.core.layout;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;

@Plugin(name = "PatternMatch", category = "Core", printObject = true)
public final class PatternMatch {
  private final String key;
  
  private final String pattern;
  
  public PatternMatch(String key, String pattern) {
    this.key = key;
    this.pattern = pattern;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public String getPattern() {
    return this.pattern;
  }
  
  public String toString() {
    return this.key + '=' + this.pattern;
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public static class Builder implements org.apache.logging.log4j.core.util.Builder<PatternMatch>, Serializable {
    private static final long serialVersionUID = 1L;
    
    @PluginBuilderAttribute
    private String key;
    
    @PluginBuilderAttribute
    private String pattern;
    
    public Builder setKey(String key) {
      this.key = key;
      return this;
    }
    
    public Builder setPattern(String pattern) {
      this.pattern = pattern;
      return this;
    }
    
    public PatternMatch build() {
      return new PatternMatch(this.key, this.pattern);
    }
    
    protected Object readResolve() throws ObjectStreamException {
      return new PatternMatch(this.key, this.pattern);
    }
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { this.key, this.pattern });
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    PatternMatch other = (PatternMatch)obj;
    if (!Objects.equals(this.key, other.key))
      return false; 
    if (!Objects.equals(this.pattern, other.pattern))
      return false; 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\PatternMatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */