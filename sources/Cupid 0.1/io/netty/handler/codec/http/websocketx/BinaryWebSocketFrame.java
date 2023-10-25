package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;

public class BinaryWebSocketFrame extends WebSocketFrame {
  public BinaryWebSocketFrame() {
    super(Unpooled.buffer(0));
  }
  
  public BinaryWebSocketFrame(ByteBuf binaryData) {
    super(binaryData);
  }
  
  public BinaryWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
    super(finalFragment, rsv, binaryData);
  }
  
  public BinaryWebSocketFrame copy() {
    return new BinaryWebSocketFrame(isFinalFragment(), rsv(), content().copy());
  }
  
  public BinaryWebSocketFrame duplicate() {
    return new BinaryWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
  }
  
  public BinaryWebSocketFrame retain() {
    super.retain();
    return this;
  }
  
  public BinaryWebSocketFrame retain(int increment) {
    super.retain(increment);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\BinaryWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */