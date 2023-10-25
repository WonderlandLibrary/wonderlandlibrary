package io.netty.channel.socket.nio;

import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DefaultDatagramChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.util.Enumeration;

class NioDatagramChannelConfig extends DefaultDatagramChannelConfig {
  private static final Object IP_MULTICAST_TTL;
  
  private static final Object IP_MULTICAST_IF;
  
  private static final Object IP_MULTICAST_LOOP;
  
  private static final Method GET_OPTION;
  
  private static final Method SET_OPTION;
  
  private final DatagramChannel javaChannel;
  
  static {
    ClassLoader classLoader = PlatformDependent.getClassLoader(DatagramChannel.class);
    Class<?> socketOptionType = null;
    try {
      socketOptionType = Class.forName("java.net.SocketOption", true, classLoader);
    } catch (Exception e) {}
    Class<?> stdSocketOptionType = null;
    try {
      stdSocketOptionType = Class.forName("java.net.StandardSocketOptions", true, classLoader);
    } catch (Exception e) {}
    Object ipMulticastTtl = null;
    Object ipMulticastIf = null;
    Object ipMulticastLoop = null;
    Method getOption = null;
    Method setOption = null;
    if (socketOptionType != null) {
      try {
        ipMulticastTtl = stdSocketOptionType.getDeclaredField("IP_MULTICAST_TTL").get(null);
      } catch (Exception e) {
        throw new Error("cannot locate the IP_MULTICAST_TTL field", e);
      } 
      try {
        ipMulticastIf = stdSocketOptionType.getDeclaredField("IP_MULTICAST_IF").get(null);
      } catch (Exception e) {
        throw new Error("cannot locate the IP_MULTICAST_IF field", e);
      } 
      try {
        ipMulticastLoop = stdSocketOptionType.getDeclaredField("IP_MULTICAST_LOOP").get(null);
      } catch (Exception e) {
        throw new Error("cannot locate the IP_MULTICAST_LOOP field", e);
      } 
      try {
        getOption = NetworkChannel.class.getDeclaredMethod("getOption", new Class[] { socketOptionType });
      } catch (Exception e) {
        throw new Error("cannot locate the getOption() method", e);
      } 
      try {
        setOption = NetworkChannel.class.getDeclaredMethod("setOption", new Class[] { socketOptionType, Object.class });
      } catch (Exception e) {
        throw new Error("cannot locate the setOption() method", e);
      } 
    } 
    IP_MULTICAST_TTL = ipMulticastTtl;
    IP_MULTICAST_IF = ipMulticastIf;
    IP_MULTICAST_LOOP = ipMulticastLoop;
    GET_OPTION = getOption;
    SET_OPTION = setOption;
  }
  
  NioDatagramChannelConfig(NioDatagramChannel channel, DatagramChannel javaChannel) {
    super(channel, javaChannel.socket());
    this.javaChannel = javaChannel;
  }
  
  public int getTimeToLive() {
    return ((Integer)getOption0(IP_MULTICAST_TTL)).intValue();
  }
  
  public DatagramChannelConfig setTimeToLive(int ttl) {
    setOption0(IP_MULTICAST_TTL, Integer.valueOf(ttl));
    return (DatagramChannelConfig)this;
  }
  
  public InetAddress getInterface() {
    NetworkInterface inf = getNetworkInterface();
    if (inf == null)
      return null; 
    Enumeration<InetAddress> addresses = inf.getInetAddresses();
    if (addresses.hasMoreElements())
      return addresses.nextElement(); 
    return null;
  }
  
  public DatagramChannelConfig setInterface(InetAddress interfaceAddress) {
    try {
      setNetworkInterface(NetworkInterface.getByInetAddress(interfaceAddress));
    } catch (SocketException e) {
      throw new ChannelException(e);
    } 
    return (DatagramChannelConfig)this;
  }
  
  public NetworkInterface getNetworkInterface() {
    return (NetworkInterface)getOption0(IP_MULTICAST_IF);
  }
  
  public DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
    setOption0(IP_MULTICAST_IF, networkInterface);
    return (DatagramChannelConfig)this;
  }
  
  public boolean isLoopbackModeDisabled() {
    return ((Boolean)getOption0(IP_MULTICAST_LOOP)).booleanValue();
  }
  
  public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled) {
    setOption0(IP_MULTICAST_LOOP, Boolean.valueOf(loopbackModeDisabled));
    return (DatagramChannelConfig)this;
  }
  
  public DatagramChannelConfig setAutoRead(boolean autoRead) {
    super.setAutoRead(autoRead);
    return (DatagramChannelConfig)this;
  }
  
  protected void autoReadCleared() {
    ((NioDatagramChannel)this.channel).setReadPending(false);
  }
  
  private Object getOption0(Object option) {
    if (PlatformDependent.javaVersion() < 7)
      throw new UnsupportedOperationException(); 
    try {
      return GET_OPTION.invoke(this.javaChannel, new Object[] { option });
    } catch (Exception e) {
      throw new ChannelException(e);
    } 
  }
  
  private void setOption0(Object option, Object value) {
    if (PlatformDependent.javaVersion() < 7)
      throw new UnsupportedOperationException(); 
    try {
      SET_OPTION.invoke(this.javaChannel, new Object[] { option, value });
    } catch (Exception e) {
      throw new ChannelException(e);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\nio\NioDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */