package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
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
   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
   private final EpollDatagramChannel datagramChannel;
   private boolean activeOnOpen;

   EpollDatagramChannelConfig(EpollDatagramChannel var1) {
      super(var1);
      this.datagramChannel = var1;
      this.setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, EpollChannelOption.SO_REUSEPORT});
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == ChannelOption.SO_BROADCAST) {
         return this.isBroadcast();
      } else if (var1 == ChannelOption.SO_RCVBUF) {
         return this.getReceiveBufferSize();
      } else if (var1 == ChannelOption.SO_SNDBUF) {
         return this.getSendBufferSize();
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         return this.isReuseAddress();
      } else if (var1 == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
         return this.isLoopbackModeDisabled();
      } else if (var1 == ChannelOption.IP_MULTICAST_ADDR) {
         return this.getInterface();
      } else if (var1 == ChannelOption.IP_MULTICAST_IF) {
         return this.getNetworkInterface();
      } else if (var1 == ChannelOption.IP_MULTICAST_TTL) {
         return this.getTimeToLive();
      } else if (var1 == ChannelOption.IP_TOS) {
         return this.getTrafficClass();
      } else if (var1 == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
         return this.activeOnOpen;
      } else {
         return var1 == EpollChannelOption.SO_REUSEPORT ? this.isReusePort() : super.getOption(var1);
      }
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == ChannelOption.SO_BROADCAST) {
         this.setBroadcast((Boolean)var2);
      } else if (var1 == ChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize((Integer)var2);
      } else if (var1 == ChannelOption.SO_SNDBUF) {
         this.setSendBufferSize((Integer)var2);
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         this.setReuseAddress((Boolean)var2);
      } else if (var1 == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
         this.setLoopbackModeDisabled((Boolean)var2);
      } else if (var1 == ChannelOption.IP_MULTICAST_ADDR) {
         this.setInterface((InetAddress)var2);
      } else if (var1 == ChannelOption.IP_MULTICAST_IF) {
         this.setNetworkInterface((NetworkInterface)var2);
      } else if (var1 == ChannelOption.IP_MULTICAST_TTL) {
         this.setTimeToLive((Integer)var2);
      } else if (var1 == ChannelOption.IP_TOS) {
         this.setTrafficClass((Integer)var2);
      } else if (var1 == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
         this.setActiveOnOpen((Boolean)var2);
      } else {
         if (var1 != EpollChannelOption.SO_REUSEPORT) {
            return super.setOption(var1, var2);
         }

         this.setReusePort((Boolean)var2);
      }

      return true;
   }

   private void setActiveOnOpen(boolean var1) {
      if (this.channel.isRegistered()) {
         throw new IllegalStateException("Can only changed before channel was registered");
      } else {
         this.activeOnOpen = var1;
      }
   }

   public EpollDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   public EpollDatagramChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public EpollDatagramChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public EpollDatagramChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public EpollDatagramChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public EpollDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public EpollDatagramChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public EpollDatagramChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public EpollDatagramChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public EpollDatagramChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public int getSendBufferSize() {
      return Native.getSendBufferSize(this.datagramChannel.fd);
   }

   public EpollDatagramChannelConfig setSendBufferSize(int var1) {
      Native.setSendBufferSize(this.datagramChannel.fd, var1);
      return this;
   }

   public int getReceiveBufferSize() {
      return Native.getReceiveBufferSize(this.datagramChannel.fd);
   }

   public EpollDatagramChannelConfig setReceiveBufferSize(int var1) {
      Native.setReceiveBufferSize(this.datagramChannel.fd, var1);
      return this;
   }

   public int getTrafficClass() {
      return Native.getTrafficClass(this.datagramChannel.fd);
   }

   public EpollDatagramChannelConfig setTrafficClass(int var1) {
      Native.setTrafficClass(this.datagramChannel.fd, var1);
      return this;
   }

   public boolean isReuseAddress() {
      return Native.isReuseAddress(this.datagramChannel.fd) == 1;
   }

   public EpollDatagramChannelConfig setReuseAddress(boolean var1) {
      Native.setReuseAddress(this.datagramChannel.fd, var1 ? 1 : 0);
      return this;
   }

   public boolean isBroadcast() {
      return Native.isBroadcast(this.datagramChannel.fd) == 1;
   }

   public EpollDatagramChannelConfig setBroadcast(boolean var1) {
      Native.setBroadcast(this.datagramChannel.fd, var1 ? 1 : 0);
      return this;
   }

   public boolean isLoopbackModeDisabled() {
      return false;
   }

   public DatagramChannelConfig setLoopbackModeDisabled(boolean var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public int getTimeToLive() {
      return -1;
   }

   public EpollDatagramChannelConfig setTimeToLive(int var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public InetAddress getInterface() {
      return null;
   }

   public EpollDatagramChannelConfig setInterface(InetAddress var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public NetworkInterface getNetworkInterface() {
      return null;
   }

   public EpollDatagramChannelConfig setNetworkInterface(NetworkInterface var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public boolean isReusePort() {
      return Native.isReusePort(this.datagramChannel.fd) == 1;
   }

   public EpollDatagramChannelConfig setReusePort(boolean var1) {
      Native.setReusePort(this.datagramChannel.fd, var1 ? 1 : 0);
      return this;
   }

   protected void autoReadCleared() {
      this.datagramChannel.clearEpollIn();
   }

   public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   public ChannelConfig setWriteBufferLowWaterMark(int var1) {
      return this.setWriteBufferLowWaterMark(var1);
   }

   public ChannelConfig setWriteBufferHighWaterMark(int var1) {
      return this.setWriteBufferHighWaterMark(var1);
   }

   public ChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   public ChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   public ChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   public ChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   public ChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   public ChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }

   public DatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   public DatagramChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   public DatagramChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   public DatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   public DatagramChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   public DatagramChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }

   public DatagramChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   public DatagramChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   public DatagramChannelConfig setNetworkInterface(NetworkInterface var1) {
      return this.setNetworkInterface(var1);
   }

   public DatagramChannelConfig setInterface(InetAddress var1) {
      return this.setInterface(var1);
   }

   public DatagramChannelConfig setTimeToLive(int var1) {
      return this.setTimeToLive(var1);
   }

   public DatagramChannelConfig setBroadcast(boolean var1) {
      return this.setBroadcast(var1);
   }

   public DatagramChannelConfig setReuseAddress(boolean var1) {
      return this.setReuseAddress(var1);
   }

   public DatagramChannelConfig setTrafficClass(int var1) {
      return this.setTrafficClass(var1);
   }

   public DatagramChannelConfig setReceiveBufferSize(int var1) {
      return this.setReceiveBufferSize(var1);
   }

   public DatagramChannelConfig setSendBufferSize(int var1) {
      return this.setSendBufferSize(var1);
   }
}
