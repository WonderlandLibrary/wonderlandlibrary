package io.netty.channel.udt;

import com.barchart.udt.nio.ChannelUDT;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import java.io.IOException;
import java.util.Map;

public class DefaultUdtServerChannelConfig extends DefaultUdtChannelConfig implements UdtServerChannelConfig {
   private volatile int backlog = 64;

   public DefaultUdtServerChannelConfig(UdtChannel var1, ChannelUDT var2, boolean var3) throws IOException {
      super(var1, var2, var3);
      if (var3) {
         this.apply(var2);
      }

   }

   protected void apply(ChannelUDT var1) throws IOException {
   }

   public int getBacklog() {
      return this.backlog;
   }

   public Object getOption(ChannelOption var1) {
      return var1 == ChannelOption.SO_BACKLOG ? this.getBacklog() : super.getOption(var1);
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_BACKLOG});
   }

   public UdtServerChannelConfig setBacklog(int var1) {
      this.backlog = var1;
      return this;
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == ChannelOption.SO_BACKLOG) {
         this.setBacklog((Integer)var2);
         return true;
      } else {
         return super.setOption(var1, var2);
      }
   }

   public UdtServerChannelConfig setProtocolReceiveBufferSize(int var1) {
      super.setProtocolReceiveBufferSize(var1);
      return this;
   }

   public UdtServerChannelConfig setProtocolSendBufferSize(int var1) {
      super.setProtocolSendBufferSize(var1);
      return this;
   }

   public UdtServerChannelConfig setReceiveBufferSize(int var1) {
      super.setReceiveBufferSize(var1);
      return this;
   }

   public UdtServerChannelConfig setReuseAddress(boolean var1) {
      super.setReuseAddress(var1);
      return this;
   }

   public UdtServerChannelConfig setSendBufferSize(int var1) {
      super.setSendBufferSize(var1);
      return this;
   }

   public UdtServerChannelConfig setSoLinger(int var1) {
      super.setSoLinger(var1);
      return this;
   }

   public UdtServerChannelConfig setSystemReceiveBufferSize(int var1) {
      super.setSystemReceiveBufferSize(var1);
      return this;
   }

   public UdtServerChannelConfig setSystemSendBufferSize(int var1) {
      super.setSystemSendBufferSize(var1);
      return this;
   }

   public UdtServerChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public UdtServerChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public UdtServerChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public UdtServerChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public UdtServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public UdtServerChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public UdtServerChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public UdtServerChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public UdtServerChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public UdtServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   public UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   public UdtChannelConfig setWriteBufferHighWaterMark(int var1) {
      return this.setWriteBufferHighWaterMark(var1);
   }

   public UdtChannelConfig setWriteBufferLowWaterMark(int var1) {
      return this.setWriteBufferLowWaterMark(var1);
   }

   public UdtChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   public UdtChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   public UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   public UdtChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   public UdtChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   public UdtChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   public UdtChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }

   public UdtChannelConfig setSystemReceiveBufferSize(int var1) {
      return this.setSystemReceiveBufferSize(var1);
   }

   public UdtChannelConfig setProtocolSendBufferSize(int var1) {
      return this.setProtocolSendBufferSize(var1);
   }

   public UdtChannelConfig setSystemSendBufferSize(int var1) {
      return this.setSystemSendBufferSize(var1);
   }

   public UdtChannelConfig setSoLinger(int var1) {
      return this.setSoLinger(var1);
   }

   public UdtChannelConfig setSendBufferSize(int var1) {
      return this.setSendBufferSize(var1);
   }

   public UdtChannelConfig setReuseAddress(boolean var1) {
      return this.setReuseAddress(var1);
   }

   public UdtChannelConfig setReceiveBufferSize(int var1) {
      return this.setReceiveBufferSize(var1);
   }

   public UdtChannelConfig setProtocolReceiveBufferSize(int var1) {
      return this.setProtocolReceiveBufferSize(var1);
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
