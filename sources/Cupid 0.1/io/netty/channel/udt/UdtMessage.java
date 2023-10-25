package io.netty.channel.udt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.ReferenceCounted;

public final class UdtMessage extends DefaultByteBufHolder {
  public UdtMessage(ByteBuf data) {
    super(data);
  }
  
  public UdtMessage copy() {
    return new UdtMessage(content().copy());
  }
  
  public UdtMessage duplicate() {
    return new UdtMessage(content().duplicate());
  }
  
  public UdtMessage retain() {
    super.retain();
    return this;
  }
  
  public UdtMessage retain(int increment) {
    super.retain(increment);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channe\\udt\UdtMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */