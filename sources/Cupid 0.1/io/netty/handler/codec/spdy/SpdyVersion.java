package io.netty.handler.codec.spdy;

public enum SpdyVersion {
  SPDY_3_1(3, 1);
  
  private final int minorVersion;
  
  private final int version;
  
  SpdyVersion(int version, int minorVersion) {
    this.version = version;
    this.minorVersion = minorVersion;
  }
  
  int getVersion() {
    return this.version;
  }
  
  int getMinorVersion() {
    return this.minorVersion;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdyVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */