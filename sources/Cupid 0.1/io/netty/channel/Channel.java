package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AttributeMap;
import java.net.SocketAddress;

public interface Channel extends AttributeMap, Comparable<Channel> {
  EventLoop eventLoop();
  
  Channel parent();
  
  ChannelConfig config();
  
  boolean isOpen();
  
  boolean isRegistered();
  
  boolean isActive();
  
  ChannelMetadata metadata();
  
  SocketAddress localAddress();
  
  SocketAddress remoteAddress();
  
  ChannelFuture closeFuture();
  
  boolean isWritable();
  
  Unsafe unsafe();
  
  ChannelPipeline pipeline();
  
  ByteBufAllocator alloc();
  
  ChannelPromise newPromise();
  
  ChannelProgressivePromise newProgressivePromise();
  
  ChannelFuture newSucceededFuture();
  
  ChannelFuture newFailedFuture(Throwable paramThrowable);
  
  ChannelPromise voidPromise();
  
  ChannelFuture bind(SocketAddress paramSocketAddress);
  
  ChannelFuture connect(SocketAddress paramSocketAddress);
  
  ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);
  
  ChannelFuture disconnect();
  
  ChannelFuture close();
  
  ChannelFuture deregister();
  
  ChannelFuture bind(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);
  
  ChannelFuture connect(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);
  
  ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise);
  
  ChannelFuture disconnect(ChannelPromise paramChannelPromise);
  
  ChannelFuture close(ChannelPromise paramChannelPromise);
  
  ChannelFuture deregister(ChannelPromise paramChannelPromise);
  
  Channel read();
  
  ChannelFuture write(Object paramObject);
  
  ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise);
  
  Channel flush();
  
  ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise);
  
  ChannelFuture writeAndFlush(Object paramObject);
  
  public static interface Unsafe {
    SocketAddress localAddress();
    
    SocketAddress remoteAddress();
    
    void register(EventLoop param1EventLoop, ChannelPromise param1ChannelPromise);
    
    void bind(SocketAddress param1SocketAddress, ChannelPromise param1ChannelPromise);
    
    void connect(SocketAddress param1SocketAddress1, SocketAddress param1SocketAddress2, ChannelPromise param1ChannelPromise);
    
    void disconnect(ChannelPromise param1ChannelPromise);
    
    void close(ChannelPromise param1ChannelPromise);
    
    void closeForcibly();
    
    void deregister(ChannelPromise param1ChannelPromise);
    
    void beginRead();
    
    void write(Object param1Object, ChannelPromise param1ChannelPromise);
    
    void flush();
    
    ChannelPromise voidPromise();
    
    ChannelOutboundBuffer outboundBuffer();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\Channel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */