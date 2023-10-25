package com.github.creeper123123321.viafabric.handler.clientside;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.exception.CancelDecoderException;

public class VRDecodeHandler extends MessageToMessageDecoder<ByteBuf> {
  private final UserConnection info;
  
  public VRDecodeHandler(UserConnection info) {
    this.info = info;
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) throws Exception {
    this.info.checkOutgoingPacket();
    if (!this.info.shouldTransformPacket()) {
      out.add(bytebuf.retain());
      return;
    } 
    ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
    try {
      this.info.transformOutgoing(transformedBuf, CancelDecoderException::generate);
      out.add(transformedBuf.retain());
    } finally {
      transformedBuf.release();
    } 
  }
  
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (cause instanceof us.myles.ViaVersion.exception.CancelCodecException)
      return; 
    super.exceptionCaught(ctx, cause);
  }
  
  public UserConnection getInfo() {
    return this.info;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\handler\clientside\VRDecodeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */