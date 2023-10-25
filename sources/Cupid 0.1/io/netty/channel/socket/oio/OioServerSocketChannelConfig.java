package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ServerSocketChannelConfig;

public interface OioServerSocketChannelConfig extends ServerSocketChannelConfig {
  OioServerSocketChannelConfig setSoTimeout(int paramInt);
  
  int getSoTimeout();
  
  OioServerSocketChannelConfig setBacklog(int paramInt);
  
  OioServerSocketChannelConfig setReuseAddress(boolean paramBoolean);
  
  OioServerSocketChannelConfig setReceiveBufferSize(int paramInt);
  
  OioServerSocketChannelConfig setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3);
  
  OioServerSocketChannelConfig setConnectTimeoutMillis(int paramInt);
  
  OioServerSocketChannelConfig setMaxMessagesPerRead(int paramInt);
  
  OioServerSocketChannelConfig setWriteSpinCount(int paramInt);
  
  OioServerSocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  OioServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  OioServerSocketChannelConfig setAutoRead(boolean paramBoolean);
  
  OioServerSocketChannelConfig setAutoClose(boolean paramBoolean);
  
  OioServerSocketChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  OioServerSocketChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  OioServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\socket\oio\OioServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */