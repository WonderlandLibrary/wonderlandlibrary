package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public class HttpResponseEncoder extends HttpObjectEncoder<HttpResponse> {
  private static final byte[] CRLF = new byte[] { 13, 10 };
  
  public boolean acceptOutboundMessage(Object msg) throws Exception {
    return (super.acceptOutboundMessage(msg) && !(msg instanceof HttpRequest));
  }
  
  protected void encodeInitialLine(ByteBuf buf, HttpResponse response) throws Exception {
    response.getProtocolVersion().encode(buf);
    buf.writeByte(32);
    response.getStatus().encode(buf);
    buf.writeBytes(CRLF);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\http\HttpResponseEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */