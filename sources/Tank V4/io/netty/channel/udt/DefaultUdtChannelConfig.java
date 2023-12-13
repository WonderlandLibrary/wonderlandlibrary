package io.netty.channel.udt;

import com.barchart.udt.OptionUDT;
import com.barchart.udt.SocketUDT;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import java.io.IOException;
import java.util.Map;

public class DefaultUdtChannelConfig extends DefaultChannelConfig implements UdtChannelConfig {
   private static final int K = 1024;
   private static final int M = 1048576;
   private volatile int protocolReceiveBuferSize = 10485760;
   private volatile int protocolSendBuferSize = 10485760;
   private volatile int systemReceiveBufferSize = 1048576;
   private volatile int systemSendBuferSize = 1048576;
   private volatile int allocatorReceiveBufferSize = 131072;
   private volatile int allocatorSendBufferSize = 131072;
   private volatile int soLinger;
   private volatile boolean reuseAddress = true;

   public DefaultUdtChannelConfig(UdtChannel var1, ChannelUDT var2, boolean var3) throws IOException {
      super(var1);
      if (var3) {
         this.apply(var2);
      }

   }

   protected void apply(ChannelUDT var1) throws IOException {
      SocketUDT var2 = var1.socketUDT();
      var2.setReuseAddress(this.isReuseAddress());
      var2.setSendBufferSize(this.getSendBufferSize());
      if (this.getSoLinger() <= 0) {
         var2.setSoLinger(false, 0);
      } else {
         var2.setSoLinger(true, this.getSoLinger());
      }

      var2.setOption(OptionUDT.Protocol_Receive_Buffer_Size, this.getProtocolReceiveBufferSize());
      var2.setOption(OptionUDT.Protocol_Send_Buffer_Size, this.getProtocolSendBufferSize());
      var2.setOption(OptionUDT.System_Receive_Buffer_Size, this.getSystemReceiveBufferSize());
      var2.setOption(OptionUDT.System_Send_Buffer_Size, this.getSystemSendBufferSize());
   }

   public int getProtocolReceiveBufferSize() {
      return this.protocolReceiveBuferSize;
   }

   public Object getOption(ChannelOption var1) {
      if (var1 == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
         return this.getProtocolReceiveBufferSize();
      } else if (var1 == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
         return this.getProtocolSendBufferSize();
      } else if (var1 == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
         return this.getSystemReceiveBufferSize();
      } else if (var1 == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
         return this.getSystemSendBufferSize();
      } else if (var1 == UdtChannelOption.SO_RCVBUF) {
         return this.getReceiveBufferSize();
      } else if (var1 == UdtChannelOption.SO_SNDBUF) {
         return this.getSendBufferSize();
      } else if (var1 == UdtChannelOption.SO_REUSEADDR) {
         return this.isReuseAddress();
      } else {
         return var1 == UdtChannelOption.SO_LINGER ? this.getSoLinger() : super.getOption(var1);
      }
   }

   public Map getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE, UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE, UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE, UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE, UdtChannelOption.SO_RCVBUF, UdtChannelOption.SO_SNDBUF, UdtChannelOption.SO_REUSEADDR, UdtChannelOption.SO_LINGER});
   }

   public int getReceiveBufferSize() {
      return this.allocatorReceiveBufferSize;
   }

   public int getSendBufferSize() {
      return this.allocatorSendBufferSize;
   }

   public int getSoLinger() {
      return this.soLinger;
   }

   public boolean isReuseAddress() {
      return this.reuseAddress;
   }

   public UdtChannelConfig setProtocolReceiveBufferSize(int var1) {
      this.protocolReceiveBuferSize = var1;
      return this;
   }

   public boolean setOption(ChannelOption var1, Object var2) {
      this.validate(var1, var2);
      if (var1 == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
         this.setProtocolReceiveBufferSize((Integer)var2);
      } else if (var1 == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
         this.setProtocolSendBufferSize((Integer)var2);
      } else if (var1 == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
         this.setSystemReceiveBufferSize((Integer)var2);
      } else if (var1 == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
         this.setSystemSendBufferSize((Integer)var2);
      } else if (var1 == UdtChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize((Integer)var2);
      } else if (var1 == UdtChannelOption.SO_SNDBUF) {
         this.setSendBufferSize((Integer)var2);
      } else if (var1 == UdtChannelOption.SO_REUSEADDR) {
         this.setReuseAddress((Boolean)var2);
      } else {
         if (var1 != UdtChannelOption.SO_LINGER) {
            return super.setOption(var1, var2);
         }

         this.setSoLinger((Integer)var2);
      }

      return true;
   }

   public UdtChannelConfig setReceiveBufferSize(int var1) {
      this.allocatorReceiveBufferSize = var1;
      return this;
   }

   public UdtChannelConfig setReuseAddress(boolean var1) {
      this.reuseAddress = var1;
      return this;
   }

   public UdtChannelConfig setSendBufferSize(int var1) {
      this.allocatorSendBufferSize = var1;
      return this;
   }

   public UdtChannelConfig setSoLinger(int var1) {
      this.soLinger = var1;
      return this;
   }

   public int getSystemReceiveBufferSize() {
      return this.systemReceiveBufferSize;
   }

   public UdtChannelConfig setSystemSendBufferSize(int var1) {
      this.systemReceiveBufferSize = var1;
      return this;
   }

   public int getProtocolSendBufferSize() {
      return this.protocolSendBuferSize;
   }

   public UdtChannelConfig setProtocolSendBufferSize(int var1) {
      this.protocolSendBuferSize = var1;
      return this;
   }

   public UdtChannelConfig setSystemReceiveBufferSize(int var1) {
      this.systemSendBuferSize = var1;
      return this;
   }

   public int getSystemSendBufferSize() {
      return this.systemSendBuferSize;
   }

   public UdtChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public UdtChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public UdtChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public UdtChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public UdtChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public UdtChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public UdtChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public UdtChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
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
