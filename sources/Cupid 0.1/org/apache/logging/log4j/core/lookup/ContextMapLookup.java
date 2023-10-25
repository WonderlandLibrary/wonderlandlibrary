package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

@Plugin(name = "ctx", category = "Lookup")
public class ContextMapLookup implements StrLookup {
  private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
  
  public String lookup(String key) {
    return (String)currentContextData().getValue(key);
  }
  
  private ReadOnlyStringMap currentContextData() {
    return this.injector.rawContextData();
  }
  
  public String lookup(LogEvent event, String key) {
    return (event == null) ? null : (String)event.getContextData().getValue(key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\ContextMapLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */