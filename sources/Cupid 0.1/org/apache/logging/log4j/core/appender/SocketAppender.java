package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.DatagramSocketManager;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.net.SocketOptions;
import org.apache.logging.log4j.core.net.SslSocketManager;
import org.apache.logging.log4j.core.net.TcpSocketManager;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.util.Booleans;

@Plugin(name = "Socket", category = "Core", elementType = "appender", printObject = true)
public class SocketAppender extends AbstractOutputStreamAppender<AbstractSocketManager> {
  private final Object advertisement;
  
  private final Advertiser advertiser;
  
  public static abstract class AbstractBuilder<B extends AbstractBuilder<B>> extends AbstractOutputStreamAppender.Builder<B> {
    @PluginBuilderAttribute
    private boolean advertise;
    
    @PluginBuilderAttribute
    private int connectTimeoutMillis;
    
    @PluginBuilderAttribute
    @ValidHost
    private String host = "localhost";
    
    @PluginBuilderAttribute
    private boolean immediateFail = true;
    
    @PluginBuilderAttribute
    @ValidPort
    private int port;
    
    @PluginBuilderAttribute
    private Protocol protocol = Protocol.TCP;
    
    @PluginBuilderAttribute
    @PluginAliases({"reconnectDelay", "reconnectionDelay", "delayMillis", "reconnectionDelayMillis"})
    private int reconnectDelayMillis;
    
    @PluginElement("SocketOptions")
    private SocketOptions socketOptions;
    
    @PluginElement("SslConfiguration")
    @PluginAliases({"SslConfig"})
    private SslConfiguration sslConfiguration;
    
    public boolean getAdvertise() {
      return this.advertise;
    }
    
    public int getConnectTimeoutMillis() {
      return this.connectTimeoutMillis;
    }
    
    public String getHost() {
      return this.host;
    }
    
    public int getPort() {
      return this.port;
    }
    
    public Protocol getProtocol() {
      return this.protocol;
    }
    
    public SslConfiguration getSslConfiguration() {
      return this.sslConfiguration;
    }
    
    public boolean getImmediateFail() {
      return this.immediateFail;
    }
    
    public B withAdvertise(boolean advertise) {
      this.advertise = advertise;
      return (B)asBuilder();
    }
    
    public B withConnectTimeoutMillis(int connectTimeoutMillis) {
      this.connectTimeoutMillis = connectTimeoutMillis;
      return (B)asBuilder();
    }
    
    public B withHost(String host) {
      this.host = host;
      return (B)asBuilder();
    }
    
    public B withImmediateFail(boolean immediateFail) {
      this.immediateFail = immediateFail;
      return (B)asBuilder();
    }
    
    public B withPort(int port) {
      this.port = port;
      return (B)asBuilder();
    }
    
    public B withProtocol(Protocol protocol) {
      this.protocol = protocol;
      return (B)asBuilder();
    }
    
    public B withReconnectDelayMillis(int reconnectDelayMillis) {
      this.reconnectDelayMillis = reconnectDelayMillis;
      return (B)asBuilder();
    }
    
    public B withSocketOptions(SocketOptions socketOptions) {
      this.socketOptions = socketOptions;
      return (B)asBuilder();
    }
    
    public B withSslConfiguration(SslConfiguration sslConfiguration) {
      this.sslConfiguration = sslConfiguration;
      return (B)asBuilder();
    }
    
    public int getReconnectDelayMillis() {
      return this.reconnectDelayMillis;
    }
    
    public SocketOptions getSocketOptions() {
      return this.socketOptions;
    }
  }
  
  public static class Builder extends AbstractBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<SocketAppender> {
    public SocketAppender build() {
      boolean immediateFlush = isImmediateFlush();
      boolean bufferedIo = isBufferedIo();
      Layout<? extends Serializable> layout = getLayout();
      if (layout == null) {
        SocketAppender.LOGGER.error("No layout provided for SocketAppender");
        return null;
      } 
      String name = getName();
      if (name == null) {
        SocketAppender.LOGGER.error("No name provided for SocketAppender");
        return null;
      } 
      Protocol protocol = getProtocol();
      Protocol actualProtocol = (protocol != null) ? protocol : Protocol.TCP;
      if (actualProtocol == Protocol.UDP)
        immediateFlush = true; 
      AbstractSocketManager manager = SocketAppender.createSocketManager(name, actualProtocol, getHost(), getPort(), 
          getConnectTimeoutMillis(), getSslConfiguration(), getReconnectDelayMillis(), getImmediateFail(), layout, getBufferSize(), getSocketOptions());
      return new SocketAppender(name, layout, getFilter(), manager, isIgnoreExceptions(), (!bufferedIo || immediateFlush), 
          getAdvertise() ? getConfiguration().getAdvertiser() : null, 
          getPropertyArray());
    }
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
    super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
    if (advertiser != null) {
      Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
      configuration.putAll(manager.getContentFormat());
      configuration.put("contentType", layout.getContentType());
      configuration.put("name", name);
      this.advertisement = advertiser.advertise(configuration);
    } else {
      this.advertisement = null;
    } 
    this.advertiser = advertiser;
  }
  
  @Deprecated
  protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
    this(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, Property.EMPTY_ARRAY);
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    stop(timeout, timeUnit, false);
    if (this.advertiser != null)
      this.advertiser.unadvertise(this.advertisement); 
    setStopped();
    return true;
  }
  
  @Deprecated
  @PluginFactory
  public static SocketAppender createAppender(String host, int port, Protocol protocol, SslConfiguration sslConfig, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, String name, boolean immediateFlush, boolean ignoreExceptions, Layout<? extends Serializable> layout, Filter filter, boolean advertise, Configuration configuration) {
    return ((Builder)newBuilder()
      .withAdvertise(advertise)
      .setConfiguration(configuration)
      .withConnectTimeoutMillis(connectTimeoutMillis).setFilter(filter))
      .withHost(host).setIgnoreExceptions(ignoreExceptions)
      .withImmediateFail(immediateFail).setLayout(layout).setName(name)
      .withPort(port)
      .withProtocol(protocol)
      .withReconnectDelayMillis(reconnectDelayMillis)
      .withSslConfiguration(sslConfig)
      .build();
  }
  
  @Deprecated
  public static SocketAppender createAppender(String host, String portNum, String protocolIn, SslConfiguration sslConfig, int connectTimeoutMillis, String delayMillis, String immediateFail, String name, String immediateFlush, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, Configuration config) {
    boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
    boolean isAdvertise = Boolean.parseBoolean(advertise);
    boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
    boolean fail = Booleans.parseBoolean(immediateFail, true);
    int reconnectDelayMillis = AbstractAppender.parseInt(delayMillis, 0);
    int port = AbstractAppender.parseInt(portNum, 0);
    Protocol p = (protocolIn == null) ? Protocol.UDP : Protocol.valueOf(protocolIn);
    return createAppender(host, port, p, sslConfig, connectTimeoutMillis, reconnectDelayMillis, fail, name, isFlush, ignoreExceptions, layout, filter, isAdvertise, config);
  }
  
  @Deprecated
  protected static AbstractSocketManager createSocketManager(String name, Protocol protocol, String host, int port, int connectTimeoutMillis, SslConfiguration sslConfig, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
    return createSocketManager(name, protocol, host, port, connectTimeoutMillis, sslConfig, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
  }
  
  protected static AbstractSocketManager createSocketManager(String name, Protocol protocol, String host, int port, int connectTimeoutMillis, SslConfiguration sslConfig, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
    if (protocol == Protocol.TCP && sslConfig != null)
      protocol = Protocol.SSL; 
    if (protocol != Protocol.SSL && sslConfig != null)
      LOGGER.info("Appender {} ignoring SSL configuration for {} protocol", name, protocol); 
    switch (protocol) {
      case TCP:
        return (AbstractSocketManager)TcpSocketManager.getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
      case UDP:
        return (AbstractSocketManager)DatagramSocketManager.getSocketManager(host, port, layout, bufferSize);
      case SSL:
        return (AbstractSocketManager)SslSocketManager.getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
    } 
    throw new IllegalArgumentException(protocol.toString());
  }
  
  protected void directEncodeEvent(LogEvent event) {
    writeByteArrayToManager(event);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\SocketAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */