package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;

@Plugin(name = "Http", category = "Core", elementType = "appender", printObject = true)
public final class HttpAppender extends AbstractAppender {
  private final HttpManager manager;
  
  public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B> implements org.apache.logging.log4j.core.util.Builder<HttpAppender> {
    @PluginBuilderAttribute
    @Required(message = "No URL provided for HttpAppender")
    private URL url;
    
    @PluginBuilderAttribute
    private String method = "POST";
    
    @PluginBuilderAttribute
    private int connectTimeoutMillis = 0;
    
    @PluginBuilderAttribute
    private int readTimeoutMillis = 0;
    
    @PluginElement("Headers")
    private Property[] headers;
    
    @PluginElement("SslConfiguration")
    private SslConfiguration sslConfiguration;
    
    @PluginBuilderAttribute
    private boolean verifyHostname = true;
    
    public HttpAppender build() {
      HttpManager httpManager = new HttpURLConnectionManager(getConfiguration(), getConfiguration().getLoggerContext(), getName(), this.url, this.method, this.connectTimeoutMillis, this.readTimeoutMillis, this.headers, this.sslConfiguration, this.verifyHostname);
      return new HttpAppender(getName(), getLayout(), getFilter(), isIgnoreExceptions(), httpManager, 
          getPropertyArray());
    }
    
    public URL getUrl() {
      return this.url;
    }
    
    public String getMethod() {
      return this.method;
    }
    
    public int getConnectTimeoutMillis() {
      return this.connectTimeoutMillis;
    }
    
    public int getReadTimeoutMillis() {
      return this.readTimeoutMillis;
    }
    
    public Property[] getHeaders() {
      return this.headers;
    }
    
    public SslConfiguration getSslConfiguration() {
      return this.sslConfiguration;
    }
    
    public boolean isVerifyHostname() {
      return this.verifyHostname;
    }
    
    public B setUrl(URL url) {
      this.url = url;
      return (B)asBuilder();
    }
    
    public B setMethod(String method) {
      this.method = method;
      return (B)asBuilder();
    }
    
    public B setConnectTimeoutMillis(int connectTimeoutMillis) {
      this.connectTimeoutMillis = connectTimeoutMillis;
      return (B)asBuilder();
    }
    
    public B setReadTimeoutMillis(int readTimeoutMillis) {
      this.readTimeoutMillis = readTimeoutMillis;
      return (B)asBuilder();
    }
    
    public B setHeaders(Property[] headers) {
      this.headers = headers;
      return (B)asBuilder();
    }
    
    public B setSslConfiguration(SslConfiguration sslConfiguration) {
      this.sslConfiguration = sslConfiguration;
      return (B)asBuilder();
    }
    
    public B setVerifyHostname(boolean verifyHostname) {
      this.verifyHostname = verifyHostname;
      return (B)asBuilder();
    }
  }
  
  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return (B)(new Builder<>()).asBuilder();
  }
  
  private HttpAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, HttpManager manager, Property[] properties) {
    super(name, filter, layout, ignoreExceptions, properties);
    Objects.requireNonNull(layout, "layout");
    this.manager = Objects.<HttpManager>requireNonNull(manager, "manager");
  }
  
  public void start() {
    super.start();
    this.manager.startup();
  }
  
  public void append(LogEvent event) {
    try {
      this.manager.send(getLayout(), event);
    } catch (Exception e) {
      error("Unable to send HTTP in appender [" + getName() + "]", event, e);
    } 
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    boolean stopped = stop(timeout, timeUnit, false);
    stopped &= this.manager.stop(timeout, timeUnit);
    setStopped();
    return stopped;
  }
  
  public String toString() {
    return "HttpAppender{name=" + 
      getName() + ", state=" + 
      getState() + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\HttpAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */