package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public abstract class SocksMessage {
  private final SocksMessageType type;
  
  private final SocksProtocolVersion protocolVersion = SocksProtocolVersion.SOCKS5;
  
  protected SocksMessage(SocksMessageType type) {
    if (type == null)
      throw new NullPointerException("type"); 
    this.type = type;
  }
  
  public SocksMessageType type() {
    return this.type;
  }
  
  public SocksProtocolVersion protocolVersion() {
    return this.protocolVersion;
  }
  
  @Deprecated
  public abstract void encodeAsByteBuf(ByteBuf paramByteBuf);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */