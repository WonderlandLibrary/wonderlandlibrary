package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;

public class PingWebSocketFrame extends WebSocketFrame {
  public PingWebSocketFrame() {
    super(true, 0, Unpooled.buffer(0));
  }
  
  public PingWebSocketFrame(ByteBuf binaryData) {
    super(binaryData);
  }
  
  public PingWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
    super(finalFragment, rsv, binaryData);
  }
  
  public PingWebSocketFrame copy() {
    return new PingWebSocketFrame(isFinalFragment(), rsv(), content().copy());
  }
  
  public PingWebSocketFrame duplicate() {
    return new PingWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
  }
  
  public PingWebSocketFrame retain() {
    super.retain();
    return this;
  }
  
  public PingWebSocketFrame retain(int increment) {
    super.retain(increment);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\PingWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */