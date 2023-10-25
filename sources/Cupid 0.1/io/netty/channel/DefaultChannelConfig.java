package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.util.IdentityHashMap;
import java.util.Map;

public class DefaultChannelConfig implements ChannelConfig {
  private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = AdaptiveRecvByteBufAllocator.DEFAULT;
  
  private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
  
  private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
  
  protected final Channel channel;
  
  private volatile ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
  
  private volatile RecvByteBufAllocator rcvBufAllocator = DEFAULT_RCVBUF_ALLOCATOR;
  
  private volatile MessageSizeEstimator msgSizeEstimator = DEFAULT_MSG_SIZE_ESTIMATOR;
  
  private volatile int connectTimeoutMillis = 30000;
  
  private volatile int maxMessagesPerRead;
  
  private volatile int writeSpinCount = 16;
  
  private volatile boolean autoRead = true;
  
  private volatile boolean autoClose = true;
  
  private volatile int writeBufferHighWaterMark = 65536;
  
  private volatile int writeBufferLowWaterMark = 32768;
  
  public DefaultChannelConfig(Channel channel) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    this.channel = channel;
    if (channel instanceof ServerChannel || channel instanceof io.netty.channel.nio.AbstractNioByteChannel) {
      this.maxMessagesPerRead = 16;
    } else {
      this.maxMessagesPerRead = 1;
    } 
  }
  
  public Map<ChannelOption<?>, Object> getOptions() {
    return getOptions(null, (ChannelOption<?>[])new ChannelOption[] { ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR });
  }
  
  protected Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> result, ChannelOption<?>... options) {
    if (result == null)
      result = new IdentityHashMap<ChannelOption<?>, Object>(); 
    for (ChannelOption<?> o : options)
      result.put(o, getOption(o)); 
    return result;
  }
  
  public boolean setOptions(Map<ChannelOption<?>, ?> options) {
    if (options == null)
      throw new NullPointerException("options"); 
    boolean setAllOptions = true;
    for (Map.Entry<ChannelOption<?>, ?> e : options.entrySet()) {
      if (!setOption(e.getKey(), e.getValue()))
        setAllOptions = false; 
    } 
    return setAllOptions;
  }
  
  public <T> T getOption(ChannelOption<T> option) {
    if (option == null)
      throw new NullPointerException("option"); 
    if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS)
      return (T)Integer.valueOf(getConnectTimeoutMillis()); 
    if (option == ChannelOption.MAX_MESSAGES_PER_READ)
      return (T)Integer.valueOf(getMaxMessagesPerRead()); 
    if (option == ChannelOption.WRITE_SPIN_COUNT)
      return (T)Integer.valueOf(getWriteSpinCount()); 
    if (option == ChannelOption.ALLOCATOR)
      return (T)getAllocator(); 
    if (option == ChannelOption.RCVBUF_ALLOCATOR)
      return (T)getRecvByteBufAllocator(); 
    if (option == ChannelOption.AUTO_READ)
      return (T)Boolean.valueOf(isAutoRead()); 
    if (option == ChannelOption.AUTO_CLOSE)
      return (T)Boolean.valueOf(isAutoClose()); 
    if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK)
      return (T)Integer.valueOf(getWriteBufferHighWaterMark()); 
    if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK)
      return (T)Integer.valueOf(getWriteBufferLowWaterMark()); 
    if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR)
      return (T)getMessageSizeEstimator(); 
    return null;
  }
  
  public <T> boolean setOption(ChannelOption<T> option, T value) {
    validate(option, value);
    if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
      setConnectTimeoutMillis(((Integer)value).intValue());
    } else if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
      setMaxMessagesPerRead(((Integer)value).intValue());
    } else if (option == ChannelOption.WRITE_SPIN_COUNT) {
      setWriteSpinCount(((Integer)value).intValue());
    } else if (option == ChannelOption.ALLOCATOR) {
      setAllocator((ByteBufAllocator)value);
    } else if (option == ChannelOption.RCVBUF_ALLOCATOR) {
      setRecvByteBufAllocator((RecvByteBufAllocator)value);
    } else if (option == ChannelOption.AUTO_READ) {
      setAutoRead(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.AUTO_CLOSE) {
      setAutoClose(((Boolean)value).booleanValue());
    } else if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
      setWriteBufferHighWaterMark(((Integer)value).intValue());
    } else if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
      setWriteBufferLowWaterMark(((Integer)value).intValue());
    } else if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
      setMessageSizeEstimator((MessageSizeEstimator)value);
    } else {
      return false;
    } 
    return true;
  }
  
  protected <T> void validate(ChannelOption<T> option, T value) {
    if (option == null)
      throw new NullPointerException("option"); 
    option.validate(value);
  }
  
  public int getConnectTimeoutMillis() {
    return this.connectTimeoutMillis;
  }
  
  public ChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
    if (connectTimeoutMillis < 0)
      throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", new Object[] { Integer.valueOf(connectTimeoutMillis) })); 
    this.connectTimeoutMillis = connectTimeoutMillis;
    return this;
  }
  
  public int getMaxMessagesPerRead() {
    return this.maxMessagesPerRead;
  }
  
  public ChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
    if (maxMessagesPerRead <= 0)
      throw new IllegalArgumentException("maxMessagesPerRead: " + maxMessagesPerRead + " (expected: > 0)"); 
    this.maxMessagesPerRead = maxMessagesPerRead;
    return this;
  }
  
  public int getWriteSpinCount() {
    return this.writeSpinCount;
  }
  
  public ChannelConfig setWriteSpinCount(int writeSpinCount) {
    if (writeSpinCount <= 0)
      throw new IllegalArgumentException("writeSpinCount must be a positive integer."); 
    this.writeSpinCount = writeSpinCount;
    return this;
  }
  
  public ByteBufAllocator getAllocator() {
    return this.allocator;
  }
  
  public ChannelConfig setAllocator(ByteBufAllocator allocator) {
    if (allocator == null)
      throw new NullPointerException("allocator"); 
    this.allocator = allocator;
    return this;
  }
  
  public RecvByteBufAllocator getRecvByteBufAllocator() {
    return this.rcvBufAllocator;
  }
  
  public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator) {
    if (allocator == null)
      throw new NullPointerException("allocator"); 
    this.rcvBufAllocator = allocator;
    return this;
  }
  
  public boolean isAutoRead() {
    return this.autoRead;
  }
  
  public ChannelConfig setAutoRead(boolean autoRead) {
    boolean oldAutoRead = this.autoRead;
    this.autoRead = autoRead;
    if (autoRead && !oldAutoRead) {
      this.channel.read();
    } else if (!autoRead && oldAutoRead) {
      autoReadCleared();
    } 
    return this;
  }
  
  protected void autoReadCleared() {}
  
  public boolean isAutoClose() {
    return this.autoClose;
  }
  
  public ChannelConfig setAutoClose(boolean autoClose) {
    this.autoClose = autoClose;
    return this;
  }
  
  public int getWriteBufferHighWaterMark() {
    return this.writeBufferHighWaterMark;
  }
  
  public ChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
    if (writeBufferHighWaterMark < getWriteBufferLowWaterMark())
      throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + getWriteBufferLowWaterMark() + "): " + writeBufferHighWaterMark); 
    if (writeBufferHighWaterMark < 0)
      throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0"); 
    this.writeBufferHighWaterMark = writeBufferHighWaterMark;
    return this;
  }
  
  public int getWriteBufferLowWaterMark() {
    return this.writeBufferLowWaterMark;
  }
  
  public ChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
    if (writeBufferLowWaterMark > getWriteBufferHighWaterMark())
      throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + getWriteBufferHighWaterMark() + "): " + writeBufferLowWaterMark); 
    if (writeBufferLowWaterMark < 0)
      throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0"); 
    this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    return this;
  }
  
  public MessageSizeEstimator getMessageSizeEstimator() {
    return this.msgSizeEstimator;
  }
  
  public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator) {
    if (estimator == null)
      throw new NullPointerException("estimator"); 
    this.msgSizeEstimator = estimator;
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\DefaultChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */