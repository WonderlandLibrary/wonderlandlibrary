package io.netty.channel.oio;

import io.netty.channel.ThreadPerChannelEventLoopGroup;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class OioEventLoopGroup extends ThreadPerChannelEventLoopGroup {
  public OioEventLoopGroup() {
    this(0);
  }
  
  public OioEventLoopGroup(int maxChannels) {
    this(maxChannels, Executors.defaultThreadFactory());
  }
  
  public OioEventLoopGroup(int maxChannels, ThreadFactory threadFactory) {
    super(maxChannels, threadFactory, new Object[0]);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\oio\OioEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */