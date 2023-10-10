package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.DefaultServerSocketChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

public class DefaultOioServerSocketChannelConfig extends DefaultServerSocketChannelConfig implements OioServerSocketChannelConfig {
   public DefaultOioServerSocketChannelConfig(ServerSocketChannel var1, ServerSocket var2) {
      super(var1, var2);
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_TIMEOUT});
   }

   public Object getOption(ChannelOption var1) {
      return var1 == ChannelOption.SO_TIMEOUT ? this.getSoTimeout() : super.getOption(var1);
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == ChannelOption.SO_TIMEOUT) {
         this.setSoTimeout((Integer)var2);
         return true;
      } else {
         return super.setOption(var1, var2);
      }
   }

   public OioServerSocketChannelConfig setSoTimeout(int var1) {
      try {
         this.javaSocket.setSoTimeout(var1);
         return this;
      } catch (IOException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getSoTimeout() {
      try {
         return this.javaSocket.getSoTimeout();
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   public OioServerSocketChannelConfig setBacklog(int var1) {
      super.setBacklog(var1);
      return this;
   }

   public OioServerSocketChannelConfig setReuseAddress(boolean var1) {
      super.setReuseAddress(var1);
      return this;
   }

   public OioServerSocketChannelConfig setReceiveBufferSize(int var1) {
      super.setReceiveBufferSize(var1);
      return this;
   }

   public OioServerSocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      super.setPerformancePreferences(var1, var2, var3);
      return this;
   }

   public OioServerSocketChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public OioServerSocketChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public OioServerSocketChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public OioServerSocketChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public OioServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public OioServerSocketChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public OioServerSocketChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public OioServerSocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public OioServerSocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public OioServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   public ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   public ServerSocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      return this.setWriteBufferLowWaterMark(var1);
   }

   public ServerSocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      return this.setWriteBufferHighWaterMark(var1);
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

   public ServerSocketChannelConfig setBacklog(int var1) {
      return this.setBacklog(var1);
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
}
