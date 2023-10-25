package io.netty.channel.epoll;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class EpollServerSocketChannel extends AbstractEpollChannel implements ServerSocketChannel {
  private final EpollServerSocketChannelConfig config;
  
  private volatile InetSocketAddress local;
  
  public EpollServerSocketChannel() {
    super(Native.socketStreamFd(), 4);
    this.config = new EpollServerSocketChannelConfig(this);
  }
  
  protected boolean isCompatible(EventLoop loop) {
    return loop instanceof EpollEventLoop;
  }
  
  protected void doBind(SocketAddress localAddress) throws Exception {
    InetSocketAddress addr = (InetSocketAddress)localAddress;
    checkResolvable(addr);
    Native.bind(this.fd, addr.getAddress(), addr.getPort());
    this.local = Native.localAddress(this.fd);
    Native.listen(this.fd, this.config.getBacklog());
    this.active = true;
  }
  
  public EpollServerSocketChannelConfig config() {
    return this.config;
  }
  
  protected InetSocketAddress localAddress0() {
    return this.local;
  }
  
  protected InetSocketAddress remoteAddress0() {
    return null;
  }
  
  protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
    return new EpollServerSocketUnsafe();
  }
  
  protected void doWrite(ChannelOutboundBuffer in) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  protected Object filterOutboundMessage(Object msg) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  final class EpollServerSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
    public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
      channelPromise.setFailure(new UnsupportedOperationException());
    }
    
    void epollInReady() {
      assert EpollServerSocketChannel.this.eventLoop().inEventLoop();
      ChannelPipeline pipeline = EpollServerSocketChannel.this.pipeline();
      Throwable exception = null;
      try {
        while (true) {
          try {
            int socketFd = Native.accept(EpollServerSocketChannel.this.fd);
            if (socketFd == -1)
              break; 
            try {
              this.readPending = false;
              pipeline.fireChannelRead(new EpollSocketChannel((Channel)EpollServerSocketChannel.this, socketFd));
            } catch (Throwable t) {
              pipeline.fireChannelReadComplete();
              pipeline.fireExceptionCaught(t);
            } 
          } catch (Throwable t) {
            exception = t;
            break;
          } 
        } 
        pipeline.fireChannelReadComplete();
        if (exception != null)
          pipeline.fireExceptionCaught(exception); 
      } finally {
        if (!EpollServerSocketChannel.this.config.isAutoRead() && !this.readPending)
          clearEpollIn0(); 
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\epoll\EpollServerSocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */