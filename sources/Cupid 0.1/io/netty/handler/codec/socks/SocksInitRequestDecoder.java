package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.ArrayList;
import java.util.List;

public class SocksInitRequestDecoder extends ReplayingDecoder<SocksInitRequestDecoder.State> {
  private static final String name = "SOCKS_INIT_REQUEST_DECODER";
  
  @Deprecated
  public static String getName() {
    return "SOCKS_INIT_REQUEST_DECODER";
  }
  
  private final List<SocksAuthScheme> authSchemes = new ArrayList<SocksAuthScheme>();
  
  private SocksProtocolVersion version;
  
  private byte authSchemeNum;
  
  private SocksRequest msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
  
  public SocksInitRequestDecoder() {
    super(State.CHECK_PROTOCOL_VERSION);
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
    int i;
    switch ((State)state()) {
      case CHECK_PROTOCOL_VERSION:
        this.version = SocksProtocolVersion.valueOf(byteBuf.readByte());
        if (this.version != SocksProtocolVersion.SOCKS5)
          break; 
        checkpoint(State.READ_AUTH_SCHEMES);
      case READ_AUTH_SCHEMES:
        this.authSchemes.clear();
        this.authSchemeNum = byteBuf.readByte();
        for (i = 0; i < this.authSchemeNum; i++)
          this.authSchemes.add(SocksAuthScheme.valueOf(byteBuf.readByte())); 
        this.msg = new SocksInitRequest(this.authSchemes);
        break;
    } 
    ctx.pipeline().remove((ChannelHandler)this);
    out.add(this.msg);
  }
  
  enum State {
    CHECK_PROTOCOL_VERSION, READ_AUTH_SCHEMES;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksInitRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */