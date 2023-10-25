package io.netty.handler.codec.socks;

public enum SocksProtocolVersion {
  SOCKS4a((byte)4),
  SOCKS5((byte)5),
  UNKNOWN((byte)-1);
  
  private final byte b;
  
  SocksProtocolVersion(byte b) {
    this.b = b;
  }
  
  @Deprecated
  public static SocksProtocolVersion fromByte(byte b) {
    return valueOf(b);
  }
  
  public byte byteValue() {
    return this.b;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksProtocolVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */