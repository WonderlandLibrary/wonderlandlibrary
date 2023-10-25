package io.netty.channel.epoll;

import io.netty.channel.ChannelOption;

public final class EpollChannelOption<T> extends ChannelOption<T> {
  public static final ChannelOption<Boolean> TCP_CORK = valueOf("TCP_CORK");
  
  public static final ChannelOption<Integer> TCP_KEEPIDLE = valueOf("TCP_KEEPIDLE");
  
  public static final ChannelOption<Integer> TCP_KEEPINTVL = valueOf("TCP_KEEPINTVL");
  
  public static final ChannelOption<Integer> TCP_KEEPCNT = valueOf("TCP_KEEPCNT");
  
  public static final ChannelOption<Boolean> SO_REUSEPORT = valueOf("SO_REUSEPORT");
  
  private EpollChannelOption(String name) {
    super(name);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\epoll\EpollChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */