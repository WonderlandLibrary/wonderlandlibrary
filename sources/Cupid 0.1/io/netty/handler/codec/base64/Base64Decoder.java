package io.netty.handler.codec.base64;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

@Sharable
public class Base64Decoder extends MessageToMessageDecoder<ByteBuf> {
  private final Base64Dialect dialect;
  
  public Base64Decoder() {
    this(Base64Dialect.STANDARD);
  }
  
  public Base64Decoder(Base64Dialect dialect) {
    if (dialect == null)
      throw new NullPointerException("dialect"); 
    this.dialect = dialect;
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    out.add(Base64.decode(msg, msg.readerIndex(), msg.readableBytes(), this.dialect));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\base64\Base64Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */