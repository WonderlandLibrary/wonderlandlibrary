package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

public class SocksAuthResponseDecoder extends ReplayingDecoder<SocksAuthResponseDecoder.State> {
  private static final String name = "SOCKS_AUTH_RESPONSE_DECODER";
  
  private SocksSubnegotiationVersion version;
  
  private SocksAuthStatus authStatus;
  
  @Deprecated
  public static String getName() {
    return "SOCKS_AUTH_RESPONSE_DECODER";
  }
  
  private SocksResponse msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
  
  public SocksAuthResponseDecoder() {
    super(State.CHECK_PROTOCOL_VERSION);
  }
  
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
    switch ((State)state()) {
      case CHECK_PROTOCOL_VERSION:
        this.version = SocksSubnegotiationVersion.valueOf(byteBuf.readByte());
        if (this.version != SocksSubnegotiationVersion.AUTH_PASSWORD)
          break; 
        checkpoint(State.READ_AUTH_RESPONSE);
      case READ_AUTH_RESPONSE:
        this.authStatus = SocksAuthStatus.valueOf(byteBuf.readByte());
        this.msg = new SocksAuthResponse(this.authStatus);
        break;
    } 
    channelHandlerContext.pipeline().remove((ChannelHandler)this);
    out.add(this.msg);
  }
  
  enum State {
    CHECK_PROTOCOL_VERSION, READ_AUTH_RESPONSE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksAuthResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */