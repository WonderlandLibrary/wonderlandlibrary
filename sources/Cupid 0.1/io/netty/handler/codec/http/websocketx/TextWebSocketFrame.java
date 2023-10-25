package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;

public class TextWebSocketFrame extends WebSocketFrame {
  public TextWebSocketFrame() {
    super(Unpooled.buffer(0));
  }
  
  public TextWebSocketFrame(String text) {
    super(fromText(text));
  }
  
  public TextWebSocketFrame(ByteBuf binaryData) {
    super(binaryData);
  }
  
  public TextWebSocketFrame(boolean finalFragment, int rsv, String text) {
    super(finalFragment, rsv, fromText(text));
  }
  
  private static ByteBuf fromText(String text) {
    if (text == null || text.isEmpty())
      return Unpooled.EMPTY_BUFFER; 
    return Unpooled.copiedBuffer(text, CharsetUtil.UTF_8);
  }
  
  public TextWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
    super(finalFragment, rsv, binaryData);
  }
  
  public String text() {
    return content().toString(CharsetUtil.UTF_8);
  }
  
  public TextWebSocketFrame copy() {
    return new TextWebSocketFrame(isFinalFragment(), rsv(), content().copy());
  }
  
  public TextWebSocketFrame duplicate() {
    return new TextWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
  }
  
  public TextWebSocketFrame retain() {
    super.retain();
    return this;
  }
  
  public TextWebSocketFrame retain(int increment) {
    super.retain(increment);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\TextWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */