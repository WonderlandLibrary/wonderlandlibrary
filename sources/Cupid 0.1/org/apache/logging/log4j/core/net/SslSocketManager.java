package org.apache.logging.log4j.core.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.util.Strings;

public class SslSocketManager extends TcpSocketManager {
  public static final int DEFAULT_PORT = 6514;
  
  private static final SslSocketManagerFactory FACTORY = new SslSocketManagerFactory();
  
  private final SslConfiguration sslConfig;
  
  @Deprecated
  public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
    super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
    this.sslConfig = sslConfig;
  }
  
  public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
    super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, socketOptions);
    this.sslConfig = sslConfig;
  }
  
  private static class SslFactoryData extends TcpSocketManager.FactoryData {
    protected SslConfiguration sslConfiguration;
    
    public SslFactoryData(SslConfiguration sslConfiguration, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
      super(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
      this.sslConfiguration = sslConfiguration;
    }
    
    public String toString() {
      return "SslFactoryData [sslConfiguration=" + this.sslConfiguration + ", host=" + this.host + ", port=" + this.port + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", reconnectDelayMillis=" + this.reconnectDelayMillis + ", immediateFail=" + this.immediateFail + ", layout=" + this.layout + ", bufferSize=" + this.bufferSize + ", socketOptions=" + this.socketOptions + "]";
    }
  }
  
  @Deprecated
  public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
    return getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
  }
  
  public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
    if (Strings.isEmpty(host))
      throw new IllegalArgumentException("A host name is required"); 
    if (port <= 0)
      port = 6514; 
    if (reconnectDelayMillis == 0)
      reconnectDelayMillis = 30000; 
    String name = "TLS:" + host + ':' + port;
    return (SslSocketManager)getManager(name, new SslFactoryData(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), FACTORY);
  }
  
  protected Socket createSocket(InetSocketAddress socketAddress) throws IOException {
    SSLSocketFactory socketFactory = createSslSocketFactory(this.sslConfig);
    Socket newSocket = socketFactory.createSocket();
    newSocket.connect(socketAddress, getConnectTimeoutMillis());
    return newSocket;
  }
  
  private static SSLSocketFactory createSslSocketFactory(SslConfiguration sslConf) {
    SSLSocketFactory socketFactory;
    if (sslConf != null) {
      socketFactory = sslConf.getSslSocketFactory();
    } else {
      socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    } 
    return socketFactory;
  }
  
  private static class SslSocketManagerFactory extends TcpSocketManager.TcpSocketManagerFactory<SslSocketManager, SslFactoryData> {
    private SslSocketManagerFactory() {}
    
    SslSocketManager createManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, SslSocketManager.SslFactoryData data) {
      return new SslSocketManager(name, os, socket, data.sslConfiguration, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.reconnectDelayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
    }
    
    Socket createSocket(SslSocketManager.SslFactoryData data) throws IOException {
      List<InetSocketAddress> socketAddresses = resolver.resolveHost(data.host, data.port);
      IOException ioe = null;
      for (InetSocketAddress socketAddress : socketAddresses) {
        try {
          return SslSocketManager.createSocket(socketAddress, data.connectTimeoutMillis, data.sslConfiguration, data.socketOptions);
        } catch (IOException ex) {
          ioe = ex;
        } 
      } 
      throw new IOException(errorMessage(data, socketAddresses), ioe);
    }
  }
  
  static Socket createSocket(InetSocketAddress socketAddress, int connectTimeoutMillis, SslConfiguration sslConfiguration, SocketOptions socketOptions) throws IOException {
    SSLSocketFactory socketFactory = createSslSocketFactory(sslConfiguration);
    SSLSocket socket = (SSLSocket)socketFactory.createSocket();
    if (socketOptions != null)
      socketOptions.apply(socket); 
    socket.connect(socketAddress, connectTimeoutMillis);
    if (socketOptions != null)
      socketOptions.apply(socket); 
    return socket;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\SslSocketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */