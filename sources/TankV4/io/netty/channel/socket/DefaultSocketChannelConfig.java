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

   public DefaultSocketChannelConfig(SocketChannel var1, Socket var2) {
      super(var1);
      if (var2 == null) {
         throw new NullPointerException("javaSocket");
      } else {
         this.javaSocket = var2;
         if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            try {
               this.setTcpNoDelay(true);
            } catch (Exception var4) {
            }
         }

      }
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE});
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
      } else {
         return var1 == ChannelOption.ALLOW_HALF_CLOSURE ? this.isAllowHalfClosure() : super.getOption(var1);
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
      } else {
         if (var1 != ChannelOption.ALLOW_HALF_CLOSURE) {
            return super.setOption(var1, var2);
         }

         this.setAllowHalfClosure((Boolean)var2);
      }

      return true;
   }

   public int getReceiveBufferSize() {
      try {
         return this.javaSocket.getReceiveBufferSize();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public int getSendBufferSize() {
      try {
         return this.javaSocket.getSendBufferSize();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public int getSoLinger() {
      try {
         return this.javaSocket.getSoLinger();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public int getTrafficClass() {
      try {
         return this.javaSocket.getTrafficClass();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public boolean isKeepAlive() {
      try {
         return this.javaSocket.getKeepAlive();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public boolean isReuseAddress() {
      try {
         return this.javaSocket.getReuseAddress();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public boolean isTcpNoDelay() {
      try {
         return this.javaSocket.getTcpNoDelay();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public SocketChannelConfig setKeepAlive(boolean var1) {
      try {
         this.javaSocket.setKeepAlive(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public SocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      this.javaSocket.setPerformancePreferences(var1, var2, var3);
      return this;
   }

   public SocketChannelConfig setReceiveBufferSize(int var1) {
      try {
         this.javaSocket.setReceiveBufferSize(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public SocketChannelConfig setReuseAddress(boolean var1) {
      try {
         this.javaSocket.setReuseAddress(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public SocketChannelConfig setSendBufferSize(int var1) {
      try {
         this.javaSocket.setSendBufferSize(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public SocketChannelConfig setSoLinger(int var1) {
      try {
         if (var1 < 0) {
            this.javaSocket.setSoLinger(false, 0);
         } else {
            this.javaSocket.setSoLinger(true, var1);
         }

         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public SocketChannelConfig setTcpNoDelay(boolean var1) {
      try {
         this.javaSocket.setTcpNoDelay(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public SocketChannelConfig setTrafficClass(int var1) {
      try {
         this.javaSocket.setTrafficClass(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public boolean isAllowHalfClosure() {
      return this.allowHalfClosure;
   }

   public SocketChannelConfig setAllowHalfClosure(boolean var1) {
      this.allowHalfClosure = var1;
      return this;
   }

   public SocketChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public SocketChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public SocketChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public SocketChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public SocketChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public SocketChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public SocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public SocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
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
