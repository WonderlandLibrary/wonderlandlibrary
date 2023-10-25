package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

public class RtspRequestEncoder extends RtspObjectEncoder<HttpRequest> {
  private static final byte[] CRLF = new byte[] { 13, 10 };
  
  public boolean acceptOutboundMessage(Object msg) throws Exception {
    return msg instanceof io.netty.handler.codec.http.FullHttpRequest;
  }
  
  protected void encodeInitialLine(ByteBuf buf, HttpRequest request) throws Exception {
    HttpHeaders.encodeAscii(request.getMethod().toString(), buf);
    buf.writeByte(32);
    buf.writeBytes(request.getUri().getBytes(CharsetUtil.UTF_8));
    buf.writeByte(32);
    encodeAscii(request.getProtocolVersion().toString(), buf);
    buf.writeBytes(CRLF);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\rtsp\RtspRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */