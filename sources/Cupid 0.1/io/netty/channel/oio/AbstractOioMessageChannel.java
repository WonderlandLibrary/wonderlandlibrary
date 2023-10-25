package io.netty.channel.oio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOioMessageChannel extends AbstractOioChannel {
  private final List<Object> readBuf = new ArrayList();
  
  protected AbstractOioMessageChannel(Channel parent) {
    super(parent);
  }
  
  protected void doRead() {
    ChannelConfig config = config();
    ChannelPipeline pipeline = pipeline();
    boolean closed = false;
    int maxMessagesPerRead = config.getMaxMessagesPerRead();
    Throwable exception = null;
    int localRead = 0;
    try {
      do {
        localRead = doReadMessages(this.readBuf);
        if (localRead == 0)
          break; 
        if (localRead < 0) {
          closed = true;
          break;
        } 
      } while (this.readBuf.size() < maxMessagesPerRead && config.isAutoRead());
    } catch (Throwable t) {
      exception = t;
    } 
    int size = this.readBuf.size();
    for (int i = 0; i < size; i++)
      pipeline.fireChannelRead(this.readBuf.get(i)); 
    this.readBuf.clear();
    pipeline.fireChannelReadComplete();
    if (exception != null) {
      if (exception instanceof java.io.IOException)
        closed = true; 
      pipeline().fireExceptionCaught(exception);
    } 
    if (closed) {
      if (isOpen())
        unsafe().close(unsafe().voidPromise()); 
    } else if (localRead == 0 && isActive()) {
      read();
    } 
  }
  
  protected abstract int doReadMessages(List<Object> paramList) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\oio\AbstractOioMessageChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */