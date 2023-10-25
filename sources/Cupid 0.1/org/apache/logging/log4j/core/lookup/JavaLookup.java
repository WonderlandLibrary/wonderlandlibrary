package org.apache.logging.log4j.core.lookup;

import java.util.Locale;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.util.Strings;

@Plugin(name = "java", category = "Lookup")
public class JavaLookup extends AbstractLookup {
  private final SystemPropertiesLookup spLookup = new SystemPropertiesLookup();
  
  public String getHardware() {
    return "processors: " + Runtime.getRuntime().availableProcessors() + ", architecture: " + 
      getSystemProperty("os.arch") + getSystemProperty("-", "sun.arch.data.model") + 
      getSystemProperty(", instruction sets: ", "sun.cpu.isalist");
  }
  
  public String getLocale() {
    return "default locale: " + Locale.getDefault() + ", platform encoding: " + getSystemProperty("file.encoding");
  }
  
  public String getOperatingSystem() {
    return getSystemProperty("os.name") + " " + getSystemProperty("os.version") + 
      getSystemProperty(" ", "sun.os.patch.level") + ", architecture: " + getSystemProperty("os.arch") + 
      getSystemProperty("-", "sun.arch.data.model");
  }
  
  public String getRuntime() {
    return getSystemProperty("java.runtime.name") + " (build " + getSystemProperty("java.runtime.version") + ") from " + 
      getSystemProperty("java.vendor");
  }
  
  private String getSystemProperty(String name) {
    return this.spLookup.lookup(name);
  }
  
  private String getSystemProperty(String prefix, String name) {
    String value = getSystemProperty(name);
    if (Strings.isEmpty(value))
      return ""; 
    return prefix + value;
  }
  
  public String getVirtualMachine() {
    return getSystemProperty("java.vm.name") + " (build " + getSystemProperty("java.vm.version") + ", " + 
      getSystemProperty("java.vm.info") + ")";
  }
  
  public String lookup(LogEvent event, String key) {
    switch (key) {
      case "version":
        return "Java version " + getSystemProperty("java.version");
      case "runtime":
        return getRuntime();
      case "vm":
        return getVirtualMachine();
      case "os":
        return getOperatingSystem();
      case "hw":
        return getHardware();
      case "locale":
        return getLocale();
    } 
    throw new IllegalArgumentException(key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\JavaLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */