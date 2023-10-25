package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.ByteBuffer;

public abstract class AbstractDerivedByteBuf extends AbstractByteBuf {
  protected AbstractDerivedByteBuf(int maxCapacity) {
    super(maxCapacity);
  }
  
  public final int refCnt() {
    return unwrap().refCnt();
  }
  
  public final ByteBuf retain() {
    unwrap().retain();
    return this;
  }
  
  public final ByteBuf retain(int increment) {
    unwrap().retain(increment);
    return this;
  }
  
  public final boolean release() {
    return unwrap().release();
  }
  
  public final boolean release(int decrement) {
    return unwrap().release(decrement);
  }
  
  public ByteBuffer internalNioBuffer(int index, int length) {
    return nioBuffer(index, length);
  }
  
  public ByteBuffer nioBuffer(int index, int length) {
    return unwrap().nioBuffer(index, length);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\AbstractDerivedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */