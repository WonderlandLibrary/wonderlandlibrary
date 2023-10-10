package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.NetUtil;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Map;

public class DefaultServerSocketChannelConfig extends DefaultChannelConfig implements ServerSocketChannelConfig {
   protected final ServerSocket javaSocket;
   private volatile int backlog;

   public DefaultServerSocketChannelConfig(ServerSocketChannel var1, ServerSocket var2) {
      super(var1);
      this.backlog = NetUtil.SOMAXCONN;
      if (var2 == null) {
         throw new NullPointerException("javaSocket");
      } else {
         this.javaSocket = var2;
      }
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG});
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == ChannelOption.SO_RCVBUF) {
         return this.getReceiveBufferSize();
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         return this.isReuseAddress();
      } else {
         return var1 == ChannelOption.SO_BACKLOG ? this.getBacklog() : super.getOption(var1);
      }
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == ChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize((Integer)var2);
      } else if (var1 == ChannelOption.SO_REUSEADDR) {
         this.setReuseAddress((Boolean)var2);
      } else {
         if (var1 != ChannelOption.SO_BACKLOG) {
            return super.setOption(var1, var2);
         }

         this.setBacklog((Integer)var2);
      }

      return true;
   }

   public boolean isReuseAddress() {
      try {
         return this.javaSocket.getReuseAddress();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public ServerSocketChannelConfig setReuseAddress(boolean var1) {
      try {
         this.javaSocket.setReuseAddress(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getReceiveBufferSize() {
      try {
         return this.javaSocket.getReceiveBufferSize();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public ServerSocketChannelConfig setReceiveBufferSize(int var1) {
      try {
         this.javaSocket.setReceiveBufferSize(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public ServerSocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      this.javaSocket.setPerformancePreferences(var1, var2, var3);
      return this;
   }

   public int getBacklog() {
      return this.backlog;
   }

   public ServerSocketChannelConfig setBacklog(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("backlog: " + var1);
      } else {
         this.backlog = var1;
         return this;
      }
   }

   public ServerSocketChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public ServerSocketChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public ServerSocketChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public ServerSocketChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public ServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public ServerSocketChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public ServerSocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public ServerSocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
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
}
