package org.apache.logging.log4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertyFilePropertySource extends PropertiesPropertySource {
  public PropertyFilePropertySource(String fileName) {
    super(loadPropertiesFile(fileName));
  }
  
  private static Properties loadPropertiesFile(String fileName) {
    Properties props = new Properties();
    for (URL url : LoaderUtil.findResources(fileName)) {
      try (InputStream in = url.openStream()) {
        props.load(in);
      } catch (IOException e) {
        LowLevelLogUtil.logException("Unable to read " + url, e);
      } 
    } 
    return props;
  }
  
  public int getPriority() {
    return 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\PropertyFilePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */