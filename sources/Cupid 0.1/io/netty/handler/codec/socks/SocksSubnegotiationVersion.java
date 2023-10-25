package io.netty.handler.codec.socks;

public enum SocksSubnegotiationVersion {
  AUTH_PASSWORD((byte)1),
  UNKNOWN((byte)-1);
  
  private final byte b;
  
  SocksSubnegotiationVersion(byte b) {
    this.b = b;
  }
  
  @Deprecated
  public static SocksSubnegotiationVersion fromByte(byte b) {
    return valueOf(b);
  }
  
  public byte byteValue() {
    return this.b;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksSubnegotiationVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */