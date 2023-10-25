package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public interface ServerSocketChannelConfig extends ChannelConfig {
  int getBacklog();
  
  ServerSocketChannelConfig setBacklog(int paramInt);
  
  boolean isReuseAddress();
  
  ServerSocketChannelConfig setReuseAddress(boolean paramBoolean);
  
  int getReceiveBufferSize();
  
  ServerSocketChannelConfig setReceiveBufferSize(int paramInt);
  
  ServerSocketChannelConfig setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3);
  
  ServerSocketChannelConfig setConnectTimeoutMillis(int paramInt);
  
  ServerSocketChannelConfig setMaxMessagesPerRead(int paramInt);
  
  ServerSocketChannelConfig setWriteSpinCount(int paramInt);
  
  ServerSocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  ServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  ServerSocketChannelConfig setAutoRead(boolean paramBoolean);
  
  ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\ServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */