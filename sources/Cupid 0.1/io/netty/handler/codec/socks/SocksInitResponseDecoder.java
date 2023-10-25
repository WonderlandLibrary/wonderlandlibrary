package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

public class SocksInitResponseDecoder extends ReplayingDecoder<SocksInitResponseDecoder.State> {
  private static final String name = "SOCKS_INIT_RESPONSE_DECODER";
  
  private SocksProtocolVersion version;
  
  private SocksAuthScheme authScheme;
  
  @Deprecated
  public static String getName() {
    return "SOCKS_INIT_RESPONSE_DECODER";
  }
  
  private SocksResponse msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
  
  public SocksInitResponseDecoder() {
    super(State.CHECK_PROTOCOL_VERSION);
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
    switch ((State)state()) {
      case CHECK_PROTOCOL_VERSION:
        this.version = SocksProtocolVersion.valueOf(byteBuf.readByte());
        if (this.version != SocksProtocolVersion.SOCKS5)
          break; 
        checkpoint(State.READ_PREFFERED_AUTH_TYPE);
      case READ_PREFFERED_AUTH_TYPE:
        this.authScheme = SocksAuthScheme.valueOf(byteBuf.readByte());
        this.msg = new SocksInitResponse(this.authScheme);
        break;
    } 
    ctx.pipeline().remove((ChannelHandler)this);
    out.add(this.msg);
  }
  
  enum State {
    CHECK_PROTOCOL_VERSION, READ_PREFFERED_AUTH_TYPE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksInitResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */