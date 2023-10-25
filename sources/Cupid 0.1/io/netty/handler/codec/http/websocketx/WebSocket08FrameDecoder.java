package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.List;

public class WebSocket08FrameDecoder extends ReplayingDecoder<WebSocket08FrameDecoder.State> implements WebSocketFrameDecoder {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameDecoder.class);
  
  private static final byte OPCODE_CONT = 0;
  
  private static final byte OPCODE_TEXT = 1;
  
  private static final byte OPCODE_BINARY = 2;
  
  private static final byte OPCODE_CLOSE = 8;
  
  private static final byte OPCODE_PING = 9;
  
  private static final byte OPCODE_PONG = 10;
  
  private int fragmentedFramesCount;
  
  private final long maxFramePayloadLength;
  
  private boolean frameFinalFlag;
  
  private int frameRsv;
  
  private int frameOpcode;
  
  private long framePayloadLength;
  
  private ByteBuf framePayload;
  
  private int framePayloadBytesRead;
  
  private byte[] maskingKey;
  
  private ByteBuf payloadBuffer;
  
  private final boolean allowExtensions;
  
  private final boolean maskedPayload;
  
  private boolean receivedClosingHandshake;
  
  private Utf8Validator utf8Validator;
  
  enum State {
    FRAME_START, MASKING_KEY, PAYLOAD, CORRUPT;
  }
  
  public WebSocket08FrameDecoder(boolean maskedPayload, boolean allowExtensions, int maxFramePayloadLength) {
    super(State.FRAME_START);
    this.maskedPayload = maskedPayload;
    this.allowExtensions = allowExtensions;
    this.maxFramePayloadLength = maxFramePayloadLength;
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (this.receivedClosingHandshake) {
      in.skipBytes(actualReadableBytes());
      return;
    } 
    try {
      byte b;
      boolean frameMasked;
      int framePayloadLen1;
      int rbytes;
      long willHaveReadByteCount;
      switch ((State)state()) {
        case FRAME_START:
          this.framePayloadBytesRead = 0;
          this.framePayloadLength = -1L;
          this.framePayload = null;
          this.payloadBuffer = null;
          b = in.readByte();
          this.frameFinalFlag = ((b & 0x80) != 0);
          this.frameRsv = (b & 0x70) >> 4;
          this.frameOpcode = b & 0xF;
          if (logger.isDebugEnabled())
            logger.debug("Decoding WebSocket Frame opCode={}", Integer.valueOf(this.frameOpcode)); 
          b = in.readByte();
          frameMasked = ((b & 0x80) != 0);
          framePayloadLen1 = b & Byte.MAX_VALUE;
          if (this.frameRsv != 0 && !this.allowExtensions) {
            protocolViolation(ctx, "RSV != 0 and no extension negotiated, RSV:" + this.frameRsv);
            return;
          } 
          if (this.maskedPayload && !frameMasked) {
            protocolViolation(ctx, "unmasked client to server frame");
            return;
          } 
          if (this.frameOpcode > 7) {
            if (!this.frameFinalFlag) {
              protocolViolation(ctx, "fragmented control frame");
              return;
            } 
            if (framePayloadLen1 > 125) {
              protocolViolation(ctx, "control frame with payload length > 125 octets");
              return;
            } 
            if (this.frameOpcode != 8 && this.frameOpcode != 9 && this.frameOpcode != 10) {
              protocolViolation(ctx, "control frame using reserved opcode " + this.frameOpcode);
              return;
            } 
            if (this.frameOpcode == 8 && framePayloadLen1 == 1) {
              protocolViolation(ctx, "received close control frame with payload len 1");
              return;
            } 
          } else {
            if (this.frameOpcode != 0 && this.frameOpcode != 1 && this.frameOpcode != 2) {
              protocolViolation(ctx, "data frame using reserved opcode " + this.frameOpcode);
              return;
            } 
            if (this.fragmentedFramesCount == 0 && this.frameOpcode == 0) {
              protocolViolation(ctx, "received continuation data frame outside fragmented message");
              return;
            } 
            if (this.fragmentedFramesCount != 0 && this.frameOpcode != 0 && this.frameOpcode != 9) {
              protocolViolation(ctx, "received non-continuation data frame while inside fragmented message");
              return;
            } 
          } 
          if (framePayloadLen1 == 126) {
            this.framePayloadLength = in.readUnsignedShort();
            if (this.framePayloadLength < 126L) {
              protocolViolation(ctx, "invalid data frame length (not using minimal length encoding)");
              return;
            } 
          } else if (framePayloadLen1 == 127) {
            this.framePayloadLength = in.readLong();
            if (this.framePayloadLength < 65536L) {
              protocolViolation(ctx, "invalid data frame length (not using minimal length encoding)");
              return;
            } 
          } else {
            this.framePayloadLength = framePayloadLen1;
          } 
          if (this.framePayloadLength > this.maxFramePayloadLength) {
            protocolViolation(ctx, "Max frame length of " + this.maxFramePayloadLength + " has been exceeded.");
            return;
          } 
          if (logger.isDebugEnabled())
            logger.debug("Decoding WebSocket Frame length={}", Long.valueOf(this.framePayloadLength)); 
          checkpoint(State.MASKING_KEY);
        case MASKING_KEY:
          if (this.maskedPayload) {
            if (this.maskingKey == null)
              this.maskingKey = new byte[4]; 
            in.readBytes(this.maskingKey);
          } 
          checkpoint(State.PAYLOAD);
        case PAYLOAD:
          rbytes = actualReadableBytes();
          willHaveReadByteCount = (this.framePayloadBytesRead + rbytes);
          if (willHaveReadByteCount == this.framePayloadLength) {
            this.payloadBuffer = ctx.alloc().buffer(rbytes);
            this.payloadBuffer.writeBytes(in, rbytes);
          } else {
            if (willHaveReadByteCount < this.framePayloadLength) {
              if (this.framePayload == null)
                this.framePayload = ctx.alloc().buffer(toFrameLength(this.framePayloadLength)); 
              this.framePayload.writeBytes(in, rbytes);
              this.framePayloadBytesRead += rbytes;
              return;
            } 
            if (willHaveReadByteCount > this.framePayloadLength) {
              if (this.framePayload == null)
                this.framePayload = ctx.alloc().buffer(toFrameLength(this.framePayloadLength)); 
              this.framePayload.writeBytes(in, toFrameLength(this.framePayloadLength - this.framePayloadBytesRead));
            } 
          } 
          checkpoint(State.FRAME_START);
          if (this.framePayload == null) {
            this.framePayload = this.payloadBuffer;
            this.payloadBuffer = null;
          } else if (this.payloadBuffer != null) {
            this.framePayload.writeBytes(this.payloadBuffer);
            this.payloadBuffer.release();
            this.payloadBuffer = null;
          } 
          if (this.maskedPayload)
            unmask(this.framePayload); 
          if (this.frameOpcode == 9) {
            out.add(new PingWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
            this.framePayload = null;
            return;
          } 
          if (this.frameOpcode == 10) {
            out.add(new PongWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
            this.framePayload = null;
            return;
          } 
          if (this.frameOpcode == 8) {
            checkCloseFrameBody(ctx, this.framePayload);
            this.receivedClosingHandshake = true;
            out.add(new CloseWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
            this.framePayload = null;
            return;
          } 
          if (this.frameFinalFlag) {
            if (this.frameOpcode != 9) {
              this.fragmentedFramesCount = 0;
              if (this.frameOpcode == 1 || (this.utf8Validator != null && this.utf8Validator.isChecking())) {
                checkUTF8String(ctx, this.framePayload);
                this.utf8Validator.finish();
              } 
            } 
          } else {
            if (this.fragmentedFramesCount == 0) {
              if (this.frameOpcode == 1)
                checkUTF8String(ctx, this.framePayload); 
            } else if (this.utf8Validator != null && this.utf8Validator.isChecking()) {
              checkUTF8String(ctx, this.framePayload);
            } 
            this.fragmentedFramesCount++;
          } 
          if (this.frameOpcode == 1) {
            out.add(new TextWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
            this.framePayload = null;
            return;
          } 
          if (this.frameOpcode == 2) {
            out.add(new BinaryWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
            this.framePayload = null;
            return;
          } 
          if (this.frameOpcode == 0) {
            out.add(new ContinuationWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
            this.framePayload = null;
            return;
          } 
          throw new UnsupportedOperationException("Cannot decode web socket frame with opcode: " + this.frameOpcode);
        case CORRUPT:
          in.readByte();
          return;
      } 
      throw new Error("Shouldn't reach here.");
    } catch (Exception e) {
      if (this.payloadBuffer != null) {
        if (this.payloadBuffer.refCnt() > 0)
          this.payloadBuffer.release(); 
        this.payloadBuffer = null;
      } 
      if (this.framePayload != null) {
        if (this.framePayload.refCnt() > 0)
          this.framePayload.release(); 
        this.framePayload = null;
      } 
      throw e;
    } 
  }
  
  private void unmask(ByteBuf frame) {
    for (int i = frame.readerIndex(); i < frame.writerIndex(); i++)
      frame.setByte(i, frame.getByte(i) ^ this.maskingKey[i % 4]); 
  }
  
  private void protocolViolation(ChannelHandlerContext ctx, String reason) {
    protocolViolation(ctx, new CorruptedFrameException(reason));
  }
  
  private void protocolViolation(ChannelHandlerContext ctx, CorruptedFrameException ex) {
    checkpoint(State.CORRUPT);
    if (ctx.channel().isActive())
      ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener((GenericFutureListener)ChannelFutureListener.CLOSE); 
    throw ex;
  }
  
  private static int toFrameLength(long l) {
    if (l > 2147483647L)
      throw new TooLongFrameException("Length:" + l); 
    return (int)l;
  }
  
  private void checkUTF8String(ChannelHandlerContext ctx, ByteBuf buffer) {
    try {
      if (this.utf8Validator == null)
        this.utf8Validator = new Utf8Validator(); 
      this.utf8Validator.check(buffer);
    } catch (CorruptedFrameException ex) {
      protocolViolation(ctx, ex);
    } 
  }
  
  protected void checkCloseFrameBody(ChannelHandlerContext ctx, ByteBuf buffer) {
    if (buffer == null || !buffer.isReadable())
      return; 
    if (buffer.readableBytes() == 1)
      protocolViolation(ctx, "Invalid close frame body"); 
    int idx = buffer.readerIndex();
    buffer.readerIndex(0);
    int statusCode = buffer.readShort();
    if ((statusCode >= 0 && statusCode <= 999) || (statusCode >= 1004 && statusCode <= 1006) || (statusCode >= 1012 && statusCode <= 2999))
      protocolViolation(ctx, "Invalid close frame getStatus code: " + statusCode); 
    if (buffer.isReadable())
      try {
        (new Utf8Validator()).check(buffer);
      } catch (CorruptedFrameException ex) {
        protocolViolation(ctx, ex);
      }  
    buffer.readerIndex(idx);
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    if (this.framePayload != null)
      this.framePayload.release(); 
    if (this.payloadBuffer != null)
      this.payloadBuffer.release(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocket08FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */