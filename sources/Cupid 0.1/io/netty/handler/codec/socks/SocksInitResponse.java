package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class SocksInitResponse extends SocksResponse {
  private final SocksAuthScheme authScheme;
  
  public SocksInitResponse(SocksAuthScheme authScheme) {
    super(SocksResponseType.INIT);
    if (authScheme == null)
      throw new NullPointerException("authScheme"); 
    this.authScheme = authScheme;
  }
  
  public SocksAuthScheme authScheme() {
    return this.authScheme;
  }
  
  public void encodeAsByteBuf(ByteBuf byteBuf) {
    byteBuf.writeByte(protocolVersion().byteValue());
    byteBuf.writeByte(this.authScheme.byteValue());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksInitResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */