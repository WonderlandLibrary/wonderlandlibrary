package io.netty.handler.codec.socks;

public abstract class SocksResponse extends SocksMessage {
  private final SocksResponseType responseType;
  
  protected SocksResponse(SocksResponseType responseType) {
    super(SocksMessageType.RESPONSE);
    if (responseType == null)
      throw new NullPointerException("responseType"); 
    this.responseType = responseType;
  }
  
  public SocksResponseType responseType() {
    return this.responseType;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */