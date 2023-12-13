package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.util.Map;

public final class EpollSocketChannelConfig extends DefaultChannelConfig implements SocketChannelConfig {
   private final EpollSocketChannel channel;
   private volatile boolean allowHalfClosure;

   EpollSocketChannelConfig(EpollSocketChannel var1) {
      super(var1);
      this.channel = var1;
      if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
         this.setTcpNoDelay(true);
      }

   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL});
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == ChannelOption.SO_RCVBUF) {
         return this.getReceiveBufferSize();
      } else if (var1 == ChannelOption.SO_SNDBUF) {
         return this.getSendBufferSize();
      } else if (var1 == ChannelOption.TCP_NODELAY) {
         return this.isTcpNoDelay();
      } else if (var1 == ChannelOption.SO_KEEPALIVE) {
         return this.isKeepAlive();
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         return this.isReuseAddress();
      } else if (var1 == ChannelOption.SO_LINGER) {
         return this.getSoLinger();
      } else if (var1 == ChannelOption.IP_TOS) {
         return this.getTrafficClass();
      } else if (var1 == ChannelOption.ALLOW_HALF_CLOSURE) {
         return this.isAllowHalfClosure();
      } else if (var1 == EpollChannelOption.TCP_CORK) {
         return this.isTcpCork();
      } else if (var1 == EpollChannelOption.TCP_KEEPIDLE) {
         return this.getTcpKeepIdle();
      } else if (var1 == EpollChannelOption.TCP_KEEPINTVL) {
         return this.getTcpKeepIntvl();
      } else {
         return var1 == EpollChannelOption.TCP_KEEPCNT ? this.getTcpKeepCnt() : super.getOption(var1);
      }
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == ChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize((Integer)var2);
      } else if (var1 == ChannelOption.SO_SNDBUF) {
         this.setSendBufferSize((Integer)var2);
      } else if (var1 == ChannelOption.TCP_NODELAY) {
         this.setTcpNoDelay((Boolean)var2);
      } else if (var1 == ChannelOption.SO_KEEPALIVE) {
         this.setKeepAlive((Boolean)var2);
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         this.setReuseAddress((Boolean)var2);
      } else if (var1 == ChannelOption.SO_LINGER) {
         this.setSoLinger((Integer)var2);
      } else if (var1 == ChannelOption.IP_TOS) {
         this.setTrafficClass((Integer)var2);
      } else if (var1 == ChannelOption.ALLOW_HALF_CLOSURE) {
         this.setAllowHalfClosure((Boolean)var2);
      } else if (var1 == EpollChannelOption.TCP_CORK) {
         this.setTcpCork((Boolean)var2);
      } else if (var1 == EpollChannelOption.TCP_KEEPIDLE) {
         this.setTcpKeepIdle((Integer)var2);
      } else if (var1 == EpollChannelOption.TCP_KEEPCNT) {
         this.setTcpKeepCntl((Integer)var2);
      } else {
         if (var1 != EpollChannelOption.TCP_KEEPINTVL) {
            return super.setOption(var1, var2);
         }

         this.setTcpKeepIntvl((Integer)var2);
      }

      return true;
   }

   public int getReceiveBufferSize() {
      return Native.getReceiveBufferSize(this.channel.fd);
   }

   public int getSendBufferSize() {
      return Native.getSendBufferSize(this.channel.fd);
   }

   public int getSoLinger() {
      return Native.getSoLinger(this.channel.fd);
   }

   public int getTrafficClass() {
      return Native.getTrafficClass(this.channel.fd);
   }

   public boolean isKeepAlive() {
      return Native.isKeepAlive(this.channel.fd) == 1;
   }

   public boolean isReuseAddress() {
      return Native.isReuseAddress(this.channel.fd) == 1;
   }

   public boolean isTcpNoDelay() {
      return Native.isTcpNoDelay(this.channel.fd) == 1;
   }

   public boolean isTcpCork() {
      return Native.isTcpCork(this.channel.fd) == 1;
   }

   public int getTcpKeepIdle() {
      return Native.getTcpKeepIdle(this.channel.fd);
   }

   public int getTcpKeepIntvl() {
      return Native.getTcpKeepIntvl(this.channel.fd);
   }

   public int getTcpKeepCnt() {
      return Native.getTcpKeepCnt(this.channel.fd);
   }

   public EpollSocketChannelConfig setKeepAlive(boolean var1) {
      Native.setKeepAlive(this.channel.fd, var1 ? 1 : 0);
      return this;
   }

   public EpollSocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      return this;
   }

   public EpollSocketChannelConfig setReceiveBufferSize(int var1) {
      Native.setReceiveBufferSize(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setReuseAddress(boolean var1) {
      Native.setReuseAddress(this.channel.fd, var1 ? 1 : 0);
      return this;
   }

   public EpollSocketChannelConfig setSendBufferSize(int var1) {
      Native.setSendBufferSize(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setSoLinger(int var1) {
      Native.setSoLinger(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpNoDelay(boolean var1) {
      Native.setTcpNoDelay(this.channel.fd, var1 ? 1 : 0);
      return this;
   }

   public EpollSocketChannelConfig setTcpCork(boolean var1) {
      Native.setTcpCork(this.channel.fd, var1 ? 1 : 0);
      return this;
   }

   public EpollSocketChannelConfig setTrafficClass(int var1) {
      Native.setTrafficClass(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpKeepIdle(int var1) {
      Native.setTcpKeepIdle(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpKeepIntvl(int var1) {
      Native.setTcpKeepIntvl(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpKeepCntl(int var1) {
      Native.setTcpKeepCnt(this.channel.fd, var1);
      return this;
   }

   public boolean isAllowHalfClosure() {
      return this.allowHalfClosure;
   }

   public EpollSocketChannelConfig setAllowHalfClosure(boolean var1) {
      this.allowHalfClosure = var1;
      return this;
   }

   public EpollSocketChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public EpollSocketChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public EpollSocketChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public EpollSocketChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public EpollSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public EpollSocketChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public EpollSocketChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public EpollSocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public EpollSocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public EpollSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   protected void autoReadCleared() {
      this.channel.clearEpollIn();
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

   public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   public SocketChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   public SocketChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   public SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   public SocketChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   public SocketChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   public SocketChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   public SocketChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }

   public SocketChannelConfig setAllowHalfClosure(boolean var1) {
      return this.setAllowHalfClosure(var1);
   }

   public SocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      return this.setPerformancePreferences(var1, var2, var3);
   }

   public SocketChannelConfig setReuseAddress(boolean var1) {
      return this.setReuseAddress(var1);
   }

   public SocketChannelConfig setTrafficClass(int var1) {
      return this.setTrafficClass(var1);
   }

   public SocketChannelConfig setKeepAlive(boolean var1) {
      return this.setKeepAlive(var1);
   }

   public SocketChannelConfig setReceiveBufferSize(int var1) {
      return this.setReceiveBufferSize(var1);
   }

   public SocketChannelConfig setSendBufferSize(int var1) {
      return this.setSendBufferSize(var1);
   }

   public SocketChannelConfig setSoLinger(int var1) {
      return this.setSoLinger(var1);
   }

   public SocketChannelConfig setTcpNoDelay(boolean var1) {
      return this.setTcpNoDelay(var1);
   }
}
