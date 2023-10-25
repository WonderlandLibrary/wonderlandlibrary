package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.util.List;

public class WebSocket08FrameEncoder extends MessageToMessageEncoder<WebSocketFrame> implements WebSocketFrameEncoder {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameEncoder.class);
  
  private static final byte OPCODE_CONT = 0;
  
  private static final byte OPCODE_TEXT = 1;
  
  private static final byte OPCODE_BINARY = 2;
  
  private static final byte OPCODE_CLOSE = 8;
  
  private static final byte OPCODE_PING = 9;
  
  private static final byte OPCODE_PONG = 10;
  
  private final boolean maskPayload;
  
  public WebSocket08FrameEncoder(boolean maskPayload) {
    this.maskPayload = maskPayload;
  }
  
  protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
    byte opcode;
    ByteBuf data = msg.content();
    if (msg instanceof TextWebSocketFrame) {
      opcode = 1;
    } else if (msg instanceof PingWebSocketFrame) {
      opcode = 9;
    } else if (msg instanceof PongWebSocketFrame) {
      opcode = 10;
    } else if (msg instanceof CloseWebSocketFrame) {
      opcode = 8;
    } else if (msg instanceof BinaryWebSocketFrame) {
      opcode = 2;
    } else if (msg instanceof ContinuationWebSocketFrame) {
      opcode = 0;
    } else {
      throw new UnsupportedOperationException("Cannot encode frame of type: " + msg.getClass().getName());
    } 
    int length = data.readableBytes();
    if (logger.isDebugEnabled())
      logger.debug("Encoding WebSocket Frame opCode=" + opcode + " length=" + length); 
    int b0 = 0;
    if (msg.isFinalFragment())
      b0 |= 0x80; 
    b0 |= msg.rsv() % 8 << 4;
    b0 |= opcode % 128;
    if (opcode == 9 && length > 125)
      throw new TooLongFrameException("invalid payload for PING (payload length must be <= 125, was " + length); 
    boolean release = true;
    ByteBuf buf = null;
    try {
      int maskLength = this.maskPayload ? 4 : 0;
      if (length <= 125) {
        int size = 2 + maskLength;
        if (this.maskPayload)
          size += length; 
        buf = ctx.alloc().buffer(size);
        buf.writeByte(b0);
        byte b = (byte)(this.maskPayload ? (0x80 | (byte)length) : (byte)length);
        buf.writeByte(b);
      } else if (length <= 65535) {
        int size = 4 + maskLength;
        if (this.maskPayload)
          size += length; 
        buf = ctx.alloc().buffer(size);
        buf.writeByte(b0);
        buf.writeByte(this.maskPayload ? 254 : 126);
        buf.writeByte(length >>> 8 & 0xFF);
        buf.writeByte(length & 0xFF);
      } else {
        int size = 10 + maskLength;
        if (this.maskPayload)
          size += length; 
        buf = ctx.alloc().buffer(size);
        buf.writeByte(b0);
        buf.writeByte(this.maskPayload ? 255 : 127);
        buf.writeLong(length);
      } 
      if (this.maskPayload) {
        int random = (int)(Math.random() * 2.147483647E9D);
        byte[] mask = ByteBuffer.allocate(4).putInt(random).array();
        buf.writeBytes(mask);
        int counter = 0;
        for (int i = data.readerIndex(); i < data.writerIndex(); i++) {
          byte byteData = data.getByte(i);
          buf.writeByte(byteData ^ mask[counter++ % 4]);
        } 
        out.add(buf);
      } else if (buf.writableBytes() >= data.readableBytes()) {
        buf.writeBytes(data);
        out.add(buf);
      } else {
        out.add(buf);
        out.add(data.retain());
      } 
      release = false;
    } finally {
      if (release && buf != null)
        buf.release(); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocket08FrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */