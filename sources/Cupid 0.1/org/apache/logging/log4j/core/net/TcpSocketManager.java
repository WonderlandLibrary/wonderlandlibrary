package org.apache.logging.log4j.core.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.core.util.NullOutputStream;
import org.apache.logging.log4j.util.Strings;

public class TcpSocketManager extends AbstractSocketManager {
  public static final int DEFAULT_RECONNECTION_DELAY_MILLIS = 30000;
  
  private static final int DEFAULT_PORT = 4560;
  
  private static final TcpSocketManagerFactory<TcpSocketManager, FactoryData> FACTORY = new TcpSocketManagerFactory<>();
  
  private final int reconnectionDelayMillis;
  
  private Reconnector reconnector;
  
  private Socket socket;
  
  private final SocketOptions socketOptions;
  
  private final boolean retry;
  
  private final boolean immediateFail;
  
  private final int connectTimeoutMillis;
  
  @Deprecated
  public TcpSocketManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
    this(name, os, socket, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
  }
  
  public TcpSocketManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
    super(name, os, inetAddress, host, port, layout, true, bufferSize);
    this.connectTimeoutMillis = connectTimeoutMillis;
    this.reconnectionDelayMillis = reconnectionDelayMillis;
    this.socket = socket;
    this.immediateFail = immediateFail;
    this.retry = (reconnectionDelayMillis > 0);
    if (socket == null) {
      this.reconnector = createReconnector();
      this.reconnector.start();
    } 
    this.socketOptions = socketOptions;
  }
  
  @Deprecated
  public static TcpSocketManager getSocketManager(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
    return getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
  }
  
  public static TcpSocketManager getSocketManager(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
    if (Strings.isEmpty(host))
      throw new IllegalArgumentException("A host name is required"); 
    if (port <= 0)
      port = 4560; 
    if (reconnectDelayMillis == 0)
      reconnectDelayMillis = 30000; 
    return (TcpSocketManager)getManager("TCP:" + host + ':' + port, new FactoryData(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), FACTORY);
  }
  
  protected void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
    if (this.socket == null) {
      if (this.reconnector != null && !this.immediateFail)
        this.reconnector.latch(); 
      if (this.socket == null)
        throw new AppenderLoggingException("Error writing to " + getName() + ": socket not available"); 
    } 
    synchronized (this) {
      try {
        writeAndFlush(bytes, offset, length, immediateFlush);
      } catch (IOException causeEx) {
        String config = this.inetAddress + ":" + this.port;
        if (this.retry && this.reconnector == null) {
          this.reconnector = createReconnector();
          try {
            this.reconnector.reconnect();
          } catch (IOException reconnEx) {
            LOGGER.debug("Cannot reestablish socket connection to {}: {}; starting reconnector thread {}", config, reconnEx
                .getLocalizedMessage(), this.reconnector.getName(), reconnEx);
            this.reconnector.start();
            throw new AppenderLoggingException(
                String.format("Error sending to %s for %s", new Object[] { getName(), config }), causeEx);
          } 
          try {
            writeAndFlush(bytes, offset, length, immediateFlush);
          } catch (IOException e) {
            throw new AppenderLoggingException(
                String.format("Error writing to %s after reestablishing connection for %s", new Object[] { getName(), config }), causeEx);
          } 
          return;
        } 
        String message = String.format("Error writing to %s for connection %s", new Object[] { getName(), config });
        throw new AppenderLoggingException(message, causeEx);
      } 
    } 
  }
  
  private void writeAndFlush(byte[] bytes, int offset, int length, boolean immediateFlush) throws IOException {
    OutputStream outputStream = getOutputStream();
    outputStream.write(bytes, offset, length);
    if (immediateFlush)
      outputStream.flush(); 
  }
  
  protected synchronized boolean closeOutputStream() {
    boolean closed = super.closeOutputStream();
    if (this.reconnector != null) {
      this.reconnector.shutdown();
      this.reconnector.interrupt();
      this.reconnector = null;
    } 
    Socket oldSocket = this.socket;
    this.socket = null;
    if (oldSocket != null)
      try {
        oldSocket.close();
      } catch (IOException e) {
        LOGGER.error("Could not close socket {}", this.socket);
        return false;
      }  
    return closed;
  }
  
  public int getConnectTimeoutMillis() {
    return this.connectTimeoutMillis;
  }
  
  public Map<String, String> getContentFormat() {
    Map<String, String> result = new HashMap<>(super.getContentFormat());
    result.put("protocol", "tcp");
    result.put("direction", "out");
    return result;
  }
  
  private class Reconnector extends Log4jThread {
    private final CountDownLatch latch = new CountDownLatch(1);
    
    private boolean shutdown = false;
    
    private final Object owner;
    
    public Reconnector(OutputStreamManager owner) {
      super("TcpSocketManager-Reconnector");
      this.owner = owner;
    }
    
    public void latch() {
      try {
        this.latch.await();
      } catch (InterruptedException interruptedException) {}
    }
    
    public void shutdown() {
      this.shutdown = true;
    }
    
    public void run() {
      while (!this.shutdown) {
        try {
          sleep(TcpSocketManager.this.reconnectionDelayMillis);
          reconnect();
        } catch (InterruptedException ie) {
          TcpSocketManager.LOGGER.debug("Reconnection interrupted.");
        } catch (ConnectException ex) {
          TcpSocketManager.LOGGER.debug("{}:{} refused connection", TcpSocketManager.this.host, Integer.valueOf(TcpSocketManager.this.port));
        } catch (IOException ioe) {
          TcpSocketManager.LOGGER.debug("Unable to reconnect to {}:{}", TcpSocketManager.this.host, Integer.valueOf(TcpSocketManager.this.port));
        } finally {
          this.latch.countDown();
        } 
      } 
    }
    
    void reconnect() throws IOException {
      TcpSocketManager.FACTORY;
      List<InetSocketAddress> socketAddresses = TcpSocketManager.TcpSocketManagerFactory.resolver.resolveHost(TcpSocketManager.this.host, TcpSocketManager.this.port);
      if (socketAddresses.size() == 1) {
        TcpSocketManager.LOGGER.debug("Reconnecting " + socketAddresses.get(0));
        connect(socketAddresses.get(0));
      } else {
        IOException ioe = null;
        for (InetSocketAddress socketAddress : socketAddresses) {
          try {
            TcpSocketManager.LOGGER.debug("Reconnecting " + socketAddress);
            connect(socketAddress);
            return;
          } catch (IOException ex) {
            ioe = ex;
          } 
        } 
        throw ioe;
      } 
    }
    
    private void connect(InetSocketAddress socketAddress) throws IOException {
      Socket sock = TcpSocketManager.this.createSocket(socketAddress);
      OutputStream newOS = sock.getOutputStream();
      InetAddress prev = (TcpSocketManager.this.socket != null) ? TcpSocketManager.this.socket.getInetAddress() : null;
      synchronized (this.owner) {
        Closer.closeSilently(TcpSocketManager.this.getOutputStream());
        TcpSocketManager.this.setOutputStream(newOS);
        TcpSocketManager.this.socket = sock;
        TcpSocketManager.this.reconnector = null;
        this.shutdown = true;
      } 
      String type = (prev != null && prev.getHostAddress().equals(socketAddress.getAddress().getHostAddress())) ? "reestablished" : "established";
      TcpSocketManager.LOGGER.debug("Connection to {}:{} {}: {}", TcpSocketManager.this.host, Integer.valueOf(TcpSocketManager.this.port), type, TcpSocketManager.this.socket);
    }
    
    public String toString() {
      return "Reconnector [latch=" + this.latch + ", shutdown=" + this.shutdown + "]";
    }
  }
  
  private Reconnector createReconnector() {
    Reconnector recon = new Reconnector(this);
    recon.setDaemon(true);
    recon.setPriority(1);
    return recon;
  }
  
  protected Socket createSocket(InetSocketAddress socketAddress) throws IOException {
    return createSocket(socketAddress, this.socketOptions, this.connectTimeoutMillis);
  }
  
  protected static Socket createSocket(InetSocketAddress socketAddress, SocketOptions socketOptions, int connectTimeoutMillis) throws IOException {
    LOGGER.debug("Creating socket {}", socketAddress.toString());
    Socket newSocket = new Socket();
    if (socketOptions != null)
      socketOptions.apply(newSocket); 
    newSocket.connect(socketAddress, connectTimeoutMillis);
    if (socketOptions != null)
      socketOptions.apply(newSocket); 
    return newSocket;
  }
  
  static class FactoryData {
    protected final String host;
    
    protected final int port;
    
    protected final int connectTimeoutMillis;
    
    protected final int reconnectDelayMillis;
    
    protected final boolean immediateFail;
    
    protected final Layout<? extends Serializable> layout;
    
    protected final int bufferSize;
    
    protected final SocketOptions socketOptions;
    
    public FactoryData(String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
      this.host = host;
      this.port = port;
      this.connectTimeoutMillis = connectTimeoutMillis;
      this.reconnectDelayMillis = reconnectDelayMillis;
      this.immediateFail = immediateFail;
      this.layout = layout;
      this.bufferSize = bufferSize;
      this.socketOptions = socketOptions;
    }
    
    public String toString() {
      return "FactoryData [host=" + this.host + ", port=" + this.port + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", reconnectDelayMillis=" + this.reconnectDelayMillis + ", immediateFail=" + this.immediateFail + ", layout=" + this.layout + ", bufferSize=" + this.bufferSize + ", socketOptions=" + this.socketOptions + "]";
    }
  }
  
  protected static class TcpSocketManagerFactory<M extends TcpSocketManager, T extends FactoryData> implements ManagerFactory<M, T> {
    static TcpSocketManager.HostResolver resolver = new TcpSocketManager.HostResolver();
    
    public M createManager(String name, T data) {
      InetAddress inetAddress;
      try {
        inetAddress = InetAddress.getByName(((TcpSocketManager.FactoryData)data).host);
      } catch (UnknownHostException ex) {
        TcpSocketManager.LOGGER.error("Could not find address of {}: {}", ((TcpSocketManager.FactoryData)data).host, ex, ex);
        return null;
      } 
      Socket socket = null;
      try {
        socket = createSocket(data);
        OutputStream os = socket.getOutputStream();
        return createManager(name, os, socket, inetAddress, data);
      } catch (IOException ex) {
        TcpSocketManager.LOGGER.error("TcpSocketManager ({}) caught exception and will continue:", name, ex);
        NullOutputStream nullOutputStream = NullOutputStream.getInstance();
        if (((TcpSocketManager.FactoryData)data).reconnectDelayMillis == 0) {
          Closer.closeSilently(socket);
          return null;
        } 
        return createManager(name, (OutputStream)nullOutputStream, null, inetAddress, data);
      } 
    }
    
    M createManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, T data) {
      return (M)new TcpSocketManager(name, os, socket, inetAddress, ((TcpSocketManager.FactoryData)data).host, ((TcpSocketManager.FactoryData)data).port, ((TcpSocketManager.FactoryData)data).connectTimeoutMillis, ((TcpSocketManager.FactoryData)data).reconnectDelayMillis, ((TcpSocketManager.FactoryData)data).immediateFail, ((TcpSocketManager.FactoryData)data).layout, ((TcpSocketManager.FactoryData)data).bufferSize, ((TcpSocketManager.FactoryData)data).socketOptions);
    }
    
    Socket createSocket(T data) throws IOException {
      List<InetSocketAddress> socketAddresses = resolver.resolveHost(((TcpSocketManager.FactoryData)data).host, ((TcpSocketManager.FactoryData)data).port);
      IOException ioe = null;
      for (InetSocketAddress socketAddress : socketAddresses) {
        try {
          return TcpSocketManager.createSocket(socketAddress, ((TcpSocketManager.FactoryData)data).socketOptions, ((TcpSocketManager.FactoryData)data).connectTimeoutMillis);
        } catch (IOException ex) {
          ioe = ex;
        } 
      } 
      throw new IOException(errorMessage(data, socketAddresses), ioe);
    }
    
    protected String errorMessage(T data, List<InetSocketAddress> socketAddresses) {
      StringBuilder sb = new StringBuilder("Unable to create socket for ");
      sb.append(((TcpSocketManager.FactoryData)data).host).append(" at port ").append(((TcpSocketManager.FactoryData)data).port);
      if (socketAddresses.size() == 1) {
        if (!((InetSocketAddress)socketAddresses.get(0)).getAddress().getHostAddress().equals(((TcpSocketManager.FactoryData)data).host)) {
          sb.append(" using ip address ").append(((InetSocketAddress)socketAddresses.get(0)).getAddress().getHostAddress());
          sb.append(" and port ").append(((InetSocketAddress)socketAddresses.get(0)).getPort());
        } 
      } else {
        sb.append(" using ip addresses and ports ");
        for (int i = 0; i < socketAddresses.size(); i++) {
          if (i > 0) {
            sb.append(", ");
            sb.append(((InetSocketAddress)socketAddresses.get(i)).getAddress().getHostAddress());
            sb.append(":").append(((InetSocketAddress)socketAddresses.get(i)).getPort());
          } 
        } 
      } 
      return sb.toString();
    }
  }
  
  public static void setHostResolver(HostResolver resolver) {
    TcpSocketManagerFactory.resolver = resolver;
  }
  
  public static class HostResolver {
    public List<InetSocketAddress> resolveHost(String host, int port) throws UnknownHostException {
      InetAddress[] addresses = InetAddress.getAllByName(host);
      List<InetSocketAddress> socketAddresses = new ArrayList<>(addresses.length);
      for (InetAddress address : addresses)
        socketAddresses.add(new InetSocketAddress(address, port)); 
      return socketAddresses;
    }
  }
  
  public SocketOptions getSocketOptions() {
    return this.socketOptions;
  }
  
  public Socket getSocket() {
    return this.socket;
  }
  
  public int getReconnectionDelayMillis() {
    return this.reconnectionDelayMillis;
  }
  
  public String toString() {
    return "TcpSocketManager [reconnectionDelayMillis=" + this.reconnectionDelayMillis + ", reconnector=" + this.reconnector + ", socket=" + this.socket + ", socketOptions=" + this.socketOptions + ", retry=" + this.retry + ", immediateFail=" + this.immediateFail + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", inetAddress=" + this.inetAddress + ", host=" + this.host + ", port=" + this.port + ", layout=" + this.layout + ", byteBuffer=" + this.byteBuffer + ", count=" + this.count + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\TcpSocketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */