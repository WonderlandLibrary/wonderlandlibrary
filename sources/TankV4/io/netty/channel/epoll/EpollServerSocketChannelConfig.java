package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.util.NetUtil;
import java.util.Map;

public final class EpollServerSocketChannelConfig extends DefaultChannelConfig implements ServerSocketChannelConfig {
   private final EpollServerSocketChannel channel;
   private volatile int backlog;

   EpollServerSocketChannelConfig(EpollServerSocketChannel var1) {
      super(var1);
      this.backlog = NetUtil.SOMAXCONN;
      this.channel = var1;
      this.setReuseAddress(true);
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG, EpollChannelOption.SO_REUSEPORT});
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == ChannelOption.SO_RCVBUF) {
         return this.getReceiveBufferSize();
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         return this.isReuseAddress();
      } else if (var1 == ChannelOption.SO_BACKLOG) {
         return this.getBacklog();
      } else {
         return var1 == EpollChannelOption.SO_REUSEPORT ? this.isReusePort() : super.getOption(var1);
      }
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == ChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize((Integer)var2);
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         this.setReuseAddress((Boolean)var2);
      } else if (var1 == ChannelOption.SO_BACKLOG) {
         this.setBacklog((Integer)var2);
      } else {
         if (var1 != EpollChannelOption.SO_REUSEPORT) {
            return super.setOption(var1, var2);
         }

         this.setReusePort((Boolean)var2);
      }

      return true;
   }

   public boolean isReuseAddress() {
      return Native.isReuseAddress(this.channel.fd) == 1;
   }

   public EpollServerSocketChannelConfig setReuseAddress(boolean var1) {
      Native.setReuseAddress(this.channel.fd, var1 ? 1 : 0);
      return this;
   }

   public int getReceiveBufferSize() {
      return Native.getReceiveBufferSize(this.channel.fd);
   }

   public EpollServerSocketChannelConfig setReceiveBufferSize(int var1) {
      Native.setReceiveBufferSize(this.channel.fd, var1);
      return this;
   }

   public EpollServerSocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      return this;
   }

   public int getBacklog() {
      return this.backlog;
   }

   public EpollServerSocketChannelConfig setBacklog(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("backlog: " + var1);
      } else {
         this.backlog = var1;
         return this;
      }
   }

   public EpollServerSocketChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public EpollServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   public boolean isReusePort() {
      return Native.isReusePort(this.channel.fd) == 1;
   }

   public EpollServerSocketChannelConfig setReusePort(boolean var1) {
      Native.setReusePort(this.channel.fd, var1 ? 1 : 0);
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

   public ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   public ServerSocketChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   public ServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   public ServerSocketChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   public ServerSocketChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   public ServerSocketChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   public ServerSocketChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }

   public ServerSocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      return this.setPerformancePreferences(var1, var2, var3);
   }

   public ServerSocketChannelConfig setReceiveBufferSize(int var1) {
      return this.setReceiveBufferSize(var1);
   }

   public ServerSocketChannelConfig setReuseAddress(boolean var1) {
      return this.setReuseAddress(var1);
   }

   public ServerSocketChannelConfig setBacklog(int var1) {
      return this.setBacklog(var1);
   }
}
