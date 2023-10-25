package org.apache.logging.log4j.core.lookup;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "jvmrunargs", category = "Lookup")
public class JmxRuntimeInputArgumentsLookup extends MapLookup {
  public static final JmxRuntimeInputArgumentsLookup JMX_SINGLETON;
  
  static {
    List<String> argsList = ManagementFactory.getRuntimeMXBean().getInputArguments();
    JMX_SINGLETON = new JmxRuntimeInputArgumentsLookup(MapLookup.toMap(argsList));
  }
  
  public JmxRuntimeInputArgumentsLookup() {}
  
  public JmxRuntimeInputArgumentsLookup(Map<String, String> map) {
    super(map);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\JmxRuntimeInputArgumentsLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */