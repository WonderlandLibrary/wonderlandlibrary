package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.UniqueName;
import io.netty.util.internal.PlatformDependent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ConcurrentMap;

public class ChannelOption<T> extends UniqueName {
  private static final ConcurrentMap<String, Boolean> names = PlatformDependent.newConcurrentHashMap();
  
  public static final ChannelOption<ByteBufAllocator> ALLOCATOR = valueOf("ALLOCATOR");
  
  public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR = valueOf("RCVBUF_ALLOCATOR");
  
  public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR = valueOf("MESSAGE_SIZE_ESTIMATOR");
  
  public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS = valueOf("CONNECT_TIMEOUT_MILLIS");
  
  public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ = valueOf("MAX_MESSAGES_PER_READ");
  
  public static final ChannelOption<Integer> WRITE_SPIN_COUNT = valueOf("WRITE_SPIN_COUNT");
  
  public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK = valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
  
  public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK = valueOf("WRITE_BUFFER_LOW_WATER_MARK");
  
  public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE = valueOf("ALLOW_HALF_CLOSURE");
  
  public static final ChannelOption<Boolean> AUTO_READ = valueOf("AUTO_READ");
  
  @Deprecated
  public static final ChannelOption<Boolean> AUTO_CLOSE = valueOf("AUTO_CLOSE");
  
  public static final ChannelOption<Boolean> SO_BROADCAST = valueOf("SO_BROADCAST");
  
  public static final ChannelOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");
  
  public static final ChannelOption<Integer> SO_SNDBUF = valueOf("SO_SNDBUF");
  
  public static final ChannelOption<Integer> SO_RCVBUF = valueOf("SO_RCVBUF");
  
  public static final ChannelOption<Boolean> SO_REUSEADDR = valueOf("SO_REUSEADDR");
  
  public static final ChannelOption<Integer> SO_LINGER = valueOf("SO_LINGER");
  
  public static final ChannelOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");
  
  public static final ChannelOption<Integer> SO_TIMEOUT = valueOf("SO_TIMEOUT");
  
  public static final ChannelOption<Integer> IP_TOS = valueOf("IP_TOS");
  
  public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR = valueOf("IP_MULTICAST_ADDR");
  
  public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF = valueOf("IP_MULTICAST_IF");
  
  public static final ChannelOption<Integer> IP_MULTICAST_TTL = valueOf("IP_MULTICAST_TTL");
  
  public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED = valueOf("IP_MULTICAST_LOOP_DISABLED");
  
  public static final ChannelOption<Boolean> TCP_NODELAY = valueOf("TCP_NODELAY");
  
  @Deprecated
  public static final ChannelOption<Long> AIO_READ_TIMEOUT = valueOf("AIO_READ_TIMEOUT");
  
  @Deprecated
  public static final ChannelOption<Long> AIO_WRITE_TIMEOUT = valueOf("AIO_WRITE_TIMEOUT");
  
  @Deprecated
  public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION = valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");
  
  public static <T> ChannelOption<T> valueOf(String name) {
    return new ChannelOption<T>(name);
  }
  
  @Deprecated
  protected ChannelOption(String name) {
    super(names, name, new Object[0]);
  }
  
  public void validate(T value) {
    if (value == null)
      throw new NullPointerException("value"); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelOption.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */