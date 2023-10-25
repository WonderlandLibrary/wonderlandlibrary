package io.netty.handler.codec.http.websocketx;

public enum WebSocketVersion {
  UNKNOWN, V00, V07, V08, V13;
  
  public String toHttpHeaderValue() {
    if (this == V00)
      return "0"; 
    if (this == V07)
      return "7"; 
    if (this == V08)
      return "8"; 
    if (this == V13)
      return "13"; 
    throw new IllegalStateException("Unknown web socket version: " + this);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\websocketx\WebSocketVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */