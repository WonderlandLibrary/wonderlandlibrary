package io.netty.buffer;

import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;

public class DefaultByteBufHolder implements ByteBufHolder {
  private final ByteBuf data;
  
  public DefaultByteBufHolder(ByteBuf data) {
    if (data == null)
      throw new NullPointerException("data"); 
    this.data = data;
  }
  
  public ByteBuf content() {
    if (this.data.refCnt() <= 0)
      throw new IllegalReferenceCountException(this.data.refCnt()); 
    return this.data;
  }
  
  public ByteBufHolder copy() {
    return new DefaultByteBufHolder(this.data.copy());
  }
  
  public ByteBufHolder duplicate() {
    return new DefaultByteBufHolder(this.data.duplicate());
  }
  
  public int refCnt() {
    return this.data.refCnt();
  }
  
  public ByteBufHolder retain() {
    this.data.retain();
    return this;
  }
  
  public ByteBufHolder retain(int increment) {
    this.data.retain(increment);
    return this;
  }
  
  public boolean release() {
    return this.data.release();
  }
  
  public boolean release(int decrement) {
    return this.data.release(decrement);
  }
  
  public String toString() {
    return StringUtil.simpleClassName(this) + '(' + content().toString() + ')';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\DefaultByteBufHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */