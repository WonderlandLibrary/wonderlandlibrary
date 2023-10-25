package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class FixedRecvByteBufAllocator implements RecvByteBufAllocator {
  private final RecvByteBufAllocator.Handle handle;
  
  private static final class HandleImpl implements RecvByteBufAllocator.Handle {
    private final int bufferSize;
    
    HandleImpl(int bufferSize) {
      this.bufferSize = bufferSize;
    }
    
    public ByteBuf allocate(ByteBufAllocator alloc) {
      return alloc.ioBuffer(this.bufferSize);
    }
    
    public int guess() {
      return this.bufferSize;
    }
    
    public void record(int actualReadBytes) {}
  }
  
  public FixedRecvByteBufAllocator(int bufferSize) {
    if (bufferSize <= 0)
      throw new IllegalArgumentException("bufferSize must greater than 0: " + bufferSize); 
    this.handle = new HandleImpl(bufferSize);
  }
  
  public RecvByteBufAllocator.Handle newHandle() {
    return this.handle;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\FixedRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */