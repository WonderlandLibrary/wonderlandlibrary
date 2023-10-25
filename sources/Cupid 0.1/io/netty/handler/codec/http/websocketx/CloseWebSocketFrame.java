package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.EmptyArrays;

public class CloseWebSocketFrame extends WebSocketFrame {
  public CloseWebSocketFrame() {
    super(Unpooled.buffer(0));
  }
  
  public CloseWebSocketFrame(int statusCode, String reasonText) {
    this(true, 0, statusCode, reasonText);
  }
  
  public CloseWebSocketFrame(boolean finalFragment, int rsv) {
    this(finalFragment, rsv, Unpooled.buffer(0));
  }
  
  public CloseWebSocketFrame(boolean finalFragment, int rsv, int statusCode, String reasonText) {
    super(finalFragment, rsv, newBinaryData(statusCode, reasonText));
  }
  
  private static ByteBuf newBinaryData(int statusCode, String reasonText) {
    byte[] reasonBytes = EmptyArrays.EMPTY_BYTES;
    if (reasonText != null)
      reasonBytes = reasonText.getBytes(CharsetUtil.UTF_8); 
    ByteBuf binaryData = Unpooled.buffer(2 + reasonBytes.length);
    binaryData.writeShort(statusCode);
    if (reasonBytes.length > 0)
      binaryData.writeBytes(reasonBytes); 
    binaryData.readerIndex(0);
    return binaryData;
  }
  
  public CloseWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
    super(finalFragment, rsv, binaryData);
  }
  
  public int statusCode() {
    ByteBuf binaryData = content();
    if (binaryData == null || binaryData.capacity() == 0)
      return -1; 
    binaryData.readerIndex(0);
    int statusCode = binaryData.readShort();
    binaryData.readerIndex(0);
    return statusCode;
  }
  
  public String reasonText() {
    ByteBuf binaryData = content();
    if (binaryData == null || binaryData.capacity() <= 2)
      return ""; 
    binaryData.readerIndex(2);
    String reasonText = binaryData.toString(CharsetUtil.UTF_8);
    binaryData.readerIndex(0);
    return reasonText;
  }
  
  public CloseWebSocketFrame copy() {
    return new CloseWebSocketFrame(isFinalFragment(), rsv(), content().copy());
  }
  
  public CloseWebSocketFrame duplicate() {
    return new CloseWebSocketFrame(isFinalFragment(), rsv(), content().duplicate());
  }
  
  public CloseWebSocketFrame retain() {
    super.retain();
    return this;
  }
  
  public CloseWebSocketFrame retain(int increment) {
    super.retain(increment);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\CloseWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */