package io.netty.handler.codec.socks;

public abstract class SocksRequest extends SocksMessage {
  private final SocksRequestType requestType;
  
  protected SocksRequest(SocksRequestType requestType) {
    super(SocksMessageType.REQUEST);
    if (requestType == null)
      throw new NullPointerException("requestType"); 
    this.requestType = requestType;
  }
  
  public SocksRequestType requestType() {
    return this.requestType;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\socks\SocksRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */