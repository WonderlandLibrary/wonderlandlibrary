package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.DatagramChannelConfig;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;

public final class EpollDatagramChannelConfig extends DefaultChannelConfig implements DatagramChannelConfig {
  private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = (RecvByteBufAllocator)new FixedRecvByteBufAllocator(2048);
  
  private final EpollDatagramChannel datagramChannel;
  
  private boolean activeOnOpen;
  
  EpollDatagramChannelConfig(EpollDatagramChannel channel) {
    super((Channel)channel);
    this.datagramChannel = channel;
    setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
  }
  
  public Map<ChannelOption<?>, Object> getOptions() {
    return getOptions(super.getOptions(), new ChannelOption[] { 
          ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, 
          EpollChannelOption.SO_REUSEPORT });
  }
  
  public <T> T getOption(ChannelOption<T> option) {
    if (option == ChannelOption.SO_BROADCAST)
      return (T)Boolean.valueOf(isBroadcast()); 
    if (option == ChannelOption.SO_RCVBUF)
      return (T)Integer.valueOf(getReceiveBufferSize()); 
    if (option == ChannelOption.SO_SNDBUF)
      return (T)Integer.valueOf(getSendBufferSize()); 
    if (option == ChannelOption.SO_REUSEADDR)
      return (T)Boolean.valueOf(isReuseAddress()); 
    if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED)
      return (T)Boolean.valueOf(isLoopbackModeDisabled()); 
    if (option == ChannelOption.IP_MULTICAST_ADDR)
      return (T)getInterface(); 
    if (option == ChannelOption.IP_MULTICAST_IF)
      return (T)getNetworkInterface(); 
    if (option == ChannelOption.IP_MULTICAST_TTL)
      return (T)Integer.valueOf(getTimeToLive()); 
    if (option == ChannelOption.IP_TOS)
      return (T)Integer.valueOf(getTrafficClass()); 
    if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)
      return (T)Boolean.valueOf(this.activeOnOpen); 
    if (option == EpollChannelOption.SO_REUSEPORT)
      return (T)Boolean.valueOf(isReusePort()); 
    return (T)super.getOption(option);
  }
  
  public <T> boolean setOption(ChannelOption<T> option, T value) {
    validate(option, value);
    if (option == ChannelOption.SO_BROADCAST) {
      setBroadcast(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.SO_RCVBUF) {
      setReceiveBufferSize(((Integer)value).intValue());
    } else if (option == ChannelOption.SO_SNDBUF) {
      setSendBufferSize(((Integer)value).intValue());
    } else if (option == ChannelOption.SO_REUSEADDR) {
      setReuseAddress(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
      setLoopbackModeDisabled(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.IP_MULTICAST_ADDR) {
      setInterface((InetAddress)value);
    } else if (option == ChannelOption.IP_MULTICAST_IF) {
      setNetworkInterface((NetworkInterface)value);
    } else if (option == ChannelOption.IP_MULTICAST_TTL) {
      setTimeToLive(((Integer)value).intValue());
    } else if (option == ChannelOption.IP_TOS) {
      setTrafficClass(((Integer)value).intValue());
    } else if (option == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
      setActiveOnOpen(((Boolean)value).booleanValue());
    } else if (option == EpollChannelOption.SO_REUSEPORT) {
      setReusePort(((Boolean)value).booleanValue());
    } else {
      return super.setOption(option, value);
    } 
    return true;
  }
  
  private void setActiveOnOpen(boolean activeOnOpen) {
    if (this.channel.isRegistered())
      throw new IllegalStateException("Can only changed before channel was registered"); 
    this.activeOnOpen = activeOnOpen;
  }
  
  public EpollDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
    super.setMessageSizeEstimator(estimator);
    return this;
  }
  
  public EpollDatagramChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
    super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
    return this;
  }
  
  public EpollDatagramChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
    super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
    return this;
  }
  
  public EpollDatagramChannelConfig setAutoClose(boolean autoClose) {
    super.setAutoClose(autoClose);
    return this;
  }
  
  public EpollDatagramChannelConfig setAutoRead(boolean autoRead) {
    super.setAutoRead(autoRead);
    return this;
  }
  
  public EpollDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
    super.setRecvByteBufAllocator(allocator);
    return this;
  }
  
  public EpollDatagramChannelConfig setWriteSpinCount(int writeSpinCount) {
    super.setWriteSpinCount(writeSpinCount);
    return this;
  }
  
  public EpollDatagramChannelConfig setAllocator(ByteBufAllocator allocator) {
    super.setAllocator(allocator);
    return this;
  }
  
  public EpollDatagramChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
    super.setConnectTimeoutMillis(connectTimeoutMillis);
    return this;
  }
  
  public EpollDatagramChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
    super.setMaxMessagesPerRead(maxMessagesPerRead);
    return this;
  }
  
  public int getSendBufferSize() {
    return Native.getSendBufferSize(this.datagramChannel.fd);
  }
  
  public EpollDatagramChannelConfig setSendBufferSize(int sendBufferSize) {
    Native.setSendBufferSize(this.datagramChannel.fd, sendBufferSize);
    return this;
  }
  
  public int getReceiveBufferSize() {
    return Native.getReceiveBufferSize(this.datagramChannel.fd);
  }
  
  public EpollDatagramChannelConfig setReceiveBufferSize(int receiveBufferSize) {
    Native.setReceiveBufferSize(this.datagramChannel.fd, receiveBufferSize);
    return this;
  }
  
  public int getTrafficClass() {
    return Native.getTrafficClass(this.datagramChannel.fd);
  }
  
  public EpollDatagramChannelConfig setTrafficClass(int trafficClass) {
    Native.setTrafficClass(this.datagramChannel.fd, trafficClass);
    return this;
  }
  
  public boolean isReuseAddress() {
    return (Native.isReuseAddress(this.datagramChannel.fd) == 1);
  }
  
  public EpollDatagramChannelConfig setReuseAddress(boolean reuseAddress) {
    Native.setReuseAddress(this.datagramChannel.fd, reuseAddress ? 1 : 0);
    return this;
  }
  
  public boolean isBroadcast() {
    return (Native.isBroadcast(this.datagramChannel.fd) == 1);
  }
  
  public EpollDatagramChannelConfig setBroadcast(boolean broadcast) {
    Native.setBroadcast(this.datagramChannel.fd, broadcast ? 1 : 0);
    return this;
  }
  
  public boolean isLoopbackModeDisabled() {
    return false;
  }
  
  public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled) {
    throw new UnsupportedOperationException("Multicast not supported");
  }
  
  public int getTimeToLive() {
    return -1;
  }
  
  public EpollDatagramChannelConfig setTimeToLive(int ttl) {
    throw new UnsupportedOperationException("Multicast not supported");
  }
  
  public InetAddress getInterface() {
    return null;
  }
  
  public EpollDatagramChannelConfig setInterface(InetAddress interfaceAddress) {
    throw new UnsupportedOperationException("Multicast not supported");
  }
  
  public NetworkInterface getNetworkInterface() {
    return null;
  }
  
  public EpollDatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
    throw new UnsupportedOperationException("Multicast not supported");
  }
  
  public boolean isReusePort() {
    return (Native.isReusePort(this.datagramChannel.fd) == 1);
  }
  
  public EpollDatagramChannelConfig setReusePort(boolean reusePort) {
    Native.setReusePort(this.datagramChannel.fd, reusePort ? 1 : 0);
    return this;
  }
  
  protected void autoReadCleared() {
    this.datagramChannel.clearEpollIn();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\epoll\EpollDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */