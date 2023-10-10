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
import io.netty.util.NetUtil;
import java.io.IOException;
import java.util.Map;

public class DefaultSctpServerChannelConfig extends DefaultChannelConfig implements SctpServerChannelConfig {
   private final com.sun.nio.sctp.SctpServerChannel javaChannel;
   private volatile int backlog;

   public DefaultSctpServerChannelConfig(SctpServerChannel var1, com.sun.nio.sctp.SctpServerChannel var2) {
      super(var1);
      this.backlog = NetUtil.SOMAXCONN;
      if (var2 == null) {
         throw new NullPointerException("javaChannel");
      } else {
         this.javaChannel = var2;
      }
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, SctpChannelOption.SCTP_INIT_MAXSTREAMS});
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == ChannelOption.SO_RCVBUF) {
         return this.getReceiveBufferSize();
      } else {
         return var1 == ChannelOption.SO_SNDBUF ? this.getSendBufferSize() : super.getOption(var1);
      }
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == ChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize((Integer)var2);
      } else if (var1 == ChannelOption.SO_SNDBUF) {
         this.setSendBufferSize((Integer)var2);
      } else {
         if (var1 != SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS) {
            return super.setOption(var1, var2);
         }

         this.setInitMaxStreams((InitMaxStreams)var2);
      }

      return true;
   }

   public int getSendBufferSize() {
      try {
         return (Integer)this.javaChannel.getOption(SctpStandardSocketOptions.SO_SNDBUF);
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   public SctpServerChannelConfig setSendBufferSize(int var1) {
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

   public SctpServerChannelConfig setReceiveBufferSize(int var1) {
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

   public SctpServerChannelConfig setInitMaxStreams(InitMaxStreams var1) {
      try {
         this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS, var1);
         return this;
      } catch (IOException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getBacklog() {
      return this.backlog;
   }

   public SctpServerChannelConfig setBacklog(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("backlog: " + var1);
      } else {
         this.backlog = var1;
         return this;
      }
   }

   public SctpServerChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public SctpServerChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public SctpServerChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public SctpServerChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public SctpServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public SctpServerChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public SctpServerChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public SctpServerChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public SctpServerChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public SctpServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
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
