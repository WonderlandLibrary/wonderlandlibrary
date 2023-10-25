package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import java.util.Properties;

public final class RuntimeStrSubstitutor extends StrSubstitutor {
  public RuntimeStrSubstitutor() {}
  
  public RuntimeStrSubstitutor(Map<String, String> valueMap) {
    super(valueMap);
  }
  
  public RuntimeStrSubstitutor(Properties properties) {
    super(properties);
  }
  
  public RuntimeStrSubstitutor(StrLookup lookup) {
    super(lookup);
  }
  
  public RuntimeStrSubstitutor(StrSubstitutor other) {
    super(other);
  }
  
  boolean isRecursiveEvaluationAllowed() {
    return false;
  }
  
  void setRecursiveEvaluationAllowed(boolean recursiveEvaluationAllowed) {
    throw new UnsupportedOperationException("recursiveEvaluationAllowed cannot be modified within RuntimeStrSubstitutor");
  }
  
  public String toString() {
    return "RuntimeStrSubstitutor{" + super.toString() + "}";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\RuntimeStrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */