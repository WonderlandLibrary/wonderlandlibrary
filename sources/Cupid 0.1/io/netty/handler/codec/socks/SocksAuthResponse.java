package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class SocksAuthResponse extends SocksResponse {
  private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;
  
  private final SocksAuthStatus authStatus;
  
  public SocksAuthResponse(SocksAuthStatus authStatus) {
    super(SocksResponseType.AUTH);
    if (authStatus == null)
      throw new NullPointerException("authStatus"); 
    this.authStatus = authStatus;
  }
  
  public SocksAuthStatus authStatus() {
    return this.authStatus;
  }
  
  public void encodeAsByteBuf(ByteBuf byteBuf) {
    byteBuf.writeByte(SUBNEGOTIATION_VERSION.byteValue());
    byteBuf.writeByte(this.authStatus.byteValue());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksAuthResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */