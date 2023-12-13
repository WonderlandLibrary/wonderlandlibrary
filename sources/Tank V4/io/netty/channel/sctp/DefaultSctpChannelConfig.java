package io.netty.channel.sctp;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import com.sun.nio.sctp.SctpStandardSocketOptions.InitMaxStreams;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.util.Map;

public class DefaultSctpChannelConfig extends DefaultChannelConfig implements SctpChannelConfig {
   private final com.sun.nio.sctp.SctpChannel javaChannel;

   public DefaultSctpChannelConfig(SctpChannel var1, com.sun.nio.sctp.SctpChannel var2) {
      super(var1);
      if (var2 == null) {
         throw new NullPointerException("javaChannel");
      } else {
         this.javaChannel = var2;
         if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            try {
               this.setSctpNoDelay(true);
            } catch (Exception var4) {
            }
         }

      }
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{SctpChannelOption.SO_RCVBUF, SctpChannelOption.SO_SNDBUF, SctpChannelOption.SCTP_NODELAY, SctpChannelOption.SCTP_INIT_MAXSTREAMS});
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == SctpChannelOption.SO_RCVBUF) {
         return this.getReceiveBufferSize();
      } else if (var1 == SctpChannelOption.SO_SNDBUF) {
         return this.getSendBufferSize();
      } else {
         return var1 == SctpChannelOption.SCTP_NODELAY ? this.isSctpNoDelay() : super.getOption(var1);
      }
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == SctpChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize((Integer)var2);
      } else if (var1 == SctpChannelOption.SO_SNDBUF) {
         this.setSendBufferSize((Integer)var2);
      } else if (var1 == SctpChannelOption.SCTP_NODELAY) {
         this.setSctpNoDelay((Boolean)var2);
      } else {
         if (var1 != SctpChannelOption.SCTP_INIT_MAXSTREAMS) {
            return super.setOption(var1, var2);
         }

         this.setInitMaxStreams((InitMaxStreams)var2);
      }

      return true;
   }

   public boolean isSctpNoDelay() {
      try {
         return (Boolean)this.javaChannel.getOption(SctpStandardSocketOptions.SCTP_NODELAY);
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   public SctpChannelConfig setSctpNoDelay(boolean var1) {
      try {
         this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_NODELAY, var1);
         return this;
      } catch (IOException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getSendBufferSize() {
      try {
         return (Integer)this.javaChannel.getOption(SctpStandardSocketOptions.SO_SNDBUF);
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   public SctpChannelConfig setSendBufferSize(int var1) {
      try {
         this.javaChannel.setOption(SctpStandardSocketOptions.SO_SNDBUF, var1);
         return this;
      } catch (IOException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getReceiveBufferSize() {
      try {
         return (Integer)this.javaChannel.getOption(SctpStandardSocketOptions.SO_RCVBUF);
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   public SctpChannelConfig setReceiveBufferSize(int var1) {
      try {
         this.javaChannel.setOption(SctpStandardSocketOptions.SO_RCVBUF, var1);
         return this;
      } catch (IOException var3) {
         throw new ChannelException(var3);
      }
   }

   public InitMaxStreams getInitMaxStreams() {
      try {
         return (InitMaxStreams)this.javaChannel.getOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS);
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   public SctpChannelConfig setInitMaxStreams(InitMaxStreams var1) {
      try {
         this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS, var1);
         return this;
      } catch (IOException var3) {
         throw new ChannelException(var3);
      }
   }

   public SctpChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public SctpChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public SctpChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public SctpChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public SctpChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public SctpChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public SctpChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public SctpChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public SctpChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public SctpChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
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
