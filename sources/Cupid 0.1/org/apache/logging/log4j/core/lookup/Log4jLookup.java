package org.apache.logging.log4j.core.lookup;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "log4j", category = "Lookup")
public class Log4jLookup extends AbstractConfigurationAwareLookup {
  public static final String KEY_CONFIG_LOCATION = "configLocation";
  
  public static final String KEY_CONFIG_PARENT_LOCATION = "configParentLocation";
  
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static String asPath(URI uri) {
    if (uri.getScheme() == null || uri.getScheme().equals("file"))
      return uri.getPath(); 
    return uri.toString();
  }
  
  private static URI getParent(URI uri) throws URISyntaxException {
    String s = uri.toString();
    int offset = s.lastIndexOf('/');
    if (offset > -1)
      return new URI(s.substring(0, offset)); 
    return new URI("../");
  }
  
  public String lookup(LogEvent event, String key) {
    if (this.configuration != null) {
      ConfigurationSource configSrc = this.configuration.getConfigurationSource();
      File file = configSrc.getFile();
      if (file != null) {
        switch (key) {
          case "configLocation":
            return file.getAbsolutePath();
          case "configParentLocation":
            return file.getParentFile().getAbsolutePath();
        } 
        return null;
      } 
      URL url = configSrc.getURL();
      if (url != null)
        try {
          switch (key) {
            case "configLocation":
              return asPath(url.toURI());
            case "configParentLocation":
              return asPath(getParent(url.toURI()));
          } 
          return null;
        } catch (URISyntaxException use) {
          LOGGER.error(use);
        }  
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\Log4jLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */