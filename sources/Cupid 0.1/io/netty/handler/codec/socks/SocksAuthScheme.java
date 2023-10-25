package io.netty.handler.codec.socks;

public enum SocksAuthScheme {
  NO_AUTH((byte)0),
  AUTH_GSSAPI((byte)1),
  AUTH_PASSWORD((byte)2),
  UNKNOWN((byte)-1);
  
  private final byte b;
  
  SocksAuthScheme(byte b) {
    this.b = b;
  }
  
  @Deprecated
  public static SocksAuthScheme fromByte(byte b) {
    return valueOf(b);
  }
  
  public byte byteValue() {
    return this.b;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksAuthScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */