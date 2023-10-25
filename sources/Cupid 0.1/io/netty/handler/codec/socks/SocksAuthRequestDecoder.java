package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import java.util.List;

public class SocksAuthRequestDecoder extends ReplayingDecoder<SocksAuthRequestDecoder.State> {
  private static final String name = "SOCKS_AUTH_REQUEST_DECODER";
  
  private SocksSubnegotiationVersion version;
  
  private int fieldLength;
  
  private String username;
  
  private String password;
  
  @Deprecated
  public static String getName() {
    return "SOCKS_AUTH_REQUEST_DECODER";
  }
  
  private SocksRequest msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
  
  public SocksAuthRequestDecoder() {
    super(State.CHECK_PROTOCOL_VERSION);
  }
  
  protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
    switch ((State)state()) {
      case CHECK_PROTOCOL_VERSION:
        this.version = SocksSubnegotiationVersion.valueOf(byteBuf.readByte());
        if (this.version != SocksSubnegotiationVersion.AUTH_PASSWORD)
          break; 
        checkpoint(State.READ_USERNAME);
      case READ_USERNAME:
        this.fieldLength = byteBuf.readByte();
        this.username = byteBuf.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
        checkpoint(State.READ_PASSWORD);
      case READ_PASSWORD:
        this.fieldLength = byteBuf.readByte();
        this.password = byteBuf.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
        this.msg = new SocksAuthRequest(this.username, this.password);
        break;
    } 
    ctx.pipeline().remove((ChannelHandler)this);
    out.add(this.msg);
  }
  
  enum State {
    CHECK_PROTOCOL_VERSION, READ_USERNAME, READ_PASSWORD;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksAuthRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */