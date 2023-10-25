package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class DefaultSocketChannelConfig extends DefaultChannelConfig implements SocketChannelConfig {
  protected final Socket javaSocket;
  
  private volatile boolean allowHalfClosure;
  
  public DefaultSocketChannelConfig(SocketChannel channel, Socket javaSocket) {
    super(channel);
    if (javaSocket == null)
      throw new NullPointerException("javaSocket"); 
    this.javaSocket = javaSocket;
    if (PlatformDependent.canEnableTcpNoDelayByDefault())
      try {
        setTcpNoDelay(true);
      } catch (Exception e) {} 
  }
  
  public Map<ChannelOption<?>, Object> getOptions() {
    return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE });
  }
  
  public <T> T getOption(ChannelOption<T> option) {
    if (option == ChannelOption.SO_RCVBUF)
      return (T)Integer.valueOf(getReceiveBufferSize()); 
    if (option == ChannelOption.SO_SNDBUF)
      return (T)Integer.valueOf(getSendBufferSize()); 
    if (option == ChannelOption.TCP_NODELAY)
      return (T)Boolean.valueOf(isTcpNoDelay()); 
    if (option == ChannelOption.SO_KEEPALIVE)
      return (T)Boolean.valueOf(isKeepAlive()); 
    if (option == ChannelOption.SO_REUSEADDR)
      return (T)Boolean.valueOf(isReuseAddress()); 
    if (option == ChannelOption.SO_LINGER)
      return (T)Integer.valueOf(getSoLinger()); 
    if (option == ChannelOption.IP_TOS)
      return (T)Integer.valueOf(getTrafficClass()); 
    if (option == ChannelOption.ALLOW_HALF_CLOSURE)
      return (T)Boolean.valueOf(isAllowHalfClosure()); 
    return (T)super.getOption(option);
  }
  
  public <T> boolean setOption(ChannelOption<T> option, T value) {
    validate(option, value);
    if (option == ChannelOption.SO_RCVBUF) {
      setReceiveBufferSize(((Integer)value).intValue());
    } else if (option == ChannelOption.SO_SNDBUF) {
      setSendBufferSize(((Integer)value).intValue());
    } else if (option == ChannelOption.TCP_NODELAY) {
      setTcpNoDelay(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.SO_KEEPALIVE) {
      setKeepAlive(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.SO_REUSEADDR) {
      setReuseAddress(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.SO_LINGER) {
      setSoLinger(((Integer)value).intValue());
    } else if (option == ChannelOption.IP_TOS) {
      setTrafficClass(((Integer)value).intValue());
    } else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
      setAllowHalfClosure(((Boolean)value).booleanValue());
    } else {
      return super.setOption(option, value);
    } 
    return true;
  }
  
  public int getReceiveBufferSize() {
    try {
      return this.javaSocket.getReceiveBufferSize();
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
  }
  
  public int getSendBufferSize() {
    try {
      return this.javaSocket.getSendBufferSize();
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
  }
  
  public int getSoLinger() {
    try {
      return this.javaSocket.getSoLinger();
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
  }
  
  public int getTrafficClass() {
    try {
      return this.javaSocket.getTrafficClass();
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
  }
  
  public boolean isKeepAlive() {
    try {
      return this.javaSocket.getKeepAlive();
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
  }
  
  public boolean isReuseAddress() {
    try {
      return this.javaSocket.getReuseAddress();
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
  }
  
  public boolean isTcpNoDelay() {
    try {
      return this.javaSocket.getTcpNoDelay();
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
  }
  
  public SocketChannelConfig setKeepAlive(boolean keepAlive) {
    try {
      this.javaSocket.setKeepAlive(keepAlive);
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return this;
  }
  
  public SocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
    this.javaSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
    return this;
  }
  
  public SocketChannelConfig setReceiveBufferSize(int receiveBufferSize) {
    try {
      this.javaSocket.setReceiveBufferSize(receiveBufferSize);
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return this;
  }
  
  public SocketChannelConfig setReuseAddress(boolean reuseAddress) {
    try {
      this.javaSocket.setReuseAddress(reuseAddress);
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return this;
  }
  
  public SocketChannelConfig setSendBufferSize(int sendBufferSize) {
    try {
      this.javaSocket.setSendBufferSize(sendBufferSize);
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return this;
  }
  
  public SocketChannelConfig setSoLinger(int soLinger) {
    try {
      if (soLinger < 0) {
        this.javaSocket.setSoLinger(false, 0);
      } else {
        this.javaSocket.setSoLinger(true, soLinger);
      } 
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return this;
  }
  
  public SocketChannelConfig setTcpNoDelay(boolean tcpNoDelay) {
    try {
      this.javaSocket.setTcpNoDelay(tcpNoDelay);
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return this;
  }
  
  public SocketChannelConfig setTrafficClass(int trafficClass) {
    try {
      this.javaSocket.setTrafficClass(trafficClass);
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return this;
  }
  
  public boolean isAllowHalfClosure() {
    return this.allowHalfClosure;
  }
  
  public SocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure) {
    this.allowHalfClosure = allowHalfClosure;
    return this;
  }
  
  public SocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
    super.setConnectTimeoutMillis(connectTimeoutMillis);
    return this;
  }
  
  public SocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
    super.setMaxMessagesPerRead(maxMessagesPerRead);
    return this;
  }
  
  public SocketChannelConfig setWriteSpinCount(int writeSpinCount) {
    super.setWriteSpinCount(writeSpinCount);
    return this;
  }
  
  public SocketChannelConfig setAllocator(ByteBufAllocator allocator) {
    super.setAllocator(allocator);
    return this;
  }
  
  public SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
    super.setRecvByteBufAllocator(allocator);
    return this;
  }
  
  public SocketChannelConfig setAutoRead(boolean autoRead) {
    super.setAutoRead(autoRead);
    return this;
  }
  
  public SocketChannelConfig setAutoClose(boolean autoClose) {
    super.setAutoClose(autoClose);
    return this;
  }
  
  public SocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
    super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
    return this;
  }
  
  public SocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
    super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
    return this;
  }
  
  public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
    super.setMessageSizeEstimator(estimator);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\DefaultSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */