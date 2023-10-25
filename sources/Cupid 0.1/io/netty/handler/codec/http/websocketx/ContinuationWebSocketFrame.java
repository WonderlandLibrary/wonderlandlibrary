package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;

public class ContinuationWebSocketFrame extends WebSocketFrame {
  public ContinuationWebSocketFrame() {
    this(Unpooled.buffer(0));
  }
  
  public ContinuationWebSocketFrame(ByteBuf binaryData) {
    super(binaryData);
  }
  
  public ContinuationWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
    super(finalFragment, rsv, binaryData);
  }
  
  public ContinuationWebSocketFrame(boolean finalFragment, int rsv, String text) {
    this(finalFragment, rsv, fromText(text));
  }
  
  public String text() {
    return content().toString(CharsetUtil.UTF_8);
  }
  
  private static ByteBuf fromText(String text) {
    if (text == null || text.isEmpty())
      return Unpooled.EMPTY_BUFFER; 
    return Unpooled.copiedBuffer(text, CharsetUtil.UTF_8);
  }
  
  public ContinuationWebSocketFrame copy() {
    return new ContinuationWebSocketFrame(isFinalFragment(), rsv(), content().copy());
  }
  
  public ContinuationWebSocketFrame duplicate() {
    return new ContinuationWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
  }
  
  public ContinuationWebSocketFrame retain() {
    super.retain();
    return this;
  }
  
  public ContinuationWebSocketFrame retain(int increment) {
    super.retain(increment);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\ContinuationWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */