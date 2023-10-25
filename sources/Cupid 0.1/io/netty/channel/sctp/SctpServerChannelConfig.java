package io.netty.channel.sctp;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public interface SctpServerChannelConfig extends ChannelConfig {
  int getBacklog();
  
  SctpServerChannelConfig setBacklog(int paramInt);
  
  int getSendBufferSize();
  
  SctpServerChannelConfig setSendBufferSize(int paramInt);
  
  int getReceiveBufferSize();
  
  SctpServerChannelConfig setReceiveBufferSize(int paramInt);
  
  SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();
  
  SctpServerChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams paramInitMaxStreams);
  
  SctpServerChannelConfig setMaxMessagesPerRead(int paramInt);
  
  SctpServerChannelConfig setWriteSpinCount(int paramInt);
  
  SctpServerChannelConfig setConnectTimeoutMillis(int paramInt);
  
  SctpServerChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  SctpServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  SctpServerChannelConfig setAutoRead(boolean paramBoolean);
  
  SctpServerChannelConfig setAutoClose(boolean paramBoolean);
  
  SctpServerChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  SctpServerChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  SctpServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\sctp\SctpServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */