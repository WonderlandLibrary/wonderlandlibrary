package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class SocksMessageEncoder extends MessageToByteEncoder<SocksMessage> {
  private static final String name = "SOCKS_MESSAGE_ENCODER";
  
  @Deprecated
  public static String getName() {
    return "SOCKS_MESSAGE_ENCODER";
  }
  
  protected void encode(ChannelHandlerContext ctx, SocksMessage msg, ByteBuf out) throws Exception {
    msg.encodeAsByteBuf(out);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksMessageEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */