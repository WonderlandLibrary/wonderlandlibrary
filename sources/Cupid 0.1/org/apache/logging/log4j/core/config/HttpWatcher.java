package org.apache.logging.log4j.core.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import org.apache.logging.log4j.core.util.AbstractWatcher;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "http", category = "Watcher", elementType = "watcher", printObject = true)
@PluginAliases({"https"})
public class HttpWatcher extends AbstractWatcher {
  private Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private SslConfiguration sslConfiguration;
  
  private URL url;
  
  private volatile long lastModifiedMillis;
  
  private static final int NOT_MODIFIED = 304;
  
  private static final int OK = 200;
  
  private static final int BUF_SIZE = 1024;
  
  private static final String HTTP = "http";
  
  private static final String HTTPS = "https";
  
  public HttpWatcher(Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
    super(configuration, reconfigurable, configurationListeners);
    this.sslConfiguration = SslConfigurationFactory.getSslConfiguration();
    this.lastModifiedMillis = lastModifiedMillis;
  }
  
  public long getLastModified() {
    return this.lastModifiedMillis;
  }
  
  public boolean isModified() {
    return refreshConfiguration();
  }
  
  public void watching(Source source) {
    if (!source.getURI().getScheme().equals("http") && !source.getURI().getScheme().equals("https"))
      throw new IllegalArgumentException("HttpWatcher requires a url using the HTTP or HTTPS protocol, not " + source
          .getURI().getScheme()); 
    try {
      this.url = source.getURI().toURL();
    } catch (MalformedURLException ex) {
      throw new IllegalArgumentException("Invalid URL for HttpWatcher " + source.getURI(), ex);
    } 
    super.watching(source);
  }
  
  public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
    HttpWatcher watcher = new HttpWatcher(getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
    if (getSource() != null)
      watcher.watching(getSource()); 
    return (Watcher)watcher;
  }
  
  private boolean refreshConfiguration() {
    try {
      HttpURLConnection urlConnection = UrlConnectionFactory.createConnection(this.url, this.lastModifiedMillis, this.sslConfiguration);
      urlConnection.connect();
      try {
        int code = urlConnection.getResponseCode();
        switch (code) {
          case 304:
            this.LOGGER.debug("Configuration Not Modified");
            return false;
          case 200:
            try (InputStream is = urlConnection.getInputStream()) {
              ConfigurationSource configSource = getConfiguration().getConfigurationSource();
              configSource.setData(readStream(is));
              this.lastModifiedMillis = urlConnection.getLastModified();
              configSource.setModifiedMillis(this.lastModifiedMillis);
              this.LOGGER.debug("Content was modified for {}", this.url.toString());
              return true;
            } catch (IOException e) {
              try (InputStream es = urlConnection.getErrorStream()) {
                this.LOGGER.info("Error accessing configuration at {}: {}", this.url, readStream(es));
              } catch (IOException ioe) {
                this.LOGGER.error("Error accessing configuration at {}: {}", this.url, e.getMessage());
              } 
              return false;
            } 
        } 
        if (code < 0) {
          this.LOGGER.info("Invalid response code returned");
        } else {
          this.LOGGER.info("Unexpected response code returned {}", Integer.valueOf(code));
        } 
        return false;
      } catch (IOException ioe) {
        this.LOGGER.error("Error accessing configuration at {}: {}", this.url, ioe.getMessage());
      } 
    } catch (IOException ioe) {
      this.LOGGER.error("Error connecting to configuration at {}: {}", this.url, ioe.getMessage());
    } 
    return false;
  }
  
  private byte[] readStream(InputStream is) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = is.read(buffer)) != -1)
      result.write(buffer, 0, length); 
    return result.toByteArray();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\HttpWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */