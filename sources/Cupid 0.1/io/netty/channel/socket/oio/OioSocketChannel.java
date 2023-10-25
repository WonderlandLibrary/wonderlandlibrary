package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.channel.oio.OioByteStreamChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class OioSocketChannel extends OioByteStreamChannel implements SocketChannel {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioSocketChannel.class);
  
  private final Socket socket;
  
  private final OioSocketChannelConfig config;
  
  public OioSocketChannel() {
    this(new Socket());
  }
  
  public OioSocketChannel(Socket socket) {
    this((Channel)null, socket);
  }
  
  public OioSocketChannel(Channel parent, Socket socket) {
    super(parent);
    this.socket = socket;
    this.config = new DefaultOioSocketChannelConfig(this, socket);
    boolean success = false;
    try {
      if (socket.isConnected())
        activate(socket.getInputStream(), socket.getOutputStream()); 
      socket.setSoTimeout(1000);
      success = true;
    } catch (Exception e) {
      throw new ChannelException("failed to initialize a socket", e);
    } finally {
      if (!success)
        try {
          socket.close();
        } catch (IOException e) {
          logger.warn("Failed to close a socket.", e);
        }  
    } 
  }
  
  public ServerSocketChannel parent() {
    return (ServerSocketChannel)super.parent();
  }
  
  public OioSocketChannelConfig config() {
    return this.config;
  }
  
  public boolean isOpen() {
    return !this.socket.isClosed();
  }
  
  public boolean isActive() {
    return (!this.socket.isClosed() && this.socket.isConnected());
  }
  
  public boolean isInputShutdown() {
    return super.isInputShutdown();
  }
  
  public boolean isOutputShutdown() {
    return (this.socket.isOutputShutdown() || !isActive());
  }
  
  public ChannelFuture shutdownOutput() {
    return shutdownOutput(newPromise());
  }
  
  protected int doReadBytes(ByteBuf buf) throws Exception {
    if (this.socket.isClosed())
      return -1; 
    try {
      return super.doReadBytes(buf);
    } catch (SocketTimeoutException ignored) {
      return 0;
    } 
  }
  
  public ChannelFuture shutdownOutput(final ChannelPromise future) {
    EventLoop loop = eventLoop();
    if (loop.inEventLoop()) {
      try {
        this.socket.shutdownOutput();
        future.setSuccess();
      } catch (Throwable t) {
        future.setFailure(t);
      } 
    } else {
      loop.execute(new Runnable() {
            public void run() {
              OioSocketChannel.this.shutdownOutput(future);
            }
          });
    } 
    return (ChannelFuture)future;
  }
  
  public InetSocketAddress localAddress() {
    return (InetSocketAddress)super.localAddress();
  }
  
  public InetSocketAddress remoteAddress() {
    return (InetSocketAddress)super.remoteAddress();
  }
  
  protected SocketAddress localAddress0() {
    return this.socket.getLocalSocketAddress();
  }
  
  protected SocketAddress remoteAddress0() {
    return this.socket.getRemoteSocketAddress();
  }
  
  protected void doBind(SocketAddress localAddress) throws Exception {
    this.socket.bind(localAddress);
  }
  
  protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
    if (localAddress != null)
      this.socket.bind(localAddress); 
    boolean success = false;
    try {
      this.socket.connect(remoteAddress, config().getConnectTimeoutMillis());
      activate(this.socket.getInputStream(), this.socket.getOutputStream());
      success = true;
    } catch (SocketTimeoutException e) {
      ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
      cause.setStackTrace(e.getStackTrace());
      throw cause;
    } finally {
      if (!success)
        doClose(); 
    } 
  }
  
  protected void doDisconnect() throws Exception {
    doClose();
  }
  
  protected void doClose() throws Exception {
    this.socket.close();
  }
  
  protected boolean checkInputShutdown() {
    if (isInputShutdown()) {
      try {
        Thread.sleep(config().getSoTimeout());
      } catch (Throwable e) {}
      return true;
    } 
    return false;
  }
  
  protected void setReadPending(boolean readPending) {
    super.setReadPending(readPending);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\oio\OioSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */