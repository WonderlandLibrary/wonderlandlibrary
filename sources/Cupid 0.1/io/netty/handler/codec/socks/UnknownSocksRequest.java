package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class UnknownSocksRequest extends SocksRequest {
  public UnknownSocksRequest() {
    super(SocksRequestType.UNKNOWN);
  }
  
  public void encodeAsByteBuf(ByteBuf byteBuf) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\UnknownSocksRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */