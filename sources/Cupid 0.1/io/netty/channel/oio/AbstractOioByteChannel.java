package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.internal.StringUtil;

public abstract class AbstractOioByteChannel extends AbstractOioChannel {
  private static final ChannelMetadata METADATA = new ChannelMetadata(false);
  
  private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
  
  private RecvByteBufAllocator.Handle allocHandle;
  
  private volatile boolean inputShutdown;
  
  protected AbstractOioByteChannel(Channel parent) {
    super(parent);
  }
  
  protected boolean isInputShutdown() {
    return this.inputShutdown;
  }
  
  public ChannelMetadata metadata() {
    return METADATA;
  }
  
  protected boolean checkInputShutdown() {
    if (this.inputShutdown) {
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {}
      return true;
    } 
    return false;
  }
  
  protected void doRead() {
    if (checkInputShutdown())
      return; 
    ChannelConfig config = config();
    ChannelPipeline pipeline = pipeline();
    RecvByteBufAllocator.Handle allocHandle = this.allocHandle;
    if (allocHandle == null)
      this.allocHandle = allocHandle = config.getRecvByteBufAllocator().newHandle(); 
    ByteBuf byteBuf = allocHandle.allocate(alloc());
    boolean closed = false;
    boolean read = false;
    Throwable exception = null;
    int localReadAmount = 0;
    try {
      int totalReadAmount = 0;
      do {
        localReadAmount = doReadBytes(byteBuf);
        if (localReadAmount > 0) {
          read = true;
        } else if (localReadAmount < 0) {
          closed = true;
        } 
        int available = available();
        if (available <= 0)
          break; 
        if (!byteBuf.isWritable()) {
          int capacity = byteBuf.capacity();
          int maxCapacity = byteBuf.maxCapacity();
          if (capacity == maxCapacity) {
            if (read) {
              read = false;
              pipeline.fireChannelRead(byteBuf);
              byteBuf = alloc().buffer();
            } 
          } else {
            int writerIndex = byteBuf.writerIndex();
            if (writerIndex + available > maxCapacity) {
              byteBuf.capacity(maxCapacity);
            } else {
              byteBuf.ensureWritable(available);
            } 
          } 
        } 
        if (totalReadAmount >= Integer.MAX_VALUE - localReadAmount) {
          totalReadAmount = Integer.MAX_VALUE;
          break;
        } 
        totalReadAmount += localReadAmount;
      } while (config.isAutoRead());
      allocHandle.record(totalReadAmount);
    } catch (Throwable t) {
      exception = t;
    } finally {
      if (read) {
        pipeline.fireChannelRead(byteBuf);
      } else {
        byteBuf.release();
      } 
      pipeline.fireChannelReadComplete();
      if (exception != null)
        if (exception instanceof java.io.IOException) {
          closed = true;
          pipeline().fireExceptionCaught(exception);
        } else {
          pipeline.fireExceptionCaught(exception);
          unsafe().close(voidPromise());
        }  
      if (closed) {
        this.inputShutdown = true;
        if (isOpen())
          if (Boolean.TRUE.equals(config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
            pipeline.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
          } else {
            unsafe().close(unsafe().voidPromise());
          }  
      } 
      if (localReadAmount == 0 && isActive())
        read(); 
    } 
  }
  
  protected void doWrite(ChannelOutboundBuffer in) throws Exception {
    while (true) {
      Object msg = in.current();
      if (msg == null)
        break; 
      if (msg instanceof ByteBuf) {
        ByteBuf buf = (ByteBuf)msg;
        int readableBytes = buf.readableBytes();
        while (readableBytes > 0) {
          doWriteBytes(buf);
          int newReadableBytes = buf.readableBytes();
          in.progress((readableBytes - newReadableBytes));
          readableBytes = newReadableBytes;
        } 
        in.remove();
        continue;
      } 
      if (msg instanceof FileRegion) {
        FileRegion region = (FileRegion)msg;
        long transfered = region.transfered();
        doWriteFileRegion(region);
        in.progress(region.transfered() - transfered);
        in.remove();
        continue;
      } 
      in.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg)));
    } 
  }
  
  protected final Object filterOutboundMessage(Object msg) throws Exception {
    if (msg instanceof ByteBuf || msg instanceof FileRegion)
      return msg; 
    throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
  }
  
  protected abstract int available();
  
  protected abstract int doReadBytes(ByteBuf paramByteBuf) throws Exception;
  
  protected abstract void doWriteBytes(ByteBuf paramByteBuf) throws Exception;
  
  protected abstract void doWriteFileRegion(FileRegion paramFileRegion) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\oio\AbstractOioByteChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */