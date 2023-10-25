package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import java.util.Properties;

public final class ConfigurationStrSubstitutor extends StrSubstitutor {
  public ConfigurationStrSubstitutor() {}
  
  public ConfigurationStrSubstitutor(Map<String, String> valueMap) {
    super(valueMap);
  }
  
  public ConfigurationStrSubstitutor(Properties properties) {
    super(properties);
  }
  
  public ConfigurationStrSubstitutor(StrLookup lookup) {
    super(lookup);
  }
  
  public ConfigurationStrSubstitutor(StrSubstitutor other) {
    super(other);
  }
  
  boolean isRecursiveEvaluationAllowed() {
    return true;
  }
  
  void setRecursiveEvaluationAllowed(boolean recursiveEvaluationAllowed) {
    throw new UnsupportedOperationException("recursiveEvaluationAllowed cannot be modified within ConfigurationStrSubstitutor");
  }
  
  public String toString() {
    return "ConfigurationStrSubstitutor{" + super.toString() + "}";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\ConfigurationStrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */