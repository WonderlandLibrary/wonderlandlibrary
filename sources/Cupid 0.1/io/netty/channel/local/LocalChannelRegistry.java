package io.netty.channel.local;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;

final class LocalChannelRegistry {
  private static final ConcurrentMap<LocalAddress, Channel> boundChannels = PlatformDependent.newConcurrentHashMap();
  
  static LocalAddress register(Channel channel, LocalAddress oldLocalAddress, SocketAddress localAddress) {
    if (oldLocalAddress != null)
      throw new ChannelException("already bound"); 
    if (!(localAddress instanceof LocalAddress))
      throw new ChannelException("unsupported address type: " + StringUtil.simpleClassName(localAddress)); 
    LocalAddress addr = (LocalAddress)localAddress;
    if (LocalAddress.ANY.equals(addr))
      addr = new LocalAddress(channel); 
    Channel boundChannel = boundChannels.putIfAbsent(addr, channel);
    if (boundChannel != null)
      throw new ChannelException("address already in use by: " + boundChannel); 
    return addr;
  }
  
  static Channel get(SocketAddress localAddress) {
    return boundChannels.get(localAddress);
  }
  
  static void unregister(LocalAddress localAddress) {
    boundChannels.remove(localAddress);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\local\LocalChannelRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */