package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
  private final int frameLength;
  
  public FixedLengthFrameDecoder(int frameLength) {
    if (frameLength <= 0)
      throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength); 
    this.frameLength = frameLength;
  }
  
  protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    Object decoded = decode(ctx, in);
    if (decoded != null)
      out.add(decoded); 
  }
  
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    if (in.readableBytes() < this.frameLength)
      return null; 
    return in.readSlice(this.frameLength).retain();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\FixedLengthFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */