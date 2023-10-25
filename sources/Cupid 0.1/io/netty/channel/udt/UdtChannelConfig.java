package io.netty.channel.udt;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public interface UdtChannelConfig extends ChannelConfig {
  int getProtocolReceiveBufferSize();
  
  int getProtocolSendBufferSize();
  
  int getReceiveBufferSize();
  
  int getSendBufferSize();
  
  int getSoLinger();
  
  int getSystemReceiveBufferSize();
  
  int getSystemSendBufferSize();
  
  boolean isReuseAddress();
  
  UdtChannelConfig setConnectTimeoutMillis(int paramInt);
  
  UdtChannelConfig setMaxMessagesPerRead(int paramInt);
  
  UdtChannelConfig setWriteSpinCount(int paramInt);
  
  UdtChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  UdtChannelConfig setAutoRead(boolean paramBoolean);
  
  UdtChannelConfig setAutoClose(boolean paramBoolean);
  
  UdtChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  UdtChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
  
  UdtChannelConfig setProtocolReceiveBufferSize(int paramInt);
  
  UdtChannelConfig setProtocolSendBufferSize(int paramInt);
  
  UdtChannelConfig setReceiveBufferSize(int paramInt);
  
  UdtChannelConfig setReuseAddress(boolean paramBoolean);
  
  UdtChannelConfig setSendBufferSize(int paramInt);
  
  UdtChannelConfig setSoLinger(int paramInt);
  
  UdtChannelConfig setSystemReceiveBufferSize(int paramInt);
  
  UdtChannelConfig setSystemSendBufferSize(int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channe\\udt\UdtChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */