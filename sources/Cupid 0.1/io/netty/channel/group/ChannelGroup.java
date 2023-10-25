package io.netty.channel.group;

import io.netty.channel.Channel;
import java.util.Set;

public interface ChannelGroup extends Set<Channel>, Comparable<ChannelGroup> {
  String name();
  
  ChannelGroupFuture write(Object paramObject);
  
  ChannelGroupFuture write(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  ChannelGroup flush();
  
  ChannelGroup flush(ChannelMatcher paramChannelMatcher);
  
  ChannelGroupFuture writeAndFlush(Object paramObject);
  
  @Deprecated
  ChannelGroupFuture flushAndWrite(Object paramObject);
  
  ChannelGroupFuture writeAndFlush(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  @Deprecated
  ChannelGroupFuture flushAndWrite(Object paramObject, ChannelMatcher paramChannelMatcher);
  
  ChannelGroupFuture disconnect();
  
  ChannelGroupFuture disconnect(ChannelMatcher paramChannelMatcher);
  
  ChannelGroupFuture close();
  
  ChannelGroupFuture close(ChannelMatcher paramChannelMatcher);
  
  @Deprecated
  ChannelGroupFuture deregister();
  
  @Deprecated
  ChannelGroupFuture deregister(ChannelMatcher paramChannelMatcher);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\group\ChannelGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */