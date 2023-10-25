package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.util.Map;

public interface ChannelConfig {
  Map<ChannelOption<?>, Object> getOptions();
  
  boolean setOptions(Map<ChannelOption<?>, ?> paramMap);
  
  <T> T getOption(ChannelOption<T> paramChannelOption);
  
  <T> boolean setOption(ChannelOption<T> paramChannelOption, T paramT);
  
  int getConnectTimeoutMillis();
  
  ChannelConfig setConnectTimeoutMillis(int paramInt);
  
  int getMaxMessagesPerRead();
  
  ChannelConfig setMaxMessagesPerRead(int paramInt);
  
  int getWriteSpinCount();
  
  ChannelConfig setWriteSpinCount(int paramInt);
  
  ByteBufAllocator getAllocator();
  
  ChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  RecvByteBufAllocator getRecvByteBufAllocator();
  
  ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  boolean isAutoRead();
  
  ChannelConfig setAutoRead(boolean paramBoolean);
  
  @Deprecated
  boolean isAutoClose();
  
  @Deprecated
  ChannelConfig setAutoClose(boolean paramBoolean);
  
  int getWriteBufferHighWaterMark();
  
  ChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  int getWriteBufferLowWaterMark();
  
  ChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  MessageSizeEstimator getMessageSizeEstimator();
  
  ChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */