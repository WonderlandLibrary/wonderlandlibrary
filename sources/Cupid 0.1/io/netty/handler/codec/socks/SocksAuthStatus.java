package io.netty.handler.codec.socks;

public enum SocksAuthStatus {
  SUCCESS((byte)0),
  FAILURE((byte)-1);
  
  private final byte b;
  
  SocksAuthStatus(byte b) {
    this.b = b;
  }
  
  @Deprecated
  public static SocksAuthStatus fromByte(byte b) {
    return valueOf(b);
  }
  
  public byte byteValue() {
    return this.b;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksAuthStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */