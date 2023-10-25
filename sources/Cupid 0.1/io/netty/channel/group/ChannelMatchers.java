package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;

public final class ChannelMatchers {
  private static final ChannelMatcher ALL_MATCHER = new ChannelMatcher() {
      public boolean matches(Channel channel) {
        return true;
      }
    };
  
  private static final ChannelMatcher SERVER_CHANNEL_MATCHER = isInstanceOf((Class)ServerChannel.class);
  
  private static final ChannelMatcher NON_SERVER_CHANNEL_MATCHER = isNotInstanceOf((Class)ServerChannel.class);
  
  public static ChannelMatcher all() {
    return ALL_MATCHER;
  }
  
  public static ChannelMatcher isNot(Channel channel) {
    return invert(is(channel));
  }
  
  public static ChannelMatcher is(Channel channel) {
    return new InstanceMatcher(channel);
  }
  
  public static ChannelMatcher isInstanceOf(Class<? extends Channel> clazz) {
    return new ClassMatcher(clazz);
  }
  
  public static ChannelMatcher isNotInstanceOf(Class<? extends Channel> clazz) {
    return invert(isInstanceOf(clazz));
  }
  
  public static ChannelMatcher isServerChannel() {
    return SERVER_CHANNEL_MATCHER;
  }
  
  public static ChannelMatcher isNonServerChannel() {
    return NON_SERVER_CHANNEL_MATCHER;
  }
  
  public static ChannelMatcher invert(ChannelMatcher matcher) {
    return new InvertMatcher(matcher);
  }
  
  public static ChannelMatcher compose(ChannelMatcher... matchers) {
    if (matchers.length < 1)
      throw new IllegalArgumentException("matchers must at least contain one element"); 
    if (matchers.length == 1)
      return matchers[0]; 
    return new CompositeMatcher(matchers);
  }
  
  private static final class CompositeMatcher implements ChannelMatcher {
    private final ChannelMatcher[] matchers;
    
    CompositeMatcher(ChannelMatcher... matchers) {
      this.matchers = matchers;
    }
    
    public boolean matches(Channel channel) {
      for (ChannelMatcher m : this.matchers) {
        if (!m.matches(channel))
          return false; 
      } 
      return true;
    }
  }
  
  private static final class InvertMatcher implements ChannelMatcher {
    private final ChannelMatcher matcher;
    
    InvertMatcher(ChannelMatcher matcher) {
      this.matcher = matcher;
    }
    
    public boolean matches(Channel channel) {
      return !this.matcher.matches(channel);
    }
  }
  
  private static final class InstanceMatcher implements ChannelMatcher {
    private final Channel channel;
    
    InstanceMatcher(Channel channel) {
      this.channel = channel;
    }
    
    public boolean matches(Channel ch) {
      return (this.channel == ch);
    }
  }
  
  private static final class ClassMatcher implements ChannelMatcher {
    private final Class<? extends Channel> clazz;
    
    ClassMatcher(Class<? extends Channel> clazz) {
      this.clazz = clazz;
    }
    
    public boolean matches(Channel ch) {
      return this.clazz.isInstance(ch);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\group\ChannelMatchers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */